package nhlstenden.bookandsales;

import nhlstenden.bookandsales.Factory.AudioBook;
import nhlstenden.bookandsales.Factory.BookProduct;
import nhlstenden.bookandsales.Model.*;

public class Main {
    public static void main(String[] args) {
        BookType bookType = new BookType(1, "AudioBook", "HasAutomaticReading", true);
        BookProduct book = new AudioBook(1, bookType, "test", 1, "test", "test", 123, Genre.HORROR, true, "test", "mario.png");
        BookProduct newBook = new AudioBook(2, bookType, "test", 1, "test", "test", 123, Genre.HORROR, true, "test", "mario.png");

        PaymentCart paymentCart = new PaymentCart();
        PaymentCartHistory history = new PaymentCartHistory();
        System.out.println(paymentCart.getCartDetails());

        history.saveState(paymentCart.save());
        paymentCart.appendCartDetails(book);
        System.out.println(paymentCart.getCartDetails());

        paymentCart.appendCartDetails(newBook);
        System.out.println(paymentCart.getCartDetails());


        PaymentCartMemento memento = history.restoreState();
        if (memento != null)
        {
            paymentCart.restore(memento);
        }
        System.out.println(paymentCart.getCartDetails());
    }
}
