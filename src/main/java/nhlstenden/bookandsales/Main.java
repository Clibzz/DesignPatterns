package nhlstenden.bookandsales;

import nhlstenden.bookandsales.Model.*;

public class Main {
    public static void main(String[] args) {
        BookType bookType = new BookType(1, "AudioBook", "HasAutomaticReading", true);
        Book book = new Book(1, bookType, "test", Genre.HORROR, 12, "test", "test", "TEST", 123, "test");
        Book newBook = new Book(2, bookType, "AAAA", Genre.HORROR, 12, "AAAA", "AAAA", "AAAA", 123, "AAAA");

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
