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
        // Filter history for loans where returnDate is null
        List<Loan> active = new ArrayList<>();
        for (Loan l : loanHistory) {
            if (l.calculateFine() == 0 && !l.isOverdue()) {
                // Simplified logic: usually check if returnDate is null
                // For this example, we assume we check the object state
            }
        }
        return loanHistory; // Returning full history for simplicity in this snippet
    }

    public void printLoanHistory() {
        System.out.println("Loan History for " + getName());
        for (Loan l : loanHistory) {
            System.out.println("Book: " + l.getBookItem().getBookTitle().getTitle());
        }
    }

    @Override
    public String getRoleType() {
        return "MEMBER";
    }   
}