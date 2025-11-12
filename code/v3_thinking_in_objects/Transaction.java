package v3_thinking_in_objects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a banking transaction with complete audit information.
 * This class demonstrates immutability, enums, and value objects.
 * Transactions are immutable once created for audit integrity.
 * 
 * @author Evis Plaku
 * @version 3.0
 */
public class Transaction {
    
    /** Enum to define types of transactions */
    public enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT
    }
    
    /** Static counter for generating unique transaction IDs */
    private static int transactionCounter = 10000;
    
    /** Unique identifier for this transaction */
    private final int transactionId;
    
    /** Type of transaction */
    private final TransactionType type;
    
    /** Amount involved in the transaction */
    private final double amount;
    
    /** Account ID where transaction occurred */
    private final int accountId;
    
    /** For transfers, the other account involved */
    private final int relatedAccountId;
    
    /** Timestamp when transaction was created */
    private final LocalDateTime timestamp;
    
    /** Balance after this transaction was completed */
    private final double balanceAfter;
    
    /** Description or notes about the transaction */
    private final String description;
    
    /** Formatter for displaying timestamps */
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Constructs a transaction (for deposits and withdrawals).
     * 
     * @param type the type of transaction
     * @param amount the transaction amount
     * @param accountId the account ID
     * @param balanceAfter the balance after transaction
     * @param description description of the transaction
     */
    public Transaction(TransactionType type, double amount, int accountId, 
                      double balanceAfter, String description) {
        this.transactionId = ++transactionCounter;
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
        this.relatedAccountId = -1; // No related account for deposit/withdraw
        this.timestamp = LocalDateTime.now();
        this.balanceAfter = balanceAfter;
        this.description = description;
    }
    
    /**
     * Constructs a transfer transaction.
     * 
     * @param type the type of transaction (TRANSFER_IN or TRANSFER_OUT)
     * @param amount the transaction amount
     * @param accountId the account ID
     * @param relatedAccountId the other account in the transfer
     * @param balanceAfter the balance after transaction
     * @param description description of the transaction
     */
    public Transaction(TransactionType type, double amount, int accountId,
                      int relatedAccountId, double balanceAfter, String description) {
        this.transactionId = ++transactionCounter;
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
        this.relatedAccountId = relatedAccountId;
        this.timestamp = LocalDateTime.now();
        this.balanceAfter = balanceAfter;
        this.description = description;
    }
    
    /**
     * Returns the transaction ID.
     * 
     * @return the transaction ID
     */
    public int getTransactionId() {
        return transactionId;
    }
    
    /**
     * Returns the transaction type.
     * 
     * @return the transaction type
     */
    public TransactionType getType() {
        return type;
    }
    
    /**
     * Returns the transaction amount.
     * 
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Returns the account ID.
     * 
     * @return the account ID
     */
    public int getAccountId() {
        return accountId;
    }
    
    /**
     * Returns the related account ID (for transfers).
     * 
     * @return the related account ID, or -1 if not applicable
     */
    public int getRelatedAccountId() {
        return relatedAccountId;
    }
    
    /**
     * Returns the timestamp of the transaction.
     * 
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * Returns the balance after this transaction.
     * 
     * @return the balance after transaction
     */
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    /**
     * Returns the description of the transaction.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns a detailed string representation of the transaction.
     * 
     * @return formatted transaction details
     */
    @Override
    public String toString() {
        String relatedInfo = (relatedAccountId != -1) 
            ? ", relatedAccount=" + relatedAccountId 
            : "";
        
        return String.format("Transaction{id=%d, type=%s, amount=€%.2f, account=%d%s, " +
                           "balanceAfter=€%.2f, time=%s, desc='%s'}",
                           transactionId, type, amount, accountId, relatedInfo,
                           balanceAfter, timestamp.format(FORMATTER), description);
    }
}
