package nhlstenden.bookandsales.Model;

import nhlstenden.bookandsales.Factory.BookProduct;

public class PaymentCart implements Cloneable {
    private int id;
    private int userId;
    private String cartDetails;

    public PaymentCart(int userId, String cartDetails) {
        this.userId = userId;
        this.cartDetails = cartDetails;
    }

    public PaymentCart()
    {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCartDetails() {
        return this.cartDetails;
    }

    public void setCartDetails(String cartDetails) {
        this.cartDetails = cartDetails;
    }

    public void appendCartDetails(BookProduct book)
    {
        this.cartDetails += book;
    }

    public PaymentCartMemento save()
    {
        return new PaymentCartMemento(this.cartDetails);
    }

    public void restore(PaymentCartMemento memento)
    {
        this.cartDetails = memento.getCartDetails();
    }
}
