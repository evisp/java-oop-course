package v6_file_io;


import java.io.IOException;

/**
 * Main class demonstrating Week 6: "File I/O and Persistence"
 * 
 * This version showcases:
 * - Saving and loading entire Bank objects (serialization)
 * - Exporting transaction history to CSV files
 * - Generating audit reports as text files
 * - Try-catch exception handling for file operations
 * - Try-with-resources for automatic resource management
 * - Backup and restore functionality
 * 
 * Features legendary computer scientists as examples.
 * 
 * @author Evis Plaku
 * @version 6.0
 */
public class BankSystemMain {
    
    /**
     * Main method demonstrating file I/O operations.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║        Bank System - Week 6: File I/O and Persistence           ║");
        System.out.println("║        Featuring Computer Science Legends                        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝\n");
        
        // ==================== CREATING BANK AND CUSTOMERS ====================
        System.out.println("--- Creating Bank and Customers ---");
        
        Bank turingBank = new Bank("Turing National Bank");
        
        Customer ada = new Customer(1815, "Ada Lovelace", 36, "10 St. James Square, London, England");
        Customer alan = new Customer(1912, "Alan Turing", 41, "Sherborne School, Dorset, England");
        
        turingBank.addCustomer(ada);
        turingBank.addCustomer(alan);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== OPENING ACCOUNTS ====================
        System.out.println("--- Opening Accounts ---");
        
        SavingsAccount adaSavings = turingBank.openSavingsAccount(ada.getId(), 5000.00, 0.03);
        CheckingAccount adaChecking = turingBank.openCheckingAccount(ada.getId(), 2000.00, 500.00);
        
        SavingsAccount alanSavings = turingBank.openSavingsAccount(alan.getId(), 3000.00, 0.025);
        CheckingAccount alanChecking = turingBank.openCheckingAccount(alan.getId(), 1500.00, 300.00);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== PERFORMING TRANSACTIONS ====================
        System.out.println("--- Performing Transactions to Build History ---");
        
        System.out.println("\n[Ada's transactions]");
        turingBank.deposit(ada.getId(), adaSavings.getAccountId(), 1000.00);
        turingBank.withdraw(ada.getId(), adaSavings.getAccountId(), 500.00);
        turingBank.deposit(ada.getId(), adaSavings.getAccountId(), 250.00);
        turingBank.deposit(ada.getId(), adaChecking.getAccountId(), 800.00);
        turingBank.withdraw(ada.getId(), adaChecking.getAccountId(), 150.00);
        
        System.out.println("\n[Alan's transactions]");
        turingBank.deposit(alan.getId(), alanSavings.getAccountId(), 1200.00);
        turingBank.withdraw(alan.getId(), alanSavings.getAccountId(), 300.00);
        turingBank.deposit(alan.getId(), alanChecking.getAccountId(), 600.00);
        turingBank.withdraw(alan.getId(), alanChecking.getAccountId(), 1800.00); // Uses overdraft
        
        System.out.println("\n[Transfers]");
        turingBank.transfer(
            ada.getId(), adaSavings.getAccountId(),
            alan.getId(), alanSavings.getAccountId(),
            750.00
        );
        
        System.out.println("\n[Monthly operations]");
        turingBank.applyInterestToAllSavings();
        turingBank.applyMonthlyFeesToAllChecking();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DISPLAYING CURRENT STATE ====================
        System.out.println("--- Current Bank State (Before Saving) ---");
        turingBank.displayAllCustomers();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING SERIALIZATION ====================
        System.out.println("--- Demonstrating Bank Serialization (Save/Load) ---");
        
        try {
            System.out.println("\n[1. Saving Bank to File]");
            BankDataManager.saveBank(turingBank, "turingbank.ser");
            
            System.out.println("\n[2. Loading Bank from File]");
            Bank loadedBank = BankDataManager.loadBank("turingbank.ser");
            
            System.out.println("\n[3. Verifying Loaded Data]");
            System.out.println("Loaded Bank: " + loadedBank.getBankName());
            System.out.println("Number of Customers: " + loadedBank.getCustomerCount());
            
            // Verify customer data
            Customer loadedAda = loadedBank.findCustomer(ada.getId());
            if (loadedAda != null) {
                System.out.println("\n✓ Customer loaded successfully: " + loadedAda.getName());
                System.out.println("  Accounts: " + loadedAda.getAccountCount());
                System.out.println("  Total Balance: €" + String.format("%.2f", loadedAda.getTotalBalance()));
                
                // Verify transaction history is preserved
                Account loadedAdaSavings = loadedAda.findAccount(adaSavings.getAccountId());
                if (loadedAdaSavings != null) {
                    System.out.println("  Transaction History Preserved: " + 
                                     loadedAdaSavings.getTransactionCount() + " transactions");
                }
            }
            
            System.out.println("\n✓ Serialization successful! All data preserved.");
            
        } catch (IOException e) {
            System.err.println("❌ File I/O Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Class Not Found: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING BACKUP ====================
        System.out.println("--- Demonstrating Backup Functionality ---");
        
        try {
            System.out.println("\n[Creating Timestamped Backup]");
            String backupFilename = BankDataManager.backupBank(turingBank);
            System.out.println("Backup created: " + backupFilename);
            
            System.out.println("\n[Listing All Save Files]");
            BankDataManager.listSaveFiles();
            
        } catch (IOException e) {
            System.err.println("❌ Backup Error: " + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== EXPORTING TO CSV ====================
        System.out.println("--- Exporting Transaction History to CSV ---");
        
        try {
            System.out.println("\n[1. Exporting Ada's Savings Account Transactions]");
            BankDataManager.exportTransactionsToCSV(
                adaSavings, 
                "ada_savings_transactions.csv"
            );
            System.out.println("   → Can be opened in Excel/Google Sheets");
            
            System.out.println("\n[2. Exporting Ada's Checking Account Transactions]");
            BankDataManager.exportTransactionsToCSV(
                adaChecking, 
                "ada_checking_transactions.csv"
            );
            
            System.out.println("\n[3. Exporting All of Alan's Transactions]");
            BankDataManager.exportCustomerTransactions(
                alan,
                "alan_all_transactions.csv"
            );
            
            System.out.println("\n✓ CSV files created successfully!");
            System.out.println("  Location: bank_data/ directory");
            System.out.println("  You can open these files in Excel or any spreadsheet program");
            
        } catch (IOException e) {
            System.err.println("❌ CSV Export Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== GENERATING TEXT REPORTS ====================
        System.out.println("--- Generating Audit Reports as Text Files ---");
        
        try {
            System.out.println("\n[1. Generating Ada's Savings Account Audit Report]");
            BankDataManager.generateAuditReportFile(
                adaSavings,
                "ada_savings_audit_report.txt"
            );
            
            System.out.println("\n[2. Generating Alan's Checking Account Audit Report]");
            BankDataManager.generateAuditReportFile(
                alanChecking,
                "alan_checking_audit_report.txt"
            );
            
            System.out.println("\n[3. Generating Bank Summary Report]");
            BankDataManager.generateBankReportFile(
                turingBank,
                "bank_summary_report.txt"
            );
            
            System.out.println("\n✓ Text reports generated successfully!");
            System.out.println("  Location: bank_data/ directory");
            System.out.println("  You can open these files in any text editor");
            
        } catch (IOException e) {
            System.err.println("❌ Report Generation Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING PERSISTENCE ====================
        System.out.println("--- Demonstrating Data Persistence ---");
        
        System.out.println("\n[Scenario: Program stops and restarts]");
        System.out.println("Simulating program restart...\n");
        
        try {
            // "Restart" - load bank from file
            System.out.println("Loading bank data from previous session...");
            Bank restoredBank = BankDataManager.loadBank("turingbank.ser");
            
            System.out.println("\n✓ Bank restored successfully!");
            System.out.println("  All customer data preserved");
            System.out.println("  All account balances preserved");
            System.out.println("  All transaction history preserved");
            
            System.out.println("\n[Performing new transaction on restored bank]");
            Customer restoredAda = restoredBank.findCustomer(ada.getId());
            Account restoredAdaSavings = restoredAda.findAccount(adaSavings.getAccountId());
            
            System.out.println("Ada's balance before: €" + restoredAdaSavings.getBalance());
            restoredBank.deposit(restoredAda.getId(), restoredAdaSavings.getAccountId(), 500.00);
            System.out.println("Ada's balance after: €" + restoredAdaSavings.getBalance());
            
            System.out.println("\n✓ New transactions work on restored data!");
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Restoration Error: " + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== EXCEPTION HANDLING DEMONSTRATION ====================
        System.out.println("--- Demonstrating Exception Handling ---");
        
        System.out.println("\n[1. Attempting to load non-existent file]");
        try {
            Bank bank = BankDataManager.loadBank("nonexistent.ser");
        } catch (IOException e) {
            System.out.println("✓ IOException caught: " + e.getMessage());
            System.out.println("  (This is expected - file doesn't exist)");
        } catch (ClassNotFoundException e) {
            System.out.println("✓ ClassNotFoundException caught: " + e.getMessage());
        }
        
        System.out.println("\n[2. Checking if file exists before loading]");
        String filename = "turingbank.ser";
        if (BankDataManager.fileExists(filename)) {
            System.out.println("✓ File exists: " + filename);
            System.out.println("  Safe to load!");
        } else {
            System.out.println("File does not exist: " + filename);
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== SUMMARY ====================
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║              Week 6: File I/O Concepts Demonstrated               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("✓ Object Serialization");
        System.out.println("  → Entire Bank object saved to file");
        System.out.println("  → All relationships preserved (customers, accounts, transactions)");
        System.out.println("  → Binary format (.ser files)");
        System.out.println();
        System.out.println("✓ Deserialization");
        System.out.println("  → Bank object restored from file");
        System.out.println("  → All data intact and usable");
        System.out.println("  → Can continue operations seamlessly");
        System.out.println();
        System.out.println("✓ CSV Export");
        System.out.println("  → Transaction history exported to CSV");
        System.out.println("  → Human-readable format");
        System.out.println("  → Can be opened in Excel/Google Sheets");
        System.out.println();
        System.out.println("✓ Text Report Generation");
        System.out.println("  → Audit reports as formatted text files");
        System.out.println("  → Easy to read and share");
        System.out.println("  → Professional documentation");
        System.out.println();
        System.out.println("✓ Exception Handling");
        System.out.println("  → Try-catch blocks for IOException");
        System.out.println("  → Try-catch blocks for ClassNotFoundException");
        System.out.println("  → Graceful error handling");
        System.out.println();
        System.out.println("✓ Try-with-Resources");
        System.out.println("  → Automatic resource management");
        System.out.println("  → Files automatically closed");
        System.out.println("  → Prevents resource leaks");
        System.out.println();
        System.out.println("✓ Data Persistence");
        System.out.println("  → Program can stop and restart");
        System.out.println("  → All data preserved between sessions");
        System.out.println("  → Backup and restore functionality");
        System.out.println();
        System.out.println("✓ File Management");
        System.out.println("  → Check if files exist");
        System.out.println("  → List available save files");
        System.out.println("  → Delete old files");
        System.out.println("  → Timestamped backups");
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                 Files Created in bank_data/                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Serialized Files (.ser):");
        System.out.println("  • turingbank.ser - Main bank save file");
        System.out.println("  • bank_backup_[timestamp].ser - Timestamped backup");
        System.out.println();
        System.out.println("CSV Files (.csv):");
        System.out.println("  • ada_savings_transactions.csv - Ada's savings transactions");
        System.out.println("  • ada_checking_transactions.csv - Ada's checking transactions");
        System.out.println("  • alan_all_transactions.csv - All of Alan's transactions");
        System.out.println();
        System.out.println("Text Reports (.txt):");
        System.out.println("  • ada_savings_audit_report.txt - Ada's savings audit");
        System.out.println("  • alan_checking_audit_report.txt - Alan's checking audit");
        System.out.println("  • bank_summary_report.txt - Bank summary");
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║            Ada Lovelace & Alan Turing approve! 🎓                 ║");
        System.out.println("║       You've mastered File I/O and Persistence in Java!          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
    }
}
