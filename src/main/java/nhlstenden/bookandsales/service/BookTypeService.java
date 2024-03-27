package nhlstenden.bookandsales.service;


import nhlstenden.bookandsales.Model.Book;
import nhlstenden.bookandsales.Model.BookType;
import nhlstenden.bookandsales.util.DatabaseUtil;
import org.hibernate.annotations.processing.SQL;

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

    public ArrayList<String> getBookTypeTypes() throws SQLException
    {
        ArrayList<BookType> bookTypes = this.getBookTypes();
        ArrayList<String> types = new ArrayList<>();
        for (BookType bookType : bookTypes)
        {
            types.add(bookType.getType());
        }
        return types;
    }

    public ArrayList<BookType> getBookTypes() throws SQLException
    {
        ArrayList<BookType> bookTypes = new ArrayList<>();
        String query = "SELECT * FROM `book_type`";
        Statement statement = sqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next())
        {
            bookTypes.add(new BookType(resultSet.getInt("id"), resultSet.getString("type"), resultSet.getString("attribute_type"), resultSet.getBoolean("has_attribute")));
        }
        return bookTypes;
    }
}
