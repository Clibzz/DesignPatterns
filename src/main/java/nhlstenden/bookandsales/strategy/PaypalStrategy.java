package nhlstenden.bookandsales.strategy;

import nhlstenden.bookandsales.model.PaymentType;

import java.util.HashMap;

public class PaypalStrategy implements PaymentStrategy<HashMap<String, String>>
{
    private String username;
    private String password;
    private HashMap<String, String> paypalUserList = new HashMap<>();
    private double balance;
    private String errorMessage = "";

    public PaypalStrategy(String username, String password)
    {
        this.setUsername(username);
        this.setPassword(password);
        this.balance = this.getMoneyAmount();
        paypalUserList.put("robin", "test");
        paypalUserList.put("voyanda", "scrub");
        paypalUserList.put("admin", "password");
    }

    public void setErrorMessage(String message)
    {
        this.errorMessage = message;
    }

    @Override
    public String getErrorMessage()
    {
        return this.errorMessage;
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
    }

    @Override
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
    }

    @Override
    public HashMap<String, String> getData(PaymentType paymentType)
    {
        return new HashMap<>(paypalUserList);
    }

    @Override
    public double getBalance()
    {
        return this.balance;
    }

    @Override
    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    @Override
    public double getMoneyAmount()
    {
        this.errorMessage = "";
        double amountOnPaypal = 0;

        if (this.getData(PaymentType.PAYPAL_USER_LIST).containsKey(this.getUsername()) &&
            this.getData(PaymentType.PAYPAL_USER_LIST).get(this.getUsername()).equals(this.getPassword()))
        {
            if (this.getUsername().equals("robin") && this.getPassword().equals("test"))
            {
                amountOnPaypal = 2;
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
            this.setErrorMessage("Invalid account details, please try again!");
        }
        return amountOnPaypal;
    }

    @Override
    public boolean payForCart(double amount)
    {
        System.out.println(amount + " A " + this.balance);
        this.errorMessage = "";
        if (this.balance >= amount)
        {
            this.setBalance(this.balance - amount);

            return true;
        }
        this.setErrorMessage("Transaction failed, not enough money on this account");
        return false;
    }
}