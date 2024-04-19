package nhlstenden.bookandsales.service;
import nhlstenden.bookandsales.factory.*;
import nhlstenden.bookandsales.model.BookType;
import nhlstenden.bookandsales.model.Genre;
import nhlstenden.bookandsales.util.DatabaseUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;

@Service
public class BookService
{
    private final Connection sqlConnection;
    private BookFactory bookFactory;

    public BookService() throws SQLException
    {
        this.sqlConnection = DatabaseUtil.getConnection();
        this.bookFactory = null;
    }

    public void addNewBook(String bookType, String description, Genre genre, double price, String author, String publisher, String title, int pageAmount, MultipartFile image) throws SQLException
    {
        String selectQuery = "SELECT * FROM `book_type` WHERE `type` = '" + bookType + "'";
        Statement selectStatement = sqlConnection.createStatement();
        ResultSet resultSet = selectStatement.executeQuery(selectQuery);

        if (resultSet.next())
        {
            String query = "INSERT INTO book(`book_type_id`, `description`, `genre`, `price`, `author`, `publisher`, `title`, `page_amount`, `image`)" +
                    "VALUES (?,?,?,?,?,?,?,?,?)";

            PreparedStatement statement = sqlConnection.prepareStatement(query);

            statement.setInt(1, resultSet.getInt("id"));
            statement.setString(2, description);
            statement.setString(3, genre.name());
            statement.setDouble(4, price);
            statement.setString(5, author);
            statement.setString(6, publisher);
            statement.setString(7, title);
            statement.setInt(8, pageAmount);
            statement.setString(9, image.getOriginalFilename());

            statement.executeUpdate();
        }
    }

    public ArrayList<BookProduct> getAllBooksRegardlessOfType() throws SQLException
    {

        ArrayList<BookProduct> bookListRegardlessOfType = new ArrayList<>();

        String query = "SELECT * FROM book";

        PreparedStatement statement = this.sqlConnection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next())
        {
            int id = resultSet.getInt(1);
            int bookTypeId = resultSet.getInt(2);
            String description = resultSet.getString(3);
            Genre genre = Genre.valueOf(resultSet.getString(4));
            double price = resultSet.getDouble(5);
            String author = resultSet.getString(6);
            String publisher = resultSet.getString(7);
            String title = resultSet.getString(8);
            int pageAmount = resultSet.getInt(9);
            String image = resultSet.getString(10);

            this.setBookFactoryType(bookTypeId);
            bookListRegardlessOfType.add(this.bookFactory.createBookProduct(id, this.getBookTypeById(bookTypeId),
                                        title, price, author, publisher, pageAmount, genre,
                                        this.getBookTypeById(bookTypeId).getHasAttribute(),
                                        description, image ));
        }

        return bookListRegardlessOfType;
    }

    public ArrayList<BookProduct> getBookList(int chosenBookTypeId) throws SQLException
    {

        ArrayList<BookProduct> bookList = new ArrayList<>();

        String query = "SELECT * FROM book WHERE book_type_id = ?";

        PreparedStatement statement = this.sqlConnection.prepareStatement(query);

        statement.setInt(1, chosenBookTypeId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next())
        {
            int id = resultSet.getInt(1);
            int bookTypeId = resultSet.getInt(2);
            String description = resultSet.getString(3);
            Genre genre = Genre.valueOf(resultSet.getString(4));
            double price = resultSet.getDouble(5);
            String author = resultSet.getString(6);
            String publisher = resultSet.getString(7);
            String title = resultSet.getString(8);
            int pageAmount = resultSet.getInt(9);
            String image = resultSet.getString(10);

            this.setBookFactoryType(bookTypeId);
            bookList.add(this.bookFactory.createBookProduct(id, getBookTypeById(bookTypeId),
                        title, price, author, publisher, pageAmount, genre, getBookTypeById(bookTypeId).getHasAttribute(),
                        description, image));
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

    public BookProduct getBookById(int bookId) throws SQLException
    {
        String query = "SELECT * FROM `book` WHERE `id` = ?";

        PreparedStatement statement = this.sqlConnection.prepareStatement(query);
        statement.setInt(1, bookId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next())
        {
            int id = resultSet.getInt(1);
            int bookTypeId = resultSet.getInt(2);
            String description = resultSet.getString(3);
            Genre genre = Genre.valueOf(resultSet.getString(4));
            double price = resultSet.getDouble(5);
            String author = resultSet.getString(6);
            String publisher = resultSet.getString(7);
            String title = resultSet.getString(8);
            int pageAmount = resultSet.getInt(9);
            String image = resultSet.getString(10);

            this.setBookFactoryType(bookTypeId);

            return this.bookFactory.createBookProduct(id, this.getBookTypeById(bookTypeId), title,
                                                    price, author, publisher, pageAmount, genre,
                                                    this.getBookTypeById(bookTypeId).getHasAttribute(),
                                                    description, image);
        }
        return null;
    }

    public int getLastInsertedId() throws SQLException
    {
        String query = "SELECT LAST_INSERT_ID()";
        PreparedStatement statement = this.sqlConnection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next())
        {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public void setBookFactoryType(int typeId)
    {
        switch (typeId)
        {
            case 1:
                this.bookFactory = new EBookFactory();
                break;
            case 2:
                this.bookFactory = new AudioBookFactory();
                break;
            case 3:
                this.bookFactory = new NormalBookFactory();
                break;
        }
    }
}
