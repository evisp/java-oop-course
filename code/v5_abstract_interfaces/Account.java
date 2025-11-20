package v5_abstract_classes_interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class representing a bank account.
 * This class cannot be instantiated directly - only concrete subclasses
 * (SavingsAccount, CheckingAccount) can be created.
 * 
 * Key changes from Week 4:
 * - Changed from concrete to abstract class
 * - Added abstract methods that subclasses MUST implement
 * - Implements Auditable interface
 * 
 * Demonstrates:
 * - Abstract classes with both concrete and abstract methods
 * - Interface implementation in abstract class
 * - Protected members for subclass access
 * 
 * @author Evis Plaku
 * @version 5.0
 */
public abstract class Account implements Auditable {
    
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
     * Protected constructor - only subclasses can call it.
     */
    protected Account() {
        this.accountId = ++accountCounter;
        this.balance = 0.0;
        this.isFrozen = false;
        this.transactionHistory = new ArrayList<>();
    }
    
    /**
     * Constructs a new Account with the specified initial balance and active status.
     * Automatically assigns a unique account ID.
     * Protected constructor - only subclasses can call it.
     * 
     * @param initialBalance the starting balance for the account (must be non-negative)
     * @throws IllegalArgumentException if initial balance is negative
     */
    protected Account(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative. Provided: €" + initialBalance);
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
     * This is a concrete method - same implementation for all account types.
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
     * Abstract method - subclasses MUST provide their own implementation.
     * Each account type has different withdrawal rules:
     * - SavingsAccount: enforces minimum balance
     * - CheckingAccount: allows overdraft
     * 
     * @param amount the amount to withdraw
     * @return true if withdrawal was successful, false otherwise
     */
    public abstract boolean withdraw(double amount);
    
    /**
     * Abstract method - subclasses MUST calculate their monthly fee.
     * Different account types have different fee structures.
     * 
     * @return the monthly maintenance fee for this account type
     */
    public abstract double calculateMonthlyFee();
    
    /**
     * Abstract method - subclasses MUST provide their account type name.
     * Used for display and identification purposes.
     * 
     * @return the descriptive name of the account type
     */
    public abstract String getAccountTypeName();
    
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
    
    // ==================== AUDITABLE INTERFACE IMPLEMENTATION ====================
    
    /**
     * Generates an audit report for this account.
     * Implementation of Auditable interface.
     */
    @Override
    public void generateAuditReport() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ACCOUNT AUDIT REPORT                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println("Account Type: " + getAccountTypeName());
        System.out.println("Account ID: " + accountId);
        System.out.println("Current Balance: €" + String.format("%.2f", balance));
        System.out.println("Account Status: " + (isFrozen ? "FROZEN" : "ACTIVE"));
        System.out.println("Total Transactions: " + transactionHistory.size());
        System.out.println("Monthly Fee: €" + String.format("%.2f", calculateMonthlyFee()));
        System.out.println("Last Modified: " + getLastModifiedTime());
        System.out.println("\nTransaction Summary:");
        
        if (transactionHistory.isEmpty()) {
            System.out.println("  No transactions recorded.");
        } else {
            int deposits = 0, withdrawals = 0, transfers = 0;
            double totalDeposited = 0, totalWithdrawn = 0;
            
            for (Transaction t : transactionHistory) {
                switch (t.getType()) {
                    case DEPOSIT:
                        deposits++;
                        totalDeposited += t.getAmount();
                        break;
                    case WITHDRAW:
                        withdrawals++;
                        totalWithdrawn += t.getAmount();
                        break;
                    case TRANSFER_IN:
                    case TRANSFER_OUT:
                        transfers++;
                        break;
                }
            }
            
            System.out.println("  Deposits: " + deposits + " (Total: €" + String.format("%.2f", totalDeposited) + ")");
            System.out.println("  Withdrawals: " + withdrawals + " (Total: €" + String.format("%.2f", totalWithdrawn) + ")");
            System.out.println("  Transfers: " + transfers);
        }
        System.out.println("═══════════════════════════════════════════════════════════════\n");
    }
    
    /**
     * Returns the audit trail (transaction history).
     * Implementation of Auditable interface.
     * 
     * @return list of all transactions
     */
    @Override
    public List<Transaction> getAuditTrail() {
        return Collections.unmodifiableList(transactionHistory);
    }
    
    /**
     * Returns the time of the last transaction.
     * Implementation of Auditable interface.
     * 
     * @return formatted timestamp of last transaction, or "No transactions" if none
     */
    @Override
    public String getLastModifiedTime() {
        if (transactionHistory.isEmpty()) {
            return "No transactions yet";
        }
        Transaction lastTransaction = transactionHistory.get(transactionHistory.size() - 1);
        return lastTransaction.getTimestamp().toString();
    }
    
    /**
     * Returns the total number of transactions.
     * Implementation of Auditable interface.
     * 
     * @return transaction count
     */
    @Override
    public int getTransactionCount() {
        return transactionHistory.size();
    }
    
    // ==================== STANDARD METHODS ====================
    
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
     * Uses abstract method getAccountTypeName() - polymorphic behavior.
     * 
     * @return a formatted string showing the account details
     */
    @Override
    public String toString() {
        String status = isFrozen ? "[FROZEN]" : "[ACTIVE]";
        return getAccountTypeName() + "{" +
                "id=" + accountId +
                ", balance=€" + String.format("%.2f", balance) +
                ", status=" + status +
                ", transactions=" + transactionHistory.size() +
                '}';
    }
}
