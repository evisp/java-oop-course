# Inheritance and Polymorphism: Extending Behavior Through Hierarchy

## Introduction - When One Account Type Isn't Enough

In Version 3, you built a banking system where every account behaved identically. The `Account` class handled deposits, withdrawals, transaction history, and frozen status. It worked perfectly, but only for generic accounts. But real banks don't offer just one account type. They offer savings accounts with interest, checking accounts with overdraft protection, and business accounts with different fee structures.

Consider what happens when your bank wants to add interest to savings accounts. Where would you put this functionality? You could add an `interestRate` field to `Account` and an `addInterest()` method, but then every account, that means including checking accounts that don't earn interest, would carry that unused field. You'd need boolean flags to track account types and conditional logic everywhere:

```java
// Fragile approach with conditionals
public class Account {
    private String accountType;  // "savings", "checking", etc.
    private double interestRate;  // Only for savings
    private double overdraftLimit;  // Only for checking
    
    public void addInterest() {
        if (accountType.equals("savings")) {
            // Apply interest
        } else {
            System.out.println("This account doesn't earn interest");
        }
    }
}
```

This approach fails quickly. Every new account type adds more fields and more conditionals. The `Account` class becomes bloated with special cases, testing becomes complex, and bugs multiply. When you need to change how savings accounts work, you risk breaking checking accounts because they're tangled in the same class.

**This tutorial introduces inheritance and polymorphism to model different account types cleanly.** You'll learn how subclasses extend parent functionality, override methods to customize behavior, and how polymorphism lets the Bank work with any account type without knowing specifics. Most importantly, you'll see how the "`is-a`" relationship creates hierarchies that mirror real-world categories.

### The Account Hierarchy Problem

Your bank needs three account types with different rules:

1. **Standard Account**: Basic functionality: deposits, withdrawals, transaction history (your Version 3 Account)
2. **Savings Account**: Earns interest, requires minimum balance, can't go below minimum
3. **Checking Account**: Allows overdraft (negative balance within limit), charges monthly fee

> These accounts share common features (balance, transactions, freeze status) but have specialized rules. 

Duplicating the common code across three separate classes creates maintenance nightmares. When you fix a bug in transaction recording, you'd need to fix it three times. When you add transaction history features, you'd implement them three times.

**Inheritance solves this by creating a hierarchy**: define common functionality once in a parent class, then create specialized child classes that inherit the common behavior and add their own unique features.

### The "Is-A" Relationship

> Inheritance models the "is-a" relationship. 

A savings account **is an** account. A checking account **is an** account. They're specialized types of the general account concept. This contrasts with composition's "has-a" relationship from Version 3: a Customer **has** accounts, a Bank **has** customers.

When thinking about inheritance, ask: "Is this a specialized type of that?" A `SavingsAccount` **is-a** specialized `Account` with extra rules. A `Dog` **is-a** `Animal`. A `Car` **is-a** `Vehicle`. If the relationship is **"is-a"**, inheritance might fit. If it's **"has-a"**, use composition instead.

## Preparing the Parent Class

Before creating subclasses, the `Account` class needs changes to support inheritance. Currently, subclasses can't access the fields they need to customize behavior.

### The Protected Access Modifier

Version 3's `Account` declared all fields `private`:

```java
// Version 3 - subclasses can't access these
private double balance;
private ArrayList<Transaction> transactionHistory;
```

Private fields are invisible to subclasses. When `SavingsAccount` tries to check the balance in its overridden `withdraw()` method, it can't; `balance` is hidden by the parent. You need a middle ground between private (invisible to everyone) and public (visible to everyone).

The `protected` modifier provides this:

```java
// Version 4 - subclasses can access these
protected double balance;
protected ArrayList<Transaction> transactionHistory;
```

Protected fields are visible to:

- The class itself (like private)
- Subclasses (unlike private)
- Classes in the same package (a side effect)

This lets `SavingsAccount` and `CheckingAccount` read and modify `balance` while keeping it hidden from external code like `Customer` or `Bank`. External code still uses `getBalance()`, but subclasses can work with the field directly when overriding behavior.

### Why Not Public?

> Making fields public breaks encapsulation. 

Any code anywhere could modify `balance` without validation, bypassing your carefully designed deposit/withdraw logic. Protected maintains control: only your account hierarchy can directly access the field, and you control what goes into that hierarchy.


## Creating the SavingsAccount Subclass

The `SavingsAccount` class demonstrates the core concepts of inheritance: extending a parent class, calling parent constructors, overriding methods, and adding specialized functionality.

### Declaring the Inheritance Relationship

The `extends` keyword establishes inheritance:

```java
public class SavingsAccount extends Account {
    // SavingsAccount inherits all public/protected members from Account
}
```

This single keyword gives `SavingsAccount` immediate access to all Account's public and protected methods and fields. You don't rewrite `deposit()`, `getBalance()`, `freeze()`, or transaction history management; they're inherited automatically. `SavingsAccount` starts with everything `Account` has, then adds or customizes behavior.

### Adding Specialized Fields

Savings accounts need fields for their unique rules:

```java
private double interestRate;        // Annual rate (e.g., 0.03 for 3%)
private double minimumBalance;      // Minimum required balance

private static final double DEFAULT_MINIMUM_BALANCE = 100.0;
```

These fields exist only in `SavingsAccount`. Standard accounts and checking accounts don't have them. Each subclass adds only what it needs for its specialization.

### Constructor Chaining with super()

The constructor demonstrates how subclasses initialize inherited fields:

```java
public SavingsAccount(double initialBalance, double interestRate) {
    super(initialBalance);  // Call parent Account constructor
    
    if (interestRate < 0 || interestRate > 1) {
        System.out.println("Interest rate must be between 0 and 1");
    }
    
    this.interestRate = interestRate;
    this.minimumBalance = DEFAULT_MINIMUM_BALANCE;
}
```

The `super(initialBalance)` call invokes the parent `Account` constructor, which sets up the account ID, balance, transaction history, and frozen status. 

> This must be the first line in your constructor 

The parent must be initialized before you add subclass-specific initialization.

After `super()` returns, the subclass constructor continues, validating and setting fields unique to savings accounts. This pattern ensures proper initialization: shared state gets set up by the parent, specialized state by the child.

There's also an overloaded constructor accepting a custom minimum balance:

```java
public SavingsAccount(double initialBalance, double interestRate, 
                      double minimumBalance) {
    super(initialBalance);
    
    if (interestRate < 0 || interestRate > 1) {
        System.out.println("Interest rate must be between 0 and 1");
    }
    
    if (minimumBalance < 0) {
        System.out.println("Minimum balance cannot be negative");
    }
    
    this.interestRate = interestRate;
    this.minimumBalance = minimumBalance;
}
```

This provides flexibility: use the default minimum or specify a custom one.

### Overriding withdraw() for Specialized Behavior

The power of inheritance appears in method overriding. Savings accounts can't withdraw below the minimum balance, but the parent `Account.withdraw()` allows any withdrawal up to the balance. The solution: override `withdraw()` with specialized logic:

```java
@Override
public boolean withdraw(double amount) {
    if (isFrozen()) {
        System.out.println("Cannot withdraw. Account #" + getAccountId() + " is frozen.");
        return false;
    }
        
    if (amount <= 0) {
        System.out.println("Withdrawal amount must be positive.");
        return false;
    }
        
    // Savings account specific check: cannot go below minimum balance
    if (balance - amount < minimumBalance) {
        System.out.println("Cannot withdraw €" + amount + ". Would violate minimum balance of €" + minimumBalance + ". Current balance: €" + balance);
        return false;
    }
        
    // If all checks pass, use parent's withdraw logic
    return super.withdraw(amount);
}
```

The `@Override` annotation isn't required but is strongly recommended. It tells the compiler "I intend to override a parent method." If you make a typo in the method name or get the parameters wrong, the compiler catches the error instead of creating a new unrelated method.

Notice the method checks `balance - amount < minimumBalance` before allowing the withdrawal. This enforcement exists only in `SavingsAccount`; standard accounts and checking accounts don't have this restriction. Each subclass customizes behavior by overriding methods.

### Adding Subclass-Specific Methods

Savings accounts have functionality that doesn't exist in the parent:

```java
/**
 * Calculates interest earned on current balance.
 */
public double calculateInterest() {
    return balance * interestRate;
}

/**
 * Adds calculated interest to the account.
 */
public double addInterest() {
    double interest = calculateInterest();
    if (interest > 0) {
        balance += interest;
            
        // Record as a deposit transaction
        Transaction transaction = new Transaction(
                Transaction.TransactionType.DEPOSIT,
                interest,
                getAccountId(),
                balance,
                "Interest credited at " + (interestRate * 100) + "%"
        );
        transactionHistory.add(transaction);
            
        System.out.println("Interest of €" + String.format("%.2f", interest) + 
                             " added to Savings Account #" + getAccountId());
    }
    return interest;
}
```
The subclass leverages parent functionality while adding new capabilities.

### Overriding getAccountType() and toString()

To complete the subclass, override identification methods:

```java
@Override
public String getAccountType() {
    return "Savings Account";
}

@Override
public String toString() {
    return String.format("SavingsAccount{id=%d, balance=€%.2f, interestRate=%.2f%%, " +
                        "minimumBalance=€%.2f, frozen=%b}",
                        getAccountId(), balance, interestRate * 100, 
                        minimumBalance, isFrozen());
}
```

These methods replace the parent implementations, allowing polymorphic code to identify and display savings accounts correctly.

## Creating the CheckingAccount Subclass

`CheckingAccount` demonstrates different specialization: allowing overdrafts and charging fees. The structure mirrors `SavingsAccount` but the rules differ.

### Specialized Fields for Checking Accounts

```java
private double overdraftLimit;   // How much can go below zero
private double monthlyFee;       // Fee charged monthly

private static final double DEFAULT_MONTHLY_FEE = 5.0;
```

Checking accounts allow negative balances within a limit, and they charge recurring fees. These fields support those rules.

### Constructor Chaining

```java
public CheckingAccount(double initialBalance, double overdraftLimit) {
    super(initialBalance);  // Initialize parent Account
    
    if (overdraftLimit < 0) {
        System.out.println("Overdraft limit cannot be negative");
    }
    
    this.overdraftLimit = overdraftLimit;
    this.monthlyFee = DEFAULT_MONTHLY_FEE;
}
```

Same pattern as `SavingsAccount`: call `super()` first to initialize inherited fields, then handle subclass-specific fields. An overloaded constructor allows custom monthly fees.

### Overriding withdraw() for Overdraft Support

The key difference from standard accounts: checking accounts can go negative:

```java
@Override
public boolean withdraw(double amount) {
    if (isFrozen()) {
        System.out.println("Cannot withdraw. Account #" + getAccountId() + " is frozen.");
        return false;
    }
        
    if (amount <= 0) {
        System.out.println("Withdrawal amount must be positive.");
        return false;
    }
        
    // Checking account specific check: can go negative up to overdraft limit
    double availableBalance = balance + overdraftLimit;
    if (amount > availableBalance) {
        System.out.println("Cannot withdraw €" + amount + ". Exceeds available balance of €" + String.format("%.2f", availableBalance) + 
        " (Balance: €" + String.format("%.2f", balance) + 
         " + Overdraft: €" + overdraftLimit + ")");
        return false;
    }
        
    // Perform withdrawal (may result in negative balance)
    balance -= amount;
        
    // Record transaction
    Transaction transaction = new Transaction(
            Transaction.TransactionType.WITHDRAW,
            amount,
            getAccountId(),
            balance,
            "Withdrawal" + (balance < 0 ? " (using overdraft)" : "")
    );
    transactionHistory.add(transaction);
        
    System.out.println("Withdrawn: €" + amount + " from Checking Account #" + getAccountId());
    if (balance < 0) {
        System.out.println("  (Account now in overdraft. Balance: €" + String.format("%.2f", balance) + ")");
    }
        
    return true;
}
```

The available balance includes the overdraft limit: `balance + overdraftLimit`. If your balance is €100 and overdraft limit is €500, you can withdraw up to €600. The balance can go to -€500.

This demonstrates polymorphism's power: three account types, three different `withdraw()` implementations, but all share the same method signature. Code using accounts doesn't need to know which type it's working with—it just calls `withdraw()` and gets the right behavior.

### Fee Management

Checking accounts need fee application:

```java
/**
 * Applies monthly maintenance fee to the account.
 */
public void applyMonthlyFee() {
    if (monthlyFee > 0) {
        balance -= monthlyFee;
        recordTransaction(TransactionType.WITHDRAW, monthlyFee, "Monthly maintenance fee");
        System.out.println("Monthly fee of €" + monthlyFee + " applied to Account #" + 
                          getAccountId());
    }
}
```

This method directly modifies the balance and records a transaction. It demonstrates how subclasses access protected parent fields to implement specialized functionality.

Utility methods provide checking account information:

```java
public boolean isInOverdraft() {
    return balance < 0;
}

public double getAvailableBalance() {
    return balance + overdraftLimit;
}
```

These methods exist only in `CheckingAccount`. Code working with generic `Account` references can't call them unless it uses type casting (covered later).

## Understanding Polymorphism

With subclasses defined, polymorphism becomes possible. This is where inheritance shows its real power: writing code that works with parent types but executes child behavior.

### Parent References to Child Objects

Consider this code:

```java
Account account1 = new SavingsAccount(1000.0, 0.03);
Account account2 = new CheckingAccount(500.0, 300.0);
Account account3 = new Account(750.0);
```

All three variables have type `Account`, but they reference different object types. This is legal because of the "is-a" relationship: a `SavingsAccount` is an `Account`, so you can store a `SavingsAccount` object in an `Account` variable.

This is **polymorphism**: one interface (the parent type), multiple implementations (the child types). The variable type is `Account`, but the actual object can be any Account subclass.

### Method Dispatch at Runtime

When you call methods on polymorphic references, Java determines which version to execute at runtime based on the actual object type:

```java
account1.withdraw(100.0);  // Calls SavingsAccount.withdraw()
account2.withdraw(100.0);  // Calls CheckingAccount.withdraw()
account3.withdraw(100.0);  // Calls Account.withdraw()
```

Even though all three variables have type `Account`, each calls its own `withdraw()` implementation. This is **dynamic dispatch**: the method called depends on the runtime object type, not the compile-time variable type.

This enables powerful abstractions. The Bank can store all accounts in one `ArrayList<Account>`, and when it calls methods, each account behaves according to its actual type without the Bank needing conditional logic:

```java
// Bank doesn't need to know account types
for (Account account : customer.getAccounts()) {
    account.withdraw(50.0);  // Right behavior for each type
}
```

### The Limits of Polymorphic References

While polymorphic references enable flexible code, they have a restriction: you can only call methods defined in the reference type. Consider:

```java
Account account = new SavingsAccount(1000.0, 0.03);

account.deposit(100.0);        // OK - defined in Account
account.withdraw(50.0);        // OK - defined in Account (calls SavingsAccount version)
account.getBalance();          // OK - defined in Account

account.addInterest();         // COMPILER ERROR - not defined in Account
```

The compiler sees the variable as type `Account`, so it only allows methods declared in `Account`. Even though the actual object is a `SavingsAccount` with an `addInterest()` method, you can't call it through an `Account` reference.

This makes sense from the compiler's perspective: if `account` could reference any Account subclass, and not all subclasses have `addInterest()`, allowing the call would be unsafe. What if `account` actually referenced a `CheckingAccount` at runtime?

### Type Checking with instanceof

To safely call subclass-specific methods, first check the actual type using `instanceof`:

```java
Account account = new SavingsAccount(1000.0, 0.03);

if (account instanceof SavingsAccount) {
    SavingsAccount savingsAccount = (SavingsAccount) account;
    savingsAccount.addInterest();  // Safe - we know it's a SavingsAccount
}
```

The `instanceof` operator returns `true` if the object is of the specified type (or a subclass of that type). After confirming the type, you cast the reference to the specific type, making subclass methods accessible.

Java 16+ introduced pattern matching that combines the check and cast:

```java
if (account instanceof SavingsAccount sa) {
    sa.addInterest();  // 'sa' is automatically cast
}
```

This is cleaner and eliminates the explicit cast. The variable `sa` is automatically the correct type within the if block.

### Why This Matters

Polymorphism enables:

- **Flexible collections**: Store different account types in one list
- **Extensible code**: Add new account types without changing existing code
- **Interface-based programming**: Work with abstractions (Account) instead of concrete types
- **Runtime behavior selection**: The right method executes based on actual object type

This is fundamental to OOP design. Systems built with polymorphism adapt to new requirements more easily than systems built with conditionals and type checking.

## Extending the Bank Class

The Bank class needs updates to support multiple account types. The changes demonstrate polymorphism in practice: storing different account types together while providing type-specific operations.

### Opening Different Account Types

Version 3's Bank couldn't distinguish account types. Version 4 adds methods to create specific account types:

```java
/**
 * Opens a new savings account for a customer.
 */
public SavingsAccount openSavingsAccount(int customerId, double initialBalance, 
                                         double interestRate) {
    Customer customer = findCustomer(customerId);
    if (customer == null) {
        System.out.println("Customer with ID " + customerId + " not found.");
        return null;
    }
    
    SavingsAccount account = new SavingsAccount(initialBalance, interestRate);
    customer.addAccount(account);
    System.out.println("Savings account #" + account.getAccountId() + 
                      " opened for " + customer.getName());
    return account;
}

/**
 * Opens a new checking account for a customer.
 */
public CheckingAccount openCheckingAccount(int customerId, double initialBalance, 
                                          double overdraftLimit) {
    Customer customer = findCustomer(customerId);
    if (customer == null) {
        System.out.println("Customer with ID " + customerId + " not found.");
        return null;
    }
    
    CheckingAccount account = new CheckingAccount(initialBalance, overdraftLimit);
    customer.addAccount(account);
    System.out.println("Checking account #" + account.getAccountId() + 
                      " opened for " + customer.getName());
    return account;
}
```

These methods return specific types (`SavingsAccount`, `CheckingAccount`) so callers can immediately use type-specific methods. But internally, `Customer.addAccount()` stores them in `ArrayList<Account>`, demonstrating polymorphic storage.

### Bank-Wide Operations Using instanceof

Some operations apply only to specific account types. The Bank needs to iterate through all accounts and process only certain types:

```java
/**
 * Applies interest to all savings accounts in the bank.
 */
public void applyInterestToAllSavings() {
    int count = 0;
    for (Customer customer : customers) {
        for (Account account : customer.getAccounts()) {
            if (account instanceof SavingsAccount) {
                SavingsAccount sa = (SavingsAccount) account;
                sa.addInterest();
                count++;
            }
        }
    }
    System.out.println("Interest applied to " + count + " savings accounts.");
}
```

This method demonstrates polymorphic iteration with type-specific operations. The inner loop iterates over `Account` references (polymorphic collection), uses `instanceof` to identify savings accounts, casts to the specific type, and calls the subclass-specific `addInterest()` method.

Monthly fee processing follows the same pattern:

```java
/**
 * Applies monthly fees to all checking accounts in the bank.
 */
public void applyMonthlyFeesToAllChecking() {
    int count = 0;
    for (Customer customer : customers) {
        for (Account account : customer.getAccounts()) {
            if (account instanceof CheckingAccount) {
                CheckingAccount ca = (CheckingAccount) account;
                ca.applyMonthlyFee();
                count++;
            }
        }
    }
    System.out.println("Monthly fees applied to " + count + " checking accounts.");
}
```

### Improved Bank Reporting

The `generateBankReport()` method now provides type-specific statistics:

```java
public void generateBankReport() {
    System.out.println("\n" + "=".repeat(60));
    System.out.println(bankName + " - Bank Report");
    System.out.println("=".repeat(60));
    
    int totalAccounts = 0;
    int savingsCount = 0;
    int checkingCount = 0;
    int standardCount = 0;
    double totalBalance = 0.0;
    
    for (Customer customer : customers) {
        List<Account> accounts = customer.getAccounts();
        totalAccounts += accounts.size();
        
        for (Account account : accounts) {
            totalBalance += account.getBalance();
            
            // Count by type using instanceof
            if (account instanceof SavingsAccount) {
                savingsCount++;
            } else if (account instanceof CheckingAccount) {
                checkingCount++;
            } else {
                standardCount++;
            }
        }
    }
    
    System.out.println("Total Customers: " + customers.size());
    System.out.println("Total Accounts: " + totalAccounts);
    System.out.println("  - Savings Accounts: " + savingsCount);
    System.out.println("  - Checking Accounts: " + checkingCount);
    System.out.println("  - Standard Accounts: " + standardCount);
    System.out.println("Total Bank Balance: €" + String.format("%.2f", totalBalance));
    System.out.println("=".repeat(60));
}
```

The method uses `instanceof` to categorize accounts by type. This demonstrates practical polymorphism: iterate with parent type references, use `instanceof` when type-specific logic is needed.

## Putting It All Together

With the inheritance hierarchy complete, here's how the system works in practice. This demonstrates the interplay between polymorphism, method overriding, and type-specific operations.

### Creating Polymorphic Accounts

```java
public class BankSystemMain {
    public static void main(String[] args) {
        Bank turingBank = new Bank("Turing National Bank");
        
        // Create customers
        Customer ada = new Customer(1815, "Ada Lovelace", 36, 
                                   "10 St. James Square, London");
        Customer alan = new Customer(1912, "Alan Turing", 41, 
                                    "Sherborne School, Dorset");
        
        turingBank.addCustomer(ada);
        turingBank.addCustomer(alan);
        
        // Open different account types
        SavingsAccount adaSavings = turingBank.openSavingsAccount(1815, 5000.0, 0.03);
        CheckingAccount adaChecking = turingBank.openCheckingAccount(1815, 2000.0, 500.0);
        
        SavingsAccount alanSavings = turingBank.openSavingsAccount(1912, 3000.0, 0.025);
        CheckingAccount alanChecking = turingBank.openCheckingAccount(1912, 1500.0, 300.0);
    }
}
```

Output:
```
Customer Ada Lovelace (ID: 1815) added to Turing National Bank
Customer Alan Turing (ID: 1912) added to Turing National Bank
Savings account #1001 opened for Ada Lovelace
Checking account #1002 opened for Ada Lovelace
Savings account #1003 opened for Alan Turing
Checking account #1004 opened for Alan Turing
```

### Demonstrating Method Overriding

Each account type behaves differently:

```java
// Savings account enforces minimum balance
adaSavings.withdraw(4950.0);  // Denied - would violate €100 minimum

// Checking account allows overdraft
adaChecking.withdraw(2300.0);  // Allowed - goes to -€300 (within -€500 limit)

// Standard behavior
adaSavings.deposit(1000.0);  // Works for all types
```

Output:
```
Withdrawal denied. Would violate minimum balance of €100.0
Withdrew: €2300.0 from Checking Account #1002 (now in overdraft: €-300.0)
Deposited: €1000.0 to Account #1001
```

The same method names (`withdraw`, `deposit`) execute different logic based on actual object type.

### Polymorphic Collections in Action

Customer stores all account types in one list:

```java
// Inside Customer class
private ArrayList<Account> accounts;  // Holds any Account subclass

// Later
for (Account account : ada.getAccounts()) {
    System.out.println(account.getAccountType() + " #" + account.getAccountId() + 
                      ": €" + account.getBalance());
}
```

Output:
```
Savings Account #1001: €6000.0
Checking Account #1002: €-300.0
```

Each account reports its correct type even though they're all stored as `Account` references.

### Type-Specific Operations

Apply interest only to savings accounts:

```java
turingBank.applyInterestToAllSavings();
```

Output:
```
Interest added: €180.00 to Account #1001
Interest added: €75.00 to Account #1003
Interest applied to 2 savings accounts.
```

Checking accounts aren't affected—the Bank identifies savings accounts using `instanceof` and applies interest only to them.

Apply fees only to checking accounts:

```java
turingBank.applyMonthlyFeesToAllChecking();
```

Output:
```
Monthly fee of €5.0 applied to Account #1002
Monthly fee of €5.0 applied to Account #1004
Monthly fees applied to 2 checking accounts.
```

### Working with Specific Types

When you need subclass-specific functionality:

```java
// Get reference as specific type
SavingsAccount savings = turingBank.openSavingsAccount(1815, 10000.0, 0.04);

// Can call subclass methods directly
double interest = savings.calculateInterest();
System.out.println("Interest to be earned: €" + interest);

savings.addInterest();

// Or use instanceof and casting
Account account = ada.findAccount(1001);
if (account instanceof SavingsAccount) {
    SavingsAccount sa = (SavingsAccount) account;
    System.out.println("Interest rate: " + (sa.getInterestRate() * 100) + "%");
}
```

### Bank Report with Type Statistics

```java
turingBank.generateBankReport();
```

Output:
```
============================================================
Turing National Bank - Bank Report
============================================================
Total Customers: 2
Total Accounts: 4
  - Savings Accounts: 2
  - Checking Accounts: 2
  - Standard Accounts: 0
Total Bank Balance: €15775.00
============================================================
```

The Bank processes all accounts polymorphically while tracking type-specific statistics.

## Summary

You've extended the banking system with inheritance and polymorphism, enabling specialized account types while maintaining clean, maintainable code. The hierarchy creates flexibility impossible with a single Account class.

### Key Concepts Mastered

#### Inheritance and the "is-a" relationship
Subclasses extend parent classes, inheriting all public and protected members. SavingsAccount is-a Account, CheckingAccount is-a Account. Use inheritance when objects share common behavior but need specialized rules.

#### The protected access modifier
Protected fields and methods are visible to subclasses but hidden from external code. This enables subclasses to access parent state (like `balance`) while maintaining encapsulation from external manipulation.

#### Constructor chaining with super()
Subclass constructors must call a parent constructor using `super()` as the first statement. This ensures proper initialization of inherited fields before subclass-specific initialization.

#### Method overriding
Subclasses can replace parent method implementations to customize behavior. Use `@Override` to catch errors. Overridden methods must have identical signatures to the parent method they replace.

#### Polymorphism
Parent type references can hold child type objects. Method calls execute the actual object's version (dynamic dispatch), not the reference type's version. This enables flexible collections and extensible designs.

#### instanceof and type casting
Check actual object types with `instanceof` before calling subclass-specific methods. Cast references to access subclass functionality. Pattern matching (Java 16+) combines the check and cast.

#### Polymorphic collections
Store different types in one collection using the parent type: `ArrayList<Account>` holds SavingsAccount, CheckingAccount, and Account objects. Iterate once, get specialized behavior automatically.

### Design Benefits

The inheritance hierarchy provides:

- **Code reuse**: Common functionality (deposits, transactions, freezing) exists once in Account
- **Extensibility**: Add new account types (BusinessAccount, StudentAccount) without changing existing code
- **Maintainability**: Fix bugs once in the parent; all subclasses benefit
- **Flexibility**: Bank operations work with any account type without conditionals
- **Type safety**: Compiler ensures only valid operations occur on each type

### When to Use Inheritance

Inheritance fits when:
- Objects have genuine "is-a" relationships (not just code reuse)
- Subclasses share significant common behavior
- You need polymorphic behavior (treating different types uniformly)
- The hierarchy models real-world categories

Inheritance doesn't fit when:
- The relationship is "has-a" (use composition)
- Subclasses would override most parent methods (weak common behavior)
- You're inheriting just to reuse code (prefer composition)

### Complete Code

Find the complete working code:

- [v3_thinking_in_objects](github.com/evisp/java-oop-course/v3_thinking_in_objects) - Version 3 with Bank, Transaction, and SRP
- [v4_inheritance_polymorphism](github.com/evisp/java-oop-course/v4_inheritance_polymorphism) - Version 4 with Account hierarchy

The Account hierarchy creates a foundation for future extensions. Next, you'll explore abstract classes and interfaces: defining contracts that subclasses must fulfill, creating pure abstractions without implementation, and designing systems around interfaces instead of concrete types. The inheritance structure makes these advanced concepts natural progressions.