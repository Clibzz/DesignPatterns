package nhlstenden.bookandsales.service;

import nhlstenden.bookandsales.model.User;
import nhlstenden.bookandsales.util.DatabaseUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.stream.StreamSupport;

@Service
public class AccountService
{
    private final Connection sqlConnection;

    public AccountService() throws SQLException
    {
        this.sqlConnection = DatabaseUtil.getConnection();
    }

    public void registerNewUser(int roleId, String firstName, String lastName, LocalDate dateOfBirth, String address, String password) throws SQLException
    {
        String query = "INSERT INTO user(`role_id`, `first_name`, `last_name`, `date_of_birth`, `address`, `password`)" +
                "VALUES (?,?,?,?,?,?)";

        PreparedStatement statement = sqlConnection.prepareStatement(query);
        statement.setInt(1, roleId);
        statement.setString(2, firstName.toLowerCase());
        statement.setString(3, lastName);
        statement.setDate(4, Date.valueOf(dateOfBirth));
        statement.setString(5, address);
        statement.setString(6, password);
        statement.executeUpdate();
    }

    public User getLoginInfo(String username, String password) throws SQLException
    {

        String query = "SELECT * FROM user WHERE first_name = ?";

        PreparedStatement statement = this.sqlConnection.prepareStatement(query);

        statement.setString(1, username.toLowerCase());

        ResultSet resultSet = statement.executeQuery();

        User userModel = null;

        if (resultSet.next())
        {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (passwordEncoder.matches(password, resultSet.getString("password")))
            {
                userModel = getUser(resultSet);
            }
        }

        resultSet.close();
        statement.close();

        return userModel;
    }

    public static User getUser(ResultSet resultSet) throws SQLException
    {
        User userModel;
        int id = resultSet.getInt(1);
        int role_id = resultSet.getInt(2);
        String firstName = resultSet.getString(3);
        String lastName = resultSet.getString(4);
        LocalDate dateOfBirth = resultSet.getDate(5).toLocalDate();
        String address = resultSet.getString(6);
        String password = resultSet.getString(7);
        userModel = new User(id, role_id, firstName, lastName, dateOfBirth, address, password);

        return userModel;
    }

    public User getUserById(int userId) throws SQLException
    {
        String query = "SELECT * FROM `user` WHERE `id` = ?";
        PreparedStatement statement = this.sqlConnection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery(query);
        User user = null;

        while (resultSet.next())
        {
            user = AccountService.getUser(resultSet);
        }

        return user;
    }
}
