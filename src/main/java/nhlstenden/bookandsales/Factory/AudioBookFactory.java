package nhlstenden.bookandsales.Factory;

import nhlstenden.bookandsales.AudioBook;
import nhlstenden.bookandsales.Book;

public class AudioBookFactory extends BookFactory {
    private double price;
    private String artist;
    private String publisher;
    public int pageAmount;
    public boolean hasVoiceActor;

    public AudioBookFactory(double price, String artist, String publisher, int pageAmount, boolean hasVoiceActor) {
        super(price, artist, publisher, pageAmount, hasVoiceActor);
        this.price = price;
        this.artist = artist;
        this.publisher = publisher;
        this.pageAmount = pageAmount;
        this.hasVoiceActor = hasVoiceActor;
    }

    @Override
    protected Book createBook() {
        return new AudioBook(this.price, this.artist, this.publisher, this.pageAmount, this.hasVoiceActor);
    }
}
