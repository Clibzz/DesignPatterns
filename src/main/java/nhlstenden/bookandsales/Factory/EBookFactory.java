package nhlstenden.bookandsales.Factory;

import nhlstenden.bookandsales.Model.Genre;

public class EBookFactory extends BookFactory
{
    private boolean hasAutomaticReading;

    @Override
    public BookProduct createBookProduct(String title, double price, String author, String publisher,
                                         int pageAmount, Genre genre, boolean hasAutomaticReading, String description, String image)
    {
        return new EBook(title, price, author, publisher, pageAmount, genre, hasAutomaticReading, description, image);
    }
}
