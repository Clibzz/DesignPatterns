package nhlstenden.bookandsales.Factory;

import nhlstenden.bookandsales.Model.Genre;

public class NormalBooKFactory extends BookFactory
{

    private boolean hasHardCover;

    @Override
    public BookProduct createBookProduct(String title, double price, String author, String publisher,
                                         int pageAmount, Genre genre, boolean hasHardCover, String description, String image)
    {
        return new NormalBook(title, price, author, publisher, pageAmount, genre, hasHardCover, description, image);
    }
}
