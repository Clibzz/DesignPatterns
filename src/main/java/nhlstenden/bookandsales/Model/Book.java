package nhlstenden.bookandsales.Model;

import jakarta.persistence.Id;
import org.springframework.lang.NonNull;

public class Book {
    @Id
    private int id;
    @NonNull
    private BookType bookTypeId;
    @NonNull
    private Genre genre;
    @NonNull
    private double price;
    @NonNull
    private String author;
    @NonNull
    private String publisher;
    @NonNull
    private String title;
    @NonNull
    private int pageAmount;
    @NonNull
    private String image;

    public Book(int id, BookType bookTypeId, Genre genre, double price, String author, String publisher, String title, int pageAmount, String image) {
        this.id = id;
        this.bookTypeId = bookTypeId;
        this.genre = genre;
        this.price = price;
        this.author = author;
        this.publisher = publisher;
        this.title = title;
        this.pageAmount = pageAmount;
        this.image = image;
    }

    public Book()
    {

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BookType getBookTypeId() {
        return this.bookTypeId;
    }

    public void setBookTypeId(BookType bookTypeId) {
        this.bookTypeId = bookTypeId;
    }

    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPageAmount() {
        return this.pageAmount;
    }

    public void setPageAmount(int pageAmount) {
        this.pageAmount = pageAmount;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
