# Abstract Classes and Interfaces: Enforcing Contracts Through Design

## Introduction - When Good Design Needs Guarantees

In Version 4, you built a working inheritance hierarchy with `Account` as the parent and `SavingsAccount` and `CheckingAccount` as specialized children. The design worked beautifully: shared code lived in the parent, specialized behavior in the children, and polymorphism let the Bank work with any account type through `Account` references.

But as you stared at the `Account` class, a question emerged: **should this class be instantiable?**

Consider this perfectly legal Version 4 code:

```java
Account generic = new Account(1000.0);
customer.addAccount(generic);
```

This creates a "generic account" with no interest rate, no overdraft protection, no specialized rules at all. It bypasses everything you carefully designed in `SavingsAccount` and `CheckingAccount`. Real banks don't offer generic accounts—they offer savings, checking, business, or student accounts. Each has specific rules and features.

The `Account` class represents a **concept**, not a concrete product. It's the idea of "what all accounts share," not something that should exist by itself. Like a university offering a "Generic Course" with no schedule, credits, or instructor—it makes no sense.

A second problem appears when you think about system-wide features. Your accounts track transactions. The Bank coordinates operations. In the future, you might want audit reports from both. These classes need a shared **capability**—generating audit trails—but they're not part of the same inheritance hierarchy. Bank isn't an Account; Customer isn't an Account. Inheritance can't help here.

### The Two Problems

Your system needs solutions for:

1. **Preventing meaningless instantiation**: `Account` should be a base for specialization, not something created directly
2. **Sharing capabilities across hierarchies**: Audit trails belong in Account, Bank, and potentially Customer; classes that aren't related by inheritance

Version 4's inheritance solved code reuse but couldn't express these architectural constraints. The compiler allows creating generic accounts even though the domain doesn't. It can't enforce that accounts provide type identification. It can't guarantee unrelated classes share audit capabilities.

You need mechanisms that:

- Prevent instantiation while preserving inheritance
- Force subclasses to implement required methods
- Let unrelated classes promise to support shared behaviors

That's where **abstract classes** and **interfaces** come in.

## Making Account Abstract

### The Problem Revisited

In Version 4, this compiles without error:

```java
Account acc = new Account(1500.0);
System.out.println(acc.getAccountType());  // Returns what? "Standard Account"?
```

Nothing stops developers from creating these meaningless accounts. The type system doesn't know that `Account` is conceptually abstract. Documentation can say "don't instantiate this," but documentation doesn't prevent bugs—the compiler does.

### Declaring the Class Abstract

The first change is adding one keyword to the class declaration:

```java
public abstract class Account implements Auditable {
    // List of properties
    // Constructors remain unchanged
    
    // All existing concrete methods remain
    public boolean deposit(double amount) { ... }
    public boolean withdraw(double amount) { ... }
    // etc.
}
```

The `abstract` keyword has a single, powerful effect:

```java
Account acc = new Account(1000.0);  // COMPILER ERROR: Cannot instantiate abstract class
```

Polymorphism still works perfectly:

```java
Account savings = new SavingsAccount(1000.0, 0.03);    // Legal - SavingsAccount is concrete
Account checking = new CheckingAccount(500.0, 300.0);  // Legal - CheckingAccount is concrete
```

All the shared implementation—constructors, `deposit()`, `withdraw()`, transaction recording—remains intact and inherited by subclasses. The only thing that changes is **you can no longer create Account objects directly**.

### Why This Matters

The abstract declaration aligns code with domain reality:

- The type system now knows `Account` is a concept, not a product
- Impossible to accidentally give customers generic accounts
- Every account in the system is guaranteed to be a concrete, meaningful type (Savings, Checking, or future types you add)

This is **design intent made explicit**. The compiler enforces what previously relied on developer discipline.

### Adding an Abstract Method

The second change introduces a method with no implementation:

```java
/**
 * Returns the type of this account as a string.
 * Subclasses must implement to identify their specific type.
 * 
 * @return the account type (e.g., "Savings Account", "Checking Account")
 */
public abstract String getAccountType();
```

An abstract method:

- Has the `abstract` keyword
- Ends with a semicolon—no method body, no curly braces
- Creates a **contract**: every concrete subclass must implement this method

Previously, you might have provided a default implementation in `Account`:

```java
// Version 4 style
public String getAccountType() {
    return "Standard Account";
}
```

But that's wrong for an abstract class. `Account` itself has no specific type—it's the category containing all types. Each concrete subclass knows its own type: `SavingsAccount` is savings, `CheckingAccount` is checking.

The abstract method **forces subclasses to answer this question**. If you create `BusinessAccount` but forget to implement `getAccountType()`, the compiler rejects the code until you provide it.

## Introducing the Auditable Interface

### The Cross-Hierarchy Capability Problem

Your `Account` class already tracks transaction history. It's natural to want methods like:

```java
String generateAuditReport();
int getAuditEntryCount();
```

Later, you'll want similar capabilities in other classes:

- `Bank`: audit of all operations across customers
- `Customer`: audit of all account activity for one customer

These classes don't belong in the same inheritance hierarchy. `Bank` is not an `Account`. `Customer` is not an `Account`. Using inheritance to share "audit" behavior would be architecturally wrong.

What they share is a **capability**—the ability to generate audit reports—not an "is-a" relationship.

### Defining the Interface

An interface is a **pure contract with no implementation**:

```java
package v5_abstract_interfaces;

public interface Auditable {

    void generateAuditReport();
    List<Transaction> getAuditTrail();
    String getLastModifiedTime();
    int getTransactionCount();

}
```

Key characteristics:
- Declared with `interface` keyword, not `class`
- Contains only method signatures; no method bodies
- No fields (interfaces can have constants, but not instance variables)
- No constructors (interfaces cannot be instantiated)


### Account Implements Auditable

The abstract `Account` class now declares it implements the interface:

```java
public abstract class Account implements Auditable {
    // All existing code...
    
    @Override
    public String generateAuditReport() {
        System.out.println("Account Type: " + getAccountTypeName());
        System.out.println("Account ID: " + accountId);
        System.out.println("Current Balance: €" + String.format("%.2f", balance));
        System.out.println("Account Status: " + (isFrozen ? "FROZEN" : "ACTIVE"));
        System.out.println("Total Transactions: " + transactionHistory.size());
        System.out.println("Monthly Fee: €" + String.format("%.2f", calculateMonthlyFee()));
        System.out.println("Last Modified: " + getLastModifiedTime());
        System.out.println("\nTransaction Summary:");
        // rest of the implementation
    }

    @Override
    public List<Transaction> getAuditTrail() {
        return Collections.unmodifiableList(transactionHistory);
    }
    
    @Override
    public String getLastModifiedTime() {
        if (transactionHistory.isEmpty()) {
            return "No transactions yet";
        }
        Transaction lastTransaction = transactionHistory.get(transactionHistory.size() - 1);
        return lastTransaction.getTimestamp().toString();
    }
}
```

This implementation uses the existing `transactionHistory` to build a formatted report. The method signatures match the interface contract exactly—same names, same parameter types, same return types.

Notice:

- `Account` is still abstract; you can't instantiate it
- `Account` now implements an interface; it promises audit capability
- Subclasses automatically inherit these implementations

### Why Interfaces Matter


The code depends on the **capability contract**, not the concrete class or its position in the hierarchy. This is **interface-based design**: programming to abstractions, not implementations.

## Changes in SavingsAccount

The `SavingsAccount` class needs minimal changes to work with the abstract parent.

### Implementing the Abstract Method

Because `Account` now has an abstract `getAccountType()` method, every concrete subclass must implement it:

```java
@Override
public String getAccountType() {
    return "Savings Account";
}
```

The `@Override` annotation isn't required but is strongly recommended. It tells the compiler "I'm implementing an abstract method from the parent." If you misspell the method name or get the signature wrong, the compiler catches it.

Without this implementation, `SavingsAccount` would need to be declared abstract itself, which defeats the purpose—you want concrete savings accounts.

### All Other Code Unchanged

Everything else in `SavingsAccount` remains identical to Version 4:

- Fields: `interestRate`, `minimumBalance`
- Constructors calling `super(...)`
- Overridden `withdraw()` enforcing minimum balance
- Methods like `calculateInterest()` and `addInterest()`

The class doesn't need to do anything special for `Auditable`—it inherits the implementation from `Account`.

## Changes in CheckingAccount

`CheckingAccount` follows the same pattern.

### Implementing the Abstract Method

```java
@Override
public String getAccountType() {
    return "Checking Account";
}
```

This is the only required addition. The compiler won't let you forget—if you don't implement `getAccountType()`, `CheckingAccount` won't compile.

### All Other Code Unchanged

All Version 4 functionality remains:

- Fields: `overdraftLimit`, `monthlyFee`
- Constructors
- Overridden `withdraw()` allowing overdraft
- Methods like `applyMonthlyFee()`, `isInOverdraft()`, `getAvailableBalance()`

The inherited `Auditable` implementation works perfectly for checking accounts too, showing overdraft transactions with the same audit trail.

## Using the New Design in BankSystemMain

From the main program's perspective, most code looks identical to Version 4. The differences are subtle but important.

### No Generic Accounts Possible

This code no longer compiles:

```java
Account generic = new Account(1000.0);  // ERROR: Cannot instantiate abstract class
```

The compiler enforces the design rule. Every account must be a concrete type:

```java
SavingsAccount adaSavings = turingBank.openSavingsAccount(1815, 5000.0, 0.03);
CheckingAccount adaChecking = turingBank.openCheckingAccount(1815, 2000.0, 500.0);
```

### Polymorphism Still Works

All polymorphic references work exactly as before:

```java
Account account1 = adaSavings;    // Legal - SavingsAccount is-a Account
Account account2 = adaChecking;   // Legal - CheckingAccount is-a Account

account1.deposit(1000.0);         // Calls inherited deposit()
account2.withdraw(500.0);         // Calls CheckingAccount's overridden withdraw()
```

The abstract declaration doesn't affect polymorphism—only direct instantiation.

### Guaranteed Type Identification

Every account now guarantees `getAccountType()` exists:

```java
for (Account account : customer.getAccounts()) {
    System.out.println(account.getAccountType() + " #" + account.getAccountId());
}
```

No risk of missing implementations—the compiler checked that every concrete class provides this method.

### Using Auditable Polymorphically

The new capability enables audit-focused code:

```java
Auditable auditable = adaSavings;  // SavingsAccount is Auditable
System.out.println(auditable.generateAuditReport());
System.out.println("Total entries: " + auditable.getAuditEntryCount());
```


The reference type is `Auditable`, not `Account`. The code works with any class that implements the interface.

### Future Extensibility

When you add new account types, the compiler enforces the contracts:

```java
public class BusinessAccount extends Account {
    // Must implement getAccountType() or won't compile
    
    @Override
    public String getAccountType() {
        return "Business Account";
    }
    
    // Automatically has audit capabilities from Account
}
```

When you add audit capabilities to other classes:

```java
public class Bank implements Auditable {
    @Override
    public String generateAuditReport() {
        // Bank's own audit implementation
    }
    
    @Override
    public int getAuditEntryCount() {
        // Count bank-level operations
    }
}

// Now Bank can be used anywhere Auditable is expected
Auditable bankAudit = turingBank;
printAudit(bankAudit);
```

The interface creates a common vocabulary across the system.

## Abstract Classes vs Interfaces: When to Use Each

Understanding when to choose each mechanism is crucial to good design.

### Use Abstract Classes When:

**1. You have shared implementation**

Abstract classes can provide concrete methods subclasses inherit. `Account` provides `deposit()`, `withdraw()`, transaction recording—all shared by savings and checking accounts. Interfaces can't provide implementation (Java 8+ allows default methods, but that's an advanced topic).

**2. You have a clear inheritance hierarchy**

Abstract classes work best with "is-a" relationships. SavingsAccount **is-a** Account, CheckingAccount **is-a** Account. They form a natural tree structure.

**3. You need to enforce some methods while providing others**

Abstract classes mix abstract and concrete methods. `Account` forces subclasses to implement `getAccountType()` while providing complete `deposit()` implementation.

**4. You have protected state**

Abstract classes can have instance variables with protected access. Subclasses access `balance` and `transactionHistory` directly. Interfaces cannot have instance variables.

### Use Interfaces When:

**1. You're defining a capability across different hierarchies**

Interfaces work best for "can-do" relationships. Account, Bank, and Customer can all be Auditable without sharing a parent class. They're unrelated but share a capability.

**2. You need multiple inheritance of behavior**

Java allows a class to implement multiple interfaces but extend only one class. A class can be `Auditable`, `Serializable`, `Comparable`, etc. all at once. This avoids the diamond problem of multiple class inheritance.

**3. You want pure contracts without implementation**

Interfaces define "what" without "how." Any class promising to be `Auditable` must provide those two methods—however it wants. The interface doesn't dictate implementation.

**4. You're designing for future extension**

Interfaces are more flexible than inheritance hierarchies. New classes can implement `Auditable` without touching existing code. New interfaces can be added to existing classes without restructuring the hierarchy.

### The Combination

Often the best design uses both:

```java
public abstract class Account implements Auditable {
    // Abstract class: shares code, defines hierarchy
    // Interface: promises audit capability
}
```

This gives you:

- Code reuse through abstract class inheritance
- Capability-based design through interface implementation
- Compiler-enforced contracts from both
- Flexibility to add more interfaces later

## Summary

You've transformed the banking system from "good inheritance" to **contract-enforced architecture**. The changes are small in code but profound in design impact.

### Key Concepts Mastered

#### Abstract classes prevent incomplete instantiation
Adding `abstract` to `Account` makes it impossible to create generic accounts. The compiler enforces that only concrete, specialized types can be instantiated. Abstract classes represent concepts, not things.

#### Abstract methods enforce implementation contracts
Declaring `getAccountType()` abstract forces every concrete subclass to provide it. Forgotten implementations become compiler errors, not runtime surprises. The architecture self-documents what must be implemented.

#### Interfaces define capabilities across hierarchies
`Auditable` creates a shared capability without requiring shared ancestry. Account, Bank, and Customer can all generate audit reports through the same interface, enabling polymorphic code that works with any auditable object.

#### Combining both creates powerful designs
Abstract classes share code within a hierarchy; interfaces share contracts across hierarchies. Using both gives you the benefits of inheritance (code reuse, specialization) and interfaces (flexibility, multiple capabilities).

#### The compiler becomes your design enforcer
Version 4 relied on developer discipline not to create generic accounts or forget `getAccountType()`. Version 5 makes violations compile-time errors. Good design becomes provably correct design.

### Design Benefits

The abstract classes and interfaces provide:

- **Type safety**: Compiler catches architectural violations, not tests or users
- **Self-documentation**: Code explicitly states what can be instantiated and what must be implemented
- **Extensibility**: Add new account types or audit-capable classes without modifying existing code
- **Flexibility**: Interfaces let unrelated classes share capabilities without coupling them
- **Maintainability**: Design intent is explicit in the type system, not just comments

### Complete Code

Find the complete working code:

- [v4_inheritance_polymorphism](github.com/evisp/java-oop-course/v4_inheritance_polymorphism) - Version 4 with concrete inheritance
- [v5_abstract_interfaces](github.com/evisp/java-oop-course/v5_abstract_interfaces) - Version 5 with abstract classes and interfaces

The banking system now expresses design intent through the type system itself. The solid foundation of abstract classes and interfaces makes all these extensions natural and maintainable.