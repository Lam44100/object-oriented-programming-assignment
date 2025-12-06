package entities;

public class Author {
    private int authorID;
    private String name;

    public Author(int authorID, String name) {
        this.authorID = authorID;
        this.name = name;
    }

    public int getAuthorID() {
        return authorID;
    }

    public String getName() {
        return name;
    }

    // Setter for CRUD
    public void setName(String name) {
        this.name = name;
    }
}