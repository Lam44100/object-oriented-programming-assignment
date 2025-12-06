package core;

import entities.Author;
import entities.BookItem;
import entities.BookTitle;
import java.util.ArrayList;
import java.util.List;
import transactions.Loan;
import users.Person;

public class LibraryDatabase {
    private List<BookTitle> catalog;
    private List<BookItem> inventory;
    private List<Person> persons;
    private List<Author> authors; // Track authors specifically for CRUD
    private List<Loan> activeLoans;

    public LibraryDatabase() {
        this.catalog = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.persons = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.activeLoans = new ArrayList<>();
    }

    // --- ADDERS ---
    public void addBookTitle(BookTitle t) {
        catalog.add(t);
    }

    public void addBookItem(BookItem i) {
        inventory.add(i);
    }

    public void addPerson(Person p) {
        persons.add(p);
    }

    public void addAuthor(Author a) {
        authors.add(a);
    }

    public void addLoan(Loan loan) {
        activeLoans.add(loan);
    }

    // --- REMOVERS (For CRUD) ---
    public void removeBookTitle(BookTitle t) {
        catalog.remove(t);
    }

    public void removeBookItem(BookItem i) {
        inventory.remove(i);
    }

    public void removePerson(Person p) {
        persons.remove(p);
    }

    public void removeAuthor(Author a) {
        authors.remove(a);
    }

    public void removeLoan(Loan loan) {
        activeLoans.remove(loan);
    }

    // --- FINDERS ---
    public BookTitle findBookByTitle(String title) {
        for (BookTitle t : catalog) {
            if (t.getTitle().toLowerCase().contains(title.toLowerCase()))
                return t;
        }
        return null;
    }

    public BookTitle findBookByISBN(String isbn) {
        for (BookTitle t : catalog) {
            if (t.getIsbn().equalsIgnoreCase(isbn))
                return t;
        }
        return null;
    }

    public BookItem findItemByBarcode(String barcode) {
        for (BookItem i : inventory) {
            if (i.getBarcode().equalsIgnoreCase(barcode))
                return i;
        }
        return null;
    }

    public Person findPersonById(int id) {
        for (Person p : persons) {
            if (p.getId() == id)
                return p;
        }
        return null;
    }

    public Author findAuthorByName(String name) {
        for (Author a : authors) {
            if (a.getName().equalsIgnoreCase(name))
                return a;
        }
        return null;
    }

    // --- GET ALL LISTS ---
    public List<BookTitle> getCatalog() {
        return catalog;
    }

    public List<BookItem> getInventory() {
        return inventory;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Loan> getAllLoans() {
        return activeLoans;
    }
}