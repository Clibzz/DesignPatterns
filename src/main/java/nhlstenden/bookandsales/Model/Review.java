package nhlstenden.bookandsales.Model;

public class Review {
    private int id;
    private int userId;
    private int bookId;
    private String title;
    private double rating;
    private long text;
    private String image;

    public Review(int userId, int bookId, String title, double rating, long text, String image) {
        this.userId = userId;
        this.bookId = bookId;
        this.title = title;
        this.rating = rating;
        this.text = text;
        this.image = image;
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

    public int getBookId() {
        return this.bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getText() {
        return this.text;
    }

    public void setText(long text) {
        this.text = text;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
