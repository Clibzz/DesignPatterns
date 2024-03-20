package nhlstenden.bookandsales.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController
{
    @GetMapping("/testSend")
    public String testSend(Model model)
    {
        model.addAttribute("message", "hello how is it going");
        return "testSend";
    }

}
