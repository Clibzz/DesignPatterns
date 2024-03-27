package nhlstenden.bookandsales.service;

import nhlstenden.bookandsales.Model.BookType;
import nhlstenden.bookandsales.Model.Genre;
import nhlstenden.bookandsales.util.DatabaseUtil;

import javax.swing.*;
import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;

public class PostService
{
    private final Connection sqlConnection;

    public PostService() throws SQLException
    {
        this.sqlConnection = DatabaseUtil.getConnection();
    }

    public void registerNewUser(String firstName, String lastName, LocalDate dateOfBirth, String address, String password) throws SQLException
    {

        String query = "INSERT INTO user(`first_name`, `last_name`, `date_of_birth`, `address`, `password`)" +
                "VALUES (?,?,?,?,?)";

        PreparedStatement statement = sqlConnection.prepareStatement(query);

        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setDate(3, Date.valueOf(dateOfBirth));
        statement.setString(4, address);
        statement.setString(5, password);

        statement.executeUpdate();
    }

    public boolean addNewBook(String bookType, Genre genre, double price, String author, String publisher, String title, int pageAmount, boolean hasHardCover) throws SQLException
    {
        String selectQuery = "SELECT * FROM `book_type` WHERE `type` = '" + bookType + "'";
        Statement selectStatement = sqlConnection.createStatement();
        ResultSet resultSet = selectStatement.executeQuery(selectQuery);

        if (resultSet.next())
        {
            String query = "INSERT INTO book(`book_type_id`, `genre`, `price`, `author`, `publisher`, `title`, `page_amount`, `has_hard_cover`)" +
                    "VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement statement = sqlConnection.prepareStatement(query);

            statement.setInt(1, resultSet.getInt("id"));
            statement.setString(2, genre.name());
            statement.setDouble(3, price);
            statement.setString(4, author);
            statement.setString(5, publisher);
            statement.setString(6, title);
            statement.setInt(7, pageAmount);
            statement.setBoolean(8, hasHardCover);

            statement.executeUpdate();
            return true;
        }
        return false;

    }
}
