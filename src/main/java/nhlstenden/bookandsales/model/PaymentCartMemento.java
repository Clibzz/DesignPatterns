package nhlstenden.bookandsales.model;

public class PaymentCartMemento
{
    private String cartDetails;

    public PaymentCartMemento(String cartDetails)
    {
        this.cartDetails = cartDetails;
    }

    public String getCartDetails()
    {
        return this.cartDetails;
    }
}
