package v5_abstract_classes_interfaces;

import java.util.ArrayList;

/**
 * Represents a Bank that manages multiple customers and provides banking services.
 * Week 5: Enhanced with Auditable interface support.
 * 
 * Demonstrates:
 * - Working with abstract Account class
 * - Using Auditable interface for audit operations
 * - Polymorphic operations on abstract types
 * 
 * @author Evis Plaku
 * @version 5.0
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
    
    public String getBankName() {
        return bankName;
    }
    
    // ==================== CUSTOMER MANAGEMENT ====================
    
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
    
    public Customer findCustomer(int customerId) {
        for (Customer customer : customers) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }
    
    public int getCustomerCount() {
        return customers.size();
    }
    
    // ==================== ACCOUNT CREATION ====================
    
    public SavingsAccount openSavingsAccount(int customerId, double initialBalance, double interestRate) {
        Customer customer = findCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found.");
            return null;
        }
        
        SavingsAccount account = new SavingsAccount(initialBalance, interestRate);
        customer.addAccount(account);
        return account;
    }
    
    public CheckingAccount openCheckingAccount(int customerId, double initialBalance, double overdraftLimit) {
        Customer customer = findCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found.");
            return null;
        }
        
        CheckingAccount account = new CheckingAccount(initialBalance, overdraftLimit);
        customer.addAccount(account);
        return account;
    }
    
    // ==================== ACCOUNT OPERATIONS ====================
    
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
        
        // Polymorphic call - withdraw() behavior depends on actual account type
        return account.withdraw(amount);
    }
    
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
    
    // ==================== POLYMORPHIC OPERATIONS ====================
    
    public double applyInterestToAllSavings() {
        System.out.println("\n--- Applying Monthly Interest to All Savings Accounts ---");
        double totalInterest = 0.0;
        int savingsCount = 0;
        
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                if (account instanceof SavingsAccount) {
                    SavingsAccount savings = (SavingsAccount) account;
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
    
    public double applyMonthlyFeesToAllChecking() {
        System.out.println("\n--- Applying Monthly Fees to All Checking Accounts ---");
        double totalFees = 0.0;
        int checkingCount = 0;
        
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                if (account instanceof CheckingAccount) {
                    CheckingAccount checking = (CheckingAccount) account;
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
    
    // ==================== AUDITABLE INTERFACE OPERATIONS (NEW FOR WEEK 5) ====================
    
    /**
     * Generates audit reports for all accounts in the bank.
     * Demonstrates using Auditable interface polymorphically.
     * All accounts implement Auditable through abstract Account class.
     */
    public void generateAllAuditReports() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║          BANK-WIDE AUDIT REPORT GENERATION                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        
        int totalAccounts = 0;
        for (Customer customer : customers) {
            System.out.println("\n[Customer: " + customer.getName() + " (ID: " + customer.getId() + ")]");
            for (Account account : customer.getAccounts()) {
                // Using Auditable interface method
                account.generateAuditReport();
                totalAccounts++;
            }
        }
        
        System.out.println("Total audit reports generated: " + totalAccounts);
    }
    
    /**
     * Gets accounts with high transaction activity for audit purposes.
     * Demonstrates using Auditable interface methods.
     * 
     * @param threshold minimum number of transactions
     * @return list of accounts exceeding threshold
     */
    public ArrayList<Account> getHighActivityAccounts(int threshold) {
        ArrayList<Account> highActivity = new ArrayList<>();
        
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                // Using Auditable interface method
                if (account.getTransactionCount() > threshold) {
                    highActivity.add(account);
                }
            }
        }
        
        return highActivity;
    }
    
    // ==================== REPORTING ====================
    
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
    
    public void generateBankReport() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("=== " + bankName + " - Bank Report ===");
        System.out.println("=".repeat(70));
        
        int totalCustomers = customers.size();
        int totalAccounts = 0;
        int savingsAccounts = 0;
        int checkingAccounts = 0;
        double totalBankBalance = 0.0;
        double totalSavingsBalance = 0.0;
        double totalCheckingBalance = 0.0;
        double totalMonthlyFees = 0.0;
        
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                totalAccounts++;
                totalBankBalance += account.getBalance();
                // Using abstract method polymorphically
                totalMonthlyFees += account.calculateMonthlyFee();
                
                if (account instanceof SavingsAccount) {
                    savingsAccounts++;
                    totalSavingsBalance += account.getBalance();
                } else if (account instanceof CheckingAccount) {
                    checkingAccounts++;
                    totalCheckingBalance += account.getBalance();
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
        
        System.out.println("\nFinancial Summary:");
        System.out.println("  - Total Bank Assets: €" + String.format("%.2f", totalBankBalance));
        System.out.println("  - Total Monthly Fee Revenue: €" + String.format("%.2f", totalMonthlyFees));
        System.out.println("  - Average Balance per Customer: €" + 
                         String.format("%.2f", totalCustomers > 0 ? totalBankBalance / totalCustomers : 0));
        
        System.out.println("=".repeat(70));
    }
    
    @Override
    public String toString() {
        return "Bank{" +
                "name='" + bankName + '\'' +
                ", customers=" + customers.size() +
                '}';
    }
}
