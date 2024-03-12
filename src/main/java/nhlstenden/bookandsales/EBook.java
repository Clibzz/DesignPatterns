package nhlstenden.bookandsales;

public class EBook {
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

    public Genre distributeGenre() {

    }
}
