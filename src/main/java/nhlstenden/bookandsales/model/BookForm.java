package nhlstenden.bookandsales.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;
public class BookForm {
    @Id
    @GeneratedValue()
    private int id;
    @NotNull(message = "Book type is required")
    @NotEmpty(message = "Book type cannot be empty")
    private String bookType;

    @NotNull(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;

    @NotNull(message = "Genre is required")
    private Genre genre;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Author is required")
    @NotEmpty(message = "Author cannot be empty")
    private String author;

    @NotNull(message = "Publisher is required")
    @NotEmpty(message = "Publisher cannot be empty")
    private String publisher;

    @NotNull(message = "Title is required")
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotNull(message = "Amount of pages is required")
    @Min(value = 1, message = "Amount of pages must be at least 1")
    private Integer pageAmount;

    @NotNull(message = "Image is required")
    private MultipartFile image;

    private String imageName;

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getBookType()
    {
        return this.bookType;
    }

    public void setBookType(String bookType)
    {
        this.bookType = bookType;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Genre getGenre()
    {
        return this.genre;
    }

    public void setGenre(Genre genre)
    {
        this.genre = genre;
    }

    public Double getPrice()
    {
        return this.price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public String getAuthor()
    {
        return this.author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getPublisher()
    {
        return this.publisher;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Integer getPageAmount()
    {
        return this.pageAmount;
    }

    public void setPageAmount(Integer pageAmount)
    {
        this.pageAmount = pageAmount;
    }

    public MultipartFile getImage()
    {
        return this.image;
    }

    public void setImage(MultipartFile image)
    {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

