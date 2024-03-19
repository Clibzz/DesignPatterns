package nhlstenden.bookandsales.Factory;

import nhlstenden.bookandsales.Book;
import nhlstenden.bookandsales.BookProduct;

public abstract class BookFactory {
    private double price;
    private String artist;
    private String publisher;
    private int pageAmount;
    private boolean hasHardCover;

    public BookFactory(double price, String artist, String publisher, int pageAmount, boolean hasHardCover) {
        this.price = price;
        this.artist = artist;
        this.publisher = publisher;
        this.pageAmount = pageAmount;
        this.hasHardCover = hasHardCover;
    }

    public BookProduct createBookProduct(double price, String artist, String publisher, int pageAmount, boolean hasHardCover) {
        BookProduct bookProduct = createBook();
        bookProduct.create();
        return bookProduct;
    }

    protected abstract Book createBook();
}
