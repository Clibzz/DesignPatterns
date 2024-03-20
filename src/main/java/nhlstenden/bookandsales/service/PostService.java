package nhlstenden.bookandsales.service;

import nhlstenden.bookandsales.util.DatabaseUtil;

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
}
