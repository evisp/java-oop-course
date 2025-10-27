package week2_basic_classes;

/**
 * Represents a bank account with basic transaction capabilities.
 * This class demonstrates encapsulation and method implementation.
 * 
 * @author Evis Plaku
 * @version 1.0
 */
public class Account {
    
    /** Current balance in the account */
    private double balance;
    
    /**
     * Constructs a new Account with zero balance.
     */
    public Account() {
        this.balance = 0.0;
    }
    
    /**
     * Constructs a new Account with the specified initial balance.
     * 
     * @param initialBalance the starting balance for the account
     */
    public Account(double initialBalance) {
        this.balance = initialBalance;
    }
    
    /**
     * Deposits the specified amount into the account.
     * 
     * @param amount the amount to deposit (must be positive)
     * @return true if deposit was successful, false otherwise
     */
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
            return true;
        } else {
            System.out.println("Deposit amount must be positive.");
            return false;
        }
    }
    
    /**
     * Withdraws the specified amount from the account.
     * 
     * @param amount the amount to withdraw (must be positive and not exceed balance)
     * @return true if withdrawal was successful, false otherwise
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        
        if (amount > balance) {
            System.out.println("Insufficient funds. Current balance: $" + balance);
            return false;
        }
        
        balance -= amount;
        System.out.println("Withdrawn: $" + amount);
        return true;
    }
    
    /**
     * Returns the current account balance.
     * 
     * @return the current balance
     */
    public double getBalance() {
        return balance;
    }
    
    /**
     * Returns a string representation of the account.
     * 
     * @return a formatted string showing the account balance
     */
    @Override
    public String toString() {
        return "Account{balance=€" + String.format("%.2f", balance) + "}";
    }
}
