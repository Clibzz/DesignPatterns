package nhlstenden.bookandsales;

import nhlstenden.bookandsales.Model.Genre;

public class Book implements BookProduct {
    private double price;
    private String artist;
    private String publisher;
    private int pageAmount;
    private boolean hasHardCover;

    public Book(double price, String artist, String publisher, int pageAmount, boolean hasHardCover) {
        this.price = price;
        this.artist = artist;
        this.publisher = publisher;
        this.pageAmount = pageAmount;
        this.hasHardCover = hasHardCover;
    }

    public Book() {}

    @Override
    public void create() {

    }
}
