package nhlstenden.bookandsales.strategy;

import nhlstenden.bookandsales.model.PaymentType;

public class INGStrategy implements PaymentStrategy
{
    private String bankNumber;
    private String username;
    private String password;
    private double balance;
    private String errorMessage;

    public INGStrategy(String bankNumber, String username, String password)
    {
        this.setBankNumber(bankNumber);
        this.setUsername(username);
        this.setPassword(password);
        this.balance = this.getMoneyAmount();
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
            this.errorMessage = "Invalid payment type for INGPaymentStrategy";
            return null;
        }
    }

    @Override
    public double getMoneyAmount()
    {
        double amountOnBank = 0;

        for (int user = 0; user < this.getData(PaymentType.ING_LIST).length; user++)
        {
            String[] userData = this.getData(PaymentType.ING_LIST)[user];
            if (userData[0].equals(this.getUsername()) && userData[1].equals(this.getPassword()) &&
                    userData[2].equals(this.getBankNumber()))
            {
                if (this.getBankNumber().equals("NL42INGB4416709382"))
                {
                    amountOnBank = 2700.00;
                }

                if (this.getBankNumber().equals("NL56INGB6733307944"))
                {
                    amountOnBank = 1000.00;
                }

                if (this.getBankNumber().equals("NL29INGB5082680188"))
                {
                    amountOnBank = 10000.00;
                }
            }
            else
            {
                this.setErrorMessage("Invalid account details, please try again!");
                return -1;
            }
        }

        return amountOnBank;
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
