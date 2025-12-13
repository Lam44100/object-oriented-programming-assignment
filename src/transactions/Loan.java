package transactions;

import entities.BookItem;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import users.Borrower;

public class Loan {
    private int loanID;
    private Borrower borrower;
    private BookItem bookItem; // Links to physical item
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(int loanID, Borrower borrower, BookItem bookItem) {
        this.loanID = loanID;
        this.borrower = borrower;
        this.bookItem = bookItem;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(14); // 2 week loan period
    }

    public int getLoanID() {
        return loanID;
    }

    public BookItem getBookItem() {
        return bookItem;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void markReturned() {
        this.returnDate = LocalDate.now();
    }

    public boolean isOverdue() {
        if (returnDate != null) {
            return returnDate.isAfter(dueDate);
        }
        return LocalDate.now().isAfter(dueDate);
    }

    public double calculateFine() {
        if (!isOverdue())
            return 0.0;

        LocalDate end = (returnDate != null) ? returnDate : LocalDate.now();
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, end);

        return daysOverdue * 0.50; // $0.50 fine per day
    }
}