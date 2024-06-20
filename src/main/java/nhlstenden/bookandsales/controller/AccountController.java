package nhlstenden.bookandsales.controller;

import jakarta.servlet.http.HttpSession;
import nhlstenden.bookandsales.model.User;
import nhlstenden.bookandsales.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;

@Controller
public class AccountController
{
    private final AccountService accountService;

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
    public String logout(HttpSession session) throws IOException
    {
        //Get the path of the loggedIn user and their cart
        Path path = Paths.get(session.getAttribute("username") + ".json");
        //Delete the file once the user logs out
        Files.deleteIfExists(path);
        session.invalidate();

        return "redirect:/login";
    }

    @PostMapping("/post-new-user")
    public String postNewUser(@RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName
            , @RequestParam("date_of_birth") LocalDate dateOfBirth, @RequestParam("address") String address,
                              @RequestParam("password") String password, Model model,
                              RedirectAttributes redirectAttributes) throws SQLException
    {
        //set roleId to 2 cause that's the roleId of a regular user
        int roleId = 2;
        if (dateOfBirth.isBefore(LocalDate.now()))
        {
            //encrypting the password
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            this.accountService.registerNewUser(roleId, firstName, lastName, dateOfBirth, address, passwordEncoder.encode(password));

            return "redirect:/login";
        }
        else
        {
            redirectAttributes.addFlashAttribute("dateError", "true");

            return "redirect:/register";
        }
    }

    //Create a json file, once something is added to the cart or a user has something in their cart
    //This is more like a general file for every single user
    public void createCartsJson() throws IOException
    {
        Path path = Paths.get("carts.json");
        if (!Files.exists(path))
        {
            Files.createFile(path);
            Files.write(path, Collections.singletonList("[]"));
        }
    }

    //Same as for the createCartsJson but specifically for a single user
    public void createUserCart(HttpSession session) throws IOException
    {
        Path path = Paths.get(session.getAttribute("username") + ".json");
        if (!Files.exists(path))
        {
            Files.createFile(path);
            Files.write(path, Collections.singletonList("[]"));
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam("first_name") String userName, @RequestParam("password") String userPassword,
                        Model model, HttpSession session, RedirectAttributes redirectAttributes)
            throws SQLException, IOException
    {
        User loginInfo = this.accountService.getLoginInfo(userName, userPassword);

        if (loginInfo != null)
        {
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("roleId", loginInfo.getRoleId());
            session.setAttribute("userId", loginInfo.getId());
            session.setAttribute("username", loginInfo.getFirstName());
            this.createCartsJson();
            this.createUserCart(session);
            
            return "redirect:/overview";
        }
        else
        {
            redirectAttributes.addFlashAttribute("error", "The username or password is incorrect, please try again!");

            return "redirect:/login";
        }
    }
}
