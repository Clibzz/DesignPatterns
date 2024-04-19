package nhlstenden.bookandsales.Factory;

import nhlstenden.bookandsales.Model.BookType;
import nhlstenden.bookandsales.Model.Genre;

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
