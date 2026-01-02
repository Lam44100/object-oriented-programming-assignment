package users;

public abstract class Staff extends Person {

    private double salary;

    public Staff(int id, String name, String password, String contactInfo, double salary) {
        super(id, name, password, contactInfo);
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }
}
