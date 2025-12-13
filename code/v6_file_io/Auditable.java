package v6_file_io;


import java.util.List;

/**
 * Interface for accounts that require audit trail capabilities.
 * All bank accounts must implement this interface to ensure compliance
 * and transparency in financial operations.
 * 
 * This demonstrates the use of interfaces to define a contract that
 * multiple unrelated classes can implement.
 * 
 * @author Evis Plaku
 * @version 6.0
 */
public interface Auditable {
    
    /**
     * Generates a comprehensive audit report for this account.
     * The report should include all transactions and account status.
     */
    void generateAuditReport();
    
    /**
     * Returns the complete audit trail (transaction history) for this account.
     * 
     * @return list of all transactions in chronological order
     */
    List<Transaction> getAuditTrail();
    
    /**
     * Returns the timestamp of the last modification to this account.
     * 
     * @return formatted string representing the last transaction time
     */
    String getLastModifiedTime();
    
    /**
     * Returns the total number of transactions for audit purposes.
     * 
     * @return count of all transactions
     */
    int getTransactionCount();
}
