package v3_thinking_in_objects;

import java.util.ArrayList;

/**
 * Represents a Bank that manages multiple customers and provides banking services.
 * This class demonstrates Single Responsibility Principle by handling:
 * - Customer management (adding, removing, finding customers)
 * - Inter-customer operations (transfers between accounts)
 * - Bank-wide reporting and statistics
 * 
 * This is the "manager" class that coordinates operations between customers.
 * 
 * @author Evis Plaku
 * @version 3.0
 */
public class Bank {
    
    /** Name of the bank */
    private String bankName;
    
    /** List of customers registered with this bank */
    private ArrayList<Customer> customers;
    
    /**
     * Constructs a new Bank with the specified name.
     * 
     * @param bankName the name of the bank
     */
    public Bank(String bankName) {
        if (bankName == null || bankName.trim().isEmpty()) {
        	System.out.println("Bank name cannot be null or empty");
        }
        this.bankName = bankName.trim();
        this.customers = new ArrayList<>();
    }
    
    /**
     * Returns the bank name.
     * 
     * @return the bank name
     */
    public String getBankName() {
        return bankName;
    }
    
    // ==================== CUSTOMER MANAGEMENT ====================
    
    /**
     * Adds a new customer to the bank.
     * 
     * @param customer the customer to add
     * @return true if customer was added successfully
     * @throws IllegalArgumentException if customer is null or already exists
     */
    public boolean addCustomer(Customer customer) {
        if (customer == null) {
        	System.out.println("Cannot add null customer");
        	return false;
        }
        
        // Check if customer already exists
        if (findCustomer(customer.getId()) != null) {
            System.out.println("Customer with ID " + customer.getId() + " already exists.");
            return false;
        }
        
        customers.add(customer);
        System.out.println("Customer " + customer.getName() + " (ID: " + customer.getId() + 
                         ") added to " + bankName);
        return true;
    }
    
    /**
     * Removes a customer from the bank.
     * 
     * @param customerId the ID of the customer to remove
     * @return true if customer was found and removed
     */
    public boolean removeCustomer(int customerId) {
        Customer customer = findCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found.");
            return false;
        }
        
        customers.remove(customer);
        System.out.println("Customer " + customer.getName() + " removed from " + bankName);
        return true;
    }
    
    /**
     * Finds a customer by their ID.
     * 
     * @param customerId the customer ID to search for
     * @return the Customer if found, null otherwise
     */
    public Customer findCustomer(int customerId) {
        for (Customer customer : customers) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }
    
    /**
     * Returns the total number of customers in the bank.
     * 
     * @return the number of customers
     */
    public int getCustomerCount() {
        return customers.size();
    }
    
    // ==================== ACCOUNT OPERATIONS ====================
    
    /**
     * Deposits money into a specific customer's account.
     * 
     * @param customerId the customer ID
     * @param accountId the account ID
     * @param amount the amount to deposit
     * @return true if deposit was successful
     */
    public boolean deposit(int customerId, int accountId, double amount) {
        Customer customer = findCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found.");
            return false;
        }
        
        Account account = customer.findAccount(accountId);
        if (account == null) {
            System.out.println("Account #" + accountId + " not found for customer " + customer.getName());
            return false;
        }
        
        return account.deposit(amount);
    }
    
    /**
     * Withdraws money from a specific customer's account.
     * 
     * @param customerId the customer ID
     * @param accountId the account ID
     * @param amount the amount to withdraw
     * @return true if withdrawal was successful
     */
    public boolean withdraw(int customerId, int accountId, double amount) {
        Customer customer = findCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found.");
            return false;
        }
        
        Account account = customer.findAccount(accountId);
        if (account == null) {
            System.out.println("Account #" + accountId + " not found for customer " + customer.getName());
            return false;
        }
        
        return account.withdraw(amount);
    }
    
    /**
     * Transfers money between two accounts (can be different customers).
     * This demonstrates why Bank class is needed - it can coordinate
     * operations between multiple customers.
     * 
     * @param fromCustomerId the source customer ID
     * @param fromAccountId the source account ID
     * @param toCustomerId the destination customer ID
     * @param toAccountId the destination account ID
     * @param amount the amount to transfer
     * @return true if transfer was successful
     */
    public boolean transfer(int fromCustomerId, int fromAccountId, 
                           int toCustomerId, int toAccountId, double amount) {
        
        // Find source customer and account
        Customer fromCustomer = findCustomer(fromCustomerId);
        if (fromCustomer == null) {
            System.out.println("Source customer with ID " + fromCustomerId + " not found.");
            return false;
        }
        
        Account fromAccount = fromCustomer.findAccount(fromAccountId);
        if (fromAccount == null) {
            System.out.println("Source account #" + fromAccountId + " not found.");
            return false;
        }
        
        // Find destination customer and account
        Customer toCustomer = findCustomer(toCustomerId);
        if (toCustomer == null) {
            System.out.println("Destination customer with ID " + toCustomerId + " not found.");
            return false;
        }
        
        Account toAccount = toCustomer.findAccount(toAccountId);
        if (toAccount == null) {
            System.out.println("Destination account #" + toAccountId + " not found.");
            return false;
        }
        
        // Validate transfer
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return false;
        }
        
        if (fromAccount.isFrozen() || toAccount.isFrozen()) {
            System.out.println("Cannot transfer: one or both accounts are frozen.");
            return false;
        }
        
        if (fromAccount.getBalance() < amount) {
            System.out.println("Insufficient funds in source account.");
            return false;
        }
        
        // Perform the transfer (atomic operation)
        if (fromAccount.transferOut(amount, toAccountId) && 
            toAccount.transferIn(amount, fromAccountId)) {
            System.out.println("Transfer successful: €" + amount + " from Account #" + 
                             fromAccountId + " to Account #" + toAccountId);
            return true;
        }
        
        return false;
    }
    
    // ==================== REPORTING ====================
    
    /**
     * Displays all customers in the bank with their account information.
     */
    public void displayAllCustomers() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("=== " + bankName + " - Customer Directory ===");
        System.out.println("=".repeat(70));
        
        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
        } else {
            for (Customer customer : customers) {
                System.out.println("\n" + customer);
                customer.displayAllAccounts();
            }
        }
        System.out.println("\n" + "=".repeat(70));
    }
    
    /**
     * Generates a summary report of the bank's statistics.
     */
    public void generateBankReport() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("=== " + bankName + " - Bank Report ===");
        System.out.println("=".repeat(70));
        
        int totalCustomers = customers.size();
        int totalAccounts = 0;
        double totalBankBalance = 0.0;
        
        for (Customer customer : customers) {
            totalAccounts += customer.getAccountCount();
            totalBankBalance += customer.getTotalBalance();
        }
        
        System.out.println("Total Customers: " + totalCustomers);
        System.out.println("Total Accounts: " + totalAccounts);
        System.out.println("Total Bank Assets: €" + String.format("%.2f", totalBankBalance));
        System.out.println("Average Balance per Customer: €" + 
                         String.format("%.2f", totalCustomers > 0 ? totalBankBalance / totalCustomers : 0));
        System.out.println("Average Accounts per Customer: " + 
                         String.format("%.2f", totalCustomers > 0 ? (double) totalAccounts / totalCustomers : 0));
        
        System.out.println("=".repeat(70));
    }
    
    /**
     * Returns a string representation of the bank.
     * 
     * @return formatted bank information
     */
    @Override
    public String toString() {
        return "Bank{" +
                "name='" + bankName + '\'' +
                ", customers=" + customers.size() +
                '}';
    }
}
