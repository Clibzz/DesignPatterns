package nhlstenden.bookandsales.strategy;

import nhlstenden.bookandsales.model.PaymentType;

import java.util.HashMap;

public class PaypalStrategy implements PaymentStrategy<HashMap<String, String>>
{
    private String username;
    private String password;
    private final HashMap<String, String> paypalUserList = new HashMap<>();
    private double balance;

    public PaypalStrategy(String username, String password)
    {
        this.setUsername(username);
        this.setPassword(password);
        this.initializeUserList();
    }

    public void initializeUserList()
    {
        paypalUserList.put("robin", "test");
        paypalUserList.put("voyanda", "scrub");
        paypalUserList.put("admin", "password");
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
        if (paymentType == PaymentType.PAYPAL_USER_LIST)
        {
            return new HashMap<>(paypalUserList);
        }

        return null;
    }

    @Override
    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    @Override
    public double getMoneyAmount() {
        if (this.paypalUserList.containsKey(this.username) && this.paypalUserList.get(this.username).equals(this.password)) {
            if (this.username.equals("robin") && this.password.equals("test")) {
                this.balance = 2.00;
            } else if (this.username.equals("voyanda") && this.password.equals("scrub")) {
                this.balance = 2000.00;
            } else if (this.username.equals("admin") && this.password.equals("password")) {
                this.balance = 10000.00;
            }
        } else {
            return -1;
        }

        return this.balance;
    }


    @Override
    public boolean isPayingForCart(double amount)
    {
        if (this.balance >= amount)
        {
            this.setBalance(this.balance - amount);

            return true;
        }

        return false;
    }
}