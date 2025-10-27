package v2_basic_collections;

/**
 * Main class to test the Customer and Account classes with ArrayList integration.
 * Features famous computer scientists as customer examples.
 * Demonstrates composition and collection management.
 * 
 * @author Evis Plaku
 * @version 2.0
 */
public class BankSystemMain {
    
    /**
     * Main method to test Customer and Account functionality with ArrayList.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== Bank Management System - Week 2 Demo ===");
        System.out.println("Featuring Computer Science Legends with Multiple Accounts!\n");
        
        // ==================== CREATING CUSTOMERS ====================
        System.out.println("--- Creating Customers ---");
        
        Customer ada = new Customer(1815, "Ada Lovelace", 36, "10 St. James Square, London, England");
        Customer alan = new Customer(1912, "Alan Turing", 41, "Sherborne School, Dorset, England");
        
        System.out.println("Created: " + ada);
        System.out.println("Created: " + alan);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== ADDING ACCOUNTS TO CUSTOMERS ====================
        System.out.println("--- Adding Accounts to Customers ---");
        
        // Ada Lovelace opens multiple accounts
        System.out.println("\nAda Lovelace (First Programmer) opens accounts:");
        Account adaSavings = new Account(5000.00);  // Inheritance savings
        Account adaResearch = new Account(2000.00); // Research fund
        Account adaPersonal = new Account();        // Personal checking
        
        ada.addAccount(adaSavings);
        ada.addAccount(adaResearch);
        ada.addAccount(adaPersonal);
        
        // Alan Turing opens accounts
        System.out.println("\nAlan Turing (Father of CS) opens accounts:");
        Account alanSavings = new Account(3000.00);   // Wartime savings
        Account alanCrypto = new Account(1500.00);    // Cryptography research
        
        alan.addAccount(alanSavings);
        alan.addAccount(alanCrypto);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DISPLAYING ALL ACCOUNTS ====================
        System.out.println("--- Displaying All Customer Accounts ---");
        
        ada.displayAllAccounts();
        alan.displayAllAccounts();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING DEPOSIT OPERATIONS ====================
        System.out.println("--- Testing Deposit Operations via Customer ---");
        
        System.out.println("\nAda receives royalties from her mathematical papers:");
        ada.deposit(adaResearch.getAccountId(), 1500.00);
        
        System.out.println("\nAlan gets paid for Enigma work:");
        alan.deposit(alanSavings.getAccountId(), 2000.00);
        
      
        System.out.println("\nTrying to deposit to non-existent account:");
        ada.deposit(9999, 100.00);  // Account doesn't exist
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING WITHDRAWAL OPERATIONS ====================
        System.out.println("--- Testing Withdrawal Operations via Customer ---");
        
        System.out.println("\nAda funds her Analytical Engine research:");
        ada.withdraw(adaResearch.getAccountId(), 500.00);
        
        System.out.println("\nAlan buys computing equipment:");
        alan.withdraw(alanCrypto.getAccountId(), 800.00);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING ACCOUNT SEARCH ====================
        System.out.println("--- Testing Account Search ---");
        
        System.out.println("\nSearching for Ada's research account #" + adaResearch.getAccountId() + ":");
        Account foundAccount = ada.findAccount(adaResearch.getAccountId());
        if (foundAccount != null) {
            System.out.println("Found: " + foundAccount);
        }
        
        System.out.println("\nSearching for non-existent account #9999:");
        Account notFound = ada.findAccount(9999);
        if (notFound == null) {
            System.out.println("Account not found (as expected)");
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING FROZEN ACCOUNTS ====================
        System.out.println("--- Testing Frozen Account Feature ---");
        
        System.out.println("\nFreezing Alan's cryptography account for security review:");
        alanCrypto.freezeAccount();
        
        System.out.println("\nTrying to withdraw from frozen account:");
        alan.withdraw(alanCrypto.getAccountId(), 100.00);  // Should fail
        
        System.out.println("\nTrying to deposit to frozen account:");
        alan.deposit(alanCrypto.getAccountId(), 500.00);   // Should fail
        
        System.out.println("\nUnfreezing the account:");
        alanCrypto.unfreezeAccount();
        
        System.out.println("\nNow withdrawal works:");
        alan.withdraw(alanCrypto.getAccountId(), 100.00);  // Should succeed
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING ACCOUNT REMOVAL ====================
        System.out.println("--- Testing Account Removal ---");
        
        System.out.println("\nAda closes her personal checking account:");
        System.out.println("Accounts before removal: " + ada.getAccountCount());
        ada.removeAccount(adaPersonal);
        System.out.println("Accounts after removal: " + ada.getAccountCount());
        
        ada.displayAllAccounts();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING TOTAL BALANCE ====================
        System.out.println("--- Testing Total Balance Calculation ---");
        
        System.out.println("\nAda Lovelace's total wealth: €" + 
                          String.format("%.2f", ada.getTotalBalance()));
        System.out.println("Alan Turing's total wealth: €" + 
                          String.format("%.2f", alan.getTotalBalance()));
       
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== FINAL CUSTOMER SUMMARY ====================
        System.out.println("--- Final Customer Summary ---");
        System.out.println("\n" + ada);
        System.out.println(alan);
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("=== End of Week 2 Demo ===");
        System.out.println("\nKey Concepts Demonstrated:");
        System.out.println("✓ Composition (Customer HAS-A Account)");
        System.out.println("✓ ArrayList for managing multiple objects");
        System.out.println("✓ Adding and removing from collections");
        System.out.println("✓ Searching for objects in a list");
        System.out.println("✓ Iterating through collections");
        System.out.println("✓ Aggregating data from multiple objects");
        System.out.println("✓ Method delegation between classes");
        System.out.println("\nThese pioneers would be proud of your OOP skills!");
    }
}
