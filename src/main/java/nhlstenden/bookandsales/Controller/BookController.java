package nhlstenden.bookandsales.Controller;

import nhlstenden.bookandsales.Model.Genre;
import nhlstenden.bookandsales.service.BookTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;

@Controller
public class BookController {
    @GetMapping("/addBook")
    public String addBook(Model model) throws SQLException
    {
        model.addAttribute("bookTypes", this.getBookTypes());
        model.addAttribute("enumValues", Genre.values());
        return "addBook";
    }

    public ArrayList<String> getBookTypes() throws SQLException
    {
        BookTypeService bookTypeService = new BookTypeService();
        return bookTypeService.getBookTypes();
    }
}
