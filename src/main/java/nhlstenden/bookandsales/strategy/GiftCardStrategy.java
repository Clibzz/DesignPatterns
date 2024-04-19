package nhlstenden.bookandsales.strategy;

import java.util.HashMap;
import java.util.Map;

public class GiftCardStrategy implements PaymentStrategy
{

    private String giftCode;
    private HashMap<String, Double> giftCodeList;

    public GiftCardStrategy(String giftCode)
    {
        this.setGiftCode(giftCode);
        this.giftCodeList = new HashMap<>();
    }

    public String getGiftCode()
    {
        return this.giftCode;
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

    public HashMap<String, Double> getGiftCodeList()
    {
        this.giftCodeList.put("lyiqEcFzFjT1", 25.0);
        this.giftCodeList.put("dWWlPRhUYjo2", 25.0);
        this.giftCodeList.put("jDEyexZs7u40", 20.0);
        this.giftCodeList.put("bcfIZK8Wycfy", 20.0);
        this.giftCodeList.put("fjHSXOVBs6o2", 10.0);
        this.giftCodeList.put("KzwTYBMpUk4F", 10.0);

        return this.giftCodeList;
    }

    @Override
    public void paymentMethod(double amount)
    {
        double amountLeftToPay = 0;

        for (Map.Entry<String, Double> set : this.getGiftCodeList().entrySet())
        {
            if (set.getKey().equals(this.getGiftCode()))
            {
                amountLeftToPay += (amount - set.getValue());
            }
        }

        if (amountLeftToPay < 0)
        {
            throw new IllegalArgumentException();
        }
    }
}
