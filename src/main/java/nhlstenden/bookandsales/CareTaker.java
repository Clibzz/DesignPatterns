package nhlstenden.bookandsales;

import nhlstenden.bookandsales.model.*;
import java.util.HashSet;

public class CareTaker {
    private HashSet<Memento> history;
    private PaymentCart paymentCart;

    public CareTaker() {
        this.history = new HashSet<>();
    }

    public void makeBackup() {

    }

    public void undo() {

    }
}
