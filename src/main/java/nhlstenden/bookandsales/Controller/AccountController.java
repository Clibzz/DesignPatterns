package nhlstenden.bookandsales.Controller;

import jakarta.servlet.http.HttpServletRequest;
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
        model.addAttribute("page", "register");
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("page", "login");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }

    @PostMapping("/post-new-user")
    public String postNewUser(@RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName
            , @RequestParam("date_of_birth") LocalDate dateOfBirth, @RequestParam("address") String address,
                              @RequestParam("password") String password, Model model) throws SQLException
    {
        int roleId = 2;
        if (dateOfBirth.isBefore(LocalDate.now()))
        {
            this.accountService.registerNewUser(roleId, firstName, lastName, dateOfBirth, address, password);
            return "redirect:/login";
        }
        else
        {
            model.addAttribute("dateError", "true");
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam("first_name") String userName, @RequestParam("password") String userPassword, Model model) throws SQLException
    {
        User loginInfo = this.accountService.getLoginInfo(userName, userPassword, model);

        if (loginInfo != null)
        {
            model.addAttribute("roleId", loginInfo.getRoleId());
            model.addAttribute("user", loginInfo);
            return "redirect:/overview";
        }
        else
        {
            model.addAttribute("error", "true");
            return "login";
        }
    }

}
