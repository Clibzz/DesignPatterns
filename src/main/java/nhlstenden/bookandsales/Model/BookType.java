package nhlstenden.bookandsales.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BookType {
    @Id
    private int id;
    private String type;
    private String attributeType;
    private boolean hasAttribute;

    public BookType(int id, String type, String attributeType, boolean hasAttribute)
    {
        this.id = id;
        this.type = type;
        this.attributeType = attributeType;
        this.hasAttribute = hasAttribute;
    }

    public BookType()
    {

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttributeType() {
        return this.attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public boolean getHasAttribute() {
        return this.hasAttribute;
    }

    public void setHasAttribute(boolean hasAttribute) {
        this.hasAttribute = hasAttribute;
    }
}
