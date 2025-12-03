package entities;

public class Author {
    private int authorID;
    private String name;

    public Author(int authorID, String name) {
        this.authorID = authorID;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}