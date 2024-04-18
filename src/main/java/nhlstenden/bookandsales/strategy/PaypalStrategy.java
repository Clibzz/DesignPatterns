package nhlstenden.bookandsales.strategy;

import java.util.HashMap;

public class PaypalStrategy implements PaymentStrategy
{
    private String userName;
    private String password;
    private final HashMap<String, String> paypalUserList;

    public PaypalStrategy(String userName, String password)
    {
        this.setUserName(userName);
        this.setPassword(password);
        this.paypalUserList = new HashMap<>();
    }

    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName(String userName)
    {
        if (!(userName.isEmpty()))
        {
            this.userName = userName;
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

    public double getMoneyAmountPaypalUser()
    {
        double amountOnPaypal = 0;

        if (this.getPaypalUserList().containsKey(this.getUserName()) &&
            this.getPaypalUserList().get(this.getUserName()).equals(this.getPassword()))
        {
            if (this.getUserName().equals("robin") && this.getPassword().equals("test"))
            {
                amountOnPaypal = 1500.00;
            }

            if (this.getUserName().equals("voyanda") && this.getPassword().equals("scrub"))
            {
                amountOnPaypal = 2000.00;
            }

            if (this.getUserName().equals("admin") && this.getPassword().equals("password"))
            {
                amountOnPaypal = 10000.00;
            }
        }
        else
        {
            throw new IllegalArgumentException("username is wrong, password is wrong or both are wrong");
        }

        return amountOnPaypal;
    }

    @Override
    public void paymentMethod(double amount)
    {
        boolean hasPayed = false;
        double tempAmount;

        if (!(this.getMoneyAmountPaypalUser() < amount))
        {
            tempAmount = (this.getMoneyAmountPaypalUser() - amount);
            if (tempAmount < this.getMoneyAmountPaypalUser())
            {
                hasPayed = true;
            }
        }
        else
        {
            throw new IllegalArgumentException("transaction failed, not enough saldo on account");
        }

        if (hasPayed)
        {
            amount = 0;
        }

        if (amount == 0)
        {
            System.out.println("successfully made the purchase");
        }
    }

}