package nhlstenden.bookandsales.Factory;

public class BookFactory {
    private double price;
    private String artist;
    private String publisher;
    private int pageAmount;

    public BookFactory(double price, String artist, String publisher, int pageAmount) {
        this.price = price;
        this.artist = artist;
        this.publisher = publisher;
        this.pageAmount = pageAmount;
    }

//    public BookProduct createBookProduct(double price, String artist, String publisher, int pageAmount) {
//
//    }
}
