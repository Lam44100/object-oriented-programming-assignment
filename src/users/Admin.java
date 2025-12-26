package users;

import core.LibraryDatabase;
import entities.BookItem;
import entities.BookTitle;

public class Admin extends Staff {

    public Admin(int id, String name, String password, String contactInfo, double salary) {
        super(id, name, password, contactInfo, salary);
    }

    // Admin-specific capabilities (Moved from original Staff class)
    public void addNewBookTitle(BookTitle title, LibraryDatabase db) {
        db.addBookTitle(title);
        System.out.println("Title added: " + title.getTitle());
    }

    public void addBookItem(BookItem item, LibraryDatabase db) {
        db.addBookItem(item);
        System.out.println("Item added. Barcode: " + item.getBarcode());
    }

    @Override
    public String getRoleType() {
        return "ADMIN";
    }
}
