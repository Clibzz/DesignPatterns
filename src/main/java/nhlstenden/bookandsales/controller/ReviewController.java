package nhlstenden.bookandsales.controller;

import jakarta.servlet.http.HttpSession;
import nhlstenden.bookandsales.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class ReviewController
{
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService)
    {
        this.reviewService = reviewService;
    }

    private boolean isLoggedIn(HttpSession session)
    {
        return session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
    }

    /**
     * Add a review to a book
     * @param reviewTitle The title of the review
     * @param reviewDescription The description of the review / The actual review
     * @param hiddenBookId The id of the book
     * @param session The current session
     * @return The bookDetails page of the book that has just been reviewed
     * @throws SQLException Throws a SQLException if anything goes wrong inserting the review into the database
     */
    @PostMapping("/addReview")
    public String addReview(@RequestParam("review_title") String reviewTitle,
                            @RequestParam("review_description") String reviewDescription,
                            @RequestParam("hiddenBookId") int hiddenBookId,
                            HttpSession session) throws SQLException
    {
        if (isLoggedIn(session))
        {
            this.reviewService.addReview((int) session.getAttribute("userId"), hiddenBookId, reviewTitle, 3.0, reviewDescription, "empty");

            return "redirect:/bookDetails/" + hiddenBookId;
        }

        return "redirect:/login";
    }
}
