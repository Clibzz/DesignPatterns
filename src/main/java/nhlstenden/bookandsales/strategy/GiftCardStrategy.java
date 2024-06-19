package nhlstenden.bookandsales.strategy;

import nhlstenden.bookandsales.model.PaymentType;

import java.util.HashMap;
import java.util.Map;

public class GiftCardStrategy implements PaymentStrategy
{

    private String giftCode;
    private final HashMap<String, Double> giftCodeList = new HashMap<>();
    private double balance;

    public GiftCardStrategy(String giftCode)
    {
        this.setGiftCode(giftCode);
        this.setGiftCardBalance();
        giftCodeList.put("lyiqEcFzFjT1", 25.0);
        giftCodeList.put("dWWlPRhUYjo2", 25.0);
        giftCodeList.put("jDEyexZs7u40", 20.0);
        giftCodeList.put("bcfIZK8Wycfy", 20.0);
        giftCodeList.put("fjHSXOVBs6o2", 10.0);
        giftCodeList.put("KzwTYBMpUk4F", 10.0);
    }

    @Override
    public String getPassword()
    {
        return this.giftCode;
    }

    @Override
    public double getMoneyAmount()
    {
        return 0;
    }

    @Override
    public String getErrorMessage()
    {
        return null;
    }

    public void setGiftCode(String giftCode)
    {
        if (!(giftCode.isEmpty()))
        {
            this.giftCode = giftCode;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public void setGiftCardBalance()
    {
        for (Map.Entry<String, Double> set : this.getData(PaymentType.GIFT_CODE_LIST).entrySet())
        {
            if (set.getKey().equals(this.getPassword()))
            {
                this.balance = set.getValue();
            }
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
    public HashMap<String, Double> getData(PaymentType paymentType) {
        if (paymentType == PaymentType.GIFT_CODE_LIST) {
            return new HashMap<>(giftCodeList);
        }
        throw new IllegalArgumentException("Invalid payment type for GiftCodePaymentStrategy");
    }

    public boolean hasEnoughBalance(double giftCardAmount, double amount)
    {
        return (giftCardAmount - amount) >= 0;
    }

    @Override
    public boolean payForCart(double amount)
    {
        double giftcardAmount = 0;

        for (Map.Entry<String, Double> set : this.getData(PaymentType.GIFT_CODE_LIST).entrySet())
        {
            if (set.getKey().equals(this.getPassword()))
            {
                giftcardAmount = set.getValue();
            }
        }

        if (hasEnoughBalance(giftcardAmount, amount))
        {
            this.setBalance(giftcardAmount - amount);
            
            return true;
        }
        return false;
    }
}
