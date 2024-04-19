package nhlstenden.bookandsales;

public class AudioBook {
    private double price;
    private String artist;
    private String publisher;
    private int pageAmount;
    private boolean hasVoiceActor;

    public AudioBook(double price, String artist, String publisher, int pageAmount, boolean hasVoiceActor) {
        this.price = price;
        this.artist = artist;
        this.publisher = publisher;
        this.pageAmount = pageAmount;
        this.hasVoiceActor = hasVoiceActor;
    }

//    public Genre distributeGenre() {
//
//    }
}
