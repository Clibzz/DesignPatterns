package nhlstenden.bookandsales.service;


import nhlstenden.bookandsales.util.DatabaseUtil;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;

public class BookTypeService
{

    private final Connection sqlConnection;

    public BookTypeService() throws SQLException
    {
        this.sqlConnection = DatabaseUtil.getConnection();
    }

    public ArrayList<String> getBookTypes() throws SQLException
    {
        ArrayList<String> bookTypes = new ArrayList<>();
        String query = "SELECT `type` FROM `book_type`";
        Statement statement = sqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            String bookType = resultSet.getString("type");
            bookTypes.add(bookType);
        }
        return bookTypes;
    }
}
