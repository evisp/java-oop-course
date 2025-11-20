package v5_abstract_classes_interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a bank customer with personal information and account management.
 * Week 5: Minimal changes - works seamlessly with abstract Account class.
 * 
 * The power of abstraction: Customer code doesn't change even though
 * Account is now abstract - polymorphism at work!
 * 
 * @author Evis Plaku
 * @version 5.0
 */
public class Customer {
    
    /** Unique identifier for the customer (immutable) */
    private final int id;
    
    /** Customer's full name */
    private String name;
    
    /** Customer's age in years */
    private int age;
    
    /** Customer's residential address */
    private String address;
    
    /** List of accounts - can hold any Account subclass (SavingsAccount, CheckingAccount) */
    private ArrayList<Account> accounts;
    
    /**
     * Constructs a new Customer with the specified details.
     * Initializes an empty list of accounts.
     * Validates all input parameters.
     * 
     * @param id the unique customer identifier
     * @param name the customer's full name
     * @param age the customer's age
     * @param address the customer's address
     * @throws IllegalArgumentException if validation fails
     */
    public Customer(int id, String name, int age, String address) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (age < 18) {
            throw new IllegalArgumentException("Customer must be at least 18 years old. Provided: " + age);
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        
        this.id = id;
        this.name = name.trim();
        this.age = age;
        this.address = address.trim();
        this.accounts = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name.trim();
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        if (age < 18) {
            throw new IllegalArgumentException("Age must be at least 18. Provided: " + age);
        }
        this.age = age;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        this.address = address.trim();
    }
    
    // ==================== ACCOUNT MANAGEMENT ====================
    
    public boolean addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Cannot add null account");
        }
        accounts.add(account);
        System.out.println(account.getAccountTypeName() + " #" + account.getAccountId() + 
                         " added to customer " + name);
        return true;
    }
    
    public boolean removeAccount(Account account) {
        if (accounts.remove(account)) {
            System.out.println("Account #" + account.getAccountId() + " removed from customer " + name);
            return true;
        } else {
            System.out.println("Account not found for customer " + name);
            return false;
        }
    }
    
    public Account findAccount(int accountId) {
        for (Account account : accounts) {
            if (account.getAccountId() == accountId) {
                return account;
            }
        }
        return null;
    }
    
    public int getAccountCount() {
        return accounts.size();
    }
    
    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }
    
    /**
     * Calculates total monthly fees across all accounts.
     * Uses the abstract calculateMonthlyFee() method - polymorphic call!
     * 
     * @return sum of all monthly fees
     */
    public double getTotalMonthlyFees() {
        double totalFees = 0.0;
        for (Account account : accounts) {
            totalFees += account.calculateMonthlyFee(); // Polymorphic call to abstract method
        }
        return totalFees;
    }
    
    public double getTotalBalance() {
        double total = 0.0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }
    
    public void displayAllAccounts() {
        System.out.println("\n--- Accounts for " + name + " (ID: " + id + ") ---");
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (Account account : accounts) {
                System.out.println("  " + account);
            }
            System.out.println("Total Balance: €" + String.format("%.2f", getTotalBalance()));
            System.out.println("Total Monthly Fees: €" + String.format("%.2f", getTotalMonthlyFees()));
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
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
                ", age=" + age +
                ", address='" + address + '\'' +
                ", accounts=" + accounts.size() +
                ", totalBalance=€" + String.format("%.2f", getTotalBalance()) +
                '}';
    }
}
