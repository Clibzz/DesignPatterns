package nhlstenden.bookandsales.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController
{
        @GetMapping("/cart")
        public String getCart(HttpSession session, Model model)
        {
                if (isLoggedIn(session))
                {
                        model.addAttribute("message", "Cart");
                        return "cart";
                }
                return "redirect:/login";
        }

        private boolean isLoggedIn(HttpSession session)
        {
                return session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
        }
}
