package nhlstenden.bookandsales.Controller;

import nhlstenden.bookandsales.service.PostService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.time.LocalDate;

@RestController
public class PostController
{

    @PostMapping("/post-new-user")
    public String postNewUser(@RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName
                                  ,@RequestParam("address") String address, @RequestParam("password") String password) throws SQLException
    {
        PostService postService = new PostService();

        postService.registerNewUser(firstName, lastName, LocalDate.now(), address, password);

        return "success";
    }

}
