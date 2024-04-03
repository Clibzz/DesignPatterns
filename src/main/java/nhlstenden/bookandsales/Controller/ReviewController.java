package nhlstenden.bookandsales.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReviewController
{
    @GetMapping("/addReview")
    public String addReview(HttpSession session, Model model)
    {
        if (isLoggedIn(session))
        {
            return "redirect:/login";
        }
        model.addAttribute("message", "Add a review");
        return "addReview";
    }

    @GetMapping("/reviews")
    public String getReviews(HttpSession session, Model model)
    {
        if (isLoggedIn(session))
        {
            model.addAttribute("message", "Get reviews");
            return "reviews";
        }
        return "redirect:/login";
    }

    private boolean isLoggedIn(HttpSession session)
    {
        return session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
    }
}
