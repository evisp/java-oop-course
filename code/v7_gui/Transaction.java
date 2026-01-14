package v7_gui;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT
    }

    private static int transactionCounter = 10000;

    private final int transactionId;
    private final TransactionType type;
    private final double amount;
    private final int accountId;
    private final int relatedAccountId;
    private final LocalDateTime timestamp;
    private final double balanceAfter;
    private final String description;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Deposit/Withdraw (no custom timestamp)
    public Transaction(TransactionType type, double amount, int accountId,
                       double balanceAfter, String description) {
        this(type, amount, accountId, balanceAfter, description, LocalDateTime.now());
    }

    // Deposit/Withdraw (custom timestamp)  <-- this is what your generator needs
    public Transaction(TransactionType type, double amount, int accountId,
                       double balanceAfter, String description, LocalDateTime timestamp) {
        this.transactionId = ++transactionCounter;
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
        this.relatedAccountId = -1;
        this.timestamp = (timestamp == null) ? LocalDateTime.now() : timestamp;
        this.balanceAfter = balanceAfter;
        this.description = description;
    }

    // Transfer (no custom timestamp)
    public Transaction(TransactionType type, double amount, int accountId,
                       int relatedAccountId, double balanceAfter, String description) {
        this(type, amount, accountId, relatedAccountId, balanceAfter, description, LocalDateTime.now());
    }

    // Transfer (custom timestamp)
    public Transaction(TransactionType type, double amount, int accountId,
                       int relatedAccountId, double balanceAfter, String description, LocalDateTime timestamp) {
        this.transactionId = ++transactionCounter;
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
        this.relatedAccountId = relatedAccountId;
        this.timestamp = (timestamp == null) ? LocalDateTime.now() : timestamp;
        this.balanceAfter = balanceAfter;
        this.description = description;
    }

    public int getTransactionId() { return transactionId; }

    public TransactionType getType() { return type; }

    public double getAmount() { return amount; }

    public int getAccountId() { return accountId; }

    public int getRelatedAccountId() { return relatedAccountId; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public double getBalanceAfter() { return balanceAfter; }

    public String getDescription() { return description; }

    @Override
    public String toString() {
        String relatedInfo = (relatedAccountId != -1) ? ", relatedAccount=" + relatedAccountId : "";
        return String.format(
                "Transaction{id=%d, type=%s, amount=€%.2f, account=%d%s, balanceAfter=€%.2f, time=%s, desc='%s'}",
                transactionId, type, amount, accountId, relatedInfo, balanceAfter, timestamp.format(FORMATTER), description
        );
    }
}
