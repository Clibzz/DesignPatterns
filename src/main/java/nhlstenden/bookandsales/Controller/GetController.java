package nhlstenden.bookandsales.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetController
{

    @GetMapping("/login-user")
    public String login(@RequestParam("first_name") String userName, @RequestParam("password") String userPassword) {

        return "login";
    }

}
