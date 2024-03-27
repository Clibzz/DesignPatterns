package nhlstenden.bookandsales.Controller;

import nhlstenden.bookandsales.Model.BookType;
import nhlstenden.bookandsales.Model.Genre;
import nhlstenden.bookandsales.service.PostService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.time.LocalDate;

@RestController
public class PostController
{

    @PostMapping("/post-new-user")
    public String postNewUser(@RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName
                                  ,@RequestParam("address") String address, @RequestParam("password") String password) throws SQLException
    {
        PostService postService = new PostService();

        postService.registerNewUser(firstName, lastName, LocalDate.now(), address, password);

        return "success";
    }

    @PostMapping("/post-new-book")
    public String postNewBook(@RequestParam("book_type_id") String bookType, @RequestParam("genre") Genre genre,
                              @RequestParam("price") double price, @RequestParam("author") String author,
                              @RequestParam("publisher") String publisher, @RequestParam("title") String title,
                              @RequestParam("page_amount") int pageAmount, @RequestParam("has_hard_cover") boolean hasHardCover, Model model) throws SQLException
    {
        PostService postService = new PostService();

        boolean success =  postService.addNewBook(bookType, genre, price, author, publisher, title, pageAmount, hasHardCover);

        if (success)
        {
            model.addAttribute("message", "Success!");
        }
        else
        {
            model.addAttribute("message", "Select valid book_type_id");
        }

        return "success";
    }
}
