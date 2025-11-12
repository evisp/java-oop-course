package v4_inheritance_polymorphism;

import java.util.ArrayList;

/**
 * Represents a Bank that manages multiple customers and provides banking services.
 * Week 4: Enhanced to support polymorphic account types (SavingsAccount, CheckingAccount).
 * 
 * This class demonstrates:
 * - Single Responsibility Principle by handling customer and account management
 * - Polymorphism by working with different Account subclasses
 * - Account-type-specific operations (interest, fees)
 * 
 * @author Evis Plaku
 * @version 4.0
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
            throw new IllegalArgumentException("Bank name cannot be null or empty");
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
            throw new IllegalArgumentException("Cannot add null customer");
        }
        
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
    
    // ==================== ACCOUNT CREATION (NEW FOR WEEK 4) ====================
    
    /**
     * Opens a new SavingsAccount for a customer.
     * Demonstrates polymorphism - creates a SavingsAccount but stores as Account.
     * 
     * @param customerId the customer ID
     * @param initialBalance the initial balance
     * @param interestRate the interest rate (e.g., 0.03 for 3%)
     * @return the created SavingsAccount, or null if customer not found
     */
    public SavingsAccount openSavingsAccount(int customerId, double initialBalance, double interestRate) {
        Customer customer = findCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found.");
            return null;
        }
        
        SavingsAccount account = new SavingsAccount(initialBalance, interestRate);
        customer.addAccount(account); // Account reference holds SavingsAccount - polymorphism!
        return account;
    }
    
    /**
     * Opens a new CheckingAccount for a customer.
     * Demonstrates polymorphism - creates a CheckingAccount but stores as Account.
     * 
     * @param customerId the customer ID
     * @param initialBalance the initial balance
     * @param overdraftLimit the overdraft limit
     * @return the created CheckingAccount, or null if customer not found
     */
    public CheckingAccount openCheckingAccount(int customerId, double initialBalance, double overdraftLimit) {
        Customer customer = findCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found.");
            return null;
        }
        
        CheckingAccount account = new CheckingAccount(initialBalance, overdraftLimit);
        customer.addAccount(account); // Account reference holds CheckingAccount - polymorphism!
        return account;
    }
    
    // ==================== ACCOUNT OPERATIONS ====================
    
    /**
     * Deposits money into a specific customer's account.
     * Works polymorphically with any Account subtype.
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
        
        // Polymorphic call - deposit() may behave differently in subclasses
        return account.deposit(amount);
    }
    
    /**
     * Withdraws money from a specific customer's account.
     * Works polymorphically - SavingsAccount checks minimum, CheckingAccount allows overdraft.
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
        
        // Polymorphic call - withdraw() behaves differently for each account type!
        return account.withdraw(amount);
    }
    
    /**
     * Transfers money between two accounts (can be different customers).
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
        
        if (fromAccount.transferOut(amount, toAccountId) && 
            toAccount.transferIn(amount, fromAccountId)) {
            System.out.println("Transfer successful: €" + amount + " from Account #" + 
                             fromAccountId + " to Account #" + toAccountId);
            return true;
        }
        
        return false;
    }
    
    // ==================== POLYMORPHIC OPERATIONS (NEW FOR WEEK 4) ====================
    
    /**
     * Applies interest to all SavingsAccounts in the bank.
     * Demonstrates polymorphism with instanceof and type casting.
     * 
     * @return the total interest paid out
     */
    public double applyInterestToAllSavings() {
        System.out.println("\n--- Applying Monthly Interest to All Savings Accounts ---");
        double totalInterest = 0.0;
        int savingsCount = 0;
        
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                // Use instanceof to identify SavingsAccount
                if (account instanceof SavingsAccount) {
                    SavingsAccount savings = (SavingsAccount) account; // Downcast
                    double interest = savings.addInterest();
                    totalInterest += interest;
                    savingsCount++;
                }
            }
        }
        
        System.out.println("Applied interest to " + savingsCount + " savings accounts.");
        System.out.println("Total interest paid: €" + String.format("%.2f", totalInterest));
        return totalInterest;
    }
    
    /**
     * Applies monthly fees to all CheckingAccounts in the bank.
     * Demonstrates polymorphism with instanceof and type casting.
     * 
     * @return the total fees collected
     */
    public double applyMonthlyFeesToAllChecking() {
        System.out.println("\n--- Applying Monthly Fees to All Checking Accounts ---");
        double totalFees = 0.0;
        int checkingCount = 0;
        
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                // Use instanceof to identify CheckingAccount
                if (account instanceof CheckingAccount) {
                    CheckingAccount checking = (CheckingAccount) account; // Downcast
                    totalFees += checking.getMonthlyFee();
                    checking.applyMonthlyFee();
                    checkingCount++;
                }
            }
        }
        
        System.out.println("Applied fees to " + checkingCount + " checking accounts.");
        System.out.println("Total fees collected: €" + String.format("%.2f", totalFees));
        return totalFees;
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
     * Generates a comprehensive bank report with account type breakdown.
     * Enhanced for Week 4 to show different account types.
     */
    public void generateBankReport() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("=== " + bankName + " - Bank Report ===");
        System.out.println("=".repeat(70));
        
        int totalCustomers = customers.size();
        int totalAccounts = 0;
        int savingsAccounts = 0;
        int checkingAccounts = 0;
        int standardAccounts = 0;
        double totalBankBalance = 0.0;
        double totalSavingsBalance = 0.0;
        double totalCheckingBalance = 0.0;
        
        // Polymorphic iteration - accounts can be any subtype
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                totalAccounts++;
                totalBankBalance += account.getBalance();
                
                // Type checking with instanceof
                if (account instanceof SavingsAccount) {
                    savingsAccounts++;
                    totalSavingsBalance += account.getBalance();
                } else if (account instanceof CheckingAccount) {
                    checkingAccounts++;
                    totalCheckingBalance += account.getBalance();
                } else {
                    standardAccounts++;
                }
            }
        }
        
        System.out.println("Total Customers: " + totalCustomers);
        System.out.println("\nAccount Breakdown:");
        System.out.println("  - Total Accounts: " + totalAccounts);
        System.out.println("  - Savings Accounts: " + savingsAccounts + 
                         " (Balance: €" + String.format("%.2f", totalSavingsBalance) + ")");
        System.out.println("  - Checking Accounts: " + checkingAccounts + 
                         " (Balance: €" + String.format("%.2f", totalCheckingBalance) + ")");
        System.out.println("  - Standard Accounts: " + standardAccounts);
        
        System.out.println("\nFinancial Summary:");
        System.out.println("  - Total Bank Assets: €" + String.format("%.2f", totalBankBalance));
        System.out.println("  - Average Balance per Customer: €" + 
                         String.format("%.2f", totalCustomers > 0 ? totalBankBalance / totalCustomers : 0));
        System.out.println("  - Average Accounts per Customer: " + 
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
