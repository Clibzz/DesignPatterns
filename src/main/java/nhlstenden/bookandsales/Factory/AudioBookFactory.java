package nhlstenden.bookandsales.Factory;

import nhlstenden.bookandsales.Model.Genre;

public class AudioBookFactory extends BookFactory
{

    private boolean hasVoiceActor;

    @Override
    public BookProduct createBookProduct(String title, double price, String author, String publisher,
                                         int pageAmount, Genre genre, boolean hasVoiceActor, String description, String image)
    {
        return new AudioBook(title, price, author, publisher, pageAmount, genre, hasVoiceActor, description, image);
    }
}
