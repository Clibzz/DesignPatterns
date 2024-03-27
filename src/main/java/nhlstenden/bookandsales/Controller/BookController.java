package nhlstenden.bookandsales.Controller;

import nhlstenden.bookandsales.Model.Book;
import nhlstenden.bookandsales.Model.Genre;
import nhlstenden.bookandsales.service.BookTypeService;
import nhlstenden.bookandsales.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.ArrayList;

@Controller
public class BookController {

    @GetMapping("/addBook")
    public String addBook(Model model) throws SQLException
    {
        model.addAttribute("bookTypes", this.getBookTypeTypes());
        model.addAttribute("enumValues", Genre.values());
        model.addAttribute("bookForm", new Book());
        return "addBook";
    }

    public ArrayList<String> getBookTypeTypes() throws SQLException
    {
        BookTypeService bookTypeService = new BookTypeService();
        return bookTypeService.getBookTypeTypes();
    }

    @GetMapping("/overview")
    public String overview(Model model)
    {
        return "overview";
    }

    @PostMapping("/post-new-book")
    public String postNewBook(@RequestParam("book_type_id") String bookType, @RequestParam("genre") Genre genre,
                              @RequestParam("price") double price, @RequestParam("author") String author,
                              @RequestParam("publisher") String publisher, @RequestParam("title") String title,
                              @RequestParam("page_amount") int pageAmount, @RequestParam("has_hard_cover") boolean hasHardCover,
                              Model model) throws SQLException
    {
        BookService bookService = new BookService();

        bookService.addNewBook(bookType, genre, price, author, publisher, title, pageAmount, hasHardCover);

        model.addAttribute("success", true);
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

        return "addBook";
    }
}
