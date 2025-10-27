package v2_basic_collections;

import java.util.ArrayList;

/**
 * Represents a bank customer with basic personal information and multiple accounts.
 * This class demonstrates encapsulation, composition, and ArrayList usage.
 * 
 * @author Evis Plaku
 * @version 2.0
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
    
    /** List of accounts owned by this customer */
    private ArrayList<Account> accounts;
    
    /**
     * Constructs a new Customer with the specified details.
     * Initializes an empty list of accounts.
     * 
     * @param id the unique customer identifier
     * @param name the customer's full name
     * @param age the customer's age
     * @param address the customer's address
     */
    public Customer(int id, String name, int age, String address) {
        this.id       = id;
        this.name     = name;
        this.age      = age;
        this.address  = address;
        this.accounts = new ArrayList<>();
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
    
    // ==================== ACCOUNT MANAGEMENT METHODS ====================
    
    /**
     * Adds a new account to this customer's account list.
     * 
     * @param account the account to add
     * @return true if account was successfully added
     */
    public boolean addAccount(Account account) {
        if (account == null) {
            System.out.println("Cannot add null account.");
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
    
    // ==================== FINANCIAL OPERATION METHODS ====================
    
    /**
     * Deposits money into a specific account.
     * 
     * @param accountId the ID of the account to deposit into
     * @param amount the amount to deposit
     * @return true if deposit was successful, false otherwise
     */
    public boolean deposit(int accountId, double amount) {
        Account account = findAccount(accountId);
        if (account == null) {
            System.out.println("Account #" + accountId + " not found for customer " + name);
            return false;
        }
        return account.deposit(amount);
    }
    
    /**
     * Withdraws money from a specific account.
     * 
     * @param accountId the ID of the account to withdraw from
     * @param amount the amount to withdraw
     * @return true if withdrawal was successful, false otherwise
     */
    public boolean withdraw(int accountId, double amount) {
        Account account = findAccount(accountId);
        if (account == null) {
            System.out.println("Account #" + accountId + " not found for customer " + name);
            return false;
        }
        return account.withdraw(amount);
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
        System.out.println("\n--- Accounts for " + name + " ---");
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
