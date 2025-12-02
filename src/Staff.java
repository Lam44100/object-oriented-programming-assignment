public class Staff extends Person {
    private StaffRole role;
    private double salary;

    public Staff(int id, String name, String password, String contactInfo, StaffRole role, double salary) {
        super(id, name, password, contactInfo);
        this.role = role;
        this.salary = salary;
    }

    public void addNewBookTitle(BookTitle title, LibraryDatabase db) {
        db.addBookTitle(title);
        System.out.println("Title added: " + title.getTitle());
    }

    public void addBookItem(BookItem item, LibraryDatabase db) {
        db.addBookItem(item);
        System.out.println("Item added. Barcode: " + item.getBarcode());
    }
}