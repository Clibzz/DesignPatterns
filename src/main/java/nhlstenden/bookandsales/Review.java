package nhlstenden.bookandsales;

public class Review {
    private String title;
    private double rating;
    private long text;
    private String image;

    public Review(String title, double rating, long text) {
        this.title = title;
        this.rating = rating;
        this.text = text;
    }
}
