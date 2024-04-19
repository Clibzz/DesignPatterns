package nhlstenden.bookandsales.strategy;

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
        this.balance = this.getMoneyAmountINGUser();
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
        else
        {
            throw new IllegalArgumentException();
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

    public double getBalance()
    {
        return this.balance;
    }

    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    public String[][] getINGList()
    {
        return new String[][]{{"test", "test", "NL42INGB4416709382"}, {"voyanda", "scrub", "NL56INGB6733307944"},
                                  {"admin", "password", "NL29INGB5082680188"}};
    }

    public double getMoneyAmountINGUser()
    {
        double amountOnBank = 0;

        for (int user = 0; user < this.getINGList().length; user++)
        {
            String[] userData = this.getINGList()[user];
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
        }

        return amountOnBank;
    }

    @Override
    public boolean payForCurrentCart(double amount)
    {
        if (this.balance >= amount)
        {
            this.setBalance(this.getMoneyAmountINGUser() - amount);
            
            return true;
        }
        else
        {
            throw new IllegalArgumentException("Transaction failed, not enough money on this account");
        }
    }
}
