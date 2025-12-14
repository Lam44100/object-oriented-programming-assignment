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

    // ANSI Escape Codes for TUI-like formatting (Works on most modern terminals)
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BOLD = "\u001B[1m";

    public LibrarySystem() {
        this.database = new LibraryDatabase();
        this.scanner = new Scanner(System.in);
        setupDummyData();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printHeader(ANSI_BOLD + ANSI_BLUE + "MAIN MENU" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "1. Login" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "2. Exit System" + ANSI_RESET);
            System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);
            String choice = readString();

            switch (choice) {
                case "1" ->
                    handleLogin();
                case "2" -> {
                    System.out.println(
                            ANSI_GREEN + "\n>>> Goodbye! Thank you for using the Library System. <<<" + ANSI_RESET);
                    running = false;
                }
                default ->
                    System.out.println(ANSI_RED + "Invalid option." + ANSI_RESET);
            }
        }
    }

    private void handleLogin() {
        System.out.print("Enter User ID: ");
        int id;
        try {
            id = Integer.parseInt(readString());
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Invalid ID format." + ANSI_RESET);
            return;
        }

        Person person = database.findPersonById(id);
        if (person == null) {
            System.out.println(ANSI_RED + "User not found." + ANSI_RESET);
            return;
        }

        System.out.print("Enter Password: ");
        String pass = readString();

        if (person.validatePassword(pass)) {
            loggedInUser = person;
            System.out.println(ANSI_GREEN + "Welcome, " + ANSI_BOLD + person.getName() + ANSI_RESET + ANSI_GREEN + " ["
                    + person.getRoleType() + "]" + ANSI_RESET);

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
            System.out.println(ANSI_RED + "Invalid Password." + ANSI_RESET);
        }
    }

    // ==========================================
    // MENUS BY ROLE
    // ==========================================
    private void showMemberMenu(Borrower member) {
        boolean active = true;
        while (active) {
            printHeader(ANSI_BOLD + ANSI_BLUE + "MEMBER MENU | " + member.getName() + ANSI_RESET);
            System.out.println(ANSI_CYAN + "1. Search Catalog" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "2. Show Catalog (All Titles)" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "3. Show Available Books (All Items)" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "4. My Active Loans" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "5. My Loan History" + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "0. Logout" + ANSI_RESET);
            System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);

            switch (readString()) {
                case "1" ->
                    actionSearchCatalog();
                case "2" ->
                    actionShowCatalog();
                case "3" ->
                    actionShowAllItems();
                case "4" ->
                    actionViewMyActiveLoans(member);
                case "5" ->
                    member.printLoanHistory();
                case "0" ->
                    active = false;
                default ->
                    System.out.println(ANSI_RED + "Invalid." + ANSI_RESET);
            }
            if (active) {
                waitForEnter();
            }
        }
    }

    private void showStaffMenu(Staff staff) {
        boolean active = true;
        while (active) {
            printHeader(ANSI_BOLD + ANSI_BLUE + "STAFF MENU | " + staff.getName() + ANSI_RESET);
            System.out.println(ANSI_CYAN + "1. Search Catalog" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "2. Show Catalog (All Titles)" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "3. Show Available Books (All Items)" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "4. Issue Book" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "5. Return Book" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "6. Search Member Active Loans" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "7. Search Member Loan History" + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "0. Logout" + ANSI_RESET);
            System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);

            switch (readString()) {
                case "1" ->
                    actionSearchCatalog();
                case "2" ->
                    actionShowCatalog();
                case "3" ->
                    actionShowAllItems();
                case "4" ->
                    actionIssueBook(staff);
                case "5" ->
                    actionReturnBook();
                case "6" ->
                    actionSearchMemberActiveLoans();
                case "7" ->
                    actionSearchMemberHistory();
                case "0" ->
                    active = false;
                default ->
                    System.out.println(ANSI_RED + "Invalid." + ANSI_RESET);
            }
            if (active) {
                waitForEnter();
            }
        }
    }

    private void showAdminMenu(Staff admin) {
        boolean active = true;
        while (active) {
            printHeader(ANSI_BOLD + ANSI_BLUE + "ADMIN MENU | " + admin.getName() + ANSI_RESET);
            System.out.println(ANSI_CYAN + "1. Search Catalog" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "2. Show Catalog (All Titles)" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "3. Show Available Books (All Items)" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "4. Manage Book Titles" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "5. Manage Book Items" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "6. Manage Authors" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "7. Manage Persons" + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "0. Logout" + ANSI_RESET);
            System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);

            switch (readString()) {
                case "1" ->
                    actionSearchCatalog();
                case "2" ->
                    actionShowCatalog();
                case "3" ->
                    actionShowAllItems();
                case "4" ->
                    menuManageTitles(admin);
                case "5" ->
                    menuManageItems(admin);
                case "6" ->
                    menuManageAuthors(admin);
                case "7" ->
                    menuManagePersons(admin);
                case "0" ->
                    active = false;
                default ->
                    System.out.println(ANSI_RED + "Invalid." + ANSI_RESET);
            }
            if (active) {
                waitForEnter();
            }
        }
    }

    // ==========================================
    // SHARED ACTIONS
    // ==========================================
    private void actionSearchCatalog() {
        System.out.print("Enter search keyword: ");
        String q = readString();
        BookTitle t = database.findBookByTitle(q);
        if (t != null) {
            t.printDetails();
        } else {
            System.out.println(ANSI_YELLOW + "No match found." + ANSI_RESET);
        }
    }

    private void actionShowCatalog() {
        System.out.println(ANSI_BOLD + "\n--- FULL CATALOG ---" + ANSI_RESET);
        for (BookTitle t : database.getCatalog()) {
            t.printDetails();
        }
    }

    private void actionShowAllItems() {
        System.out.println(ANSI_BOLD + "\n--- ALL PHYSICAL ITEMS ---" + ANSI_RESET);
        System.out.printf(ANSI_BOLD + "%-10s | %-20s | %-10s | %-10s%n" + ANSI_RESET, "Barcode", "Title", "Status",
                "Location");
        for (BookItem i : database.getInventory()) {
            System.out.printf("%-10s | %-20s | %-10s | %-10s%n",
                    i.getBarcode(),
                    truncate(i.getBookTitle().getTitle(), 20),
                    i.getStatus(),
                    "Shelf"); // Assuming getter
        }
    }

    private void actionViewMyActiveLoans(Borrower b) {
        System.out.println(ANSI_BOLD + "\nYour Active Loans:" + ANSI_RESET);
        if (b.getActiveLoans().isEmpty()) {
            System.out.println(ANSI_YELLOW + " - No active loans found." + ANSI_RESET);
        }
        for (Loan l : b.getActiveLoans()) {
            System.out.println("- " + l.getBookItem().getBookTitle().getTitle() + " (Due: " + ANSI_RED + l.getDueDate()
                    + ANSI_RESET + ")");
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
            System.out.println(ANSI_RED + "Not a valid borrower." + ANSI_RESET);
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
            System.out.println(ANSI_GREEN + "Book Issued." + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Item not available." + ANSI_RESET);
        }
    }

    private void actionReturnBook() {
        System.out.print("Scan Barcode: ");
        String bc = readString();
        BookItem item = database.findItemByBarcode(bc);
        if (item != null && item.getStatus() == BookStatus.LOANED) {

            // Find the active Loan and close it.
            Loan loan = database.findActiveLoanByBookItem(item);

            if (loan != null) {
                loan.markReturned(); // Mark the loan as returned (sets returnDate)

                // Calculate Fine
                double fine = loan.calculateFine();
                if (fine > 0) {
                    System.out.println(ANSI_YELLOW + ANSI_BOLD + "ALERT: Book is Overdue." + ANSI_RESET + ANSI_YELLOW
                            + " Fine calculated: $" + String.format("%.2f", fine) + ANSI_RESET);
                }

                database.removeLoan(loan); // Remove from the database's active loan list
            }

            // Update the BookItem status
            item.setStatus(BookStatus.AVAILABLE);

            System.out.println(ANSI_GREEN + "Book Returned." + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Item not currently loaned or not found." + ANSI_RESET);
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
                System.out.println(ANSI_RED + "User is not a borrower." + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Invalid ID." + ANSI_RESET);
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
                System.out.println(ANSI_RED + "User is not a borrower." + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Invalid ID." + ANSI_RESET);
        }
    }

    // ==========================================
    // ADMIN CRUD MENUS
    // ==========================================
    private void menuManageTitles(Staff admin) {
        System.out.println(ANSI_BOLD + "\n--- MANAGE TITLES ---" + ANSI_RESET);
        System.out.println("1. Add Title\n2. Update Title\n3. Delete Title");
        System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);
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
            if (a != null) {
                t.addAuthor(a);
            } else {
                System.out.println(ANSI_YELLOW + "Warning: Author not found, added without author." + ANSI_RESET);
            }

            admin.addNewBookTitle(t, database);
        } else if (c.equals("2")) {
            System.out.print("Enter ISBN to edit: ");
            BookTitle t = database.findBookByISBN(readString());
            if (t != null) {
                System.out.print("New Title: ");
                t.setTitle(readString());
                System.out.println(ANSI_GREEN + "Updated." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Title not found." + ANSI_RESET);
            }
        } else if (c.equals("3")) {
            System.out.print("Enter ISBN to delete: ");
            BookTitle t = database.findBookByISBN(readString());
            if (t != null) {
                database.removeBookTitle(t);
                System.out.println(ANSI_GREEN + "Deleted." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Title not found." + ANSI_RESET);
            }
        }
    }

    private void menuManageItems(Staff admin) {
        System.out.println(ANSI_BOLD + "\n--- MANAGE ITEMS ---" + ANSI_RESET);
        System.out.println("1. Add Item\n2. Delete Item");
        System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);
        String c = readString();
        if (c.equals("1")) {
            System.out.print("Link to ISBN: ");
            BookTitle t = database.findBookByISBN(readString());
            if (t != null) {
                System.out.print("New Barcode: ");
                String bc = readString();
                admin.addBookItem(new BookItem(bc, t, "Stack"), database);
            } else {
                System.out.println(ANSI_RED + "Book Title (ISBN) not found." + ANSI_RESET);
            }
        } else if (c.equals("2")) {
            System.out.print("Barcode to delete: ");
            BookItem i = database.findItemByBarcode(readString());
            if (i != null) {
                database.removeBookItem(i);
                System.out.println(ANSI_GREEN + "Deleted." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Item not found." + ANSI_RESET);
            }
        }
    }

    private void menuManageAuthors(Staff admin) {
        System.out.println(ANSI_BOLD + "\n--- MANAGE AUTHORS ---" + ANSI_RESET);
        System.out.println("1. Add Author\n2. Delete Author");
        System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);
        String c = readString();
        if (c.equals("1")) {
            System.out.print("Name: ");
            database.addAuthor(new Author((int) (Math.random() * 1000), readString()));
            System.out.println(ANSI_GREEN + "Added." + ANSI_RESET);
        } else if (c.equals("2")) {
            System.out.print("Name to delete: ");
            Author a = database.findAuthorByName(readString());
            if (a != null) {
                database.removeAuthor(a);
                System.out.println(ANSI_GREEN + "Deleted." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Author not found." + ANSI_RESET);
            }
        }
    }

    private void menuManagePersons(Staff admin) {
        System.out.println(ANSI_BOLD + "\n--- MANAGE PERSONS (Users) ---" + ANSI_RESET);
        System.out.println("1. Add Borrower\n2. Add Staff\n3. Delete Person");
        System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);
        String c = readString();
        if (c.equals("1") || c.equals("2")) {
            System.out.print("ID: ");
            int id = Integer.parseInt(readString());
            System.out.print("Name: ");
            String name = readString();
            System.out.print("Pass: ");
            String pass = readString();
            if (c.equals("1")) {
                database.addPerson(new Borrower(id, name, pass, "Contact"));
            } else {
                database.addPerson(new Staff(id, name, pass, "Contact", StaffRole.STAFF, 3000));
            }
            System.out.println(ANSI_GREEN + "User added." + ANSI_RESET);
        } else if (c.equals("3")) {
            System.out.print("ID to delete: ");
            Person p = database.findPersonById(Integer.parseInt(readString()));
            if (p != null) {
                database.removePerson(p);
                System.out.println(ANSI_GREEN + "Deleted." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Person not found." + ANSI_RESET);
            }
        }
    }

    // ==========================================
    // DUMMY DATA SETUP (Reconfigured with real examples)
    // ==========================================
    private void setupDummyData() {
        System.out.println(ANSI_CYAN + "Initializing Database..." + ANSI_RESET);

        // 1. Authors (5)
        Author author1 = new Author(1, "Isaac Asimov");
        Author author2 = new Author(2, "Ursula K. Le Guin");
        Author author3 = new Author(3, "George Orwell");
        Author author4 = new Author(4, "Jane Austen");
        Author author5 = new Author(5, "Ta-Nehisi Coates");

        database.addAuthor(author1);
        database.addAuthor(author2);
        database.addAuthor(author3);
        database.addAuthor(author4);
        database.addAuthor(author5);

        // 2. BookTitles (10)
        // Sci-Fi
        BookTitle title1 = new BookTitle("978-0553293357", "Foundation", "Science Fiction", "Bantam Books");
        title1.addAuthor(author1);
        BookTitle title2 = new BookTitle("978-0441478005", "The Left Hand of Darkness", "Science Fiction", "Ace Books");
        title2.addAuthor(author2);
        BookTitle title3 = new BookTitle("978-0553293371", "Foundation and Empire", "Science Fiction", "Bantam Books");
        title3.addAuthor(author1);
        BookTitle title4 = new BookTitle("978-0061054884", "The Dispossessed", "Science Fiction", "Harper");
        title4.addAuthor(author2);

        // Classics / Dystopian
        BookTitle title5 = new BookTitle("978-0451524935", "1984", "Dystopian Classic", "Signet Classics");
        title5.addAuthor(author3);
        BookTitle title6 = new BookTitle("978-0141439518", "Pride and Prejudice", "Classic Fiction", "Penguin Classics");
        title6.addAuthor(author4);
        BookTitle title7 = new BookTitle("978-0451526342", "Animal Farm", "Political Satire", "Signet Classics");
        title7.addAuthor(author3);
        BookTitle title8 = new BookTitle("978-0141439556", "Sense and Sensibility", "Classic Fiction", "Penguin Classics");
        title8.addAuthor(author4);

        // Non-Fiction / Historical Fiction
        BookTitle title9 = new BookTitle("978-0812993547", "Between the World and Me", "Non-Fiction", "Spiegel & Grau");
        title9.addAuthor(author5);
        BookTitle title10 = new BookTitle("978-0399590597", "The Water Dancer", "Historical Fiction", "One World");
        title10.addAuthor(author5);

        BookTitle[] titles = {title1, title2, title3, title4, title5, title6, title7, title8, title9, title10};
        for (BookTitle t : titles) {
            database.addBookTitle(t);
        }

        // 3. BookItems (25 physical copies)
        int itemCounter = 1000;
        for (int i = 0; i < 25; i++) {
            // Distribute items across the 10 titles (approx 2-3 copies each)
            BookTitle t = titles[i % 10];
            // Use specific rack locations for more realism
            String rackLocation;
            if (i < 4) { // First 4 items are Sci-Fi
                rackLocation = "SF-" + (i % 2);
            } else if (i < 8) { // Next 4 items are Classics/Dystopian
                rackLocation = "Cls-" + (i % 2);
            } else if (i < 10) { // Next 2 items are Non-Fiction
                rackLocation = "NF-0";
            } else { // Repeat distribution
                rackLocation = "Gen-" + (i % 3);
            }

            BookItem item = new BookItem("B" + itemCounter++, t, rackLocation);
            database.addBookItem(item);
        }

        // 4. Persons (6 Total: 1 Admin, 2 Staff, 3 Borrowers) - Keeping original data.
        // Admin
        database.addPerson(new Staff(1, "Super Admin", "admin123", "admin@lib.com", StaffRole.ADMIN, 5000));

        // Staff
        database.addPerson(new Staff(2, "Alice Staff", "staff1", "alice@lib.com", StaffRole.STAFF, 3000));
        database.addPerson(new Staff(3, "Bob Staff", "staff2", "bob@lib.com", StaffRole.STAFF, 3000));

        // Borrowers
        database.addPerson(new Borrower(4, "Charlie Member", "pass1", "charlie@gmail.com"));
        database.addPerson(new Borrower(5, "Dave Member", "pass2", "dave@gmail.com"));
        database.addPerson(new Borrower(6, "Eve Member", "pass3", "eve@gmail.com"));

        System.out.println(ANSI_CYAN + "Data Loaded: 5 Authors, 10 Titles, 25 Items, 6 Users (Real Data)." + ANSI_RESET);
    }

    // Helpers
    private void printHeader(String title) {
        System.out.println("\n" + ANSI_BLUE + "==========================================" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "=== " + title + " ===" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "==========================================" + ANSI_RESET);
    }

    private String readString() {
        return scanner.nextLine().trim();
    }

    private void waitForEnter() {
        System.out.println(ANSI_YELLOW + "\n[Press Enter to continue...]" + ANSI_RESET);
        scanner.nextLine();
    }

    private String truncate(String s, int len) {
        if (s.length() > len) {
            return s.substring(0, len - 3) + "...";
        }
        return s;
    }

    public static void main(String[] args) {
        new LibrarySystem().run();
    }
}
