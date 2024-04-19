package nhlstenden.bookandsales.Factory;

import nhlstenden.bookandsales.Model.BookType;
import nhlstenden.bookandsales.Model.Genre;

public class AudioBookFactory extends BookFactory
{

    private boolean hasVoiceActor;

    @Override
    public BookProduct createBookProduct(int id, BookType bookType, String title, double price, String author, String publisher,
                                         int pageAmount, Genre genre, boolean hasVoiceActor, String description, String image)
    {
        return new AudioBook(id, bookType, title, price, author, publisher, pageAmount, genre, hasVoiceActor, description, image);
    }
}
