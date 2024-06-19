package nhlstenden.bookandsales.strategy;

import nhlstenden.bookandsales.model.PaymentType;

public class INGStrategy implements PaymentStrategy
{
    private String bankNumber;
    private String username;
    private String password;
    private double balance;

    public INGStrategy(String bankNumber, String username, String password)
    {
        this.setBankNumber(bankNumber);
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getBankNumber()
    {
        return this.bankNumber;
    }

    public void setBankNumber(String bankNumber)
    {
        if (!(bankNumber.isEmpty()))
        {
            this.bankNumber = bankNumber;
        }
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
    public String[][] getData(PaymentType paymentType) {
        if (paymentType == PaymentType.ING_LIST) {
            return new String[][]{
                    {"test", "test", "NL42INGB4416709382"},
                    {"voyanda", "scrub", "NL56INGB6733307944"},
                    {"admin", "password", "NL29INGB5082680188"}
            };
        }
        else
        {
            return null;
        }
    }

    @Override
    public double getMoneyAmount() {
        String[][] ingList = this.getData(PaymentType.ING_LIST);

        for (String[] userData : ingList)
        {
            if (userData[0].equals(this.getUsername()) &&
                    userData[1].equals(this.getPassword()) &&
                    userData[2].equals(this.getBankNumber()))
            {

                // Credentials matched, update balance based on bank number
                String bankNumber = this.getBankNumber();
                switch (bankNumber)
                {
                    case "NL42INGB4416709382" -> this.balance = 2.00;
                    case "NL56INGB6733307944" -> this.balance = 1000.00;
                    case "NL29INGB5082680188" -> this.balance = 10000.00;
                }

                // Return the updated balance
                return this.balance;
            }
        }

        return -1;
    }


    @Override
    public boolean payForCart(double amount)
    {
        if (this.balance >= amount)
        {
            this.setBalance(this.getMoneyAmount() - amount);

            return true;
        }

        return false;
    }
}
