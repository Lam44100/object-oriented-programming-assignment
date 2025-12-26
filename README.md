
## Project Introduction
This project is a Library Management System developed as part of an Object-Oriented Programming assignment. It demonstrates core OOP principles such as inheritance, encapsulation, and polymorphism, and provides functionalities for managing books, authors, users, and transactions in a library environment.

## Project Structure

```
object-oriented-programming-assignment/
├── README.md
├── bin/
│   ├── core/
│   ├── entities/
│   ├── enums/
│   ├── transactions/
│   └── users/
├── src/
│   ├── Test.java
│   ├── core/
│   │   ├── LibraryDatabase.java
│   │   └── LibrarySystem.java
│   ├── entities/
│   │   ├── Author.java
│   │   ├── BookItem.java
│   │   └── BookTitle.java
│   ├── enums/
│   │   ├── AccountStatus.java
│   │   └── BookStatus.java
│   ├── transactions/
│   │   └── Loan.java
│   └── users/
│       ├── Admin.java
│       ├── Borrower.java
│       ├── Librarian.java
│       ├── Person.java
│       └── Staff.java
```

### Folder Overview
- **src/**: Contains all source code files.
  - **core/**: Main system logic and database connection classes.
  - **entities/**: Data models for authors, books, and titles.
  - **enums/**: Enumerations for statuses and roles.
  - **transactions/**: Classes for handling loans and hold requests.
  - **users/**: User-related classes (borrowers, staff, etc.).
  - **test.java**: Entry point for running and testing the application.
- **bin/**: Compiled Java class files (generated after building).

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or above
- A Java IDE (e.g., VS Code, IntelliJ IDEA, Eclipse) or command line

### How to Compile and Run

1. **Open a terminal in the project root directory.**

2. **Run the program:**
   Execute the `test.java` main class.


## User Login Credentials (RBAC Demo)
The system uses Role-Based Access Control (RBAC) with the following default users:

| Role   | Name           | User ID | Username         | Password   | Email             |
|--------|----------------|---------|------------------|------------|-------------------|
| Admin  | Super Admin    | 1       | admin@lib.com    | admin123   | admin@lib.com     |
| Staff  | Alice Staff    | 2       | alice@lib.com    | staff1     | alice@lib.com     |
| Staff  | Bob Staff      | 3       | bob@lib.com      | staff2     | bob@lib.com       |
| Member | Charlie Member | 4       | charlie@gmail.com| pass1      | charlie@gmail.com |
| Member | Dave Member    | 5       | dave@gmail.com   | pass2      | dave@gmail.com    |
| Member | Eve Member     | 6       | eve@gmail.com    | pass3      | eve@gmail.com     |


**Login Instructions:**
- Use the User ID and Password above to log in as different roles.
- Admins have full access to CRUD operations and management features.
- Staff can manage loans and view member info.
- Members can view/search catalog and their own loans.

**Note:**
- You can add or remove users via the Admin menu in the running application.
- All data is in-memory and resets on each run.

---
Feel free to explore the code, make improvements, and ask questions if you're new to Java or object-oriented programming!

