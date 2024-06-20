package nhlstenden.bookandsales.strategy;

import nhlstenden.bookandsales.model.PaymentType;

public interface PaymentStrategy<T> {
    boolean isPayingForCart(double amount);
    void setBalance(double balance);
    T getData(PaymentType paymentType);
    String getPassword();
    double getMoneyAmount();
}
