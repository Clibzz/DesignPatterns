package nhlstenden.bookandsales.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookDetailsController
{
    @GetMapping("/bookDetails")
    public String getBookDetails()
    {
        return "bookDetails";
    }
}
