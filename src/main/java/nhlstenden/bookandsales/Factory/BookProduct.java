package nhlstenden.bookandsales.Factory;

import nhlstenden.bookandsales.Model.Genre;

public interface BookProduct
{
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
