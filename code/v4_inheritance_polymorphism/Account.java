package v4_inheritance_polymorphism;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a bank account with transaction history and enhanced functionality.
 * This version serves as the parent class for specialized account types.
 * Demonstrates inheritance, with methods that can be overridden by subclasses.
 * 
 * @author Evis Plaku
 * @version 4.0
 */
public class Account {
    
    /** Static counter for generating unique account IDs */
    private static int accountCounter = 1000;
    
    /** Unique identifier for this account (immutable) */
    private final int accountId;
    
    /** Current balance in the account - protected for subclass access */
    protected double balance;
    
    /** Status of the account (active or frozen) */
    private boolean isFrozen;
    
    /** Transaction history for this account - protected for subclass access */
    protected ArrayList<Transaction> transactionHistory;
    
    /**
     * Constructs a new Account with zero balance and active status.
     * Automatically assigns a unique account ID.
     */
    public Account() {
        this.accountId = ++accountCounter;
        this.balance = 0.0;
        this.isFrozen = false;
        this.transactionHistory = new ArrayList<>();
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
        this.balance = initialBalance;
        this.isFrozen = false;
        this.transactionHistory = new ArrayList<>();
        
        // Record the initial balance as a transaction
        if (initialBalance > 0) {
            Transaction initialDeposit = new Transaction(
                Transaction.TransactionType.DEPOSIT,
                initialBalance,
                accountId,
                balance,
                "Initial deposit"
            );
            transactionHistory.add(initialDeposit);
        }
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
     * Records transaction in history.
     * Can be overridden by subclasses for specialized behavior.
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
            
            // Record transaction
            Transaction transaction = new Transaction(
                Transaction.TransactionType.DEPOSIT,
                amount,
                accountId,
                balance,
                "Deposit"
            );
            transactionHistory.add(transaction);
            
            System.out.println("Deposited: €" + amount + " to Account #" + accountId);
            return true;
        } else {
            System.out.println("Deposit amount must be positive.");
            return false;
        }
    }
    
    /**
     * Withdraws the specified amount from the account.
     * Records transaction in history.
     * Can be overridden by subclasses for specialized behavior (e.g., minimum balance, overdraft).
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
        
        // Record transaction
        Transaction transaction = new Transaction(
            Transaction.TransactionType.WITHDRAW,
            amount,
            accountId,
            balance,
            "Withdrawal"
        );
        transactionHistory.add(transaction);
        
        System.out.println("Withdrawn: €" + amount + " from Account #" + accountId);
        return true;
    }
    
    /**
     * Records a transfer out from this account.
     * Package-private method to be called by Bank class only.
     * 
     * @param amount the amount to transfer
     * @param toAccountId the destination account ID
     * @return true if successful
     */
    boolean transferOut(double amount, int toAccountId) {
        if (isFrozen || amount <= 0 || amount > balance) {
            return false;
        }
        
        balance -= amount;
        Transaction transaction = new Transaction(
            Transaction.TransactionType.TRANSFER_OUT,
            amount,
            accountId,
            toAccountId,
            balance,
            "Transfer to account #" + toAccountId
        );
        transactionHistory.add(transaction);
        return true;
    }
    
    /**
     * Records a transfer into this account.
     * Package-private method to be called by Bank class only.
     * 
     * @param amount the amount to receive
     * @param fromAccountId the source account ID
     * @return true if successful
     */
    boolean transferIn(double amount, int fromAccountId) {
        if (isFrozen || amount <= 0) {
            return false;
        }
        
        balance += amount;
        Transaction transaction = new Transaction(
            Transaction.TransactionType.TRANSFER_IN,
            amount,
            accountId,
            fromAccountId,
            balance,
            "Transfer from account #" + fromAccountId
        );
        transactionHistory.add(transaction);
        return true;
    }
    
    /**
     * Returns an unmodifiable view of the transaction history.
     * Demonstrates defensive copying for encapsulation.
     * 
     * @return unmodifiable list of transactions
     */
    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }
    
    /**
     * Displays the complete transaction history for this account.
     */
    public void displayTransactionHistory() {
        System.out.println("\n--- Transaction History for Account #" + accountId + " ---");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction t : transactionHistory) {
                System.out.println("  " + t);
            }
        }
        System.out.println("Current Balance: €" + String.format("%.2f", balance));
    }
    
    /**
     * Returns the type of account as a string.
     * Can be overridden by subclasses to identify their type.
     * 
     * @return the account type
     */
    public String getAccountType() {
        return "Standard Account";
    }
    
    /**
     * Compares this account to another object for equality.
     * Two accounts are equal if they have the same account ID.
     * 
     * @param obj the object to compare with
     * @return true if accounts have same ID, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account account = (Account) obj;
        return accountId == account.accountId;
    }
    
    /**
     * Returns the hash code for this account.
     * Based on account ID for consistency with equals().
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
    
    /**
     * Returns a string representation of the account.
     * Can be overridden by subclasses to include additional information.
     * 
     * @return a formatted string showing the account details
     */
    @Override
    public String toString() {
        String status = isFrozen ? "[FROZEN]" : "[ACTIVE]";
        return getAccountType() + "{" +
                "id=" + accountId +
                ", balance=€" + String.format("%.2f", balance) +
                ", status=" + status +
                ", transactions=" + transactionHistory.size() +
                '}';
    }
}
