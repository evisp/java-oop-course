# Abstraction and Encapsulation: The Foundation of OOP

## Introduction - Hiding What Matters

Your banking system demonstrates two fundamental OOP principles that work together to create maintainable code: **encapsulation** (hiding data) and **abstraction** (hiding complexity). While often confused, they serve distinct purposes that are visible throughout your Account hierarchy.

Consider what happens when external code tries to directly modify an account balance:

```java
// Without encapsulation - dangerous
account.balance = -5000.0;  // Bypass all validation!

// With encapsulation - safe
account.withdraw(100.0);  // Goes through validation
```

Encapsulation prevents the first scenario. Abstraction lets the Bank call `withdraw()` without knowing whether it's a SavingsAccount checking minimum balance or a CheckingAccount allowing overdraft.

## Encapsulation: Hiding Data

> **Encapsulation** bundles data with the methods that operate on it, and restricts direct access to internal state. External code interacts through a controlled public interface.

### Private Fields with Controlled Access

Your Account class demonstrates encapsulation fundamentals:

```java
public class Account {
    private final int accountId;      // Immutable - no setter
    protected double balance;         // Protected for subclass access
    private boolean isFrozen;         // Private - controlled access
    
    // Controlled access through methods
    public double getBalance() {
        return balance;
    }
    
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit must be positive");
            return false;
        }
        balance += amount;  // Internal modification with validation
        return true;
    }
}
```

**Why this matters**: External code can't set `balance = -1000` or modify `accountId` after creation. All changes go through methods that validate input, creating data integrity.

### Immutability Through Final Fields

Transaction demonstrates a stricter form of encapsulation:

```java
public class Transaction {
    private final int transactionId;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timestamp;
    
    // Constructor sets values once
    public Transaction(TransactionType type, double amount, ...) {
        this.transactionId = ++transactionCounter;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
    
    // Only getters - no setters
    public double getAmount() { return amount; }
}
```

Once created, transactions cannot be modified. This prevents audit trail tampering and eliminates a whole class of bugs.

### Defensive Copying

Your Account exposes transaction history safely:

```java
public List<Transaction> getTransactionHistory() {
    return Collections.unmodifiableList(transactionHistory);
}
```

External code can read transactions but cannot add, remove, or modify them. The internal list stays protected while providing necessary access.

## Abstraction: Hiding Complexity

> **Abstraction** hides implementation details and exposes only essential features. Users interact with simplified interfaces without needing to understand internal complexity.

### Polymorphic Interface

The Bank demonstrates abstraction by working with the `Account` interface:

```java
public boolean withdraw(int customerId, int accountId, double amount) {
    Account account = customer.findAccount(accountId);  // Any Account subtype
    
    return account.withdraw(amount);  // Don't know/care which type
}
```

The Bank doesn't know whether it's calling `SavingsAccount.withdraw()` (which checks minimum balance) or `CheckingAccount.withdraw()` (which allows overdraft). It works with the abstraction—the `withdraw()` method signature—not the implementation.

### Method Overriding Hides Implementation

Each account type implements `withdraw()` differently, but callers use the same interface:

```java
// SavingsAccount - hidden complexity: minimum balance enforcement
@Override
public boolean withdraw(double amount) {
    if (balance - amount < minimumBalance) {
        System.out.println("Would violate minimum balance");
        return false;
    }
    return super.withdraw(amount);
}

// CheckingAccount - hidden complexity: overdraft calculation
@Override
public boolean withdraw(double amount) {
    if (amount > balance + overdraftLimit) {
        System.out.println("Exceeds available balance");
        return false;
    }
    balance -= amount;  // Can go negative
    return true;
}
```

The abstraction is the `withdraw()` method. The complexity—different validation rules—is hidden inside each implementation.

### High-Level Operations

The Bank provides abstracted operations:

```java
turingBank.openSavingsAccount(customerId, 5000.0, 0.03);
turingBank.applyInterestToAllSavings();
```

Users don't see the complexity: finding customers, creating accounts, iterating through account types, type checking with `instanceof`, downcasting, and calling subclass methods. The abstraction presents a simple interface to complex operations.

## How They Work Together

**Encapsulation** secures the data. **Abstraction** simplifies the interface.

```java
// Encapsulation: balance is protected, accessed through methods
protected double balance;

// Abstraction: Account provides withdraw() interface
public abstract boolean withdraw(double amount);

// Together: Safe data with simple usage
Account account = new SavingsAccount(1000.0, 0.03);
account.withdraw(50.0);  // Simple call, complex validation hidden
```

Your banking system uses both constantly:

- **Encapsulation**: Private fields, validation in methods, immutable transactions, defensive copying
- **Abstraction**: Polymorphic Account interface, method overriding, Bank coordinating without knowing types

### Key Differences

| Concept | Purpose | Implementation | Example from Your Code |
|---------|---------|----------------|----------------------|
| Encapsulation | Protect data | Private/protected fields, public methods | `private final int accountId` with only getter |
| Abstraction | Hide complexity | Inheritance, interfaces, polymorphism | Bank calling `account.withdraw()` without knowing type |

Both principles create maintainable systems. Encapsulation prevents accidental data corruption. Abstraction reduces cognitive load—you work with high-level concepts instead of implementation details.

