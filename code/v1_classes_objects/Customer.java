package week2_basic_classes;

/**
 * Represents a bank customer with basic personal information.
 * This class demonstrates encapsulation 
 * with private fields and public accessor methods.
 * 
 * @author Evis Plaku
 * @version 1.0
 */
public class Customer {
    
    /** Unique identifier for the customer */
    private int id;
    
    /** Customer's full name */
    private String name;
    
    /** Customer's age in years */
    private int age;
    
    /** Customer's residential address */
    private String address;
    
    /**
     * Constructs a new Customer with the specified details.
     * 
     * @param id the unique customer identifier
     * @param name the customer's full name
     * @param age the customer's age
     * @param address the customer's address
     */
    public Customer(int id, String name, int age, String address) {
        this.id 	 = id;
        this.name 	 = name;
        this.age 	 = age;
        this.address = address;
    }
    
    /**
     * Returns the customer's ID.
     * 
     * @return the customer ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Sets the customer's ID.
     * 
     * @param id the new customer ID
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Returns the customer's name.
     * 
     * @return the customer name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the customer's name.
     * 
     * @param name the new customer name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the customer's age.
     * 
     * @return the customer age
     */
    public int getAge() {
        return age;
    }
    
    /**
     * Sets the customer's age.
     * 
     * @param age the new customer age
     */
    public void setAge(int age) {
        this.age = age;
    }
    
    /**
     * Returns the customer's address.
     * 
     * @return the customer address
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * Sets the customer's address.
     * 
     * @param address the new customer address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * Returns a string representation of the customer.
     * 
     * @return a formatted string containing customer details
     */
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}

