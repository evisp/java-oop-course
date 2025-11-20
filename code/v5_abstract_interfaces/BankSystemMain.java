package v5_abstract_classes_interfaces;

import java.util.ArrayList;

/**
 * Main class demonstrating Version 5: "Abstract Classes and Interfaces"
 * 
 * This version showcases:
 * - Abstract Account class that cannot be instantiated
 * - Abstract methods that subclasses MUST implement
 * - Auditable interface implemented by all accounts
 * - Polymorphic use of abstract methods
 * - Interface-based operations
 * - Benefits of abstraction and interfaces together
 * 
 * Features legendary computer scientists as examples.
 * 
 * @author Evis Plaku
 * @version 5.0
 */
public class BankSystemMain {
    
    /**
     * Main method demonstrating abstract classes and interfaces.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║   Bank System - Version 5: Abstract Classes & Interfaces           ║");
        System.out.println("║   Featuring Computer Science Legends                             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝\n");
        
        // ==================== DEMONSTRATING ABSTRACT CLASS ====================
        System.out.println("--- Demonstrating Abstract Classes ---");
        
        System.out.println("\n[Attempting to create abstract Account - WILL NOT COMPILE]");
        System.out.println("// Account acc = new Account(1000);  // ❌ COMPILE ERROR!");
        System.out.println("// Cannot instantiate abstract class Account");
        
        System.out.println("\n[Must use concrete subclasses - this works ✓]");
        System.out.println("// SavingsAccount savings = new SavingsAccount(1000, 0.03);");
        System.out.println("// CheckingAccount checking = new CheckingAccount(1000, 500);");
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== CREATING BANK AND CUSTOMERS ====================
        System.out.println("--- Creating Bank and Customers ---");
        
        Bank turingBank = new Bank("Turing National Bank");
        System.out.println("Created: " + turingBank);
        
        Customer ada = new Customer(1815, "Ada Lovelace", 36, "10 St. James Square, London, England");
        Customer alan = new Customer(1912, "Alan Turing", 41, "Sherborne School, Dorset, England");
        
        turingBank.addCustomer(ada);
        turingBank.addCustomer(alan);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== OPENING ACCOUNTS ====================
        System.out.println("--- Opening Accounts (Concrete Subclasses of Abstract Account) ---");
        
        System.out.println("\n[Ada Lovelace opens accounts]");
        SavingsAccount adaSavings = turingBank.openSavingsAccount(ada.getId(), 5000.00, 0.03);
        CheckingAccount adaChecking = turingBank.openCheckingAccount(ada.getId(), 2000.00, 500.00);
        
        System.out.println("\n[Alan Turing opens accounts]");
        SavingsAccount alanSavings = turingBank.openSavingsAccount(alan.getId(), 3000.00, 0.025);
        CheckingAccount alanChecking = turingBank.openCheckingAccount(alan.getId(), 1500.00, 300.00);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING ABSTRACT METHOD IMPLEMENTATION ====================
        System.out.println("--- Demonstrating Abstract Methods (MUST be implemented) ---");
        
        System.out.println("\n[Abstract method: withdraw() - Different implementations]");
        System.out.println("SavingsAccount.withdraw() enforces minimum balance:");
        turingBank.withdraw(ada.getId(), adaSavings.getAccountId(), 4950.00); // Fails - below minimum
        
        System.out.println("\nCheckingAccount.withdraw() allows overdraft:");
        turingBank.withdraw(alan.getId(), alanChecking.getAccountId(), 1700.00); // Succeeds - uses overdraft
        
        System.out.println("\n[Abstract method: calculateMonthlyFee() - Different implementations]");
        System.out.println("Ada's Savings Account fee: €" + adaSavings.calculateMonthlyFee());
        System.out.println("Ada's Checking Account fee: €" + adaChecking.calculateMonthlyFee());
        System.out.println("Alan's Checking Account fee: €" + alanChecking.calculateMonthlyFee());
        
        System.out.println("\n[Abstract method: getAccountTypeName() - Different implementations]");
        System.out.println("Account #" + adaSavings.getAccountId() + " is: " + adaSavings.getAccountTypeName());
        System.out.println("Account #" + adaChecking.getAccountId() + " is: " + adaChecking.getAccountTypeName());
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== PERFORMING TRANSACTIONS ====================
        System.out.println("--- Performing Various Transactions ---");
        
        System.out.println("\n[Deposits and withdrawals to build transaction history]");
        turingBank.deposit(ada.getId(), adaSavings.getAccountId(), 1000.00);
        turingBank.withdraw(ada.getId(), adaSavings.getAccountId(), 200.00);
        turingBank.deposit(ada.getId(), adaChecking.getAccountId(), 500.00);
        turingBank.withdraw(ada.getId(), adaChecking.getAccountId(), 100.00);
        
        turingBank.deposit(alan.getId(), alanSavings.getAccountId(), 800.00);
        turingBank.deposit(alan.getId(), alanChecking.getAccountId(), 300.00);
        
        System.out.println("\n[Transfer between accounts]");
        turingBank.transfer(
            ada.getId(), adaSavings.getAccountId(),
            alan.getId(), alanSavings.getAccountId(),
            500.00
        );
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING AUDITABLE INTERFACE ====================
        System.out.println("--- Demonstrating Auditable Interface ---");
        
        System.out.println("\n[All accounts implement Auditable through abstract Account]");
        System.out.println("Every account MUST provide audit capabilities");
        
        System.out.println("\n[Using Auditable interface methods]");
        
        // Using interface reference
        Auditable auditableAccount = adaSavings;
        System.out.println("\nAccount as Auditable interface:");
        System.out.println("  Transaction Count: " + auditableAccount.getTransactionCount());
        System.out.println("  Last Modified: " + auditableAccount.getLastModifiedTime());
        System.out.println("  Audit Trail Size: " + auditableAccount.getAuditTrail().size());
        
        System.out.println("\n[Generating individual audit report]");
        adaSavings.generateAuditReport();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== BANK-WIDE AUDITABLE OPERATIONS ====================
        System.out.println("--- Bank-Wide Audit Operations (Using Auditable Interface) ---");
        
        System.out.println("\n[Generating audit reports for all accounts]");
        turingBank.generateAllAuditReports();
        
        System.out.println("\n[Finding high-activity accounts]");
        ArrayList<Account> highActivity = turingBank.getHighActivityAccounts(3);
        System.out.println("Accounts with more than 3 transactions:");
        for (Account account : highActivity) {
            System.out.println("  " + account.getAccountTypeName() + " #" + account.getAccountId() + 
                             " - " + account.getTransactionCount() + " transactions");
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== POLYMORPHIC OPERATIONS ====================
        System.out.println("--- Polymorphic Operations on Abstract Types ---");
        
        System.out.println("\n[Applying interest - uses instanceof for type checking]");
        turingBank.applyInterestToAllSavings();
        
        System.out.println("\n[Applying monthly fees - uses abstract calculateMonthlyFee()]");
        turingBank.applyMonthlyFeesToAllChecking();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== CUSTOMER TOTAL FEES ====================
        System.out.println("--- Customer Monthly Fees (Using Abstract Method) ---");
        
        System.out.println("\n[getTotalMonthlyFees() uses abstract calculateMonthlyFee()]");
        System.out.println("Ada's total monthly fees: €" + String.format("%.2f", ada.getTotalMonthlyFees()));
        System.out.println("Alan's total monthly fees: €" + String.format("%.2f", alan.getTotalMonthlyFees()));
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING BENEFITS ====================
        System.out.println("--- Benefits of Abstract Classes & Interfaces ---");
        
        System.out.println("\n[Abstract Class Benefits]");
        System.out.println("1. Cannot create generic 'Account' - forces use of specific types");
        System.out.println("2. Subclasses MUST implement abstract methods - compiler enforced");
        System.out.println("3. Shared code (deposit, transaction history) in one place");
        System.out.println("4. Polymorphic collections: ArrayList<Account> holds all subtypes");
        
        System.out.println("\n[Interface Benefits]");
        System.out.println("1. Auditable defines contract - all accounts must provide audit trail");
        System.out.println("2. Can use Auditable reference for audit-specific operations");
        System.out.println("3. Future accounts automatically get audit capability");
        System.out.println("4. Separates 'what' (interface) from 'how' (implementation)");
        
        System.out.println("\n[Code Flexibility]");
        System.out.println("Adding new account types (e.g., BusinessAccount):");
        System.out.println("  - Must extend abstract Account ✓");
        System.out.println("  - Must implement withdraw(), calculateMonthlyFee(), getAccountTypeName() ✓");
        System.out.println("  - Automatically implements Auditable ✓");
        System.out.println("  - Works with all existing Bank operations ✓");
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== FINAL REPORTS ====================
        System.out.println("--- Final Bank Status ---");
        
        turingBank.displayAllCustomers();
        turingBank.generateBankReport();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== ABSTRACT VS INTERFACE COMPARISON ====================
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║       Version 5: Abstract Classes vs Interfaces - Summary           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("┌─────────────────────────┬─────────────────────────────────────────┐");
        System.out.println("│   ABSTRACT CLASSES      │   INTERFACES                            │");
        System.out.println("├─────────────────────────┼─────────────────────────────────────────┤");
        System.out.println("│ Account (abstract)      │ Auditable                               │");
        System.out.println("│ - Has fields            │ - No fields (just contract)             │");
        System.out.println("│ - Has constructors      │ - No constructors                       │");
        System.out.println("│ - Can have concrete     │ - Only method signatures                │");
        System.out.println("│   methods (deposit)     │   (until Java 8 default methods)        │");
        System.out.println("│ - Can have abstract     │ - All methods must be implemented       │");
        System.out.println("│   methods (withdraw)    │   by class                              │");
        System.out.println("│ - Single inheritance    │ - Multiple implementation               │");
        System.out.println("│ - 'IS-A' relationship   │ - 'CAN-DO' capability                   │");
        System.out.println("└─────────────────────────┴─────────────────────────────────────────┘");
        System.out.println();
        System.out.println("✓ Use Abstract Class when:");
        System.out.println("  → Classes are closely related (SavingsAccount IS-A Account)");
        System.out.println("  → Need to share code/state between classes");
        System.out.println("  → Want to provide default implementation");
        System.out.println();
        System.out.println("✓ Use Interface when:");
        System.out.println("  → Define capability/behavior contract (Auditable)");
        System.out.println("  → Unrelated classes need same behavior");
        System.out.println("  → Need multiple inheritance");
        System.out.println();
        System.out.println("✓ Our Design:");
        System.out.println("  → Account is abstract - all accounts share common structure");
        System.out.println("  → Auditable is interface - defines audit capability");
        System.out.println("  → Subclasses must implement abstract methods (enforced)");
        System.out.println("  → All accounts automatically auditable (through inheritance)");
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║            Ada Lovelace & Alan Turing approve! 🎓                 ║");
        System.out.println("║      You've mastered Abstract Classes and Interfaces!            ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
    }
}

