package nhlstenden.bookandsales.Model;

public class PaymentCart implements Cloneable {
    private int id;
    private int userId;
    private StringBuilder cartDetails;

    public PaymentCart(int userId, StringBuilder cartDetails) {
        this.userId = userId;
        this.cartDetails = cartDetails;
    }

    public PaymentCart()
    {
        this.setCartDetails(new StringBuilder("_______PaymentCart_______"));
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

    public StringBuilder getCartDetails() {
        return this.cartDetails;
    }

    public void setCartDetails(StringBuilder cartDetails) {
        this.cartDetails = cartDetails;
    }

    public void appendCartDetails(Book book)
    {
        this.cartDetails.append(book);
    }

    public PaymentCartMemento save()
    {
        return new PaymentCartMemento(this.cartDetails.toString());
    }

    public void restore(PaymentCartMemento memento)
    {
        this.cartDetails = new StringBuilder(memento.getCartDetails());
    }
}
