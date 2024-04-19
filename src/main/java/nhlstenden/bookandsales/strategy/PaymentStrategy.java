package nhlstenden.bookandsales.strategy;

public interface PaymentStrategy {
    boolean payForCurrentCart(double amount);
}
