package nhlstenden.bookandsales.strategy;

import nhlstenden.bookandsales.model.PaymentType;

public interface PaymentStrategy<T> {
    boolean payForCart(double amount);
    double getBalance();
    void setBalance(double balance);
    T getData(PaymentType paymentType);
    String getPassword();
    double getMoneyAmount();
}
