package nhlstenden.bookandsales.factory;

import nhlstenden.bookandsales.model.BookType;
import nhlstenden.bookandsales.model.Genre;

public abstract class BookFactory
{
    public BookFactory() {}

    public abstract BookProduct createBookProduct(int id, BookType bookType, String title, double price, String author, String publisher,
                                                  int pageAmount, Genre genre, boolean bookCondition, String description, String image);
}
