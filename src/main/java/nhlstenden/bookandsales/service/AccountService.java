package nhlstenden.bookandsales.service;

import nhlstenden.bookandsales.Model.User;
import nhlstenden.bookandsales.util.DatabaseUtil;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;

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
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setDate(4, Date.valueOf(dateOfBirth));
        statement.setString(5, address);
        statement.setString(6, password);

        statement.executeUpdate();
    }

    public User getLoginInfo(String userName, String userPassword) throws SQLException
    {

        String query = "SELECT * FROM user WHERE first_name = ? AND password = ?";

        PreparedStatement statement = this.sqlConnection.prepareStatement(query);

        statement.setString(1, userName);
        statement.setString(2, userPassword);

        ResultSet resultSet = statement.executeQuery();

        User userModel = null;

        if (resultSet.next())
        {
            int id = resultSet.getInt(1);
            int role_id = resultSet.getInt(2);
            String firstName = resultSet.getString(3);
            String lastName = resultSet.getString(4);
            LocalDate dateOfBirth = resultSet.getDate(5).toLocalDate();
            String address = resultSet.getString(6);
            String password = resultSet.getString(7);

            userModel = new User(id, role_id, firstName, lastName, dateOfBirth, address, password);
        }

        resultSet.close();
        statement.close();

        return userModel;
    }

}
