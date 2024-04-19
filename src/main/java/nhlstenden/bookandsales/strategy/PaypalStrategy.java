package nhlstenden.bookandsales.strategy;

import java.util.HashMap;

public class PaypalStrategy implements PaymentStrategy
{
    private String username;
    private String password;
    private final HashMap<String, String> paypalUserList;
    private double balance;

    public PaypalStrategy(String username, String password)
    {
        this.setUsername(username);
        this.setPassword(password);
        this.paypalUserList = new HashMap<>();
        this.balance = this.getMoneyAmountPaypalUser();
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        if (!(username.isEmpty()))
        {
            this.username = username;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        if (!(password.isEmpty()))
        {
            this.password = password;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public HashMap<String, String> getPaypalUserList()
    {
        this.paypalUserList.put("robin", "test");
        this.paypalUserList.put("voyanda", "scrub");
        this.paypalUserList.put("admin", "password");

        return this.paypalUserList;
    }

    public double getBalance()
    {
        return this.balance;
    }

    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    public double getMoneyAmountPaypalUser()
    {
        double amountOnPaypal = 0;

        if (this.getPaypalUserList().containsKey(this.getUsername()) &&
            this.getPaypalUserList().get(this.getUsername()).equals(this.getPassword()))
        {
            if (this.getUsername().equals("robin") && this.getPassword().equals("test"))
            {
                amountOnPaypal = 1500.00;
            }

            if (this.getUsername().equals("voyanda") && this.getPassword().equals("scrub"))
            {
                amountOnPaypal = 2000.00;
            }

            if (this.getUsername().equals("admin") && this.getPassword().equals("password"))
            {
                amountOnPaypal = 10000.00;
            }
        }
        else
        {
            throw new IllegalArgumentException("The login details are incorrect, please try again!");
        }

        return amountOnPaypal;
    }

    @Override
    public boolean payForCurrentCart(double amount)
    {
        if (this.balance >= amount)
        {
            this.setBalance(this.balance - amount);

            return true;
        }
        else
        {
            throw new IllegalArgumentException("Transaction failed, not enough money on this account");
        }
    }
}