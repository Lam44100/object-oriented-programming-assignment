package core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entities.*;
import enums.*;
import users.*;
import transactions.Loan;

public class LibraryDatabase {

    // =============================================================
    // BOOK TITLE (METADATA) OPERATIONS
    // =============================================================

    public void addBookTitle(BookTitle title) {
        String sql = "INSERT INTO book_titles (isbn, title, author, genre, publisher) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title.getIsbn());
            stmt.setString(2, title.getTitle());
            stmt.setString(3, title.getAuthorNames()); // Storing authors as string for simplicity
            stmt.setString(4, "General"); // Assuming genre getter or default
            stmt.setString(5, "Publisher"); // Assuming publisher getter or default

            stmt.executeUpdate();
            System.out.println("Saved Title: " + title.getTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BookTitle findBookByTitle(String searchTitle) {
        String sql = "SELECT * FROM book_titles WHERE title LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + searchTitle + "%");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Map SQL Row -> Java Object
                BookTitle bt = new BookTitle(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("publisher"));
                // Reconstruct Author object from string
                String authorName = rs.getString("author");
                if (authorName != null) {
                    bt.addAuthor(new Author(0, authorName));
                }
                return bt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // =============================================================
    // BOOK ITEM (INVENTORY) OPERATIONS
    // =============================================================

    public void addBookItem(BookItem item) {
        String sql = "INSERT INTO book_items (barcode, isbn, status, rack_location, purchase_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getBarcode());
            stmt.setString(2, item.getBookTitle().getIsbn());
            stmt.setString(3, item.getStatus().toString());
            stmt.setString(4, "Shelf A"); // Assuming location getter
            stmt.setDate(5, java.sql.Date.valueOf(java.time.LocalDate.now()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BookItem findItemByBarcode(String barcode) {
        String sql = "SELECT * FROM book_items WHERE barcode = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // 1. We need the Title first
                String isbn = rs.getString("isbn");
                BookTitle title = findBookByISBN(isbn); // Helper method needed

                // 2. Create the Item
                BookItem item = new BookItem(
                        rs.getString("barcode"),
                        title,
                        rs.getString("rack_location"));
                item.setStatus(BookStatus.valueOf(rs.getString("status")));
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to support finding items
    private BookTitle findBookByISBN(String isbn) {
        String sql = "SELECT * FROM book_titles WHERE isbn = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BookTitle bt = new BookTitle(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("publisher"));
                return bt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // =============================================================
    // USER OPERATIONS
    // =============================================================

    public Person findPersonById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role_type");

                if ("MEMBER".equalsIgnoreCase(role)) {
                    return new Borrower(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getString("contact_info"));
                } else {
                    // It's a Staff member
                    return new Staff(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getString("contact_info"),
                            StaffRole.valueOf(role), // LIBRARIAN, ADMIN, etc.
                            rs.getDouble("salary"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to add users (optional, but good for setup)
    public void addPerson(Person p) {
        String sql = "INSERT INTO users (id, name, password, contact_info, role_type, salary) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getId());
            stmt.setString(2, p.getName());
            // Note: In real app, password should be hashed here but Person doesn't expose
            // it easily.
            // For now assuming a constructor or getter we can't see, or hardcoding "pass"
            // for testing.
            stmt.setString(3, "password");
            stmt.setString(4, "contact");
            stmt.setString(5, p.getRoleType());

            if (p instanceof Staff) {
                stmt.setDouble(6, 5000.00); // Placeholder or getter
            } else {
                stmt.setDouble(6, 0.0);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =============================================================
    // LOAN OPERATIONS
    // =============================================================

    public void addLoan(Loan loan) {
        String sql = "INSERT INTO loans (borrower_id, barcode, issue_date, due_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loan.getBorrower().getId());
            stmt.setString(2, loan.getBookItem().getBarcode());
            stmt.setDate(3, java.sql.Date.valueOf(loan.getIssueDate())); // You need a getter in Loan
            stmt.setDate(4, java.sql.Date.valueOf(loan.getDueDate())); // You need a getter in Loan

            stmt.executeUpdate();
            System.out.println("Loan saved to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE return_date IS NULL"; // Only active loans

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Reconstruct the objects needed for Loan
                int loanId = rs.getInt("loan_id");
                Borrower b = (Borrower) findPersonById(rs.getInt("borrower_id"));
                BookItem item = findItemByBarcode(rs.getString("barcode"));

                // Create Loan object (assuming your Loan constructor fits or you create a new
                // one)
                // Note: You might need to update Loan class to accept specific dates if they
                // differ from "now"
                Loan l = new Loan(loanId, b, item);
                loans.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }
}