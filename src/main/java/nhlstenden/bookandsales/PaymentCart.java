package nhlstenden.bookandsales;

import java.util.HashSet;
import java.util.ArrayList;

public class PaymentCart {
    private HashSet<String> images;
    private HashSet<String> titles;
    private ArrayList<Double> prices;
    private ArrayList<Integer> amount;

    public PaymentCart() {
        this.images = new HashSet<>();
        this.titles = new HashSet<>();
        this.prices = new ArrayList<>();
        this.amount = new ArrayList<>();
    }

    public Memento takeSnapshot() {

    }

    public void restore(Memento memento) {

    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {

    }

    public PaymentStrategy executePaymentStrategy() {

    }
}
