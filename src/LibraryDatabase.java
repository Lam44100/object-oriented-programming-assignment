public class LibraryDatabase {
    // Corrected attributes based on the new UML (Catalog vs Inventory)
    private List<BookTitle> catalog;
    private List<BookItem> inventory;
    private List<Person> persons;
    private List<Loan> activeLoans;
    private List<HoldRequest> holdRequests;

    public LibraryDatabase() {
        this.catalog = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.persons = new ArrayList<>();
        this.activeLoans = new ArrayList<>();
        this.holdRequests = new ArrayList<>();
    }

    public void addBookTitle(BookTitle t) {
        catalog.add(t);
    }

    public void addBookItem(BookItem i) {
        inventory.add(i);
    }

    public BookTitle findBookByTitle(String title) {
        for (BookTitle t : catalog) {
            if (t.getTitle().equalsIgnoreCase(title))
                return t;
        }
        return null;
    }

    public BookItem findItemByBarcode(String barcode) {
        for (BookItem i : inventory) {
            if (i.getBarcode().equals(barcode))
                return i;
        }
        return null;
    }

    public void addLoan(Loan loan) {
        activeLoans.add(loan);
    }

    public void removeLoan(Loan loan) {
        activeLoans.remove(loan);
    }

    public List<BookItem> getInventory() {
        return inventory;
    }
}