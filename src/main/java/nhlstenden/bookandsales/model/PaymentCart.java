package nhlstenden.bookandsales.model;

public class PaymentCart {
    private int id;
    private int userId;
    private String cartDetails;

    public PaymentCart(int userId, String cartDetails) {
        this.userId = userId;
        this.cartDetails = cartDetails;
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
}
