package nhlstenden.bookandsales.service;

import nhlstenden.bookandsales.Model.User;
import nhlstenden.bookandsales.util.DatabaseUtil;
import org.springframework.web.bind.annotation.RestController;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class GetService
{
    private final Connection sqlConnection;

    public GetService() throws SQLException
    {
        this.sqlConnection = DatabaseUtil.getConnection();
    }

    public User getLoginInfo(String userName, String userPassword) throws SQLException
    {

        String query = "{ SELECT * FROM user WHERE first_name = ? AND password = ? }";

        CallableStatement statement = this.sqlConnection.prepareCall(query);

        statement.setString(1, userName);
        statement.setString(2, userPassword);

        ResultSet resultSet = statement.getResultSet();

        User userModel = null;

        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            LocalDate dateOfBirth = resultSet.getDate(4).toLocalDate();
            String address = resultSet.getString(5);
            String password = resultSet.getString(6);

            userModel = new User(id, firstName, lastName, dateOfBirth, address, password);
        }

        return userModel;
    }

}
