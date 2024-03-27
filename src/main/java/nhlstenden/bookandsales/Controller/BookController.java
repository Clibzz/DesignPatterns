package nhlstenden.bookandsales.Controller;

import nhlstenden.bookandsales.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
