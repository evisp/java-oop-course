# Thinking in Objects: Design Principles and Refactoring

## Introduction - When Working Code Isn't Enough

Last week you built a functional banking system where customers managed multiple accounts using `ArrayList`. The code worked perfectly: customers could open accounts, deposit money, withdraw funds, and track total balances. But as you stared at the `Customer` class, something felt wrong.

Consider what happens when a customer wants to transfer money to another customer's account. Your current design can't handle this naturally. The `Customer` class would need to know about other customers, search through another customer's accounts, and coordinate the withdrawal-deposit sequence. This violates a fundamental principle

> **objects should have one clear responsibility**.

The problem runs deeper. Your `Customer` class is juggling three distinct jobs: managing personal information (name, age, address), organizing the customer's account collection, and executing financial operations (deposits, withdrawals). Change any of these requirements and you risk breaking the others. The class has **too many reasons to change** - the classic sign of poor design.

**This tutorial refactors your banking system using design principles that separate concerns cleanly.** You'll learn the **Single Responsibility Principle** through practical refactoring, discover why immutable objects prevent bugs, and understand when to use `enums` instead of primitive types. Most importantly, you'll see how introducing new classes (Transaction and Bank) makes the system easier to understand, test, and extend.


### Three Jobs in One Class

Look at the responsibilities hidden in your current `Customer` implementation:

```java
public class Customer {
    // Job 1: Store personal information
    private int id;
    private String name;
    private int age;
    private String address;
    
    // Job 2: Manage account collection
    private ArrayList<Account> accounts;
    
    // Job 3: Execute financial operations
    public boolean deposit(int accountId, double amount) { ... }
    public boolean withdraw(int accountId, double amount) { ... }
}
```

Each job represents a different **reason to change**. If customer information requirements change (add email, phone number), you touch this class. If account management rules change (limit number of accounts, add account types), you touch this class. If transaction processing changes (add validation, logging, security checks), you touch this class again.

### The Transfer Money Problem

The design breaks down completely when you try to implement transfers between customers:

```java
// How would you implement this?
public boolean transferTo(Customer otherCustomer, int fromAccountId, 
                          int toAccountId, double amount) {
    // Customer would need to access another customer's accounts
    // Who coordinates the withdrawal and deposit?
    // What if withdrawal succeeds but deposit fails?
    // This doesn't feel right...
}
```

There's no good place to put this method. If you put it in `Customer`, then customers know about other customers' internal structure. If you put it in `Account`, accounts would need to know about other accounts and customers. The design is fighting you because the responsibilities are tangled.

### Single Responsibility Principle (SRP)

> The Single Responsibility Principle states: A class should have only one reason to change.

When a class has multiple responsibilities, changes to one responsibility affect code handling the others. Testing becomes difficult because you can't test account management without also setting up transaction processing. Understanding the class requires learning everything it does instead of focusing on one clear purpose.

The solution is **separation of concerns**: split the responsibilities across multiple focused classes, each with a single, well-defined job.

## Introducing the Transaction Class

Before refactoring `Customer`, we need a new concept: the transaction itself. Real banks don't just move money around. Instead, they create permanent records of every operation. Your system needs the same capability.

### Why Transactions Need Their Own Class

Currently, when you deposit money, this happens:

```java
// Version 2 approach
public boolean deposit(int accountId, double amount) {
    Account account = findAccount(accountId);
    if (account != null) {
        return account.deposit(amount);  // Money moves, but no record kept
    }
    return false;
}
```

Money moves, the balance updates, but no history exists. If a customer disputes a charge, or you need to audit accounts, or the system crashes mid-transaction, you have no record of what happened. Professional banking systems maintain complete audit trails.

> A transaction is a **value object**; it represents data that describes a specific event. Once created, it never changes.

### Understanding Immutability

Immutable objects cannot be modified after creation. Every field is `final`, and there are no setter methods. For transactions, immutability is crucial:

```java
public class Transaction {
    // All fields are final - cannot be changed after construction
    private final int transactionId;
    private final TransactionType type;
    private final double amount;
    private final int accountId;
    private final LocalDateTime timestamp;
    private final double balanceAfter;
    private final String description;
    
    // Constructor sets values once
    // No setters exist
}
```

Why does this matter? Imagine if transactions were mutable. Someone could accidentally change the amount on a past transaction, alter timestamps to hide activity, or modify balances after the fact. Immutability makes these bugs impossible. Once a transaction record is created, it's frozen forever.

Thread safety is another benefit. When multiple parts of your program access the same transaction object, you don't need locks or synchronization because nobody can change it. The object is safe by design.

### Enums for Type Safety

Before creating the Transaction class, we need to solve the "magic string" problem. Look at how you might track transaction types with strings:

```java
// Fragile approach - typos become runtime bugs
String type = "DEPOSIT";    // Works
String type2 = "deposit";   // Different string!
String type3 = "DEPOSITT";  // Typo - no compile error
```

Strings are dangerous for representing fixed categories because the compiler can't help you. Typos slip through, and comparisons become error-prone. **Enums** solve this by defining a fixed set of possible values:

```java
/**
 * Enum representing types of banking transactions.
 */
public enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER_IN,
    TRANSFER_OUT
}
```

Now you can only use these four values. The IDE autocompletes them, the compiler catches typos, and switch statements warn you if you forget a case. It's impossible to create an invalid transaction type.

### Building the Transaction Class

Here's the complete Transaction class with automatic ID generation:

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    /** Enum to define types of transactions */
    public enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT
    }
    
    /** Static counter for generating unique transaction IDs */
    private static int transactionCounter = 10000;
    
    private final int transactionId;
    private final TransactionType type;
    private final double amount;
    private final int accountId;
    private final int relatedAccountId;  // For transfers
    private final LocalDateTime timestamp;
    private final double balanceAfter;
    private final String description;
    
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Constructs a transaction for deposits and withdrawals.
     */
    public Transaction(TransactionType type, double amount, int accountId, 
                      double balanceAfter, String description) {
        this.transactionId = ++transactionCounter;
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
        this.relatedAccountId = -1;
        this.timestamp = LocalDateTime.now();
        this.balanceAfter = balanceAfter;
        this.description = description;
    }
    
    /**
     * Constructs a transfer transaction with related account.
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
    
    // Getters only - no setters because immutable
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
        String relatedInfo = (relatedAccountId != -1) 
            ? ", relatedAccount=" + relatedAccountId 
            : "";
        
        return String.format("Transaction{id=%d, type=%s, amount=€%.2f, account=%d%s, " +
                           "balanceAfter=€%.2f, time=%s, desc='%s'}",
                           transactionId, type, amount, accountId, relatedInfo,
                           balanceAfter, timestamp.format(FORMATTER), description);
    }
}
```

Notice the design patterns at work:

- **Constructor overloading**: Two constructors handle different transaction types. Regular deposits/withdrawals don't need a related account ID, but transfers do.
- **Automatic ID generation**: The static counter ensures every transaction gets a unique ID, just like you did with Account IDs.
- **LocalDateTime**: Java's modern date-time API provides immutable timestamps that are thread-safe and easy to format.
- **No setters**: The absence of setter methods makes immutability explicit. Once constructed, the transaction is frozen.

### Adding Transaction History to Account

Now that transactions exist as objects, Account can maintain a history:

```java
import java.util.ArrayList;

public class Account {
    // Existing fields
    private static int accountCounter = 1000;
    private final int accountId;
    private double balance;
    private boolean isFrozen;
    
    // NEW: Transaction history
    private ArrayList<Transaction> transactionHistory;
    
    public Account() {
        this.accountId = ++accountCounter;
        this.balance = 0.0;
        this.isFrozen = false;
        this.transactionHistory = new ArrayList<>();  // Initialize empty list
    }
    
    public Account(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.accountId = ++accountCounter;
        this.balance = initialBalance;
        this.isFrozen = false;
        this.transactionHistory = new ArrayList<>();
        
        // Record initial deposit as first transaction
        if (initialBalance > 0) {
            Transaction initialDeposit = new Transaction(
                Transaction.TransactionType.DEPOSIT,
                initialBalance,
                accountId,
                balance,
                "Initial deposit"
            );
            transactionHistory.add(initialDeposit);
        }
    }
}
```

Now every operation creates a transaction record. Update the deposit method:

```java
public boolean deposit(double amount) {
    if (isFrozen) {
        System.out.println("Cannot deposit. Account #" + accountId + " is frozen.");
        return false;
    }
    
    if (amount > 0) {
        balance += amount;
        
        // Create and store transaction record
        Transaction transaction = new Transaction(
            Transaction.TransactionType.DEPOSIT,
            amount,
            accountId,
            balance,
            "Deposit"
        );
        transactionHistory.add(transaction);
        
        System.out.println("Deposited: €" + amount + " to Account #" + accountId);
        return true;
    } else {
        System.out.println("Deposit amount must be positive.");
        return false;
    }
}
```

The withdrawal method follows the same pattern—update balance, create transaction, add to history. Now you have a complete audit trail.

### Defensive Copying with Collections

When exposing the transaction history, you need to prevent external code from modifying it:

```java
import java.util.Collections;
import java.util.List;

/**
 * Returns an unmodifiable view of the transaction history.
 */
public List<Transaction> getTransactionHistory() {
    return Collections.unmodifiableList(transactionHistory);
}
```

This returns a **read-only view**. External code can iterate through transactions and read data, but cannot add, remove, or modify transactions. Your internal list stays protected.

### Why This Design Works

- **Immutability prevents bugs**: Transaction records cannot be accidentally corrupted after creation.
- **Enums catch errors early**: Type-safe constants mean typos become compile errors, not runtime failures.
- **Complete audit trail**: Every operation leaves a permanent, timestamped record for compliance and debugging.
- **Encapsulation maintained**: The unmodifiable list prevents external manipulation while allowing inspection.

Transaction is a perfect example of a value object: it represents data, doesn't change, and has no complex behavior beyond describing itself.

## Introducing the Bank Class

With transactions tracking operations, the next step addresses the core design flaw: `Customer` doing too many things. The solution is a new class that coordinates operations between customers—a Bank.

### The Coordination Problem

Remember the transfer problem? Here's why it needs a dedicated coordinator:

```java
// Customer can't naturally handle this
customer1.transferTo(customer2, account1Id, account2Id, 500.0);

// This requires:
// 1. Access to customer2's internal account list
// 2. Coordinating withdrawal from customer1's account
// 3. Coordinating deposit to customer2's account
// 4. Handling the case where withdrawal works but deposit fails
```

Customer shouldn't know about other customers' internals. That would create tight coupling where every customer depends on the implementation details of every other customer. Instead, we need a **manager class** that sits above individual customers and coordinates interactions.

### Single Responsibility Applied

The Bank class takes on exactly one responsibility: **managing customers and coordinating operations between them**. It doesn't store personal customer data (that's Customer's job), doesn't track account balances (that's Account's job), and doesn't create transaction records (Account handles that). It just orchestrates.

```java
import java.util.ArrayList;

public class Bank {
    private String bankName;
    private ArrayList<Customer> customers;
    
    public Bank(String bankName) {
        this.bankName = bankName;
        this.customers = new ArrayList<>();
    }
    
    public String getBankName() {
        return bankName;
    }
}
```

The structure mirrors Customer's relationship with Account: Bank has a collection of Customers, just like Customer has a collection of Accounts. This creates a clean three-level hierarchy: Bank → Customer → Account.

### Customer Management Operations

Bank needs methods to add, remove, and find customers:

```java
/**
 * Adds a new customer to the bank.
 */
public boolean addCustomer(Customer customer) {
    if (customer == null) {
        System.out.println("Cannot add null customer");
        return false;
    }
    
    if (findCustomer(customer.getId()) != null) {
        System.out.println("Customer with ID " + customer.getId() + " already exists.");
        return false;
    }
    
    customers.add(customer);
    System.out.println("Customer " + customer.getName() + " (ID: " + customer.getId() + 
                     ") added to " + bankName);
    return true;
}

/**
 * Finds a customer by their ID.
 */
public Customer findCustomer(int customerId) {
    for (Customer customer : customers) {
        if (customer.getId() == customerId) {
            return customer;
        }
    }
    return null;
}

/**
 * Removes a customer from the bank.
 */
public boolean removeCustomer(int customerId) {
    Customer customer = findCustomer(customerId);
    if (customer == null) {
        System.out.println("Customer with ID " + customerId + " not found.");
        return false;
    }
    
    customers.remove(customer);
    System.out.println("Customer " + customer.getName() + " removed from " + bankName);
    return true;
}
```

This follows the same pattern you used in Customer for managing accounts: search, validate, then act. The Bank is a collection manager one level up in the hierarchy.

### Delegating Financial Operations

Here's where Bank's coordination role becomes clear. When someone deposits money, Bank doesn't handle the money—it routes the operation to the right account:

```java
/**
 * Deposits money into a specific customer's account.
 */
public boolean deposit(int customerId, int accountId, double amount) {
    Customer customer = findCustomer(customerId);
    if (customer == null) {
        System.out.println("Customer with ID " + customerId + " not found.");
        return false;
    }
    
    Account account = customer.findAccount(accountId);
    if (account == null) {
        System.out.println("Account #" + accountId + " not found for customer " + customer.getName());
        return false;
    }
    
    return account.deposit(amount);  // Delegate to Account
}
```

Bank knows nothing about balance validation, freeze checks, or transaction recording; that's Account's job. Bank just ensures the request reaches the right account.

Withdrawal follows the identical pattern:

```java
/**
 * Withdraws money from a specific customer's account.
 */
public boolean withdraw(int customerId, int accountId, double amount) {
    Customer customer = findCustomer(customerId);
    if (customer == null) {
        System.out.println("Customer with ID " + customerId + " not found.");
        return false;
    }
    
    Account account = customer.findAccount(accountId);
    if (account == null) {
        System.out.println("Account #" + accountId + " not found for customer " + customer.getName());
        return false;
    }
    
    return account.withdraw(amount);
}
```

This is **delegation**: Bank coordinates access but doesn't duplicate the business logic. Each class stays focused on its own responsibility.

### The Transfer Operation

Now we can solve the transfer problem properly:

```java
/**
 * Transfers money between two accounts (can be different customers).
 */
public boolean transfer(int fromCustomerId, int fromAccountId, 
                       int toCustomerId, int toAccountId, double amount) {
    
    // Find source customer and account
    Customer fromCustomer = findCustomer(fromCustomerId);
    if (fromCustomer == null) {
        System.out.println("Source customer with ID " + fromCustomerId + " not found.");
        return false;
    }
    
    Account fromAccount = fromCustomer.findAccount(fromAccountId);
    if (fromAccount == null) {
        System.out.println("Source account #" + fromAccountId + " not found.");
        return false;
    }
    
    // Find destination customer and account
    Customer toCustomer = findCustomer(toCustomerId);
    if (toCustomer == null) {
        System.out.println("Destination customer with ID " + toCustomerId + " not found.");
        return false;
    }
    
    Account toAccount = toCustomer.findAccount(toAccountId);
    if (toAccount == null) {
        System.out.println("Destination account #" + toAccountId + " not found.");
        return false;
    }
    
    // Validate transfer conditions
    if (amount <= 0) {
        System.out.println("Transfer amount must be positive.");
        return false;
    }
    
    if (fromAccount.isFrozen() || toAccount.isFrozen()) {
        System.out.println("Cannot transfer: one or both accounts are frozen.");
        return false;
    }
    
    if (fromAccount.getBalance() < amount) {
        System.out.println("Insufficient funds in source account.");
        return false;
    }
    
    // Execute the transfer atomically
    if (fromAccount.transferOut(amount, toAccountId) && 
        toAccount.transferIn(amount, fromAccountId)) {
        System.out.println("Transfer successful: €" + amount + " from Account #" + 
                         fromAccountId + " to Account #" + toAccountId);
        return true;
    }
    
    return false;
}
```

Bank is the only class with visibility into both customers and their accounts, making it the natural coordinator for transfers. The method validates everything up front, then executes the transfer as an atomic operation through special package-private methods in Account:

```java
// Add these to Account class
boolean transferOut(double amount, int toAccountId) {
    if (isFrozen || amount <= 0 || amount > balance) {
        return false;
    }
    
    balance -= amount;
    Transaction transaction = new Transaction(
        Transaction.TransactionType.TRANSFER_OUT,
        amount,
        accountId,
        toAccountId,
        balance,
        "Transfer to account #" + toAccountId
    );
    transactionHistory.add(transaction);
    return true;
}

boolean transferIn(double amount, int fromAccountId) {
    if (isFrozen || amount <= 0) {
        return false;
    }
    
    balance += amount;
    Transaction transaction = new Transaction(
        Transaction.TransactionType.TRANSFER_IN,
        amount,
        accountId,
        fromAccountId,
        balance,
        "Transfer from account #" + fromAccountId
    );
    transactionHistory.add(transaction);
    return true;
}
```

These methods are package-private (no access modifier) so only classes in the same package—like Bank—can call them. They're not part of Account's public API because external code shouldn't call them directly; only the coordinated Bank.transfer() method should.

### Why This Design Works

- **Clear hierarchy**: Bank manages Customers, Customers manage Accounts, Accounts manage Transactions.
- **Single responsibility**: Bank coordinates, Customer organizes, Account executes financial logic.
- **Proper delegation**: Each method finds the right object and delegates the actual work to it.
- **Natural fit**: The design mirrors real banking where banks coordinate between customers.

Bank doesn't bloat with unrelated functionality because it has exactly one job: coordinate operations across customers. When you need to add bank-level features like reports, interest processing, or multi-account operations, there's an obvious place for them.

## Refactoring the Customer Class

With Bank handling coordination, Customer can focus solely on managing its own accounts. Several methods need to move or change, demonstrating how refactoring maintains functionality while improving structure.

### What Moves to Bank

The deposit and withdraw methods that took customer and account IDs? Those move to Bank because they require coordination:

```java
// REMOVE these from Customer:
public boolean deposit(int accountId, double amount) { ... }
public boolean withdraw(int accountId, double amount) { ... }

// They now live in Bank as:
public boolean deposit(int customerId, int accountId, double amount) { ... }
public boolean withdraw(int customerId, int accountId, double amount) { ... }
```

This makes sense: Bank knows which customer to route the request to, while Customer shouldn't need external customer IDs in its method signatures.

### What Stays in Customer

Everything related to managing the customer's own account collection remains:

```java
public class Customer {
    private final int id;
    private String name;
    private int age;
    private String address;
    private ArrayList<Account> accounts;
    
    // These stay - managing own accounts
    public boolean addAccount(Account account) { ... }
    public boolean removeAccount(Account account) { ... }
    public Account findAccount(int accountId) { ... }
    public int getAccountCount() { ... }
    public double getTotalBalance() { ... }
    public void displayAllAccounts() { ... }
}
```

Customer's job is clear now: manage personal information and the account collection. No financial operations, no knowledge of other customers—just focused account management.

### Making Customer ID Immutable

One subtle but important change: the customer ID should never change after creation, so make it `final`:

```java
public class Customer {
    private final int id;  // Cannot be changed after construction
    
    public Customer(int id, String name, int age, String address) {
        this.id = id;
        // ...
    }
    
    public int getId() {
        return id;
    }
    
    // NO setId() method - ID is permanent
}
```

This prevents bugs where IDs accidentally change, breaking the Bank's ability to find customers. Immutability for identity fields is a best practice.

## Understanding equals() and hashCode()

With objects now representing customers, accounts, and transactions, you need to understand how Java determines if two objects are "the same." This becomes critical when searching collections or preventing duplicate customers.

### The Default Equality Problem

Without custom equals(), Java compares memory addresses:

```java
Customer ada1 = new Customer(1815, "Ada Lovelace", 36, "London");
Customer ada2 = new Customer(1815, "Ada Lovelace", 36, "London");

System.out.println(ada1 == ada2);  // false - different objects in memory
System.out.println(ada1.equals(ada2));  // false - default equals uses ==
```

Even though both customers have the same ID and data, Java sees them as different because they occupy different memory locations. For banking, this is wrong—two customers with the same ID should be considered the same customer regardless of memory location.

### Overriding equals() for Meaningful Equality

Add this to Customer:

```java
import java.util.Objects;

@Override
public boolean equals(Object obj) {
    // Same reference? Definitely equal
    if (this == obj) return true;
    
    // Null or different class? Not equal
    if (obj == null || getClass() != obj.getClass()) return false;
    
    // Cast and compare ID
    Customer customer = (Customer) obj;
    return id == customer.id;
}
```

This implements **identity-based equality**: two customers are equal if they have the same ID, regardless of name, age, or address. The ID is the unique identifier, so that's what matters for equality.

The method follows a standard pattern:

1. **Reference check**: If it's literally the same object, return true immediately.
2. **Null/class check**: Ensure we're comparing with another Customer object.
3. **Field comparison**: Compare the identifying field(s)—in this case, just the ID.

### The hashCode() Contract

Whenever you override equals(), you must override hashCode():

```java
@Override
public int hashCode() {
    return Objects.hash(id);
}
```

This is Java's **fundamental rule**: objects that are equal must have the same hash code. If two customers have the same ID (and are therefore equal), they must return the same hash code.

Why? Hash-based collections like HashMap and HashSet use hash codes to organize objects. If equal objects have different hash codes, the collections break. You might put a customer in a HashSet, then later search for an equal customer and not find it because the hash codes differ.

The `Objects.hash()` utility method generates a hash code from the fields you pass it. Use the same fields you compared in equals()—for Customer, that's just the ID.

### Adding equals() and hashCode() to Account

Account needs the same treatment:

```java
public class Account {
    private final int accountId;
    // ...
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account account = (Account) obj;
        return accountId == account.accountId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}
```

Two accounts are equal if they have the same account ID. Balance, freeze status, and transaction history don't matter for identity—those are changeable state, not identity.

### Why This Matters

- **Searching works correctly**: When Bank searches for a customer by creating a temporary Customer object with matching ID, equals() ensures it finds the right one.
- **Collections work properly**: HashSet won't allow duplicate customers with the same ID.
- **Comparisons make sense**: Business logic comparing customers compares identity, not memory addresses.

## Putting It All Together

With all pieces in place, here's how the refactored system works in practice. This demonstrates the interplay between Bank, Customer, Account, and Transaction.

### Creating the Bank and Customers

```java
public class Main {
    public static void main(String[] args) {
        // Create the bank
        Bank turingBank = new Bank("Turing National Bank");
        
        // Create customers
        Customer ada = new Customer(1815, "Ada Lovelace", 36, "10 St. James Square, London");
        Customer alan = new Customer(1912, "Alan Turing", 41, "Sherborne School, Dorset");
        
        // Add customers to bank
        turingBank.addCustomer(ada);
        turingBank.addCustomer(alan);
    }
}
```

Output:
```
Customer Ada Lovelace (ID: 1815) added to Turing National Bank
Customer Alan Turing (ID: 1912) added to Turing National Bank
```

### Opening Accounts

```java
// Create accounts
Account adaSavings = new Account(5000.0);
Account adaChecking = new Account(2000.0);

// Add to customer
ada.addAccount(adaSavings);
ada.addAccount(adaChecking);

// Alan's accounts
Account alanSavings = new Account(3000.0);
Account alanChecking = new Account(1500.0);

alan.addAccount(alanSavings);
alan.addAccount(alanChecking);
```

Output:
```
Account #1001 added to customer Ada Lovelace
Account #1002 added to customer Ada Lovelace
Account #1003 added to customer Alan Turing
Account #1004 added to customer Alan Turing
```

Notice the automatic ID generation—accounts get sequential IDs without any manual coordination.

### Performing Transactions Through Bank

```java
// Deposit to Ada's savings
turingBank.deposit(1815, 1001, 1000.0);

// Withdraw from Alan's checking
turingBank.withdraw(1912, 1004, 200.0);

// Transfer from Ada to Alan
turingBank.transfer(1815, 1001, 1912, 1003, 750.0);
```

Output:
```
Deposited: €1000.0 to Account #1001
Withdrawn: €200.0 from Account #1004
Transfer successful: €750.0 from Account #1001 to Account #1003
```

Each operation creates transaction records automatically. Bank coordinates, Account executes and records.

### Viewing Transaction History

```java
Account adaSavingsAccount = ada.findAccount(1001);
for (Transaction t : adaSavingsAccount.getTransactionHistory()) {
    System.out.println(t);
}
```

Output:
```
Transaction{id=10001, type=DEPOSIT, amount=€5000.00, account=1001, balanceAfter=€5000.00, time=2024-10-24 20:15:03, desc='Initial deposit'}
Transaction{id=10002, type=DEPOSIT, amount=€1000.00, account=1001, balanceAfter=€6000.00, time=2024-10-24 20:15:05, desc='Deposit'}
Transaction{id=10003, type=TRANSFER_OUT, amount=€750.00, account=1001, relatedAccount=1003, balanceAfter=€5250.00, time=2024-10-24 20:15:07, desc='Transfer to account #1003'}
```

Complete audit trail with timestamps, transaction IDs, and balance after each operation. Immutable records that can't be tampered with.

### Testing equals() and hashCode()

```java
// Create duplicate customer with same ID
Customer ada2 = new Customer(1815, "Ada Lovelace", 36, "Different Address");

System.out.println(ada.equals(ada2));  // true - same ID
System.out.println(ada.hashCode() == ada2.hashCode());  // true - required by contract

// Bank prevents duplicate
turingBank.addCustomer(ada2);  // "Customer with ID 1815 already exists."
```

The equals() method ensures the Bank correctly identifies the duplicate customer despite different object references.

## Summary

You've transformed a functional banking system into one built on solid design principles. The changes touched every class but preserved all functionality while dramatically improving structure.

### Key Concepts Mastered

#### Single Responsibility Principle 
Each class has one clear job. Customer manages its accounts, Account tracks its balance and transactions, Bank coordinates between customers. When requirements change, you know exactly which class to modify.

####  Immutability for safety 
`Transaction` objects cannot change after creation. This prevents bugs, enables thread-safety, and maintains audit trail integrity. Immutability is a best practice for value objects representing data.

####  Enums for type safety
`TransactionType enum` replaces error-prone strings with compiler-enforced constants. Impossible to create invalid transaction types, and IDE autocomplete guides usage.

#### Refactoring without breaking
You reorganized responsibilities extensively yet all operations still work. This demonstrates that good design isn't about getting it right first time—it's about recognizing problems and fixing them systematically.

####  `equals()` and `hashCode()` contract

Custom equality based on identity fields (IDs) makes collections work correctly and comparisons meaningful. Always override both methods together, using the same fields in each.

#### Defensive copying with Collections

Returning unmodifiable views of internal lists prevents external manipulation while allowing inspection. This maintains encapsulation even when exposing collection data.

#### Object coordination 
Bank demonstrates how manager classes can coordinate operations between objects without duplicating their business logic. Delegation keeps responsibilities separate.


### Complete Code

Find the complete working code organized by version:

- [v2_basic_collections](github.com/evisp/java-oop-course/week2_collections) - Starting point with ArrayList and relationships
- [v3_thinking_in_objects](github.com/evisp/java-oop-course/week3_design_principles) - Refactored version with Transaction, Bank, and proper design

The same Customer and Account functionality exists, but the structure now supports growth. Next week you'll build on this foundation with inheritance and polymorphism, introducing different account types (savings, checking) that share common behavior while implementing specialized rules. The clean design makes that extension natural instead of painful.