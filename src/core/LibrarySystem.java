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

    // ANSI Colors
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
            try {
                printHeader(ANSI_BOLD + ANSI_BLUE + "MAIN MENU" + ANSI_RESET);
                System.out.println(ANSI_CYAN + "1. Login" + ANSI_RESET);
                System.out.println(ANSI_CYAN + "2. Exit System" + ANSI_RESET);
                System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);
                String choice = readString();

                switch (choice) {
                    case "1" ->
                        handleLogin();
                    case "2" -> {
                        System.out.println(ANSI_GREEN + "\n>>> Goodbye! <<<" + ANSI_RESET);
                        running = false;
                    }
                    default ->
                        System.out.println(ANSI_RED + "Invalid option." + ANSI_RESET);
                }
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Unexpected Error: " + e.getMessage() + ANSI_RESET);
            }
        }
    }

    private void handleLogin() {
        int id = readInt("Enter User ID: ");

        Person person = database.findPersonById(id);
        if (person == null) {
            System.out.println(ANSI_RED + "User not found." + ANSI_RESET);
            return;
        }

        System.out.print("Enter Password: ");
        String pass = readString();

        if (person.validatePassword(pass)) {
            loggedInUser = person;
            System.out.println(ANSI_GREEN + "Welcome, " + ANSI_BOLD + person.getName() + ANSI_RESET
                    + ANSI_GREEN + " [" + person.getRoleType() + "]" + ANSI_RESET);

            // --- REFACTORED ROUTING (Inheritance Check) ---
            if (person instanceof Borrower) {
                showMemberMenu((Borrower) person);
            } else if (person instanceof Admin) {
                showAdminMenu((Admin) person);
            } else if (person instanceof Librarian) {
                showStaffMenu((Librarian) person);
            } else {
                System.out.println(ANSI_RED + "Error: Unknown role type." + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_RED + "Invalid Password." + ANSI_RESET);
        }
    }

    // ==========================================
    // MENUS
    // ==========================================
    private void showMemberMenu(Borrower member) {
        boolean active = true;
        while (active) {
            printHeader(ANSI_BOLD + ANSI_BLUE + "MEMBER MENU | " + member.getName() + ANSI_RESET);
            System.out.println("1. View/Search Catalog");
            System.out.println("2. Show Available Books (All Items)");
            System.out.println("3. My Active Loans");
            System.out.println("4. My Loan History");
            System.out.println("0. Logout");
            System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);

            switch (readString()) {
                case "1" ->
                    actionViewCatalog();
                case "2" ->
                    actionShowAllItems();
                case "3" ->
                    actionViewMyActiveLoans(member);
                case "4" ->
                    member.printLoanHistory();
                case "0" ->
                    active = false;
                default ->
                    System.out.println(ANSI_RED + "Invalid option." + ANSI_RESET);
            }
            if (active) {
                waitForEnter();
            }
        }
    }

    // Uses Librarian class
    private void showStaffMenu(Librarian staff) {
        boolean active = true;
        while (active) {
            printHeader(ANSI_BOLD + ANSI_BLUE + "LIBRARIAN MENU | " + staff.getName() + ANSI_RESET);
            System.out.println("1. View/Search Catalog");
            System.out.println("2. Show Available Books");
            System.out.println(ANSI_GREEN + "3. Issue Book" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "4. Return Book" + ANSI_RESET);
            System.out.println("5. Search Member Active Loans");
            System.out.println("6. Search Member Loan History");
            System.out.println("0. Logout");
            System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);

            switch (readString()) {
                case "1" ->
                    actionViewCatalog();
                case "2" ->
                    actionShowAllItems();
                case "3" ->
                    actionIssueBook(staff);
                case "4" ->
                    actionReturnBook();
                case "5" ->
                    actionSearchMemberActiveLoans();
                case "6" ->
                    actionSearchMemberHistory();
                case "0" ->
                    active = false;
                default ->
                    System.out.println(ANSI_RED + "Invalid option." + ANSI_RESET);
            }
            if (active) {
                waitForEnter();
            }
        }
    }

    // Uses Admin class
    private void showAdminMenu(Admin admin) {
        boolean active = true;
        while (active) {
            printHeader(ANSI_BOLD + ANSI_BLUE + "ADMIN MENU | " + admin.getName() + ANSI_RESET);
            System.out.println("1. View/Search Catalog");
            System.out.println("2. Show Available Books");
            System.out.println(ANSI_GREEN + "3. Manage Book Titles" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "4. Manage Book Items" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "5. Manage Authors" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "6. Manage Persons" + ANSI_RESET);
            System.out.println("0. Logout");
            System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);

            switch (readString()) {
                case "1" ->
                    actionViewCatalog();
                case "2" ->
                    actionShowAllItems();
                case "3" ->
                    menuManageTitles(admin);
                case "4" ->
                    menuManageItems(admin);
                case "5" ->
                    menuManageAuthors(admin);
                case "6" ->
                    menuManagePersons(admin);
                case "0" ->
                    active = false;
                default ->
                    System.out.println(ANSI_RED + "Invalid option." + ANSI_RESET);
            }
            if (active) {
                waitForEnter();
            }
        }
    }

    // ==========================================
    // SHARED ACTIONS
    // ==========================================
    private void actionViewCatalog() {
        System.out.println(ANSI_CYAN + "Enter keyword to search (or press Enter for all):" + ANSI_RESET);
        String q = readString();
        if (q.isEmpty()) {
            System.out.println(ANSI_BOLD + "\n--- FULL CATALOG ---" + ANSI_RESET);
            if (database.getCatalog().isEmpty()) {
                System.out.println("No books found.");
            }
            for (BookTitle t : database.getCatalog()) {
                t.printDetails();
            }
        } else {
            BookTitle t = database.findBookByTitle(q);
            if (t != null) {
                t.printDetails();
            } else {
                System.out.println(ANSI_YELLOW + "No match found." + ANSI_RESET);
            }
        }
    }

    private void actionShowAllItems() {
        System.out.println(ANSI_BOLD + "\n--- ALL PHYSICAL ITEMS ---" + ANSI_RESET);
        if (database.getInventory().isEmpty()) {
            System.out.println("Inventory empty.");
            return;
        }
        System.out.printf(ANSI_BOLD + "%-10s | %-20s | %-10s%n" + ANSI_RESET, "Barcode", "Title", "Status");
        for (BookItem i : database.getInventory()) {
            System.out.printf("%-10s | %-20s | %-10s%n",
                    i.getBarcode(),
                    truncate(i.getBookTitle().getTitle(), 20),
                    i.getStatus());
        }
    }

    private void actionViewMyActiveLoans(Borrower b) {
        System.out.println(ANSI_BOLD + "\nYour Active Loans:" + ANSI_RESET);
        if (b.getActiveLoans().isEmpty()) {
            System.out.println(ANSI_YELLOW + " - No active loans." + ANSI_RESET);
        }
        for (Loan l : b.getActiveLoans()) {
            System.out.println("- " + l.getBookItem().getBookTitle().getTitle() + " (Due: " + ANSI_RED + l.getDueDate() + ANSI_RESET + ")");
        }
    }

    // ==========================================
    // LIBRARIAN ACTIONS
    // ==========================================
    private void actionIssueBook(Staff staff) {
        int bid = readInt("Enter Borrower ID: ");
        Person p = database.findPersonById(bid);

        if (!(p instanceof Borrower)) {
            System.out.println(ANSI_RED + "Error: Not a valid borrower." + ANSI_RESET);
            return;
        }

        System.out.print("Enter Item Barcode: ");
        String bc = readRequiredString();
        BookItem item = database.findItemByBarcode(bc);

        if (item != null && item.getStatus() == BookStatus.AVAILABLE) {
            Loan l = new Loan((int) (Math.random() * 10000), (Borrower) p, item);
            database.addLoan(l);
            ((Borrower) p).addLoan(l);
            item.setStatus(BookStatus.LOANED);
            System.out.println(ANSI_GREEN + "Book Issued." + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Item not available or found." + ANSI_RESET);
        }
    }

    private void actionReturnBook() {
        System.out.print("Scan Barcode: ");
        String bc = readRequiredString();
        BookItem item = database.findItemByBarcode(bc);

        if (item != null && item.getStatus() == BookStatus.LOANED) {
            Loan loan = database.findActiveLoanByBookItem(item);
            if (loan != null) {
                loan.markReturned();
                double fine = loan.calculateFine();
                if (fine > 0) {
                    System.out.println(ANSI_YELLOW + "ALERT: Book Overdue. Fine: $" + String.format("%.2f", fine) + ANSI_RESET);
                }
                database.removeLoan(loan);
            }
            item.setStatus(BookStatus.AVAILABLE);
            System.out.println(ANSI_GREEN + "Book Returned." + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Item not currently loaned." + ANSI_RESET);
        }
    }

    private void actionSearchMemberActiveLoans() {
        int id = readInt("Enter Member ID: ");
        Person p = database.findPersonById(id);
        if (p instanceof Borrower) {
            actionViewMyActiveLoans((Borrower) p);
        } else {
            System.out.println(ANSI_RED + "User is not a borrower." + ANSI_RESET);
        }
    }

    private void actionSearchMemberHistory() {
        int id = readInt("Enter Member ID: ");
        Person p = database.findPersonById(id);
        if (p instanceof Borrower) {
            ((Borrower) p).printLoanHistory();
        } else {
            System.out.println(ANSI_RED + "User is not a borrower." + ANSI_RESET);
        }
    }

    // ==========================================
    // ADMIN ACTIONS
    // ==========================================
    private void menuManageTitles(Admin admin) {
        System.out.println(ANSI_BOLD + "\n--- MANAGE TITLES ---" + ANSI_RESET);
        System.out.println("1. Add Title\n2. Update Title\n3. Delete Title");
        System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);
        String c = readString();

        if (c.equals("1")) {
            System.out.print("ISBN: ");
            String isbn = readRequiredString();
            if (database.findBookByISBN(isbn) != null) {
                System.out.println(ANSI_RED + "ISBN Exists." + ANSI_RESET);
                return;
            }

            System.out.print("Title: ");
            String title = readRequiredString();
            System.out.print("Genre: ");
            String genre = readRequiredString();
            System.out.print("Pub: ");
            String pub = readRequiredString();
            BookTitle t = new BookTitle(isbn, title, genre, pub);

            System.out.print("Author Name: ");
            Author a = database.findAuthorByName(readString());
            if (a != null) {
                t.addAuthor(a);
            }

            // ADMIN method
            admin.addNewBookTitle(t, database);

        } else if (c.equals("2")) {
            System.out.print("ISBN to edit: ");
            BookTitle t = database.findBookByISBN(readString());
            if (t != null) {
                System.out.print("New Title: ");
                t.setTitle(readRequiredString());
                System.out.println(ANSI_GREEN + "Updated." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Not found." + ANSI_RESET);
            }

        } else if (c.equals("3")) {
            System.out.print("ISBN to delete: ");
            BookTitle t = database.findBookByISBN(readString());
            if (t != null) {
                database.removeBookTitle(t);
                System.out.println(ANSI_GREEN + "Deleted." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Not found." + ANSI_RESET);
            }
        }
    }

    private void menuManageItems(Admin admin) {
        System.out.println(ANSI_BOLD + "\n--- MANAGE ITEMS ---" + ANSI_RESET);
        System.out.println("1. Add Item\n2. Delete Item");
        System.out.print(ANSI_YELLOW + "Select: " + ANSI_RESET);
        String c = readString();

        if (c.equals("1")) {
            System.out.print("Link to ISBN: ");
            BookTitle t = database.findBookByISBN(readString());
            if (t != null) {
                System.out.print("New Barcode: ");
                String bc = readRequiredString();
                // Call BookItem constructor (NO LOCATION)
                admin.addBookItem(new BookItem(bc, t), database);
            } else {
                System.out.println(ANSI_RED + "Title not found." + ANSI_RESET);
            }
        } else if (c.equals("2")) {
            System.out.print("Barcode to delete: ");
            BookItem i = database.findItemByBarcode(readString());
            if (i != null) {
                database.removeBookItem(i);
                System.out.println(ANSI_GREEN + "Deleted." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Not found." + ANSI_RESET);
            }
        }
    }

    private void menuManageAuthors(Admin admin) {
        System.out.println(ANSI_BOLD + "\n--- MANAGE AUTHORS ---" + ANSI_RESET);
        // Show Existing
        System.out.println("Current Authors:");
        if (database.getAuthors().isEmpty()) {
            System.out.println(" - None.");
        } else {
            for (Author a : database.getAuthors()) {
                System.out.printf(" - %d: %s%n", a.getAuthorID(), a.getName());
            }
        }
        System.out.println("----------------------");

        System.out.println("1. Add Author\n2. Delete Author");
        String c = readString();
        if (c.equals("1")) {
            System.out.print("Name: ");
            database.addAuthor(new Author((int) (Math.random() * 1000), readRequiredString()));
            System.out.println(ANSI_GREEN + "Added." + ANSI_RESET);
        } else if (c.equals("2")) {
            System.out.print("Name to delete: ");
            Author a = database.findAuthorByName(readString());
            if (a != null) {
                database.removeAuthor(a);
                System.out.println(ANSI_GREEN + "Deleted." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Not found." + ANSI_RESET);
            }
        }
    }

    private void menuManagePersons(Admin admin) {
        System.out.println(ANSI_BOLD + "\n--- MANAGE PERSONS ---" + ANSI_RESET);
        // Show Existing
        System.out.printf("%-5s | %-20s | %-15s%n", "ID", "Name", "Role");
        for (Person p : database.getPersons()) {
            System.out.printf("%-5d | %-20s | %-15s%n", p.getId(), p.getName(), p.getRoleType());
        }
        System.out.println("---------------------------------------------");

        System.out.println("1. Add Borrower\n2. Add Librarian\n3. Add Admin\n4. Delete Person");
        String c = readString();

        if (c.equals("1") || c.equals("2") || c.equals("3")) {
            int id = readInt("Enter New User ID: ");
            if (database.findPersonById(id) != null) {
                System.out.println(ANSI_RED + "ID taken." + ANSI_RESET);
                return;
            }

            System.out.print("Name: ");
            String name = readRequiredString();
            System.out.print("Pass: ");
            String pass = readRequiredString();

            // Create Specific Classes
            if (c.equals("1")) {
                database.addPerson(new Borrower(id, name, pass, "Contact"));
            } else if (c.equals("2")) {
                database.addPerson(new Librarian(id, name, pass, "Contact", 3000));
            } else if (c.equals("3")) {
                database.addPerson(new Admin(id, name, pass, "Contact", 5000));
            }

            System.out.println(ANSI_GREEN + "User added." + ANSI_RESET);
        } else if (c.equals("4")) {
            int id = readInt("ID to delete: ");
            Person p = database.findPersonById(id);
            if (p != null) {
                if (p.getId() == loggedInUser.getId()) {
                    System.out.println(ANSI_RED + "Cannot delete self." + ANSI_RESET);
                    return;
                }
                database.removePerson(p);
                System.out.println(ANSI_GREEN + "Deleted." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Not found." + ANSI_RESET);
            }
        }
    }

    // ==========================================
    // HELPERS
    // ==========================================
    private String readString() {
        return scanner.nextLine().trim();
    }

    private String readRequiredString() {
        String input = "";
        while (input.isEmpty()) {
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.print(ANSI_RED + ">> Required. Try again: " + ANSI_RESET);
            }
        }
        return input;
    }

    private int readInt(String prompt) {
        int result = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                result = Integer.parseInt(scanner.nextLine().trim());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + ">> Enter a number." + ANSI_RESET);
            }
        }
        return result;
    }

    private void waitForEnter() {
        System.out.println(ANSI_YELLOW + "\n[Press Enter]" + ANSI_RESET);
        scanner.nextLine();
    }

    private String truncate(String s, int len) {
        if (s.length() > len) {
            return s.substring(0, len - 3) + "...";
        }
        return s;
    }

    private void printHeader(String title) {
        System.out.println("\n" + ANSI_BLUE + "==========================================" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "=== " + title + " ===" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "==========================================" + ANSI_RESET);
    }

    // ==========================================
    // DUMMY DATA SETUP
    // ==========================================
    private void setupDummyData() {
        System.out.println(ANSI_CYAN + "Initializing Database..." + ANSI_RESET);

        // ---------------------------------------------------------
        // 1. AUTHORS (5 Total)
        // ---------------------------------------------------------
        Author[] authors = new Author[5];
        authors[0] = new Author(1, "J.K. Rowling");
        authors[1] = new Author(2, "J.R.R. Tolkien");
        authors[2] = new Author(3, "Agatha Christie");
        authors[3] = new Author(4, "George Orwell");
        authors[4] = new Author(5, "Haruki Murakami");

        for (Author a : authors) {
            database.addAuthor(a);
        }

        // ---------------------------------------------------------
        // 2. BOOK TITLES (10 Total)
        // ---------------------------------------------------------
        BookTitle[] titles = new BookTitle[10];

        // Fantasy
        titles[0] = new BookTitle("978-1", "Harry Potter 1", "Fantasy", "Scholastic");
        titles[0].addAuthor(authors[0]);
        titles[1] = new BookTitle("978-2", "Harry Potter 2", "Fantasy", "Scholastic");
        titles[1].addAuthor(authors[0]);
        titles[2] = new BookTitle("978-3", "The Hobbit", "Fantasy", "Houghton");
        titles[2].addAuthor(authors[1]);
        titles[3] = new BookTitle("978-4", "The Lord of the Rings", "Fantasy", "Houghton");
        titles[3].addAuthor(authors[1]);

        // Mystery
        titles[4] = new BookTitle("978-5", "And Then There Were None", "Mystery", "Harper");
        titles[4].addAuthor(authors[2]);
        titles[5] = new BookTitle("978-6", "Murder on the Orient Express", "Mystery", "Harper");
        titles[5].addAuthor(authors[2]);

        // Classics
        titles[6] = new BookTitle("978-7", "1984", "Dystopian", "Secker");
        titles[6].addAuthor(authors[3]);
        titles[7] = new BookTitle("978-8", "Animal Farm", "Satire", "Secker");
        titles[7].addAuthor(authors[3]);

        // Fiction
        titles[8] = new BookTitle("978-9", "Norwegian Wood", "Fiction", "Kodansha");
        titles[8].addAuthor(authors[4]);
        titles[9] = new BookTitle("978-10", "Kafka on the Shore", "Fiction", "Kodansha");
        titles[9].addAuthor(authors[4]);
        titles[9].addAuthor(authors[3]);

        for (BookTitle t : titles) {
            database.addBookTitle(t);
        }

        // ---------------------------------------------------------
        // 3. BOOK ITEMS (25 Total)
        // ---------------------------------------------------------
        // Distribute 25 items across the 10 titles (approx 2-3 copies each)
        int barcode = 1000;
        for (int i = 0; i < 25; i++) {
            BookTitle t = titles[i % 10]; // Cycles through titles 0-9
            database.addBookItem(new BookItem("B" + barcode++, t));
        }

        // ---------------------------------------------------------
        // 4. USERS (6 Total: 1 Admin, 2 Librarians, 3 Borrowers)
        // ---------------------------------------------------------
        database.addPerson(new Admin(1, "Super Admin", "admin123", "admin@lib.com", 5000));

        database.addPerson(new Librarian(2, "Alice Lib", "staff1", "alice@lib.com", 3000));
        database.addPerson(new Librarian(3, "Bob Lib", "staff2", "bob@lib.com", 3000));

        database.addPerson(new Borrower(4, "Charlie", "pass1", "charlie@gmail.com"));
        database.addPerson(new Borrower(5, "Dave", "pass2", "dave@gmail.com"));
        database.addPerson(new Borrower(6, "Eve", "pass3", "eve@gmail.com"));

        System.out.println(ANSI_CYAN + "Data Loaded: 5 Authors, 10 Titles, 25 Items, 6 Users." + ANSI_RESET);
    }

}
