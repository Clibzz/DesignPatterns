package nhlstenden.bookandsales;

import java.util.HashSet;

import nhlstenden.bookandsales.Factory.AudioBook;
import nhlstenden.bookandsales.Factory.EBook;
import nhlstenden.bookandsales.Model.*;

public class Overview {
    private HashSet<EBook> eBookList;
    private HashSet<AudioBook> audioBookList;
    private HashSet<Book> bookList;

    public Overview() {
        this.eBookList = new HashSet<>();
        this.audioBookList = new HashSet<>();
        this.bookList = new HashSet<>();
    }


}
