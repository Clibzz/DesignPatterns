package nhlstenden.bookandsales;

import java.util.HashSet;
import nhlstenden.bookandsales.model.*;

public class BookStore {
    private HashSet<PaymentCart> paymentCartList;
    private HashSet<User> userList;
    private Overview overview;

    public BookStore(Overview overview) {
        this.paymentCartList = new HashSet<PaymentCart>();
        this.userList = new HashSet<>();
        this.overview = overview;
    }
}
