package nhlstenden.bookandsales.Service;

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
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            LocalDate dateOfBirth = resultSet.getDate(4).toLocalDate();
            String address = resultSet.getString(5);
            String password = resultSet.getString(6);

            userModel = new User(id, firstName, lastName, dateOfBirth, address, password);
        }

        resultSet.close();
        statement.close();

        return userModel;
    }

}
