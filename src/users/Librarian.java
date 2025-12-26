package users;

public class Librarian extends Staff {

    public Librarian(int id, String name, String password, String contactInfo, double salary) {
        super(id, name, password, contactInfo, salary);
    }

    @Override
    public String getRoleType() {
        return "LIBRARIAN";
    }
}
