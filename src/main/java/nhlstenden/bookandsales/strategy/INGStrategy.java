package nhlstenden.bookandsales.strategy;

public class INGStrategy implements PaymentStrategy
{
    private String bankNumber;
    private String userName;
    private String password;

    public INGStrategy(String bankNumber, String userName, String password)
    {
        this.setBankNumber(bankNumber);
        this.setUserName(userName);
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
        else
        {
            throw new IllegalArgumentException();
        }
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

    public String[][] getINGList()
    {
        return new String[][]{{"robin", "test", "NL42INGB4416709382"}, {"voyanda", "scrub", "NL56INGB6733307944"},
                                  {"admin", "password", "NL29INGB5082680188"}};
    }

    public double getMoneyAmountINGUser()
    {
        double amountOnBank = 0;

        for (int user = 0; user < this.getINGList().length; user++)
        {
            for (int pass = 0; pass < this.getINGList()[user].length; pass++)
            {
                if (this.getINGList()[user][pass].equals(this.getUserName()) && this.getINGList()[user][pass].equals(this.getPassword()) &&
                    this.getINGList()[user][pass].equals(this.getBankNumber()))
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
        }

        return amountOnBank;
    }

    @Override
    public void paymentMethod(double amount)
    {
        boolean hasPayed = false;
        double tempAmount;

        if (!(this.getMoneyAmountINGUser() < amount))
        {
            tempAmount = (this.getMoneyAmountINGUser() - amount);
            if (tempAmount < this.getMoneyAmountINGUser())
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
