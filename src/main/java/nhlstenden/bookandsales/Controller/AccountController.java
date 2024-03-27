package nhlstenden.bookandsales.Controller;

import nhlstenden.bookandsales.Model.User;
import nhlstenden.bookandsales.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.time.LocalDate;

@Controller
public class AccountController
{
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService)
    {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String registerPage(Model model)
    {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @PostMapping("/post-new-user")
    public String postNewUser(@RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName
            , @RequestParam("address") String address, @RequestParam("password") String password) throws SQLException
    {

        this.accountService.registerNewUser(firstName, lastName, LocalDate.now(), address, password);

        return "redirect:/login";
    }

    @PostMapping("/login-user")
    public String login(@RequestParam("first_name") String userName, @RequestParam("password") String userPassword, Model model) throws SQLException
    {

        User loginInfo = this.accountService.getLoginInfo(userName, userPassword);

        if (loginInfo != null)
        {
            return "redirect:/overview";
        }
        else
        {
            model.addAttribute("error", "Invalid username or password");

            return "redirect:/login";
        }
    }

}
