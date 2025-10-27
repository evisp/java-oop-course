package v2_basic_collections;

/**
 * Represents a bank account with basic transaction capabilities.
 * This class demonstrates encapsulation and method implementation.
 * 
 * @author Evis Plaku
 * @version 2.0
 */
public class Account {
    
    /** Static counter for generating unique account IDs */
    private static int accountCounter = 1000;
    
    /** Unique identifier for this account */
    private int accountId;
    
    /** Current balance in the account */
    private double balance;
    
    /** Status of the account (active or frozen) */
    private boolean isFrozen;
    
    /**
     * Constructs a new Account with zero balance and active status.
     * Automatically assigns a unique account ID.
     */
    public Account() {
        this.accountId = ++accountCounter;
        this.balance   = 0.0;
        this.isFrozen  = false;
    }
    
    /**
     * Constructs a new Account with the specified initial balance and active status.
     * Automatically assigns a unique account ID.
     * 
     * @param initialBalance the starting balance for the account (must be non-negative)
     * @throws IllegalArgumentException if initial balance is negative
     */
    public Account(double initialBalance) {
        if (initialBalance < 0) {
            System.out.println("Initial balance cannot be negative. Provided: €" + initialBalance);
        }
        this.accountId = ++accountCounter;
        this.balance   = initialBalance;
        this.isFrozen  = false;
    }
    
    /**
     * Returns the unique account ID.
     * 
     * @return the account ID
     */
    public int getAccountId() {
        return accountId;
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
     * Checks if the account is frozen.
     * 
     * @return true if account is frozen, false otherwise
     */
    public boolean isFrozen() {
        return isFrozen;
    }
    
    /**
     * Freezes the account, preventing withdrawals and deposits.
     */
    public void freezeAccount() {
        this.isFrozen = true;
        System.out.println("Account #" + accountId + " has been frozen.");
    }
    
    /**
     * Unfreezes the account, allowing normal operations.
     */
    public void unfreezeAccount() {
        this.isFrozen = false;
        System.out.println("Account #" + accountId + " has been unfrozen.");
    }
    
    /**
     * Deposits the specified amount into the account.
     * 
     * @param amount the amount to deposit (must be positive)
     * @return true if deposit was successful, false otherwise
     */
    public boolean deposit(double amount) {
        if (isFrozen) {
            System.out.println("Cannot deposit. Account #" + accountId + " is frozen.");
            return false;
        }
        
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: €" + amount + " to Account #" + accountId);
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
        if (isFrozen) {
            System.out.println("Cannot withdraw. Account #" + accountId + " is frozen.");
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        
        if (amount > balance) {
            System.out.println("Insufficient funds. Current balance: €" + balance);
            return false;
        }
        
        balance -= amount;
        System.out.println("Withdrawn: €" + amount + " from Account #" + accountId);
        return true;
    }
    
    /**
     * Returns a string representation of the account.
     * 
     * @return a formatted string showing the account details
     */
    @Override
    public String toString() {
        String status = isFrozen ? " [FROZEN]" : " [ACTIVE]";
        return "Account{" +
                "id=" + accountId +
                ", balance=€" + String.format("%.2f", balance) +
                ", status=" + status +
                '}';
    }
}
