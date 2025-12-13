package v6_file_io;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for handling file operations related to the banking system.
 * Provides functionality for:
 * - Saving and loading entire Bank objects (serialization)
 * - Exporting transaction history to CSV files
 * - Creating audit reports as text files
 * - Backup and restore operations
 * 
 * Demonstrates:
 * - Object serialization/deserialization
 * - Try-with-resources for automatic resource management
 * - File I/O operations
 * - Exception handling with IOException and ClassNotFoundException
 * 
 * @author Evis Plaku
 * @version 6.0
 */
public class BankDataManager {
    
    /** Default directory for data files */
    private static final String DATA_DIR = "bank_data/";
    
    /** Date formatter for timestamps in filenames */
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    // ==================== SERIALIZATION OPERATIONS ====================
    
    /**
     * Saves the entire Bank object to a file using serialization.
     * Try-with-resources ensures file is closed even if exception occurs.
     * 
     * @param bank the Bank object to save
     * @param filename the name of the file to save to
     * @throws IOException if an I/O error occurs
     */
    public static void saveBank(
    		Bank bank, String filename) 
    				throws IOException {
    	
        // Ensure directory exists
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String fullPath = DATA_DIR + filename;
        
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(fullPath)))) {
            out.writeObject(bank);
            System.out.println("✓ Bank data saved successfully to: " + fullPath);
        }
    }
    
    /**
     * Loads a Bank object from a serialized file.
     * Try-with-resources ensures file is closed even if exception occurs.
     * 
     * @param filename the name of the file to load from
     * @return the loaded Bank object
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if the Bank class cannot be found
     */
    public static Bank loadBank(String filename) throws IOException, ClassNotFoundException {
        String fullPath = DATA_DIR + filename;
        
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(fullPath)))) {
            Bank bank = (Bank) in.readObject();
            System.out.println("Bank data loaded successfully from: " + fullPath);
            return bank;
        }
    }
    
    /**
     * Creates a backup of the bank with timestamp in filename.
     * 
     * @param bank the Bank object to backup
     * @return the backup filename
     * @throws IOException if an I/O error occurs
     */
    public static String backupBank(Bank bank) throws IOException {
        String timestamp = 
        		LocalDateTime.now().format(FORMATTER);
        String backupFilename = "bank_backup_" + timestamp + ".ser";
        saveBank(bank, backupFilename);
        return backupFilename;
    }
    
    // ==================== CSV EXPORT OPERATIONS ====================
    
    /**
     * Exports transaction history for an account to a CSV file.
     * Creates a human-readable file that can be opened in Excel.
     * 
     * @param account the account whose transactions to export
     * @param filename the name of the CSV file
     * @throws IOException if an I/O error occurs
     */
    public static void exportTransactionsToCSV(Account account, String filename) throws IOException {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String fullPath = DATA_DIR + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {
            // Write CSV header
            writer.println("Transaction ID,Type,Amount,Account ID,Balance After,Timestamp,Description");
            
            // Write each transaction
            for (Transaction t : account.getAuditTrail()) {
                writer.printf("%d,%s,%.2f,%d,%.2f,%s,\"%s\"%n",
                    t.getTransactionId(),
                    t.getType(),
                    t.getAmount(),
                    t.getAccountId(),
                    t.getBalanceAfter(),
                    t.getTimestamp(),
                    t.getDescription()
                );
            }
            
            System.out.println("  Transactions exported to: " + fullPath);
            System.out.println("  Total transactions: " + account.getTransactionCount());
        }
    }
    
    /**
     * Exports all transactions for all accounts of a customer to CSV.
     * 
     * @param customer the customer whose transactions to export
     * @param filename the name of the CSV file
     * @throws IOException if an I/O error occurs
     */
    public static void exportCustomerTransactions(
    		Customer customer, String filename) throws IOException {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String fullPath = DATA_DIR + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {
            writer.println("Account ID,Account Type,Transaction ID,Type,Amount,Balance After,Timestamp,Description");
            
            for (Account account : customer.getAccounts()) {
                for (Transaction t : account.getAuditTrail()) {
                    writer.printf("%d,%s,%d,%s,%.2f,%.2f,%s,\"%s\"%n",
                        account.getAccountId(),
                        account.getAccountTypeName(),
                        t.getTransactionId(),
                        t.getType(),
                        t.getAmount(),
                        t.getBalanceAfter(),
                        t.getTimestamp(),
                        t.getDescription()
                    );
                }
            }
            
            System.out.println("  Customer transactions exported to: " + fullPath);
        }
    }
    
    // ==================== TEXT REPORT GENERATION ====================
    
    /**
     * Generates a detailed audit report as a text file.
     * 
     * @param account the account to generate report for
     * @param filename the name of the text file
     * @throws IOException if an I/O error occurs
     */
    public static void generateAuditReportFile(Account account, String filename) throws IOException {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String fullPath = DATA_DIR + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {
            writer.println("═══════════════════════════════════════════════════════════════");
            writer.println("                    ACCOUNT AUDIT REPORT                       ");
            writer.println("═══════════════════════════════════════════════════════════════");
            writer.println("Generated: " + LocalDateTime.now().format(FORMATTER));
            writer.println();
            writer.println("Account Type: " + account.getAccountTypeName());
            writer.println("Account ID: " + account.getAccountId());
            writer.println("Current Balance: €" + String.format("%.2f", account.getBalance()));
            writer.println("Account Status: " + (account.isFrozen() ? "FROZEN" : "ACTIVE"));
            writer.println("Total Transactions: " + account.getTransactionCount());
            writer.println("Monthly Fee: €" + String.format("%.2f", account.calculateMonthlyFee()));
            writer.println();
            writer.println("═══════════════════════════════════════════════════════════════");
            writer.println("                    TRANSACTION HISTORY                        ");
            writer.println("═══════════════════════════════════════════════════════════════");
            
            if (account.getTransactionCount() == 0) {
                writer.println("No transactions recorded.");
            } else {
                for (Transaction t : account.getAuditTrail()) {
                    writer.println();
                    writer.println("Transaction #" + t.getTransactionId());
                    writer.println("  Type: " + t.getType());
                    writer.println("  Amount: €" + String.format("%.2f", t.getAmount()));
                    writer.println("  Balance After: €" + String.format("%.2f", t.getBalanceAfter()));
                    writer.println("  Timestamp: " + t.getTimestamp());
                    writer.println("  Description: " + t.getDescription());
                }
            }
            
            writer.println();
            writer.println("═══════════════════════════════════════════════════════════════");
            writer.println("                    END OF REPORT                              ");
            writer.println("═══════════════════════════════════════════════════════════════");
            
            System.out.println("  Audit report generated: " + fullPath);
        }
    }
    
    /**
     * Generates a bank summary report as a text file.
     * 
     * @param bank the bank to generate report for
     * @param filename the name of the text file
     * @throws IOException if an I/O error occurs
     */
    public static void generateBankReportFile(Bank bank, String filename) throws IOException {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String fullPath = DATA_DIR + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {
            writer.println("═══════════════════════════════════════════════════════════════");
            writer.println("                    BANK SUMMARY REPORT                        ");
            writer.println("═══════════════════════════════════════════════════════════════");
            writer.println("Bank: " + bank.getBankName());
            writer.println("Generated: " + LocalDateTime.now().format(FORMATTER));
            writer.println("Total Customers: " + bank.getCustomerCount());
            writer.println();
            writer.println("Customer Details:");
            writer.println("─────────────────────────────────────────────────────────────");
            
            // Note: This assumes Bank has a method to get all customers
            // You may need to add a getCustomers() method to Bank class
            writer.println("See individual customer reports for detailed information.");
            
            writer.println("═══════════════════════════════════════════════════════════════");
            
            System.out.println("  Bank report generated: " + fullPath);
        }
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Checks if a save file exists.
     * 
     * @param filename the filename to check
     * @return true if file exists, false otherwise
     */
    public static boolean fileExists(String filename) {
        File file = new File(DATA_DIR + filename);
        return file.exists();
    }
    
    /**
     * Deletes a save file.
     * 
     * @param filename the filename to delete
     * @return true if file was deleted, false otherwise
     */
    public static boolean deleteFile(String filename) {
        File file = new File(DATA_DIR + filename);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("  File deleted: " + filename);
            }
            return deleted;
        }
        return false;
    }
    
    /**
     * Lists all save files in the data directory.
     */
    public static void listSaveFiles() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            System.out.println("No save files found.");
            return;
        }
        
        File[] files = dir.listFiles((d, name) -> name.endsWith(".ser"));
        if (files == null || files.length == 0) {
            System.out.println("No save files found.");
        } else {
            System.out.println("\nAvailable save files:");
            for (File file : files) {
                System.out.println("  - " + file.getName() + 
                                 " (" + file.length() + " bytes)");
            }
        }
    }
}
