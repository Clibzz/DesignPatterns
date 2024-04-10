package nhlstenden.bookandsales.Factory;

import nhlstenden.bookandsales.Model.Genre;

public class EBook implements BookProduct
{
    private String title;
    private double price;
    private String author;
    private String publisher;
    private int pageAmount;
    private Genre genre;
    private boolean hasAutomaticReading;
    private String description;
    private String image;

    public EBook(String title, double price, String author, String publisher,
                     int pageAmount, Genre genre, boolean hasAutomaticReading, String description, String image)
    {
        this.setTitle(title);
        this.setPrice(price);
        this.setAuthor(author);
        this.setPublisher(publisher);
        this.setPageAmount(pageAmount);
        this.setGenre(genre);
        this.setHasAutomaticReading(hasAutomaticReading);
        this.setDescription(description);
        this.setImage(image);
    }

    @Override
    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        if (!(title.isEmpty()))
        {
            this.title = title;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public double getPrice()
    {
        return this.price;
    }

    public void setPrice(double price)
    {
        if (!(price < 0))
        {
            this.price = price;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String getAuthor()
    {
        return this.author;
    }

    public void setAuthor(String author)
    {
        if (!(author.isEmpty()))
        {
            this.author = author;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String getPublisher()
    {
        return this.publisher;
    }

    public void setPublisher(String publisher)
    {
        if (!(publisher.isEmpty()))
        {
            this.publisher = publisher;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int getPageAmount()
    {
        return this.pageAmount;
    }

    public void setPageAmount(int pageAmount)
    {
        if (!(pageAmount < 0))
        {
            this.pageAmount = pageAmount;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Genre getGenre()
    {
        return this.genre;
    }

    public void setGenre(Genre genre)
    {
        if (!(genre.ToString().isEmpty()))
        {
            this.genre = genre;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean getBookCondition()
    {
        return this.hasAutomaticReading;
    }

    public void setHasAutomaticReading(boolean hasAutomaticReading)
    {
        this.hasAutomaticReading = hasAutomaticReading;
    }

    @Override
    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        if (!(description.isEmpty()))
        {
            this.description = description;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String getImage()
    {
        return this.image;
    }

    public void setImage(String image)
    {
        if (!(image.isEmpty()))
        {
            this.image = image;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
}