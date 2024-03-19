package nhlstenden.bookandsales;

import nhlstenden.bookandsales.Model.Genre;

public class EBook implements BookProduct{
    private double price;
    private String artist;
    private String publisher;
    private int pageAmount;
    private boolean hasAutomaticReading;

    public EBook(double price, String artist, String publisher, int pageAmount, boolean hasAutomaticReading) {
        this.price = price;
        this.artist = artist;
        this.publisher = publisher;
        this.pageAmount = pageAmount;
        this.hasAutomaticReading = hasAutomaticReading;
    }

    @Override
    public void create() {

    }
}
