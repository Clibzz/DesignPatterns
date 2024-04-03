package nhlstenden.bookandsales.Controller;

import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import nhlstenden.bookandsales.Model.Book;
import nhlstenden.bookandsales.Model.Genre;
import nhlstenden.bookandsales.service.BookService;
import org.springframework.stereotype.Controller;
import nhlstenden.bookandsales.service.BookTypeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
            model.addAttribute("bookForm", new Book());

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

            this.bookService.getBookList(bookTypeId);

            model.addAttribute("bookList", this.bookService.getBookList(bookTypeId));

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
        model.addAttribute("bookForm", new Book());

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
    public String getBookById(@PathVariable int bookId, Model model) throws SQLException
    {
        Book book = this.bookService.getBookById(bookId);
        model.addAttribute("book", book);
        String imagePath = getBaseImagePath(model) + book.getImage();
        model.addAttribute("imagePath", imagePath);
        return "bookDetails";
    }

    private String getBaseImagePath(Model model)
    {
        String basePath = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "BookAndSales" + File.separator + "Books" + File.separator;
        model.addAttribute("basePath", basePath);
        return basePath;
    }
}