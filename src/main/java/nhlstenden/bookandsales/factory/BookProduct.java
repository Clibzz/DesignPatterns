package nhlstenden.bookandsales.factory;

import nhlstenden.bookandsales.model.BookType;
import nhlstenden.bookandsales.model.Genre;

public interface BookProduct
{
    int getId();
    BookType getBookType();
    String getTitle();
    double getPrice();
    String getAuthor();
    String getPublisher();
    int getPageAmount();
    Genre getGenre();
    boolean getBookCondition();
    String getDescription();
    String getImage();
}
