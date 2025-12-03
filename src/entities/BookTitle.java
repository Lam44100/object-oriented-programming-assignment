package entities;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookTitle {
    private String isbn;
    private String title;
    private List<Author> authors;
    private String genre;
    private String publisher;

    public BookTitle(String isbn, String title, String genre, String publisher) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.publisher = publisher;
        this.authors = new ArrayList<>();
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthorNames() {
        return authors.stream()
                .map(Author::getName)
                .collect(Collectors.joining(", "));
    }

    public void printDetails() {
        System.out.println("Title: " + title);
        System.out.println("ISBN: " + isbn);
        System.out.println("Author(s): " + getAuthorNames());
        System.out.println("Publisher: " + publisher);
    }
}