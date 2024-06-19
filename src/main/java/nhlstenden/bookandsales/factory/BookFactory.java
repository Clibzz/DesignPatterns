package nhlstenden.bookandsales.factory;

import nhlstenden.bookandsales.model.*;

public class BookFactory
{
    public BookFactory() {}

    public BookProduct createBookProduct(int id, BookType bookType, String title, double price, String author, String publisher,
                                                  int pageAmount, Genre genre, boolean bookCondition, String description, String image)
    {
        return switch (bookType.getType())
        {
            case "EBook" ->
                    new EBook(id, bookType, title, price, author, publisher, pageAmount, genre, bookCondition, description, image);
            case "AudioBook" ->
                    new AudioBook(id, bookType, title, price, author, publisher, pageAmount, genre, bookCondition, description, image);
            case "NormalBook" ->
                    new NormalBook(id, bookType, title, price, author, publisher, pageAmount, genre, bookCondition, description, image);
            default -> throw new IllegalArgumentException("Unknown book type: " + bookType.getType());
        };
    }
}
