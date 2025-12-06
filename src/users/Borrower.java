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
            // In a real app check if l.getReturnDate() == null
            if (!l.isOverdue() && l.calculateFine() == 0) {
                // logic simplified for demo
                active.add(l);
            }
        }
        return loanHistory;
    }

    public void printLoanHistory() {
        System.out.println("Loan History for " + getName());
        if (loanHistory.isEmpty()) {
            System.out.println(" - No history.");
        }
        for (Loan l : loanHistory) {
            System.out.println(" - " + l.getBookItem().getBookTitle().getTitle() +
                    " (Due: " + l.getDueDate() + ")");
        }
    }

    @Override
    public String getRoleType() {
        return "MEMBER";
    }
}