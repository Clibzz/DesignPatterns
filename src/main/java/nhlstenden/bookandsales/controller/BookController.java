package nhlstenden.bookandsales.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import nhlstenden.bookandsales.factory.BookProduct;
import nhlstenden.bookandsales.model.BookForm;
import nhlstenden.bookandsales.model.Genre;
import nhlstenden.bookandsales.model.PaymentCart;
import nhlstenden.bookandsales.model.PaymentCartHistory;
import nhlstenden.bookandsales.service.BookService;
import nhlstenden.bookandsales.service.BookTypeService;
import nhlstenden.bookandsales.service.ReviewService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    private final ReviewService reviewService;

    public BookController(BookService bookService, BookTypeService bookTypeService, ReviewService reviewService) {
        this.bookService = bookService;
        this.bookTypeService = bookTypeService;
        this.reviewService = reviewService;
    }

    @GetMapping("/addBook")
    public String addBook(Model model, HttpSession session) throws SQLException {
        if (isLoggedIn(session)) {
            if (session.getAttribute("roleId").equals(1)) {
                model.addAttribute("bookTypes", this.getBookTypeTypes());
                model.addAttribute("enumValues", Genre.values());

                // Initialize and add the bookForm object to the model
                model.addAttribute("bookForm", new BookForm());

                return "addBook";
            } else {
                return "redirect:/overview";
            }
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

    private boolean isEmptyStore(ArrayList<BookProduct> products)
    {
        return products.isEmpty();
    }

    @GetMapping("/overview")
    public String overview(Model model, HttpSession session) throws SQLException
    {
        if (isLoggedIn(session))
        {
            model.addAttribute("bookTypes", this.bookTypeService.getBookTypes());
            model.addAttribute("bookRegardlessOfTypeList", this.bookService.getAllBooksRegardlessOfType());
            model.addAttribute("hasBookTypeBeenChosen", false);
            model.addAttribute("isEmptyStore", this.isEmptyStore(this.bookService.getAllBooksRegardlessOfType()));

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
    public String postNewBook(@Valid @ModelAttribute("bookForm") BookForm bookForm,
                              BindingResult bindingResult,
                              Model model) throws SQLException, IOException {

        // Add model attributes for book types and genre values
        model.addAttribute("bookTypes", this.getBookTypeTypes());
        model.addAttribute("enumValues", Genre.values());

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // Return the form with validation errors
            return "addBook";
        }

        // Get last inserted ID
        int lastInsertedId = this.bookService.getLastInsertedId() + 1;

        // Determine upload directory and target file
        String uploadDirectory = getBaseImagePath(model) + lastInsertedId + File.separator;
        File targetDirectory = new File(uploadDirectory);

        // Create directory if it doesn't exist
        if (!targetDirectory.exists()) {
            if (!targetDirectory.mkdirs()) {
                bindingResult.reject("image", "Failed to create directory for image upload");
                return "addBook";
            }
        }

        // Handle image upload or default image setting
        String imageName;
        if (bookForm.getImage() != null && !bookForm.getImage().isEmpty()) {
            // If an image is uploaded, save it to the upload directory
            File targetFile = new File(uploadDirectory + bookForm.getImage().getOriginalFilename());
            bookForm.getImage().transferTo(targetFile);
            imageName = bookForm.getImage().getOriginalFilename();
        } else {
            // If no image is uploaded, use the default image
            String defaultImagePath = getBaseImagePath(model) + "default" + File.separator + "default.jpeg";
            File defaultImage = new File(defaultImagePath);
            File targetFile = new File(uploadDirectory + "default.jpeg");

            try (InputStream is = new FileInputStream(defaultImage);
                 OutputStream os = new FileOutputStream(targetFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } catch (IOException e) {
                bindingResult.reject("image", "Failed to copy default image");
                return "addBook";
            }

            imageName = "default.jpeg";
        }

        // Add new book using service method
        this.bookService.addNewBook(bookForm.getBookType(), bookForm.getDescription(), bookForm.getGenre(),
                bookForm.getPrice(), bookForm.getAuthor(), bookForm.getPublisher(),
                bookForm.getTitle(), bookForm.getPageAmount(), imageName);

        // Add success attribute to indicate successful book addition
        model.addAttribute("success", true);

        return "addBook";
    }

    @GetMapping("/bookDetails/{bookId}")
    public String getBookById(@PathVariable int bookId, Model model, HttpSession session) throws SQLException
    {
        if (isLoggedIn(session))
        {
            BookProduct bookProduct = this.bookService.getBookById(bookId);
            model.addAttribute("reviewList", this.reviewService.getAllReviewsConnectedToBookDetailsId(bookId));
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

        return "redirect:/overview";
    }

    private void removeBookFolder(@RequestParam("bookId") int bookId, Model model) throws IOException, SQLException
    {
        String uploadDirectory = getBaseImagePath(model) + bookId + File.separator;
        File directory = new File(uploadDirectory);
        if (directory.exists())
        {
            FileUtils.deleteDirectory(directory);
        }
    }

    @PostMapping("/deleteBook")
    public String deleteBookFromStore(@RequestParam("bookId") int bookId, Model model) throws SQLException, IOException
    {
        this.bookService.deleteBookFromStore(bookId);
        this.removeBookFolder(bookId, model);

        return "redirect:/overview";
    }
}