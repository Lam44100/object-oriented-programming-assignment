package core;

import entities.*;
import enums.*;
import java.util.Scanner;
import transactions.Loan;
import users.*;

public class LibrarySystem {
    private LibraryDatabase database;
    private Scanner scanner;
    private Person loggedInUser;

    public LibrarySystem() {
        this.database = new LibraryDatabase();
        this.scanner = new Scanner(System.in);
        setupDummyData();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printHeader("MAIN MENU");
            System.out.println("1. Login");
            System.out.println("2. Exit System");
            System.out.print("Select: ");
            String choice = readString();

            switch (choice) {
                case "1" -> handleLogin();
                case "2" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void handleLogin() {
        System.out.print("Enter User ID: ");
        int id;
        try {
            id = Integer.parseInt(readString());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
            return;
        }

        Person person = database.findPersonById(id);
        if (person == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter Password: ");
        String pass = readString();

        if (person.validatePassword(pass)) {
            loggedInUser = person;
            System.out.println("Welcome, " + person.getName() + " [" + person.getRoleType() + "]");

            // Route based on Class Type and Role
            if (person instanceof Borrower) {
                showMemberMenu((Borrower) person);
            } else if (person instanceof Staff) {
                Staff staff = (Staff) person;
                if (staff.getRole() == StaffRole.ADMIN) {
                    showAdminMenu(staff);
                } else {
                    showStaffMenu(staff);
                }
            }
        } else {
            System.out.println("Invalid Password.");
        }
    }

    // ==========================================
    // MENUS BY ROLE
    // ==========================================

    private void showMemberMenu(Borrower member) {
        boolean active = true;
        while (active) {
            printHeader("MEMBER MENU | " + member.getName());
            System.out.println("1. Search Catalog");
            System.out.println("2. Show Catalog (All Titles)");
            System.out.println("3. Show Available Books (All Items)");
            System.out.println("4. My Active Loans");
            System.out.println("5. My Loan History");
            System.out.println("0. Logout");
            System.out.print("Select: ");

            switch (readString()) {
                case "1" -> actionSearchCatalog();
                case "2" -> actionShowCatalog();
                case "3" -> actionShowAllItems();
                case "4" -> actionViewMyActiveLoans(member);
                case "5" -> member.printLoanHistory();
                case "0" -> active = false;
                default -> System.out.println("Invalid.");
            }
            if (active)
                waitForEnter();
        }
    }

    private void showStaffMenu(Staff staff) {
        boolean active = true;
        while (active) {
            printHeader("STAFF MENU | " + staff.getName());
            System.out.println("1. Search Catalog");
            System.out.println("2. Show Catalog (All Titles)");
            System.out.println("3. Show Available Books (All Items)");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Search Member Active Loans");
            System.out.println("7. Search Member Loan History");
            System.out.println("0. Logout");
            System.out.print("Select: ");

            switch (readString()) {
                case "1" -> actionSearchCatalog();
                case "2" -> actionShowCatalog();
                case "3" -> actionShowAllItems();
                case "4" -> actionIssueBook(staff);
                case "5" -> actionReturnBook();
                case "6" -> actionSearchMemberActiveLoans();
                case "7" -> actionSearchMemberHistory();
                case "0" -> active = false;
                default -> System.out.println("Invalid.");
            }
            if (active)
                waitForEnter();
        }
    }

    private void showAdminMenu(Staff admin) {
        boolean active = true;
        while (active) {
            printHeader("ADMIN MENU | " + admin.getName());
            System.out.println("1. Search Catalog");
            System.out.println("2. Show Catalog (All Titles)");
            System.out.println("3. Show Available Books (All Items)");
            System.out.println("4. Manage Book Titles (CRUD)");
            System.out.println("5. Manage Book Items (CRUD)");
            System.out.println("6. Manage Authors (CRUD)");
            System.out.println("7. Manage Persons (CRUD)");
            System.out.println("0. Logout");
            System.out.print("Select: ");

            switch (readString()) {
                case "1" -> actionSearchCatalog();
                case "2" -> actionShowCatalog();
                case "3" -> actionShowAllItems();
                case "4" -> menuManageTitles(admin);
                case "5" -> menuManageItems(admin);
                case "6" -> menuManageAuthors(admin);
                case "7" -> menuManagePersons(admin);
                case "0" -> active = false;
                default -> System.out.println("Invalid.");
            }
            if (active)
                waitForEnter();
        }
    }

    // ==========================================
    // SHARED ACTIONS
    // ==========================================

    private void actionSearchCatalog() {
        System.out.print("Enter search keyword: ");
        String q = readString();
        BookTitle t = database.findBookByTitle(q);
        if (t != null)
            t.printDetails();
        else
            System.out.println("No match found.");
    }

    private void actionShowCatalog() {
        System.out.println("--- FULL CATALOG ---");
        for (BookTitle t : database.getCatalog()) {
            t.printDetails();
        }
    }

    private void actionShowAllItems() {
        System.out.println("--- ALL PHYSICAL ITEMS ---");
        System.out.printf("%-10s | %-20s | %-10s | %-10s%n", "Barcode", "Title", "Status", "Location");
        for (BookItem i : database.getInventory()) {
            System.out.printf("%-10s | %-20s | %-10s | %-10s%n",
                    i.getBarcode(),
                    truncate(i.getBookTitle().getTitle(), 20),
                    i.getStatus(),
                    "Shelf"); // Assuming getter
        }
    }

    private void actionViewMyActiveLoans(Borrower b) {
        System.out.println("Your Active Loans:");
        for (Loan l : b.getActiveLoans()) {
            System.out.println("- " + l.getBookItem().getBookTitle().getTitle() + " (Due: " + l.getDueDate() + ")");
        }
    }

    // ==========================================
    // STAFF/ADMIN ACTIONS
    // ==========================================

    private void actionIssueBook(Staff staff) {
        System.out.print("Borrower ID: ");
        int bid = Integer.parseInt(readString());
        Person p = database.findPersonById(bid);
        if (!(p instanceof Borrower)) {
            System.out.println("Not a valid borrower.");
            return;
        }

        System.out.print("Item Barcode: ");
        String bc = readString();
        BookItem item = database.findItemByBarcode(bc);
        if (item != null && item.getStatus() == BookStatus.AVAILABLE) {
            Loan l = new Loan((int) (Math.random() * 10000), (Borrower) p, item);
            database.addLoan(l);
            ((Borrower) p).addLoan(l);
            item.setStatus(BookStatus.LOANED);
            System.out.println("Book Issued.");
        } else {
            System.out.println("Item not available.");
        }
    }

    private void actionReturnBook() {
        System.out.print("Scan Barcode: ");
        String bc = readString();
        BookItem item = database.findItemByBarcode(bc);
        if (item != null && item.getStatus() == BookStatus.LOANED) {
            item.setStatus(BookStatus.AVAILABLE);
            // In a real system, you'd close the specific Loan record here
            System.out.println("Book Returned.");
        } else {
            System.out.println("Item not currently loaned.");
        }
    }

    private void actionSearchMemberActiveLoans() {
        System.out.print("Enter Member ID: ");
        try {
            int id = Integer.parseInt(readString());
            Person p = database.findPersonById(id);
            if (p instanceof Borrower) {
                actionViewMyActiveLoans((Borrower) p);
            } else {
                System.out.println("User is not a borrower.");
            }
        } catch (Exception e) {
            System.out.println("Invalid ID.");
        }
    }

    private void actionSearchMemberHistory() {
        System.out.print("Enter Member ID: ");
        try {
            int id = Integer.parseInt(readString());
            Person p = database.findPersonById(id);
            if (p instanceof Borrower) {
                ((Borrower) p).printLoanHistory();
            } else {
                System.out.println("User is not a borrower.");
            }
        } catch (Exception e) {
            System.out.println("Invalid ID.");
        }
    }

    // ==========================================
    // ADMIN CRUD MENUS
    // ==========================================

    private void menuManageTitles(Staff admin) {
        System.out.println("1. Add Title\n2. Update Title\n3. Delete Title");
        String c = readString();
        if (c.equals("1")) {
            System.out.print("ISBN: ");
            String isbn = readString();
            System.out.print("Title: ");
            String title = readString();
            System.out.print("Genre: ");
            String genre = readString();
            System.out.print("Pub: ");
            String pub = readString();
            BookTitle t = new BookTitle(isbn, title, genre, pub);
            System.out.print("Author Name: ");
            Author a = database.findAuthorByName(readString());
            if (a != null)
                t.addAuthor(a);
            else
                System.out.println("Warning: Author not found, added without author.");

            admin.addNewBookTitle(t, database);
        } else if (c.equals("2")) {
            System.out.print("Enter ISBN to edit: ");
            BookTitle t = database.findBookByISBN(readString());
            if (t != null) {
                System.out.print("New Title: ");
                t.setTitle(readString());
                System.out.println("Updated.");
            }
        } else if (c.equals("3")) {
            System.out.print("Enter ISBN to delete: ");
            BookTitle t = database.findBookByISBN(readString());
            if (t != null) {
                database.removeBookTitle(t);
                System.out.println("Deleted.");
            }
        }
    }

    private void menuManageItems(Staff admin) {
        System.out.println("1. Add Item\n2. Delete Item");
        String c = readString();
        if (c.equals("1")) {
            System.out.print("Link to ISBN: ");
            BookTitle t = database.findBookByISBN(readString());
            if (t != null) {
                System.out.print("New Barcode: ");
                String bc = readString();
                admin.addBookItem(new BookItem(bc, t, "Stack"), database);
            }
        } else if (c.equals("2")) {
            System.out.print("Barcode to delete: ");
            BookItem i = database.findItemByBarcode(readString());
            if (i != null) {
                database.removeBookItem(i);
                System.out.println("Deleted.");
            }
        }
    }

    private void menuManageAuthors(Staff admin) {
        System.out.println("1. Add Author\n2. Delete Author");
        String c = readString();
        if (c.equals("1")) {
            System.out.print("Name: ");
            database.addAuthor(new Author((int) (Math.random() * 1000), readString()));
            System.out.println("Added.");
        } else if (c.equals("2")) {
            System.out.print("Name to delete: ");
            Author a = database.findAuthorByName(readString());
            if (a != null) {
                database.removeAuthor(a);
                System.out.println("Deleted.");
            }
        }
    }

    private void menuManagePersons(Staff admin) {
        System.out.println("1. Add Borrower\n2. Add Staff\n3. Delete Person");
        String c = readString();
        if (c.equals("1") || c.equals("2")) {
            System.out.print("ID: ");
            int id = Integer.parseInt(readString());
            System.out.print("Name: ");
            String name = readString();
            System.out.print("Pass: ");
            String pass = readString();
            if (c.equals("1"))
                database.addPerson(new Borrower(id, name, pass, "Contact"));
            else
                database.addPerson(new Staff(id, name, pass, "Contact", StaffRole.STAFF, 3000));
            System.out.println("User added.");
        } else if (c.equals("3")) {
            System.out.print("ID to delete: ");
            Person p = database.findPersonById(Integer.parseInt(readString()));
            if (p != null) {
                database.removePerson(p);
                System.out.println("Deleted.");
            }
        }
    }

    // ==========================================
    // DUMMY DATA SETUP
    // ==========================================
    private void setupDummyData() {
        System.out.println("Initializing Database...");

        // 1. Authors (5)
        Author[] authors = new Author[5];
        for (int i = 0; i < 5; i++) {
            authors[i] = new Author(i + 1, "Author " + (char) ('A' + i));
            database.addAuthor(authors[i]);
        }

        // 2. BookTitles (10)
        BookTitle[] titles = new BookTitle[10];
        for (int i = 0; i < 10; i++) {
            titles[i] = new BookTitle("978-00" + i, "Book Title " + (i + 1), "Genre X", "Publisher Y");
            // Assign random author
            titles[i].addAuthor(authors[i % 5]);
            database.addBookTitle(titles[i]);
        }

        // 3. BookItems (25) linked to Titles
        int itemCounter = 1000;
        for (int i = 0; i < 25; i++) {
            // Distribute items across the 10 titles (approx 2-3 copies each)
            BookTitle t = titles[i % 10];
            BookItem item = new BookItem("B" + itemCounter++, t, "Shelf " + (i % 5));
            database.addBookItem(item);
        }

        // 4. Persons (6 Total: 1 Admin, 2 Staff, 3 Borrowers)
        // Admin
        database.addPerson(new Staff(1, "Super Admin", "admin123", "admin@lib.com", StaffRole.ADMIN, 5000));

        // Staff
        database.addPerson(new Staff(2, "Alice Staff", "staff1", "alice@lib.com", StaffRole.STAFF, 3000));
        database.addPerson(new Staff(3, "Bob Staff", "staff2", "bob@lib.com", StaffRole.STAFF, 3000));

        // Borrowers
        database.addPerson(new Borrower(4, "Charlie Member", "pass1", "charlie@gmail.com"));
        database.addPerson(new Borrower(5, "Dave Member", "pass2", "dave@gmail.com"));
        database.addPerson(new Borrower(6, "Eve Member", "pass3", "eve@gmail.com"));

        System.out.println("Data Loaded: 5 Authors, 10 Titles, 25 Items, 6 Users.");
    }

    // Helpers
    private void printHeader(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    private String readString() {
        return scanner.nextLine().trim();
    }

    private void waitForEnter() {
        System.out.println("[Press Enter]");
        scanner.nextLine();
    }

    private String truncate(String s, int len) {
        if (s.length() > len)
            return s.substring(0, len - 3) + "...";
        return s;
    }

    public static void main(String[] args) {
        new LibrarySystem().run();
    }
}