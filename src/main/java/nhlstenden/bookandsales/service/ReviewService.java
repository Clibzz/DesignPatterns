package nhlstenden.bookandsales.service;

import nhlstenden.bookandsales.model.Review;
import nhlstenden.bookandsales.util.DatabaseUtil;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public ArrayList<Review> getAllReviewsConnectedToBookDetailsId(int bookId) throws SQLException
    {
        ArrayList<Review> reviews = new ArrayList<>();

        String query = "SELECT review.id, review.user_id, review.book_id, review.title, " +
                       "review.rating, review.text, review.image, user.first_name " +
                       "FROM review " +
                       "INNER JOIN user ON review.user_id = user.id " +
                       "WHERE review.book_id = ?";

        PreparedStatement statement = this.sqlConnection.prepareStatement(query);

        statement.setInt(1, bookId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next())
        {

            int id = resultSet.getInt(1);
            int userId = resultSet.getInt(2);
            int reviewBookId = resultSet.getInt(3);
            String title = resultSet.getString(4);
            double rating = resultSet.getDouble(5);
            String text = resultSet.getString(6);
            String image = resultSet.getString(7);
            String userName = resultSet.getString(8);

            Review review = new Review(id, userId, reviewBookId, userName, title, rating, text, image);
            reviews.add(review);
        }

        return reviews;
    }

}
