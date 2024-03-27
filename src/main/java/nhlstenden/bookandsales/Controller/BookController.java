package nhlstenden.bookandsales.Controller;

import nhlstenden.bookandsales.Service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;

@Controller
public class BookController {
    //     public Genre distributeGenre() {
    //
    //    }

    private BookService bookService;

    public BookController(BookService bookService)
    {
        this.bookService = bookService;
    }

    @GetMapping("/overview")
    public String overview(Model model)
    {
        return "overview";
    }

    @GetMapping("/overview/{bookTypeId}")
    public String chooseOverview(Model model, @PathVariable int bookTypeId) throws SQLException
    {
        bookService.getBookList(bookTypeId);

        return "redirect:/overview/{bookTypeId}";
    }

}
