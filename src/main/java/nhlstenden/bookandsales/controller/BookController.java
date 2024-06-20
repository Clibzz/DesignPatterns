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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final ReviewService reviewService;

    public BookController(BookService bookService, BookTypeService bookTypeService, ReviewService reviewService) {
        this.bookService = bookService;
        this.bookTypeService = bookTypeService;
        this.reviewService = reviewService;
    }

    /**
     * Retrieve the addBook page
     * @param model The model of the page
     * @param session The current session
     * @return The addBook page or redirect to overview
     * @throws SQLException Throws a SQLException if retrieving the data from the database for the page fails
     */
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


    /**
     * Get a list of all the available book types
     * @return The list of book types
     * @throws SQLException Throws a SQLException if retrieving the data from the database fails
     */
    private ArrayList<String> getBookTypeTypes() throws SQLException
    {
        return this.bookTypeService.getBookTypeTypes();
    }

    /**
     * Check if a user is logged in or not
     * @param session The current session
     * @return True if logged in, false if not logged in
     */
    private boolean isLoggedIn(HttpSession session)
    {
        return session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
    }

    /**
     * Check if the store has any books to offer
     * @param products The list of products
     * @return True if empty, false if filled
     */
    private boolean isEmptyStore(ArrayList<BookProduct> products)
    {
        return products.isEmpty();
    }

    /**
     * Get the overview page with all books of the store
     * @param model The model of the page
     * @param session The current session
     * @return The overview page
     * @throws SQLException Throws a SQLException if retrieving the data from the database fails
     */
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

    /**
     * Choose an overview of books with a certain book type
     * @param bookTypeId The ID of the book type
     * @param model The model of the page
     * @param session The current session
     * @return The overview page
     * @throws SQLException Throws a SQLException if retrieving the data from the database fails
     */
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

    /**
     * Post a new book to the database
     * @param bookForm A custom book class which contains all the necessary fields of a book
     * @param bindingResult Contains potential errors of image processing
     * @param model The model of the page
     * @return The add book page
     * @throws SQLException Throws a SQLException if inserting the book fails
     * @throws IOException Throws an IOException when the image processing fails
     */
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

    /**
     * Get the details of a specific bok
     * @param bookId The id of the book
     * @param model The model of the page
     * @param session The current session
     * @return The bookDetails page
     * @throws SQLException Throws a SQLException if retrieving the data from the database fails
     */
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

    /**
     * Get the base image path in the static folder
     * @param model The model of the page
     * @return The base image path
     */
    private String getBaseImagePath(Model model)
    {
        String basePath = System.getProperty("user.dir") + File.separator + "src" +
                          File.separator + "main" + File.separator + "resources" +
                          File.separator + "static" + File.separator + "images" + File.separator;
        model.addAttribute("basePath", basePath);

        return basePath;
    }

    /**
     * Read the cart information of a specific user from the user's personal .json file
     * @param session The current session
     * @return The cart of the specific user in a JSONArray
     * @throws IOException Throws an IOException when reading the file fails
     * @throws JSONException Throws a JSONException when something related to the JSON fails
     */
    private JSONArray readJsonFromCart(HttpSession session) throws IOException, JSONException
    {
        Path path = Paths.get(session.getAttribute("username") + ".json");
        String content = String.join("\n", Files.readAllLines(path));

        return new JSONArray(content);
    }

    /**
     * Read the cart information of all carts from the carts.json file
     * @return The cart information of all users in a JSONArray
     * @throws IOException Throws an IOException when reading the file fails
     * @throws JSONException Throws a JSONException when something related to the JSON fails
     */
    private JSONArray readJsonFromAllCarts() throws IOException, JSONException
    {
        Path path = Paths.get("carts.json");
        String content = String.join("\n", Files.readAllLines(path));

        return new JSONArray(content);
    }

    /**
     * Write data to a .json file
     * @param path The path of the file
     * @param data The data that has to be written to the file
     */
    private void writeDataToFile(Path path, JSONArray data)
    {
        try (FileWriter writer = new FileWriter(path.toFile(), false)) {
            writer.write(data.toString(4));
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write data to the carts.json file
     * @param jsonArray The data to be written to the file
     */
    private void writeDataToAllCartsFile(JSONArray jsonArray)
    {
        Path path = Paths.get("carts.json");
        this.writeDataToFile(path, jsonArray);
    }

    /**
     * Searches the JSONArray for an object that matches the book's id and the user's id
     * @param array The array to be searched in
     * @param bookId The id of the book
     * @param session The current session
     * @return The matching JSONObject
     * @throws JSONException Throws a JSONException when something related to the JSON fails
     */
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

    /**
     * Update a JSONArray
     * @param array The array
     * @param bookId The book id
     * @param session The current session
     * @param book The book product
     * @return The updated JSONArray
     * @throws JSONException Throws a JSONException when something related to the JSON fails
     * @throws JsonProcessingException Throws a JSONException when processing the JSON fails
     */
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

    /**
     * Update the state of a cart
     * @param jsonArray The jsonArray
     * @param book The book product
     */
    private void updateCartState(JSONArray jsonArray, BookProduct book)
    {
        PaymentCart paymentCart = new PaymentCart();
        paymentCart.setCartDetails(jsonArray.toString());
        PaymentCartHistory history = new PaymentCartHistory();
        paymentCart.appendCartDetails(book);
        history.saveState(paymentCart.save());
    }

    /**
     * Add a book to cart
     * @param bookId The id of the book
     * @param session The current session
     * @param redirectAttributes RedirectAttributes to show error / success messages
     * @return The overview page
     * @throws SQLException Throws when something goes wrong related to the database actions
     * @throws IOException Throws an IOException when writing to the file fails
     * @throws JSONException Throws a JSONException when something related to the JSON fails
     */
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("bookId") int bookId, HttpSession session,
                            RedirectAttributes redirectAttributes) throws SQLException, IOException, JSONException
    {
        BookProduct book = this.bookService.getBookById(bookId);
        Path path = Paths.get(session.getAttribute("username") + ".json");
        JSONArray jsonArray = this.readJsonFromCart(session);
        JSONArray fullCartsArray = this.readJsonFromAllCarts();
        this.writeDataToFile(path, this.updateItemArray(jsonArray, bookId, session, book));
        this.writeDataToAllCartsFile(this.updateItemArray(fullCartsArray, bookId, session, book));
        this.updateCartState(jsonArray, book);

        redirectAttributes.addFlashAttribute("success", "Book has been added to the cart successfully");
        return "redirect:/overview";
    }

    /**
     * Remove an image folder of a book
     * @param bookId The id of the book
     * @param model The model of the page
     * @throws IOException Throws an IOException when removing the folder fails
     */
    private void removeBookFolder(@RequestParam("bookId") int bookId, Model model) throws IOException
    {
        String uploadDirectory = getBaseImagePath(model) + bookId + File.separator;
        File directory = new File(uploadDirectory);
        if (directory.exists())
        {
            FileUtils.deleteDirectory(directory);
        }
    }

    /**
     * Delete a book from the database
     * @param bookId The id of the book
     * @param model The model of the page
     * @return The overview page
     * @throws SQLException Throws when something goes wrong while deleting the book from the database
     * @throws IOException Throws an IOException when removing the folder fails
     */
    @PostMapping("/deleteBook")
    public String deleteBookFromStore(@RequestParam("bookId") int bookId, Model model) throws SQLException, IOException
    {
        this.bookService.deleteBookFromStore(bookId);
        this.removeBookFolder(bookId, model);

        return "redirect:/overview";
    }
}