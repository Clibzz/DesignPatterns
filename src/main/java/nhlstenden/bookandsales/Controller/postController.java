package nhlstenden.bookandsales.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class postController
{

    @GetMapping("/register")
    public String register(Model model)
    {
        return "register";
    }

}
