## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure


# Object-Oriented Programming Assignment

## Project Structure

```
README.md
bin/
	core/
	entities/
	enums/
	transactions/
	users/
src/
	test.java
	core/
		DatabaseConnection.java
		LibraryDatabase.java
		LibrarySystem.java
	entities/
		Author.java
		BookItem.java
		BookTitle.java
	enums/
		AccountStatus.java
		BookStatus.java
		RequestStatus.java
		StaffRole.java
	transactions/
		HoldRequest.java
		Loan.java
	users/
		Borrower.java
		Person.java
		Staff.java
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
2. **Compile the source files:**
   ```powershell
   javac -d bin src/**/*.java
   ```
   This will compile all Java files in `src/` and place the class files in `bin/`.
3. **Run the program:**
   ```powershell
   java -cp bin test
   ```
   This will execute the `test.java` main class.

### Notes
- Make sure your terminal's current directory is the project root (where `README.md` is located).
- If you add new Java files, recompile before running.
- If you encounter any issues, ensure your JDK is properly installed and your PATH is set up.

## Contribution

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

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
