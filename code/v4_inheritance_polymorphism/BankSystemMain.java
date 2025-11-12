package v4_inheritance_polymorphism;

/**
 * Main class demonstrating Version 4: "Inheritance and Polymorphism"
 * 
 * This version showcases:
 * - Inheritance with Account, SavingsAccount, CheckingAccount
 * - Method overriding (different withdraw behaviors)
 * - Polymorphism (parent reference to child objects)
 * - super keyword for constructor chaining
 * - instanceof and type casting
 * - Polymorphic collections (ArrayList of parent type holding children)
 * 
 * Features legendary computer scientists as examples.
 * 
 * @author Evis Plaku
 * @version 4.0
 */
public class BankSystemMain {
    
    /**
     * Main method demonstrating inheritance and polymorphism.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║   Bank Management System - Version 4: Inheritance & Polymorphism   ║");
        System.out.println("║   Featuring Computer Science Legends                             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝\n");
        
        // ==================== CREATING THE BANK ====================
        System.out.println("--- Creating Bank ---");
        Bank lovelaceBank = new Bank("Lovelace National Bank");
        System.out.println("Created: " + lovelaceBank);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== CREATING CUSTOMERS ====================
        System.out.println("--- Creating Customers ---");
        
        Customer ada = new Customer(1815, "Ada Lovelace", 36, "10 St. James Square, London, England");
        Customer alan = new Customer(1912, "Alan Turing", 41, "Sherborne School, Dorset, England");
        
        lovelaceBank.addCustomer(ada);
        lovelaceBank.addCustomer(alan);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING INHERITANCE ====================
        System.out.println("--- Demonstrating Inheritance: Creating Different Account Types ---");
        
        System.out.println("\n[Ada Lovelace opens accounts]");
        // Using Bank's polymorphic methods
        SavingsAccount adaSavings = lovelaceBank.openSavingsAccount(ada.getId(), 5000.00, 0.03);
        CheckingAccount adaChecking = lovelaceBank.openCheckingAccount(ada.getId(), 2000.00, 500.00);
        
        System.out.println("\n[Alan Turing opens accounts]");
        SavingsAccount alanSavings = lovelaceBank.openSavingsAccount(alan.getId(), 3000.00, 0.025);
        CheckingAccount alanChecking = lovelaceBank.openCheckingAccount(alan.getId(), 1500.00, 300.00);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING POLYMORPHISM ====================
        System.out.println("--- Demonstrating Polymorphism: Same Reference Type, Different Behaviors ---");
        
        System.out.println("\nPolymorphic references - all are 'Account' type:");
        Account acc1 = adaSavings;      // Parent reference to child object
        Account acc2 = adaChecking;     // Parent reference to child object
        Account acc3 = alanSavings;     // Parent reference to child object
        
        System.out.println("acc1 (SavingsAccount):  " + acc1.getAccountType() + " #" + acc1.getAccountId());
        System.out.println("acc2 (CheckingAccount): " + acc2.getAccountType() + " #" + acc2.getAccountId());
        System.out.println("acc3 (SavingsAccount):  " + acc3.getAccountType() + " #" + acc3.getAccountId());
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING METHOD OVERRIDING ====================
        System.out.println("--- Demonstrating Method Overriding: Different withdraw() Behaviors ---");
        
        System.out.println("\n[Test 1: SavingsAccount - Minimum Balance Enforcement]");
        System.out.println("Ada's savings has €5000, minimum balance is €100");
        System.out.println("Trying to withdraw €4950 (would leave €50, below minimum):");
        lovelaceBank.withdraw(ada.getId(), adaSavings.getAccountId(), 4950.00);  // Should fail
        
        System.out.println("\nTrying to withdraw €4850 (would leave €150, above minimum):");
        lovelaceBank.withdraw(ada.getId(), adaSavings.getAccountId(), 4850.00);  // Should succeed
        
        System.out.println("\n[Test 2: CheckingAccount - Overdraft Protection]");
        System.out.println("Alan's checking has €1500, overdraft limit is €300");
        System.out.println("Trying to withdraw €1700 (would use €200 overdraft):");
        lovelaceBank.withdraw(alan.getId(), alanChecking.getAccountId(), 1700.00);  // Should succeed
        
        System.out.println("\nTrying to withdraw €200 more (would exceed overdraft limit):");
        lovelaceBank.withdraw(alan.getId(), alanChecking.getAccountId(), 200.00);  // Should fail
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING SUBCLASS-SPECIFIC METHODS ====================
        System.out.println("--- Demonstrating Subclass-Specific Methods ---");
        
        System.out.println("\n[SavingsAccount: Adding Interest]");
        System.out.println("Ada's savings balance before interest: €" + 
                         String.format("%.2f", adaSavings.getBalance()));
        double interest = adaSavings.calculateInterest();
        System.out.println("Interest calculated: €" + String.format("%.2f", interest));
        adaSavings.addInterest();
        System.out.println("Balance after interest: €" + String.format("%.2f", adaSavings.getBalance()));
        
        System.out.println("\n[CheckingAccount: Applying Monthly Fee]");
        System.out.println("Alan's checking balance before fee: €" + 
                         String.format("%.2f", alanChecking.getBalance()));
        System.out.println("Monthly fee: €" + alanChecking.getMonthlyFee());
        alanChecking.applyMonthlyFee();
        System.out.println("Balance after fee: €" + String.format("%.2f", alanChecking.getBalance()));
        System.out.println("Is in overdraft? " + alanChecking.isInOverdraft());
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING POLYMORPHIC COLLECTIONS ====================
        System.out.println("--- Demonstrating Polymorphic Collections ---");
        
        System.out.println("\nIterating through Ada's accounts (polymorphic collection):");
        for (Account account : ada.getAccounts()) {
            // Polymorphic call - each account type displays differently
            System.out.println("  " + account);
            
            // Different behavior based on actual type at runtime
            System.out.println("    Depositing €100...");
            account.deposit(100);  // Polymorphic method call
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING INSTANCEOF AND DOWNCASTING ====================
        System.out.println("--- Demonstrating instanceof and Type Casting ---");
        
        System.out.println("\nChecking account types for Ada:");
        for (Account account : ada.getAccounts()) {
            System.out.println("\nAccount #" + account.getAccountId() + ":");
            
            if (account instanceof SavingsAccount) {
                System.out.println("  Type: Savings Account");
                SavingsAccount savings = (SavingsAccount) account;  // Downcast
                System.out.println("  Interest Rate: " + (savings.getInterestRate() * 100) + "%");
                System.out.println("  Minimum Balance: €" + savings.getMinimumBalance());
            } else if (account instanceof CheckingAccount) {
                System.out.println("  Type: Checking Account");
                CheckingAccount checking = (CheckingAccount) account;  // Downcast
                System.out.println("  Overdraft Limit: €" + checking.getOverdraftLimit());
                System.out.println("  Monthly Fee: €" + checking.getMonthlyFee());
                System.out.println("  Available Balance: €" + checking.getAvailableBalance());
            } else {
                System.out.println("  Type: Standard Account");
            }
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== BANK-WIDE POLYMORPHIC OPERATIONS ====================
        System.out.println("--- Bank-Wide Polymorphic Operations ---");
        
        System.out.println("\nApplying interest to all savings accounts:");
        lovelaceBank.applyInterestToAllSavings();
        
        System.out.println("\nApplying fees to all checking accounts:");
        lovelaceBank.applyMonthlyFeesToAllChecking();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DEMONSTRATING POLYMORPHIC TRANSFERS ====================
        System.out.println("--- Demonstrating Transfers Between Different Account Types ---");
        
        System.out.println("\nTransfer from Ada's savings to Alan's checking:");
        lovelaceBank.transfer(
            ada.getId(), adaSavings.getAccountId(),
            alan.getId(), alanChecking.getAccountId(),
            500.00
        );
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== TRANSACTION HISTORY ====================
        System.out.println("--- Viewing Transaction History (Inherited Method) ---");
        
        System.out.println("\nAda's Savings Account History:");
        adaSavings.displayTransactionHistory();
        
        System.out.println("\nAlan's Checking Account History:");
        alanChecking.displayTransactionHistory();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== COMPREHENSIVE BANK REPORT ====================
        System.out.println("--- Comprehensive Bank Report with Account Types ---");
        lovelaceBank.generateBankReport();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== DISPLAY ALL CUSTOMERS ====================
        lovelaceBank.displayAllCustomers();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // ==================== FINAL SUMMARY ====================
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║              Version 4: Key Inheritance & Polymorphism Concepts   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("✓ Inheritance (IS-A Relationship)");
        System.out.println("  → SavingsAccount IS-A Account");
        System.out.println("  → CheckingAccount IS-A Account");
        System.out.println("  → Both inherit all Account functionality");
        System.out.println();
        System.out.println("✓ Constructor Chaining with super()");
        System.out.println("  → Subclass constructors call parent constructor");
        System.out.println("  → Ensures proper initialization of inherited fields");
        System.out.println();
        System.out.println("✓ Method Overriding");
        System.out.println("  → SavingsAccount.withdraw() enforces minimum balance");
        System.out.println("  → CheckingAccount.withdraw() allows overdraft");
        System.out.println("  → Same method signature, different behavior");
        System.out.println();
        System.out.println("✓ Polymorphism (Parent Reference to Child Object)");
        System.out.println("  → Account reference can hold SavingsAccount or CheckingAccount");
        System.out.println("  → Method calls resolved at runtime based on actual object type");
        System.out.println("  → Enables flexible, extensible code");
        System.out.println();
        System.out.println("✓ Protected Access Modifier");
        System.out.println("  → balance and transactionHistory accessible to subclasses");
        System.out.println("  → Allows subclasses to work with inherited data");
        System.out.println();
        System.out.println("✓ instanceof and Type Casting");
        System.out.println("  → Runtime type checking with instanceof");
        System.out.println("  → Safe downcasting to access subclass-specific methods");
        System.out.println("  → Used in Bank's interest and fee operations");
        System.out.println();
        System.out.println("✓ Polymorphic Collections");
        System.out.println("  → ArrayList<Account> can hold any Account subtype");
        System.out.println("  → Enables uniform handling of different account types");
        System.out.println("  → Code works with current and future account types");
        System.out.println();
        System.out.println("✓ Code Reuse");
        System.out.println("  → Transaction history, freeze, equals/hashCode all inherited");
        System.out.println("  → No need to rewrite common functionality");
        System.out.println("  → Subclasses add only what's unique to them");
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     Ready for Version 5: Abstract Classes & Interfaces!           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
    }
}
