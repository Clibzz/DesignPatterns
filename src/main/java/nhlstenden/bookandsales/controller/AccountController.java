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

    /**
     * Retrieve the register page
     * @param model The model of the page
     * @return The register page
     */
    @GetMapping("/register")
    public String registerPage(Model model)
    {
        model.addAttribute("page", "register");

        return "register";
    }

    /**
     * Retrieve the login page
     * @param model The model of the page
     * @return The login page
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("page", "login");

        return "login";
    }

    /**
     * Direct a user to the logout page, user gets logged out
     * @param session The current session
     * @return The login page
     * @throws IOException Throws an IOException when deleting the user's personal cart file fails
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) throws IOException
    {
        Path path = Paths.get(session.getAttribute("username") + ".json");
        Files.deleteIfExists(path);
        session.invalidate();

        return "redirect:/login";
    }

    /**
     * Post a new user to the database
     * @param firstName The first name
     * @param lastName The last name
     * @param dateOfBirth The date of birth
     * @param address The address
     * @param password The password
     * @param redirectAttributes RedirectAttributes to show the user success & error messages
     * @return The login page if success, register if failed
     * @throws SQLException Throws a SQLException when something goes wrong inserting the new user in the database
     */
    @PostMapping("/post-new-user")
    public String postNewUser(@RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName,
                              @RequestParam("date_of_birth") LocalDate dateOfBirth, @RequestParam("address") String address,
                              @RequestParam("password") String password, RedirectAttributes redirectAttributes) throws SQLException
    {
        int roleId = 2;
        if (dateOfBirth.isBefore(LocalDate.now()))
        {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            this.accountService.registerNewUser(roleId, firstName, lastName, dateOfBirth, address, passwordEncoder.encode(password));
            redirectAttributes.addFlashAttribute("success", "User has been successfully registered");

            return "redirect:/login";
        }
        else
        {
            redirectAttributes.addFlashAttribute("dateError", "true");

            return "redirect:/register";
        }
    }

    /**
     * Create the general carts.json file which contains all cart information of all users
     * @throws IOException Throws an IOException when creating the file fails
     */
    public void createCartsJson() throws IOException
    {
        Path path = Paths.get("carts.json");
        if (!Files.exists(path))
        {
            Files.createFile(path);
            Files.write(path, Collections.singletonList("[]"));
        }
    }

    /**
     * Create the user's personal cart .json file
     * @throws IOException Throws an IOException when creating the file fails
     */
    public void createUserCart(HttpSession session) throws IOException
    {
        Path path = Paths.get(session.getAttribute("username") + ".json");
        if (!Files.exists(path))
        {
            Files.createFile(path);
            Files.write(path, Collections.singletonList("[]"));
        }
    }

    /**
     * Allow user to login
     * @param userName The username
     * @param userPassword The password
     * @param session The newly made current session
     * @param redirectAttributes The redirectAttributes to show the user error & success messages
     * @return The overview page if success, the login page if failed
     * @throws SQLException Throws a SQLException when checking the user's login details fails
     * @throws IOException Throws an IOException when creating the cart files fails
     */
    @PostMapping("/login")
    public String login(@RequestParam("first_name") String userName, @RequestParam("password") String userPassword,
                        HttpSession session, RedirectAttributes redirectAttributes)
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
