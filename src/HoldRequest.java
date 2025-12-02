public class HoldRequest {
    private LocalDate requestDate;
    private Borrower borrower;
    private BookTitle bookTitle; // Links to Title, not Item
    private RequestStatus status;

    public HoldRequest(Borrower borrower, BookTitle bookTitle) {
        this.borrower = borrower;
        this.bookTitle = bookTitle;
        this.requestDate = LocalDate.now();
        this.status = RequestStatus.PENDING;
    }

    public void cancelRequest() {
        this.status = RequestStatus.CANCELED;
        System.out.println("Hold request canceled.");
    }
}