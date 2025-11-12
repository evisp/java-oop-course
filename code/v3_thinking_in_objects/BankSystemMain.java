package v3_thinking_in_objects;

/**
 * Main class demonstrating Version 3: "Thinking in Objects"
 * 
 * This version showcases:
 * - Single Responsibility Principle (SRP) with Bank class
 * - Transaction tracking and audit trail
 * - Proper equals/hashCode implementation
 * - Immutability concepts
 * - Inter-object coordination (transfers)
 * - Better encapsulation and validation
 * 
 * Features legendary computer scientists as examples.
 * 
 * @author Evis Plaku
 * @version 3.0
 */
public class BankSystemMain {
    
    /**
     * Main method demonstrating refactored OOP design.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║   Bank Management System - Version 3: Thinking in Objects         ║");
        System.out.println("║   Featuring Computer Science Legends                              ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝\n");
        
        // ==================== CREATING THE BANK ====================
        System.out.println("--- Creating Bank ---");
        Bank turingBank = new Bank("Turing National Bank");
        System.out.println("Created: " + turingBank);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== CREATING CUSTOMERS ====================
        System.out.println("--- Creating Customers ---");
        
        Customer ada = new Customer(1815, "Ada Lovelace", 36, "10 St. James Square, London, England");
        Customer alan = new Customer(1912, "Alan Turing", 41, "Sherborne School, Dorset, England");
        
        // Add customers to the bank
        turingBank.addCustomer(ada);
        turingBank.addCustomer(alan);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== CREATING ACCOUNTS ====================
        System.out.println("--- Opening Accounts ---");
        
        // Ada opens two accounts
        System.out.println("\nAda Lovelace opens accounts:");
        Account adaSavings = new Account(5000.00);
        Account adaResearch = new Account(2000.00);
        
        ada.addAccount(adaSavings);
        ada.addAccount(adaResearch);
        
        // Alan opens two accounts
        System.out.println("\nAlan Turing opens accounts:");
        Account alanSavings = new Account(3000.00);
        Account alanCrypto = new Account(1500.00);
        
        alan.addAccount(alanSavings);
        alan.addAccount(alanCrypto);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING BANK OPERATIONS ====================
        System.out.println("--- Testing Bank Operations ---");
        
        System.out.println("\nAda deposits research grant:");
        turingBank.deposit(ada.getId(), adaResearch.getAccountId(), 3000.00);
        
        System.out.println("\nAlan withdraws for computing equipment:");
        turingBank.withdraw(alan.getId(), alanCrypto.getAccountId(), 500.00);
        
        System.out.println("\nAda makes multiple transactions:");
        turingBank.deposit(ada.getId(), adaSavings.getAccountId(), 1000.00);
        turingBank.withdraw(ada.getId(), adaSavings.getAccountId(), 200.00);
        turingBank.deposit(ada.getId(), adaSavings.getAccountId(), 500.00);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING TRANSFERS ====================
        System.out.println("--- Testing Transfers Between Accounts ---");
        
        System.out.println("\nAda transfers money from savings to research account:");
        turingBank.transfer(
            ada.getId(), adaSavings.getAccountId(),
            ada.getId(), adaResearch.getAccountId(),
            1000.00
        );
        
        System.out.println("\nAlan transfers money to Ada (collaboration funding):");
        turingBank.transfer(
            alan.getId(), alanSavings.getAccountId(),
            ada.getId(), adaResearch.getAccountId(),
            800.00
        );
        
        System.out.println("\nTrying invalid transfer (insufficient funds):");
        turingBank.transfer(
            alan.getId(), alanCrypto.getAccountId(),
            ada.getId(), adaSavings.getAccountId(),
            5000.00  // Alan doesn't have this much
        );
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING TRANSACTION HISTORY ====================
        System.out.println("--- Viewing Transaction History ---");
        
        System.out.println("\nAda's Research Account Transaction History:");
        adaResearch.displayTransactionHistory();
        
        System.out.println("\nAlan's Savings Account Transaction History:");
        alanSavings.displayTransactionHistory();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING FROZEN ACCOUNTS ====================
        System.out.println("--- Testing Frozen Account Features ---");
        
        System.out.println("\nBank freezes Ada's research account for audit:");
        adaResearch.freezeAccount();
        
        System.out.println("\nTrying to withdraw from frozen account:");
        turingBank.withdraw(ada.getId(), adaResearch.getAccountId(), 100.00);
        
        System.out.println("\nTrying to transfer into frozen account:");
        turingBank.transfer(
            ada.getId(), adaSavings.getAccountId(),
            ada.getId(), adaResearch.getAccountId(),
            500.00
        );
        
        System.out.println("\nUnfreezing the account after audit:");
        adaResearch.unfreezeAccount();
        
        System.out.println("\nNow operations work again:");
        turingBank.deposit(ada.getId(), adaResearch.getAccountId(), 500.00);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING EQUALS/HASHCODE ====================
        System.out.println("--- Testing equals() and hashCode() ---");
        
        Customer ada2 = new Customer(1815, "Ada Lovelace", 36, "Different Address");
        System.out.println("\nCreated another Customer with same ID but different address");
        System.out.println("Original Ada: " + ada);
        System.out.println("New Ada:      " + ada2);
        System.out.println("Are they equal? " + ada.equals(ada2) + " (same ID, so yes!)");
        System.out.println("Same hashCode? " + (ada.hashCode() == ada2.hashCode()));
        
        Account testAccount = ada.findAccount(adaSavings.getAccountId());
        System.out.println("\nFinding account by ID:");
        System.out.println("Found account equals original? " + testAccount.equals(adaSavings));
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TESTING VALIDATION ====================
        System.out.println("--- Testing Input Validation ---");
        
        System.out.println("\nTrying to create customer with invalid age:");
        try {
            Customer youngCustomer = new Customer(2000, "Young Person", 15, "Some Address");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Caught exception: " + e.getMessage());
        }
        
        System.out.println("\nTrying to create account with negative balance:");
        try {
            Account negativeAccount = new Account(-100.00);
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Caught exception: " + e.getMessage());
        }
        
        System.out.println("\nTrying to set invalid name:");
        try {
            ada.setName("");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Caught exception: " + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DISPLAY ALL CUSTOMERS ====================
        System.out.println("--- Current Bank Status ---");
        turingBank.displayAllCustomers();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== BANK REPORT ====================
        System.out.println("--- Bank Summary Report ---");
        turingBank.generateBankReport();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING REFACTORING BENEFITS ====================
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║              Version 3: Key OOP Concepts Demonstrated                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("✓ Single Responsibility Principle (SRP)");
        System.out.println("  → Bank manages customers and coordinates operations");
        System.out.println("  → Customer manages personal info and own accounts");
        System.out.println("  → Account handles transactions and history");
        System.out.println();
        System.out.println("✓ Transaction Tracking");
        System.out.println("  → Every operation creates immutable Transaction objects");
        System.out.println("  → Complete audit trail with timestamps");
        System.out.println("  → Enum for type-safe transaction types");
        System.out.println();
        System.out.println("✓ Proper Encapsulation");
        System.out.println("  → Immutable IDs (final fields)");
        System.out.println("  → Defensive copying (unmodifiable lists)");
        System.out.println("  → Input validation in constructors and setters");
        System.out.println();
        System.out.println("✓ equals() and hashCode()");
        System.out.println("  → Based on unique IDs for proper object comparison");
        System.out.println("  → Enables correct usage in collections");
        System.out.println();
        System.out.println("✓ Inter-Object Coordination");
        System.out.println("  → Bank coordinates transfers between customers");
        System.out.println("  → Demonstrates why manager classes are needed");
        System.out.println();
        System.out.println("✓ Better Code Organization");
        System.out.println("  → Clear separation of concerns");
        System.out.println("  → Easier to test and maintain");
        System.out.println("  → Ready for future enhancements (inheritance, interfaces)");
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║        Ada Lovelace and Alan Turing approve this design! 🎓       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
    }
}
