package nhlstenden.bookandsales.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import nhlstenden.bookandsales.Factory.BookProduct;
import nhlstenden.bookandsales.Model.Genre;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

@Controller
public class BookController
{

    private final BookService bookService;
    private final BookTypeService bookTypeService;
    private boolean hasBookTypeBeenChosen;

    public BookController(BookService bookService, BookTypeService bookTypeService) {
        this.bookService = bookService;
        this.bookTypeService = bookTypeService;
        this.hasBookTypeBeenChosen = false;
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

    private JSONArray readJsonFromAllCarts(Path path) throws IOException, JSONException
    {
        String content = String.join("\n", Files.readAllLines(path));
        return new JSONArray(content);
    }

    private void writeObjectToAllCartsJson(JSONObject object) throws JSONException, IOException
    {
        Path path = Paths.get("carts.json");
        JSONArray data = this.readJsonFromAllCarts(path);
        data.put(object);
        try (FileWriter writer = new FileWriter(path.toFile(), false))
        {
            writer.write(data.toString(4));
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("bookId") int bookId, HttpSession session) throws SQLException, IOException, JSONException
    {
        BookProduct book = this.bookService.getBookById(bookId);
        Path path = Paths.get(session.getAttribute("username") + ".json");

        ObjectMapper objectMapper = new ObjectMapper();

        JSONArray jsonArray = this.readJsonFromCart(session);

        JSONObject jsonObj = new JSONObject(objectMapper.writeValueAsString(book));
        jsonObj.put("userId", session.getAttribute("userId"));
        this.writeObjectToAllCartsJson(jsonObj);
        jsonArray.put(jsonObj);
        try (FileWriter writer = new FileWriter(path.toFile(), false))
        {
            writer.write(jsonArray.toString(4));
        }
        return "bookDetails";
    }
}