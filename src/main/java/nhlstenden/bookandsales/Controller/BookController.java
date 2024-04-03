package nhlstenden.bookandsales.Controller;

import jakarta.servlet.http.HttpSession;

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

import java.util.ArrayList;

@Controller
public class BookController {

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

    public ArrayList<String> getBookTypeTypes() throws SQLException
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

    @PostMapping("/post-new-book")
    public String postNewBook(@RequestParam("book_type_id") String bookType, @RequestParam("genre") Genre genre,
                              @RequestParam("price") double price, @RequestParam("author") String author,
                              @RequestParam("publisher") String publisher, @RequestParam("title") String title,
                              @RequestParam("page_amount") int pageAmount, @RequestParam("has_hard_cover") boolean hasHardCover,
                              Model model) throws SQLException
    {

        model.addAttribute("bookTypes", this.getBookTypeTypes());
        model.addAttribute("enumValues", Genre.values());
        model.addAttribute("bookForm", new Book());

        model.addAttribute("bookType", bookType);
        model.addAttribute("genre", genre);
        model.addAttribute("price", price);
        model.addAttribute("author", author);
        model.addAttribute("publisher", publisher);
        model.addAttribute("title", title);
        model.addAttribute("pageAmount", pageAmount);
        model.addAttribute("hasHardCover", hasHardCover);

        ArrayList<String> errors = new ArrayList<>();

        if (price < 0)
        {
            errors.add("Price is incorrect, please enter a valid amount");
        }

        if (pageAmount < 0)
        {
            errors.add("Page amount is incorrect, please enter a valid amount");
        }

        if (!errors.isEmpty())
        {
            model.addAttribute("errors", errors);
        }
        else
        {
            this.bookService.addNewBook(bookType, genre, price, author, publisher, title, pageAmount, hasHardCover);
            model.addAttribute("success", true);
        }
        return "addBook";
    }
}
