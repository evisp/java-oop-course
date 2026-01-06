import java.util.Objects;

public class Customer implements Comparable<Customer> {

    private final int id;
    private final String name;
    private final String email;
    private final int age;
    private final String city;

    public Customer(int id, String name, String email, int age, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    /**
     * Natural ordering: by id (ascending).
     * Could also choose by name or age if that fits your lecture.
     */
    @Override
    public int compareTo(Customer other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        // Identity by id, assuming unique IDs
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                '}';
    }
}
