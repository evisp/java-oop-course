package v4_inheritance_polymorphism;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a bank customer with personal information and account management.
 * This refactored version focuses on Single Responsibility Principle:
 * - Manages personal customer information
 * - Manages customer's own accounts
 * - Does NOT handle inter-customer operations (moved to Bank)
 * 
 * @author Evis Plaku
 * @version 3.0
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
    
    /** List of accounts owned by this customer */
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
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be null or empty");
        }
        if (age < 18) {
        	System.out.println("Customer must be at least 18 years old. Provided: " + age);
        }
        if (address == null || address.trim().isEmpty()) {
        	System.out.println("Address cannot be null or empty");
        }
        
        this.id = id;
        this.name = name.trim();
        this.age = age;
        this.address = address.trim();
        this.accounts = new ArrayList<>();
    }
    
    /**
     * Returns the customer's ID.
     * ID is immutable and cannot be changed after creation.
     * 
     * @return the customer ID
     */
    public int getId() {
        return id;
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
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
        	System.out.println("Name cannot be null or empty");
        	return;
        }
        this.name = name.trim();
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
     * @throws IllegalArgumentException if age is less than 18
     */
    public void setAge(int age) {
        if (age < 18) {
        	System.out.println("Age must be at least 18. Provided: " + age);
        	return;
        }
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
     * @throws IllegalArgumentException if address is null or empty
     */
    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
        	System.out.println("Address cannot be null or empty");
        	return;
        }
        this.address = address.trim();
    }
    
    // ==================== ACCOUNT MANAGEMENT METHODS ====================
    
    /**
     * Adds a new account to this customer's account list.
     * 
     * @param account the account to add
     * @return true if account was successfully added
     * @throws IllegalArgumentException if account is null
     */
    public boolean addAccount(Account account) {
        if (account == null) {
        	System.out.println("Cannot add null account");
        	return false;
        }
        accounts.add(account);
        System.out.println("Account #" + account.getAccountId() + " added to customer " + name);
        return true;
    }
    
    /**
     * Removes an account from this customer's account list.
     * 
     * @param account the account to remove
     * @return true if account was found and removed, false otherwise
     */
    public boolean removeAccount(Account account) {
        if (accounts.remove(account)) {
            System.out.println("Account #" + account.getAccountId() + " removed from customer " + name);
            return true;
        } else {
            System.out.println("Account not found for customer " + name);
            return false;
        }
    }
    
    /**
     * Searches for an account by its account ID.
     * 
     * @param accountId the ID of the account to find
     * @return the Account if found, null otherwise
     */
    public Account findAccount(int accountId) {
        for (Account account : accounts) {
            if (account.getAccountId() == accountId) {
                return account;
            }
        }
        return null;
    }
    
    /**
     * Returns the total number of accounts owned by this customer.
     * 
     * @return the number of accounts
     */
    public int getAccountCount() {
        return accounts.size();
    }
    
    /**
     * Returns an unmodifiable view of this customer's accounts.
     * Demonstrates defensive copying for encapsulation.
     * 
     * @return unmodifiable list of accounts
     */
    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }
    
    /**
     * Calculates the total balance across all accounts.
     * 
     * @return the sum of all account balances
     */
    public double getTotalBalance() {
        double total = 0.0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }
    
    /**
     * Displays all accounts owned by this customer.
     */
    public void displayAllAccounts() {
        System.out.println("\n--- Accounts for " + name + " (ID: " + id + ") ---");
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (Account account : accounts) {
                System.out.println("  " + account);
            }
            System.out.println("Total Balance: €" + String.format("%.2f", getTotalBalance()));
        }
    }
    
    /**
     * Compares this customer to another object for equality.
     * Two customers are equal if they have the same customer ID.
     * 
     * @param obj the object to compare with
     * @return true if customers have same ID, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return id == customer.id;
    }
    
    /**
     * Returns the hash code for this customer.
     * Based on customer ID for consistency with equals().
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
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
                ", accounts=" + accounts.size() +
                ", totalBalance=€" + String.format("%.2f", getTotalBalance()) +
                '}';
    }
}
