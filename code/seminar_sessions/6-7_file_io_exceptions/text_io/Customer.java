import java.io.Serializable;

public class Customer implements Serializable {
	
	private static final long serialVersionUID = 1L; 
	
    private int id;
    private String name;
    private String email;
    private int age;
    private String city;
    
    // Constructor
    public Customer(int id, String name, String email, int age, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.city = city;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getAge() { return age; }
    public String getCity() { return city; }
    
    @Override
    public String toString() {
        return String.format("#%d: %s (%d) | %s | %s", id, name, age, email, city);
    }
}
