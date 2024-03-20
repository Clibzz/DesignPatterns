package nhlstenden.bookandsales.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OverviewController
{
    @GetMapping("/overview")
    public String getOverview()
    {
        return "overview";
    }
}
