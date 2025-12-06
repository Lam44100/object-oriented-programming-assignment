package core;

import entities.*;
import enums.*;
import java.util.List;
import java.util.Scanner;
import transactions.*;
import users.*;

public class LibrarySystem {
    private LibraryDatabase database;
    private Scanner scanner;
    private Person loggedInUser;

    public LibrarySystem() {
        this.database = new LibraryDatabase();
        this.scanner = new Scanner(System.in);
        // setupDummyData();
    }

    public static void main(String[] args) {
        LibrarySystem app = new LibrarySystem();
        app.run();
    }

    // ==========================================
    // MAIN ENTRY POINT
    // ==========================================
    public void run() {
        boolean systemRunning = true;
        while (systemRunning) {
            printHeader("MAIN MENU");
            System.out.println("1. Login");
            System.out.println("2. Exit System");
            System.out.print("Select Option: ");

            String choice = readString(); // Safe input reader

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    System.out.println("Shutting down... Goodbye!");
                    systemRunning = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ==========================================
    // LOGIN LOGIC & ROUTING
    // ==========================================
    private void handleLogin() {
        printHeader("SECURE LOGIN");

        // 1. INPUT ID
        System.out.print("Enter User ID: ");
        int id;
        try {
            id = Integer.parseInt(readString());
        } catch (NumberFormatException e) {
            System.out.println(">> Error: Invalid ID format.");
            return;
        }

        // 2. FIND USER (Verification)
        Person person = database.findPersonById(id);
        if (person == null) {
            System.out.println(">> Error: User ID not found.");
            return;
        }

        // 3. INPUT PASSWORD (Authentication)
        System.out.print("Enter Password: ");
        String inputPass = readString();

        // 4. VALIDATE CREDENTIALS
        if (person.validatePassword(inputPass)) {
            this.loggedInUser = person;
            System.out.println("\nLogin Successful! Welcome, " + person.getName());
            System.out.println("Access Level: " + person.getRoleType());

            // 5. ROUTE TO ROLE-SPECIFIC DASHBOARD
            if (person instanceof Staff) {
                showStaffDashboard((Staff) person);
            } else if (person instanceof Borrower) {
                showBorrowerDashboard((Borrower) person);
            }
        } else {
            System.out.println(">> Error: Incorrect Password.");
            // Security best practice: Don't tell them if it was the ID or Pass that was
            // wrong,
            // but for this console app, being specific helps testing.
        }
    }

    // ==========================================
    // DASHBOARD: STAFF
    // ==========================================
    private void showStaffDashboard(Staff staff) {
        boolean sessionActive = true;
        while (sessionActive) {
            // Dynamic Header based on Role
            printHeader("STAFF PANEL | " + staff.getName() + " | " + staff.getRoleType());

            // Common Functions for ALL Staff
            System.out.println("1. Search Catalog");
            System.out.println("2. Issue Book (Checkout)");
            System.out.println("3. Return Book (Checkin)");

            // RBAC: Restricted Functions (Librarian/Admin Only)
            // We hide these options from Clerks to prevent confusion
            if (isAdminOrLibrarian(staff)) {
                System.out.println("4. [ADMIN] Add New Book Title");
                System.out.println("5. [ADMIN] Add Book Copy");
                System.out.println("6. [ADMIN] View All Active Loans");
            }

            System.out.println("0. Logout");
            System.out.print("Select Action: ");

            String choice = readString();

            switch (choice) {
                case "1":
                    actionSearchBook();
                    break;
                case "2":
                    actionIssueBook(staff);
                    break;
                case "3":
                    actionReturnBook();
                    break;

                // RBAC ENFORCEMENT
                case "4":
                    if (checkPermission(staff))
                        actionAddNewTitle(staff);
                    break;
                case "5":
                    if (checkPermission(staff))
                        actionAddBookCopy(staff);
                    break;
                case "6":
                    if (checkPermission(staff))
                        actionViewAllLoans();
                    break;

                case "0":
                    sessionActive = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
            if (sessionActive)
                waitForEnter();
        }
    }

    private void actionViewAllLoans() {
        System.out.println("--- ALL ACTIVE LOANS (ADMIN VIEW) ---");

        List<Loan> allLoans = database.getAllLoans();

        if (allLoans.isEmpty()) {
            System.out.println("No books are currently on loan.");
        } else {
            System.out.println(String.format("%-5s | %-20s | %-15s | %-10s", "ID", "Title", "Borrower", "Due Date"));
            System.out.println("-------------------------------------------------------------");

            for (Loan loan : allLoans) {
                System.out.println(String.format("%-5d | %-20s | %-15s | %-10s",
                        loan.getLoanID(),
                        truncate(loan.getBookItem().getBookTitle().getTitle(), 20),
                        truncate(loan.getBorrower().getName(), 15),
                        loan.getDueDate().toString()));
            }
        }
    }

    private String truncate(String value, int length) {
        if (value.length() > length) {
            return value.substring(0, length - 3) + "...";
        }
        return value;
    }

    // Helper for RBAC Logic
    private boolean isAdminOrLibrarian(Staff staff) {
        StaffRole role = staff.getRole(); // Assuming you added a getter in Staff class
        return role == StaffRole.LIBRARIAN || role == StaffRole.ADMIN;
    }

    // Security Check Helper
    private boolean checkPermission(Staff staff) {
        if (isAdminOrLibrarian(staff)) {
            return true;
        } else {
            System.out.println(">> ACCESS DENIED: You do not have permission to perform this action.");
            return false;
        }
    }

    // ==========================================
    // DASHBOARD: BORROWER
    // ==========================================
    private void showBorrowerDashboard(Borrower borrower) {
        boolean sessionActive = true;
        while (sessionActive) {
            printHeader("MEMBER DASHBOARD | " + borrower.getName());
            System.out.println("1. Search Catalog");
            System.out.println("2. My Active Loans");
            System.out.println("3. My Loan History");
            System.out.println("4. Place Hold Request");
            System.out.println("5. Logout");
            System.out.print("Select Action: ");

            String choice = readString();

            switch (choice) {
                case "1":
                    actionSearchBook();
                    break;
                case "2":
                    actionViewMyActiveLoans(borrower);
                    break;
                case "3":
                    borrower.printLoanHistory();
                    break; // Using method from Borrower class
                case "4":
                    actionPlaceHold(borrower);
                    break;
                case "5":
                    sessionActive = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
            if (sessionActive)
                waitForEnter();
        }
    }

    // ==========================================
    // ACTIONS (Implemented Functions)
    // ==========================================

    // 1. SEARCH (Shared by all)
    private void actionSearchBook() {
        System.out.print("Enter partial title to search: ");
        String query = readString();
        BookTitle result = database.findBookByTitle(query);

        if (result != null) {
            result.printDetails();
        } else {
            System.out.println("No books found matching '" + query + "'.");
        }
    }

    // 2. ISSUE BOOK (Staff Only)
    private void actionIssueBook(Staff staff) {
        System.out.print("Enter Borrower ID: ");
        int bId = Integer.parseInt(readString());
        Person p = database.findPersonById(bId);

        if (!(p instanceof Borrower)) {
            System.out.println("Error: ID does not belong to a Borrower.");
            return;
        }

        System.out.print("Scan Book Barcode: ");
        String barcode = readString();
        BookItem item = database.findItemByBarcode(barcode); // You need this in DB

        if (item == null) {
            System.out.println("Error: Item not found.");
            return;
        }

        // Logic Check
        if (item.getStatus() == BookStatus.AVAILABLE) {
            Loan loan = new Loan(generateId(), (Borrower) p, item);
            database.addLoan(loan);
            ((Borrower) p).addLoan(loan);
            item.setStatus(BookStatus.LOANED);
            System.out.println("SUCCESS: Book issued to " + p.getName());
        } else {
            System.out.println("FAILURE: Book is currently " + item.getStatus());
        }
    }

    // 3. RETURN BOOK (Staff Only)
    private void actionReturnBook() {
        System.out.print("Scan Book Barcode: ");
        String barcode = readString();
        BookItem item = database.findItemByBarcode(barcode);

        if (item != null && item.getStatus() == BookStatus.LOANED) {
            item.setStatus(BookStatus.AVAILABLE);
            // In a real app, find the specific Loan object and set returnDate
            System.out.println("SUCCESS: Book returned. Status set to AVAILABLE.");
        } else {
            System.out.println("Error: Book is not currently loaned out or does not exist.");
        }
    }

    // 4. ADD NEW TITLE (Staff Only)
    private void actionAddNewTitle(Staff staff) {
        System.out.println("--- Creating New Biblio Record ---");
        System.out.print("Enter ISBN: ");
        String isbn = readString();
        System.out.print("Enter Title: ");
        String title = readString();
        System.out.print("Enter Genre: ");
        String genre = readString();
        System.out.print("Enter Publisher: ");
        String pub = readString();

        BookTitle newTitle = new BookTitle(isbn, title, genre, pub);

        System.out.print("Enter Author Name: ");
        String authorName = readString();
        newTitle.addAuthor(new Author(generateId(), authorName));

        // Use the Staff method to add to DB
        staff.addNewBookTitle(newTitle, database);
    }

    // 5. ADD COPY (Staff Only)
    private void actionAddBookCopy(Staff staff) {
        System.out.print("Enter ISBN to link copy to: ");
        String isbn = readString();
        // NOTE: You need a findByISBN method in DB, or search by title for now
        System.out.println("(Simulated) Found ISBN match.");

        // Mocking the title for this example since we don't have findByISBN implemented
        // yet
        BookTitle t = database.findBookByTitle("Harry Potter");

        if (t != null) {
            System.out.print("Enter New Barcode: ");
            String bc = readString();
            System.out.print("Enter Shelf Location: ");
            String loc = readString();

            BookItem newItem = new BookItem(bc, t, loc);
            staff.addBookItem(newItem, database);
        } else {
            System.out.println("Error: ISBN not found in catalog. Create Title first.");
        }
    }

    // 6. PLACE HOLD (Borrower Only)
    private void actionPlaceHold(Borrower borrower) {
        System.out.print("Enter Title to Reserve: ");
        String query = readString();
        BookTitle title = database.findBookByTitle(query);

        if (title != null) {
            HoldRequest req = new HoldRequest(borrower, title);
            // Ideally add to a list in Database
            System.out.println("Hold Request placed for: " + title.getTitle());
            System.out.println("You are #1 in the queue.");
        } else {
            System.out.println("Book title not found.");
        }
    }

    private void actionViewMyActiveLoans(Borrower b) {
        System.out.println("--- Active Loans ---");
        // This assumes Borrower has a getActiveLoans method
        for (Loan l : b.getActiveLoans()) {
            System.out.println(l.getBookItem().getBookTitle().getTitle() +
                    " (Due: " + l.getDueDate() + ")");
        }
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================

    // Solves the "Ghost Newline" Scanner bug completely
    private String readString() {
        return scanner.nextLine().trim();
    }

    private void printHeader(String title) {
        System.out.println("\n========================================");
        System.out.println(" " + title.toUpperCase());
        System.out.println("========================================");
    }

    private void waitForEnter() {
        System.out.println("\n[Press Enter to Continue]");
        scanner.nextLine();
    }

    private int generateId() {
        return (int) (Math.random() * 10000);
    }

    // private void setupDummyData() {
    // // Reuse the data setup code provided in the previous answer
    // // ... (Paste the setupDummyData content here) ...
    // // Ensure you add users to the list:
    // // database.addPerson(new Borrower(101, "John", "pass", "contact"));
    // // database.addPerson(new Staff(901, "Admin", "pass", "contact",
    // // StaffRole.LIBRARIAN, 5000));

    // // --- Quick Mock for testing navigation ---
    // Author a1 = new Author(1, "J.K. Rowling");
    // Author a2 = new Author(2, "J.R.R. Tolkien");
    // Author a3 = new Author(3, "George R.R. Martin");
    // Author a4 = new Author(4, "Agatha Christie");

    // BookTitle t1 = new BookTitle("978-1", "Harry Potter", "Fantasy",
    // "Bloomsbury");
    // BookTitle t2 = new BookTitle("978-2", "The Hobbit", "Fantasy",
    // "HarperCollins");
    // BookTitle t3 = new BookTitle("978-3", "A Game of Thrones", "Fantasy", "Bantam
    // Books");

    // t1.addAuthor(a1);
    // t2.addAuthor(a2);
    // t3.addAuthor(a3);
    // t3.addAuthor(a4);

    // database.addBookTitle(t1);
    // database.addBookTitle(t2);
    // database.addBookTitle(t3);

    // BookItem i1 = new BookItem("B001", t1, "A1");
    // database.addBookItem(i1);
    // BookItem i2 = new BookItem("B002", t2, "B2");
    // database.addBookItem(i2);
    // BookItem i3 = new BookItem("B003", t3, "C3");
    // database.addBookItem(i3);
    // BookItem i4 = new BookItem("B004", t3, "C4");
    // database.addBookItem(i4);

    // Borrower b1 = new Borrower(101, "John Doe", "pass", "c");
    // Staff s1 = new Staff(901, "Admin", "pass", "c", StaffRole.ADMIN, 5000);
    // Staff s2 = new Staff(902, "Librarian", "pass", "c", StaffRole.LIBRARIAN,
    // 4000);

    // database.addPerson(b1);
    // database.addPerson(s1);
    // database.addPerson(s2);
    // }

    // public static void main(String[] args) {
    // LibrarySystem app = new LibrarySystem();
    // app.run();
    // }
}