package nhlstenden.bookandsales.Factory;

import nhlstenden.bookandsales.Model.Genre;

public abstract class BookFactory
{
    public BookFactory() {}

    public abstract BookProduct createBookProduct(String title, double price, String author, String publisher,
                                                  int pageAmount, Genre genre, boolean bookCondition, String description, String image);
}
