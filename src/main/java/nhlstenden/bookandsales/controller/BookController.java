package nhlstenden.bookandsales.controller;

import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import nhlstenden.bookandsales.factory.BookProduct;
import nhlstenden.bookandsales.model.Genre;
import nhlstenden.bookandsales.service.BookService;
import nhlstenden.bookandsales.service.ReviewService;
import org.springframework.stereotype.Controller;
import nhlstenden.bookandsales.service.BookTypeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
}