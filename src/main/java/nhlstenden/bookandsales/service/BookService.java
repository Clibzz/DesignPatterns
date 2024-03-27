package nhlstenden.bookandsales.service;

import nhlstenden.bookandsales.Model.Genre;
import nhlstenden.bookandsales.util.DatabaseUtil;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;

@Service
public class BookService
{
    private final Connection sqlConnection;

    public BookService() throws SQLException
    {
        this.sqlConnection = DatabaseUtil.getConnection();
    }

    public void addNewBook(String bookType, Genre genre, double price, String author, String publisher, String title, int pageAmount, boolean hasHardCover) throws SQLException
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
        }
    }
}
