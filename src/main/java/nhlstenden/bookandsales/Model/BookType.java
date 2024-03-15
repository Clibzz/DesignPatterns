package nhlstenden.bookandsales.Model;

public class BookType {
    private int id;
    private String type;
    private String attributeType;
    private boolean hasAttribute;

    public BookType(String type, String attributeType, boolean hasAttribute) {
        this.type = type;
        this.attributeType = attributeType;
        this.hasAttribute = hasAttribute;
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
