package users;
public abstract class Person {
    private int id;
    private String name;
    private String password; // In real app, store Hash
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
}