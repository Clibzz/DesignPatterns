package nhlstenden.bookandsales.factory;

import nhlstenden.bookandsales.model.BookType;
import nhlstenden.bookandsales.model.EBook;
import nhlstenden.bookandsales.model.Genre;

public class EBookFactory extends BookFactory
{
    private boolean hasAutomaticReading;

    @Override
    public BookProduct createBookProduct(int id, BookType bookType, String title, double price, String author, String publisher,
                                         int pageAmount, Genre genre, boolean hasAutomaticReading, String description, String image)
    {
        return new EBook(id, bookType, title, price, author, publisher, pageAmount, genre, hasAutomaticReading, description, image);
    }
}
