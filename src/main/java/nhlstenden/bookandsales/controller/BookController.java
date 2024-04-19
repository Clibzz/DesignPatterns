package nhlstenden.bookandsales.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import nhlstenden.bookandsales.factory.BookProduct;
import nhlstenden.bookandsales.model.Genre;
import nhlstenden.bookandsales.model.PaymentCart;
import nhlstenden.bookandsales.model.PaymentCartHistory;
import nhlstenden.bookandsales.service.BookService;
import nhlstenden.bookandsales.service.BookTypeService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Controller
public class BookController
{

    private final BookService bookService;
    private final BookTypeService bookTypeService;

    public BookController(BookService bookService, BookTypeService bookTypeService) {
        this.bookService = bookService;
        this.bookTypeService = bookTypeService;
    }

    @GetMapping("/addBook")
    public String addBook(Model model, HttpSession session) throws SQLException
    {
        if (isLoggedIn(session))
        {
            model.addAttribute("bookTypes", this.getBookTypeTypes());
            model.addAttribute("enumValues", Genre.values());

            return "addBook";
        }

        return "redirect:/login";
    }

    private ArrayList<String> getBookTypeTypes() throws SQLException
    {
        return this.bookTypeService.getBookTypeTypes();
    }

    private boolean isLoggedIn(HttpSession session)
    {
        return session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
    }

    @GetMapping("/overview")
    public String overview(Model model, HttpSession session) throws SQLException
    {
        if (isLoggedIn(session))
        {
            model.addAttribute("bookTypes", this.bookTypeService.getBookTypes());
            model.addAttribute("bookRegardlessOfTypeList", this.bookService.getAllBooksRegardlessOfType());
            model.addAttribute("hasBookTypeBeenChosen", false);

            return "overview";
        }

        return "redirect:/login";
    }

    @PostMapping("/overview")
    public String chooseOverview(@RequestParam("bookTypeId") int bookTypeId, Model model, HttpSession session) throws SQLException
    {

        if (isLoggedIn(session))
        {
            model.addAttribute("bookTypeId", bookTypeId);
            model.addAttribute("bookTypes", this.bookTypeService.getBookTypes());
            model.addAttribute("bookList", this.bookService.getBookList(bookTypeId));
            model.addAttribute("hasBookTypeBeenChosen", true);

            return "overview";
        }

        return "redirect:/login";
    }

    @PostMapping(path = "/post-new-book")
    public String postNewBook(@RequestParam("book_type_id") String bookType, @RequestParam("description") String description,
                              @RequestParam("genre") Genre genre, @RequestParam("price") double price,
                              @RequestParam("author") String author, @RequestParam("publisher") String publisher,
                              @RequestParam("title") String title, @RequestParam("page_amount") Integer pageAmount,
                              @RequestParam("image") MultipartFile image, Model model) throws SQLException, IOException
    {
        model.addAttribute("bookTypes", this.getBookTypeTypes());
        model.addAttribute("enumValues", Genre.values());

        model.addAttribute("bookType", bookType);
        model.addAttribute("description", description);
        model.addAttribute("genre", genre);
        model.addAttribute("price", price);
        model.addAttribute("author", author);
        model.addAttribute("publisher", publisher);
        model.addAttribute("title", title);
        model.addAttribute("pageAmount", pageAmount);

        this.bookService.addNewBook(bookType, description, genre, price, author, publisher, title, pageAmount, image);

        String uploadDirectory = getBaseImagePath(model) + this.bookService.getLastInsertedId() + File.separator;
        File targetFile = new File(uploadDirectory + image.getOriginalFilename());

        if (!targetFile.exists())
        {
            if (targetFile.mkdirs())
            {
                image.transferTo(targetFile);
                model.addAttribute("success", true);
                System.out.println("Directory created successfully: " + targetFile);
            } else
            {
                System.err.println("Failed to create directory: " + targetFile);
            }
        }
        return "addBook";
    }

    @GetMapping("/bookDetails/{bookId}")
    public String getBookById(@PathVariable int bookId, Model model, HttpSession session) throws SQLException
    {
        if (isLoggedIn(session))
        {
            BookProduct bookProduct = this.bookService.getBookById(bookId);
            model.addAttribute("bookProduct", bookProduct);

            return "bookDetails";
        }

        return "redirect:/login";
    }

    private String getBaseImagePath(Model model)
    {
        String basePath = System.getProperty("user.dir") + File.separator + "src" +
                          File.separator + "main" + File.separator + "resources" +
                          File.separator + "static" + File.separator + "images" + File.separator;
        model.addAttribute("basePath", basePath);
        return basePath;
    }

    private JSONArray readJsonFromCart(HttpSession session) throws IOException, JSONException
    {
        Path path = Paths.get(session.getAttribute("username") + ".json");
        String content = String.join("\n", Files.readAllLines(path));
        return new JSONArray(content);
    }

    private JSONArray readJsonFromAllCarts() throws IOException, JSONException
    {
        Path path = Paths.get("carts.json");
        String content = String.join("\n", Files.readAllLines(path));
        return new JSONArray(content);
    }

    private void writeDataToFile(Path path, JSONArray data)
    {
        try (FileWriter writer = new FileWriter(path.toFile(), false)) {
            writer.write(data.toString(4));
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeDataToAllCartsFile(JSONArray jsonArray)
    {
        Path path = Paths.get("carts.json");
        this.writeDataToFile(path, jsonArray);
    }

    private JSONObject getMatchingObjectNumber(JSONArray array, int bookId, HttpSession session) throws JSONException
    {
        for (int i = 0; i < array.length(); i++)
        {
            JSONObject obj = array.getJSONObject(i);
            if (obj.getInt("id") == bookId && obj.getInt("userId") == (int)session.getAttribute("userId"))
            {
                return obj;
            }
        }
        return null;
    }

    private JSONArray updateItemArray(JSONArray array, int bookId, HttpSession session, BookProduct book) throws JSONException, JsonProcessingException
    {
        JSONObject obj = this.getMatchingObjectNumber(array, bookId, session);
        ObjectMapper objectMapper = new ObjectMapper();
        if (obj != null)
        {
            for (int i = 0; i < array.length(); i++)
            {
                if (array.getJSONObject(i).equals(obj))
                {
                    int amount = obj.getInt("amount") + 1;
                    obj.put("amount", amount);
                    array.remove(i);
                    array.put(obj);
                    break;
                }
            }
        }
        else
        {
            JSONObject jsonObj = new JSONObject(objectMapper.writeValueAsString(book));
            jsonObj.put("userId", session.getAttribute("userId"));
            jsonObj.put("amount", 1);
            array.put(jsonObj);
        }

        return array;
    }

    private void updateCartState(JSONArray jsonArray, BookProduct book)
    {
        PaymentCart paymentCart = new PaymentCart();
        paymentCart.setCartDetails(jsonArray.toString());
        PaymentCartHistory history = new PaymentCartHistory();
        paymentCart.appendCartDetails(book);
        history.saveState(paymentCart.save());
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("bookId") int bookId, HttpSession session) throws SQLException, IOException, JSONException
    {
        BookProduct book = this.bookService.getBookById(bookId);
        Path path = Paths.get(session.getAttribute("username") + ".json");
        JSONArray jsonArray = this.readJsonFromCart(session);
        JSONArray fullCartsArray = this.readJsonFromAllCarts();
        this.writeDataToFile(path, this.updateItemArray(jsonArray, bookId, session, book));
        this.writeDataToAllCartsFile(this.updateItemArray(fullCartsArray, bookId, session, book));
        this.updateCartState(jsonArray, book);
        return "cart";
    }
}