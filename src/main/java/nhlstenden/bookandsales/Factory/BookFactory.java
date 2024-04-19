package nhlstenden.bookandsales.Factory;

import nhlstenden.bookandsales.Model.BookType;
import nhlstenden.bookandsales.Model.Genre;

public abstract class BookFactory
{
    public BookFactory() {}

    public abstract BookProduct createBookProduct(int id, BookType bookType, String title, double price, String author, String publisher,
                                                  int pageAmount, Genre genre, boolean bookCondition, String description, String image);
}
