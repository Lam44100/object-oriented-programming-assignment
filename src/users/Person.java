package users;

public abstract class Person {
    private int id;
    private String name;
    private String password;
    private String contactInfo;

    public Person(int id, String name, String password, String contactInfo) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.contactInfo = contactInfo;
    }

    public boolean login(String pw) {
        return this.password.equals(pw);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Abstract method to force subclasses to identify their role type
    // This helps in the UI to display "LIBRARIAN" or "MEMBER"
    public abstract String getRoleType();
}