package entities;

import enums.BookStatus;
import java.time.LocalDate;

public class BookItem {

    private String barcode;
    private BookTitle bookTitle;
    private BookStatus status;
    private LocalDate purchaseDate;

    public BookItem(String barcode, BookTitle bookTitle) { 
        this.barcode = barcode;
        this.bookTitle = bookTitle;
        this.status = BookStatus.AVAILABLE; // Default
        this.purchaseDate = LocalDate.now();
    }

    public String getBarcode() {
        return barcode;
    }

    public BookTitle getBookTitle() {
        return bookTitle;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public void printInfo() {
        bookTitle.printDetails();
        System.out.println("Barcode: " + barcode);
        System.out.println("Status: " + status);
        System.out.println("-------------------------");
    }
}
