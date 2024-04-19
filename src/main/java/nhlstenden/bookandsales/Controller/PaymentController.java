package nhlstenden.bookandsales.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController
{
        @GetMapping("/cart")
        public String getCart(Model model)
        {
                model.addAttribute("message", "Cart");
                return "cart";
        }
}
