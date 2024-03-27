package nhlstenden.bookandsales.Controller;

import nhlstenden.bookandsales.Model.Book;
import nhlstenden.bookandsales.Model.Genre;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;

@Controller
public class ReviewController
{
    @GetMapping("/addReview")
    public String addReview(Model model)
    {
        model.addAttribute("message", "Add a review");
        return "addReview";
    }

    @GetMapping("/reviews")
    public String getReviews(Model model)
    {
        model.addAttribute("message", "Get reviews");
        return "reviews";
    }

}
