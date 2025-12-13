package users;

import enums.AccountStatus;
import java.util.ArrayList;
import java.util.List;
import transactions.Loan;

public class Borrower extends Person {
    private AccountStatus accountStatus;
    private int maxBookLimit;
    private List<Loan> loanHistory;

    public Borrower(int id, String name, String password, String contactInfo) {
        super(id, name, password, contactInfo);
        this.accountStatus = AccountStatus.ACTIVE;
        this.maxBookLimit = 5;
        this.loanHistory = new ArrayList<>();
    }

    public void addLoan(Loan loan) {
        loanHistory.add(loan);
    }

    public List<Loan> getActiveLoans() {
        List<Loan> active = new ArrayList<>();
        for (Loan l : loanHistory) {
            // FIX 2: An active loan is one that has NOT been marked as returned.
            if (l.getReturnDate() == null) {
                active.add(l);
            }
        }
        // FIX 3: Return the filtered 'active' list, not the whole 'loanHistory'.
        return active;
    }

    public void printLoanHistory() {
        System.out.println("Loan History for " + getName());
        if (loanHistory.isEmpty()) {
            System.out.println(" - No history.");
        }
        for (Loan l : loanHistory) {
            // IMPROVEMENT: Show return date if the book has been returned
            String dateInfo = (l.getReturnDate() != null)
                    ? "Returned: " + l.getReturnDate()
                    : "Due: " + l.getDueDate();

            System.out.println(" - " + l.getBookItem().getBookTitle().getTitle() +
                    " (" + dateInfo + ")");
        }
    }

    @Override
    public String getRoleType() {
        return "MEMBER";
    }
}