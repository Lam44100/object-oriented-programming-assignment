public class LibrarySystem {
    private LibraryDatabase database;
    private Scanner scanner;
    private Person loggedInUser;

    public LibrarySystem() {
        this.database = new LibraryDatabase();
        this.scanner = new Scanner(System.in);
        // Pre-load some dummy data for testing
        setupDummyData();
    }

    private void setupDummyData() {
        BookTitle t1 = new BookTitle("978-3-16", "Java Programming", "Education", "TechPress");
        BookItem i1 = new BookItem("B001", t1, "A1");
        Borrower b1 = new Borrower(1, "John Doe", "pass123", "john@email.com");

        database.addBookTitle(t1);
        database.addBookItem(i1);
        // In a real app, you would add person to database list too
    }

    public void run() {
        System.out.println("Welcome to Library System");
        // Simple login flow
        System.out.print("Enter ID: ");
        // Assuming user ID 1 exists (John Doe)
        int id = scanner.nextInt();

        // Simulating logged in user for this example
        Borrower currentUser = new Borrower(id, "John Doe", "pass", "contact");
        System.out.println("Logged in as " + currentUser.getName());

        boolean running = true;
        while (running) {
            System.out.println("\n1. Search Book\n2. Issue Book\n3. Return Book\n4. Exit");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    searchBook();
                    break;
                case 2:
                    // Hardcoded inputs for demonstration
                    BookItem item = database.findItemByBarcode("B001");
                    if (item != null)
                        issueBook(currentUser, item);
                    break;
                case 3:
                    returnBook("B001");
                    break;
                case 4:
                    running = false;
                    break;
            }
        }
    }

    public void searchBook() {
        System.out.print("Enter Title: ");
        String query = scanner.next();
        BookTitle result = database.findBookByTitle(query);
        if (result != null) {
            result.printDetails();
        } else {
            System.out.println("Book not found.");
        }
    }

    public void issueBook(Borrower b, BookItem item) {
        if (item.getStatus() == BookStatus.AVAILABLE) {
            Loan loan = new Loan(101, b, item); // ID generated randomly in real app

            database.addLoan(loan);
            b.addLoan(loan);
            item.setStatus(BookStatus.LOANED);

            System.out.println("Book issued successfully!");
        } else {
            System.out.println("Book is not available.");
        }
    }

    public void returnBook(String barcode) {
        BookItem item = database.findItemByBarcode(barcode);
        if (item != null && item.getStatus() == BookStatus.LOANED) {
            item.setStatus(BookStatus.AVAILABLE);
            System.out.println("Book returned. Fine calculated based on dates.");
            // Logic to find specific loan and markReturned() would go here
        } else {
            System.out.println("Invalid return.");
        }
    }

    public static void main(String[] args) {
        LibrarySystem app = new LibrarySystem();
        app.run();
    }
}