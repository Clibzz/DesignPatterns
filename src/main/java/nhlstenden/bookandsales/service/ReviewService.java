package nhlstenden.bookandsales.service;

import nhlstenden.bookandsales.util.DatabaseUtil;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class ReviewService
{

    private Connection sqlConnection;

    public ReviewService() throws SQLException
    {
        this.sqlConnection = DatabaseUtil.getConnection();
    }

    public void addReview(int userId, int bookId, String title, double rating, String description, String image) throws SQLException
    {

        String query = "INSERT INTO review(`user_id`, `book_id`, `title`, `rating`, `text`, `image`)" +
                       "VALUES(?,?,?,?,?,?)";

        PreparedStatement statement = this.sqlConnection.prepareStatement(query);

        statement.setInt(1, userId);
        statement.setInt(2, bookId);
        statement.setString(3, title);
        statement.setDouble(4, rating);
        statement.setString(5, description);
        statement.setString(6, image);

        statement.executeUpdate();
    }

}
