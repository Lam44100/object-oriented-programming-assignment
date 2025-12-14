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
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BOLD = "\u001B[1m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";

        System.out.println(ANSI_BOLD + "\nLoan History for " + getName() + ANSI_RESET);
        if (loanHistory.isEmpty()) {
            System.out.println(ANSI_YELLOW + " - No history." + ANSI_RESET);
        }
        for (Loan l : loanHistory) {
            // IMPROVEMENT: Show return date if the book has been returned, with status
            // colors
            String dateInfo;
            if (l.getReturnDate() != null) {
                dateInfo = ANSI_GREEN + "Returned: " + l.getReturnDate() + ANSI_RESET;
            } else if (l.isOverdue()) {
                dateInfo = ANSI_RED + "OVERDUE (Due: " + l.getDueDate() + ")" + ANSI_RESET;
            } else {
                dateInfo = "Due: " + l.getDueDate();
            }

            System.out.println(" - " + l.getBookItem().getBookTitle().getTitle() +
                    " (" + dateInfo + ")");
        }
    }

    @Override
    public String getRoleType() {
        return "MEMBER";
    }
}