package nhlstenden.bookandsales.Service;

import nhlstenden.bookandsales.Model.Book;
import nhlstenden.bookandsales.Model.BookType;
import nhlstenden.bookandsales.Model.Genre;
import nhlstenden.bookandsales.util.DatabaseUtil;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class BookService
{

    private final Connection sqlConnection;

    public BookService() throws SQLException
    {
        this.sqlConnection = DatabaseUtil.getConnection();
    }

    public ArrayList<Book> getBookList(int chosenBookTypeId) throws SQLException
    {

        ArrayList<Book> bookList = new ArrayList<>();

        String query = "SELECT * FROM book WHERE book_type_id = ?";

        PreparedStatement statement = this.sqlConnection.prepareStatement(query);

        statement.setInt(1, chosenBookTypeId);

        ResultSet resultSet = statement.executeQuery();

        Book bookModel = null;

        if (resultSet.next())
        {
            int id = resultSet.getInt(1);
            int bookTypeId = resultSet.getInt(2);
            Genre genre = Genre.valueOf(resultSet.getString(3));
            double price = resultSet.getDouble(4);
            String author = resultSet.getString(5);
            String publisher = resultSet.getString(6);
            String title = resultSet.getString(7);
            int pageAmount = resultSet.getInt(8);
            boolean hasHardCover = resultSet.getBoolean(9);

            bookModel = new Book(id, getBookTypeById(bookTypeId), genre, price, author, publisher, title, pageAmount, hasHardCover);

            bookList.add(bookModel);
        }

        return bookList;
    }

    public BookType getBookTypeById(int bookTypeId) throws SQLException
    {

        String query = "SELECT * FROM book_type WHERE id = ?";

        PreparedStatement statement = this.sqlConnection.prepareStatement(query);

        statement.setInt(1, bookTypeId);

        ResultSet resultSet = statement.executeQuery();

        BookType bookTypeModel = null;

        if (resultSet.next())
        {
            int id = resultSet.getInt(1);
            String type = resultSet.getString(2);
            String attributeType = resultSet.getString(3);
            boolean hasAttribute = resultSet.getBoolean(4);

            bookTypeModel = new BookType(id, type, attributeType, hasAttribute);
        }

        return bookTypeModel;
    }

}
