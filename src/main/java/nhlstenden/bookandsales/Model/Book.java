package nhlstenden.bookandsales.Model;

import jakarta.persistence.Id;
import org.springframework.lang.NonNull;

public class Book {
    @Id
    private int id;
    @NonNull
    private int bookTypeId;
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
    private boolean hasHardCover;

    public Book(int bookTypeId, Genre genre, double price, String author, String publisher, String title, int pageAmount, boolean hasHardCover) {
        this.bookTypeId = bookTypeId;
        this.genre = genre;
        this.price = price;
        this.author = author;
        this.publisher = publisher;
        this.title = title;
        this.pageAmount = pageAmount;
        this.hasHardCover = hasHardCover;
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

    public int getBookTypeId() {
        return this.bookTypeId;
    }

    public void setBookTypeId(int bookTypeId) {
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

    public boolean getHasHardCover() {
        return this.hasHardCover;
    }

    public void setHasHardCover(boolean hasHardCover) {
        this.hasHardCover = hasHardCover;
    }
}
