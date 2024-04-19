package nhlstenden.bookandsales.controller;

import jakarta.servlet.http.HttpSession;
import nhlstenden.bookandsales.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class ReviewController
{

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService)
    {
        this.reviewService = reviewService;
    }

    @PostMapping("/addReview")
    public String addReview(@RequestParam("review_title") String reviewTitle,
                            @RequestParam("review_description") String reviewDescription,
                            @RequestParam("hiddenBookId") int hiddenBookId,
                            HttpSession session, Model model) throws SQLException
    {
        if (isLoggedIn(session))
        {

            this.reviewService.addReview((int) session.getAttribute("userId"), hiddenBookId, reviewTitle, 3.0, reviewDescription, "empty");
            return "redirect:/bookDetails/" + hiddenBookId;
        }

        return "redirect:/login";
    }

    private boolean isLoggedIn(HttpSession session)
    {
        return session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
    }
}
