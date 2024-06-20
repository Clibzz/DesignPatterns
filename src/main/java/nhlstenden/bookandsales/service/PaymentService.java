package nhlstenden.bookandsales.service;

import nhlstenden.bookandsales.strategy.PaymentStrategy;
import nhlstenden.bookandsales.util.DatabaseUtil;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class PaymentService
{
    private final Connection sqlConnection;
    private PaymentStrategy paymentStrategy;

    public PaymentService() throws SQLException
    {
        this.sqlConnection = DatabaseUtil.getConnection();
    }

    public PaymentStrategy getPaymentStrategy()
    {
        return this.paymentStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy)
    {
        this.paymentStrategy = paymentStrategy;
    }

    public boolean hasCheckedOut(double amount)
    {
        return this.paymentStrategy.isPayingForCart(amount);
    }
}
