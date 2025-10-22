# Collections and Class Relationships

## Introduction - Why Evolve What Works

Last week you built a **minimal working version** of a banking system with `Customer` and `Account` classes. The foundation was solid: encapsulated fields, validated constructors, and methods that protected sensitive data. But real banking systems have additional requirements that expose limitations in the initial design.

Consider what happens when a customer opens multiple accounts over time. Your current system can create individual `Account` objects, but there's no natural way for a `Customer` to manage their collection of accounts. You need methods to add new accounts, close old ones, search by account number, and calculate total holdings across all accounts. Arrays would require knowing the maximum number of accounts in advance and manual resizing logic.

**This tutorial refines your banking system to handle more complexity.** You'll upgrade the `Account` class to use `static` fields properly for class-level identity management, and transform the `Customer` class to manage multiple accounts using ArrayList—Java's dynamic, resizable collection.

The starting code is available at: [github.com/evisp/java-oop-course/week2_classes_objects](https://github.com/evisp/java-oop-course/week2_classes_objects)

By the end, you'll understand when static belongs at the class level rather than the object level, how to manage object collections idiomatically with `ArrayList`, and how to design methods that separate concerns cleanly—preparing you for the next step of adding transaction history and deeper object relationships.

![Bank System](https://i.imgur.com/nESWRQy.jpeg)

## Upgrading the Account Class

The original `Account` class handled balance and basic transactions, but lacked two critical features: unique identification and the ability to freeze accounts for security. Real banks need both—every account must have a permanent ID, and suspicious activity requires the ability to freeze accounts temporarily.

### Adding Account Identity with Static Fields

Every bank account needs a unique identifier that never changes. You could manually assign IDs when creating accounts, but that's error-prone and tedious. Instead, we'll use a **static field** to automatically generate unique IDs.

#### Understanding Static: Class-Level vs Object-Level Data

Until now, every field you've seen belongs to individual objects. When you create three `Account` objects, each has its own `balance` field storing different values. This is **instance-level data**—each object instance maintains its own copy.

But some data doesn't belong to any single object; it belongs to the entire class. Think of it like this: if each account is a bank customer, the counter that assigns new customer numbers belongs to the bank itself, not to any individual customer.

Here's the key distinction:

```java
public class Account {
    private double balance;          // Instance field: each account has its own balance
    private static int accountCounter = 1000;  // Static field: shared by ALL accounts
}
```

The `static` keyword means this field exists at the **class level**. There's only one `accountCounter` shared across all `Account` objects. When any account increments it, all future accounts see the new value.

#### Why Static Matters for ID Generation

Static fields are perfect for ID generation because you need exactly one counter shared across all accounts:

```java
public class Account {
    /** Static counter for generating unique account IDs */
    private static int accountCounter = 1000;
    
    /** Unique identifier for this account */
    private int accountId;
    
    /** Current balance in the account */
    private double balance;
```

Notice the pattern: `accountCounter` is static (class-level, shared), while `accountId` is instance-level (each account has its own ID). The counter generates IDs; each account stores the ID it received.

#### Generating IDs in the Constructor

The constructor is the perfect place to assign IDs because every account must get one at birth:

```java
/**
 * Constructs a new Account with zero balance and active status.
 * Automatically assigns a unique account ID.
 */
public Account() {
    this.accountId = ++accountCounter;  // Increment counter, then assign
    this.balance   = 0.0;
}

/**
 * Constructs a new Account with the specified initial balance and active status.
 * Automatically assigns a unique account ID.
 * 
 * @param initialBalance the starting balance for the account (must be non-negative)
 */
public Account(double initialBalance) {
    if (initialBalance < 0) {
        System.out.println("Initial balance cannot be negative. Provided: €" + initialBalance);
    }
    this.accountId = ++accountCounter;  // Same ID generation logic
    this.balance   = initialBalance;
}
```

The expression `++accountCounter` is crucial: it increments the static counter **first**, then returns the new value. This ensures each account gets a unique, sequential ID starting from 1001, 1002, 1003, and so on.

#### Accessing Static vs Instance Fields

This is where many programmers make mistakes. Static fields belong to the class, not to objects:

```java
// WRONG: accessing static field through an object
Account acc = new Account();
int counter = acc.accountCounter;  // Bad style - static accessed through instance

// CORRECT: accessing static field through the class
int counter = Account.accountCounter;  // Good - but only if accountCounter were public
```

Since `accountCounter` is private, external code can't access it directly. If you needed to expose it (for reporting, for example), you'd add a static getter:

```java
/**
 * Returns the current account counter value.
 * 
 * @return the next account ID that will be assigned
 */
public static int getAccountCounter() {
    return accountCounter;
}
```

Notice the getter is also `static`—it accesses static data, so it must be a static method.

#### Adding the Account ID Getter

Each account needs to expose its own ID through a regular (non-static) getter:

```java
/**
 * Returns the unique account ID.
 * 
 * @return the account ID
 */
public int getAccountId() {
    return accountId;
}
```

This is an instance method because it returns data specific to each account object.

### Adding Account Freeze Capability

Banks need the ability to freeze accounts when fraud is suspected or legal issues arise. A frozen account should reject all transactions until unfrozen.

#### The isFrozen Field

Add a new instance field to track freeze status:

```java
public class Account {
    private static int accountCounter = 1000;
    private int accountId;
    private double balance;
    
    /** Status of the account (active or frozen) */
    private boolean isFrozen;
}
```

Every account starts unfrozen, so initialize it in both constructors:

```java
public Account() {
    this.accountId = ++accountCounter;
    this.balance   = 0.0;
    this.isFrozen  = false;  // New accounts are active
}

public Account(double initialBalance) {
    if (initialBalance < 0) {
        System.out.println("Initial balance cannot be negative. Provided: €" + initialBalance);
    }
    this.accountId = ++accountCounter;
    this.balance   = initialBalance;
    this.isFrozen  = false;  // New accounts are active
}
```

#### Freeze and Unfreeze Methods

Add methods to control the freeze state:

```java
/**
 * Checks if the account is frozen.
 * 
 * @return true if account is frozen, false otherwise
 */
public boolean isFrozen() {
    return isFrozen;
}

/**
 * Freezes the account, preventing withdrawals and deposits.
 */
public void freezeAccount() {
    this.isFrozen = true;
    System.out.println("Account #" + accountId + " has been frozen.");
}

/**
 * Unfreezes the account, allowing normal operations.
 */
public void unfreezeAccount() {
    this.isFrozen = false;
    System.out.println("Account #" + accountId + " has been unfrozen.");
}
```

Notice how the freeze methods reference `accountId` in their messages—this is why having unique IDs matters for logging and auditing.

#### Enforcing Freeze in Transactions

Now update `deposit()` and `withdraw()` to check freeze status before processing:

```java
/**
 * Deposits the specified amount into the account.
 * 
 * @param amount the amount to deposit (must be positive)
 * @return true if deposit was successful, false otherwise
 */
public boolean deposit(double amount) {
    if (isFrozen) {
        System.out.println("Cannot deposit. Account #" + accountId + " is frozen.");
        return false;
    }
    
    if (amount > 0) {
        balance += amount;
        System.out.println("Deposited: €" + amount + " to Account #" + accountId);
        return true;
    } else {
        System.out.println("Deposit amount must be positive.");
        return false;
    }
}

/**
 * Withdraws the specified amount from the account.
 * 
 * @param amount the amount to withdraw (must be positive and not exceed balance)
 * @return true if withdrawal was successful, false otherwise
 */
public boolean withdraw(double amount) {
    if (isFrozen) {
        System.out.println("Cannot withdraw. Account #" + accountId + " is frozen.");
        return false;
    }
    
    if (amount <= 0) {
        System.out.println("Withdrawal amount must be positive.");
        return false;
    }
    
    if (amount > balance) {
        System.out.println("Insufficient funds. Current balance: €" + balance);
        return false;
    }
    
    balance -= amount;
    System.out.println("Withdrawn: €" + amount + " from Account #" + accountId);
    return true;
}
```

The freeze check comes **first** in both methods, creating a security barrier that prevents any transaction on frozen accounts.

#### Updating toString for Complete Information

The `toString()` method should now show the ID and freeze status:

```java
/**
 * Returns a string representation of the account.
 * 
 * @return a formatted string showing the account details
 */
@Override
public String toString() {
    String status = isFrozen ? "[FROZEN]" : "[ACTIVE]";
    return "Account{" +
            "id=" + accountId +
            ", balance=€" + String.format("%.2f", balance) +
            ", status=" + status +
            '}';
}
```

Now when you print an account, you see its complete state: ID, balance, and security status.

### Testing the Enhanced Account

Here's how the new features work in practice:

```java
public class Main {
    public static void main(String[] args) {
        // Create accounts - IDs auto-generated
        Account acc1 = new Account(500.0);
        Account acc2 = new Account(1000.0);
        
        System.out.println(acc1);  // Account{id=1001, balance=€500.00, status=[ACTIVE]}
        System.out.println(acc2);  // Account{id=1002, balance=€1000.00, status=[ACTIVE]}
        
        // Freeze an account
        acc1.freezeAccount();  // Account #1001 has been frozen.
        
        // Try transactions on frozen account
        acc1.deposit(100.0);   // Cannot deposit. Account #1001 is frozen.
        acc1.withdraw(50.0);   // Cannot withdraw. Account #1001 is frozen.
        
        // Unfreeze and retry
        acc1.unfreezeAccount(); // Account #1001 has been unfrozen.
        acc1.deposit(100.0);    // Deposited: €100.0 to Account #1001
        
        System.out.println(acc1);  // Account{id=1001, balance=€600.00, status=[ACTIVE]}
    }
}
```

### Why This Design Works

**Static for shared identity**: One counter ensures all accounts get unique, sequential IDs without manual coordination.

**Instance fields for object state**: Each account maintains its own balance, ID, and freeze status independently.

**Security layering**: Freeze checks at the method entry points create a single, consistent enforcement point.

**Clear representation**: The `toString()` method shows all critical information at a glance for debugging and logging.

The Account class now handles real banking requirements: automatic unique identification through static fields, and security controls through instance state. Next, you'll see how the Customer class uses collections to manage multiple accounts like these.

---

## Building Customer-Account Relationships with ArrayList

The original `Customer` class stored personal information but had no way to manage accounts. In real banking, customers open multiple accounts over time: checking accounts, savings accounts, investment accounts. Your code needs to reflect this one-to-many relationship: one customer, many accounts.

### The One-to-Many Relationship Problem

Consider what you'd need without collections:

```java
// Inflexible approach - what if customer needs more than 3 accounts?
private Account account1;
private Account account2;
private Account account3;
```

This doesn't scale. You'd need separate fields for every possible account, manual null checks everywhere, and custom logic for searching. Arrays would be slightly better but still require knowing the maximum account count in advance and manually shifting elements when removing accounts.

> ArrayList solves this elegantly. It's a dynamic, resizable collection that grows and shrinks automatically. 

You add accounts as needed, remove them when closed, and search without worrying about array bounds or null slots.

### Adding the ArrayList Import and Field

First, import ArrayList from the Java Collections Framework:

```java
import java.util.ArrayList;
```

Then add the collection field to Customer:

```java
public class Customer {
    private int id;
    private String name;
    private int age;
    private String address;
    
    /** List of accounts owned by this customer */
    private ArrayList<Account> accounts;
}
```

The syntax `ArrayList<Account>` uses **generics**—it tells Java this list will only hold `Account` objects. The compiler enforces this, preventing you from accidentally adding the wrong type of object.

### Initializing the ArrayList

Collections must be initialized before use. The constructor is the right place:

```java
/**
 * Constructs a new Customer with the specified details.
 * Initializes an empty list of accounts.
 * 
 * @param id the unique customer identifier
 * @param name the customer's full name
 * @param age the customer's age
 * @param address the customer's address
 */
public Customer(int id, String name, int age, String address) {
    this.id       = id;
    this.name     = name;
    this.age      = age;
    this.address  = address;
    this.accounts = new ArrayList<>();  // Start with empty list
}
```

The `new ArrayList<>()` creates an empty list ready to accept `Account` objects. The diamond operator `<>` lets Java infer the type from the field declaration, avoiding repetition.

### Understanding Encapsulation with Collections

Notice `accounts` is `private`. This is crucial, because external code shouldn't access the internal list directly. If you exposed it with a simple getter:

```java
// DANGEROUS: don't do this
public ArrayList<Account> getAccounts() {
    return accounts;  // Returns direct reference to internal list
}
```

Any code could then add, remove, or modify accounts without going through your validation logic. Instead, you provide controlled methods that maintain the integrity of the relationship.

### Adding Accounts: The addAccount Method

The first relationship method lets customers open new accounts:

```java
/**
 * Adds a new account to this customer's account list.
 * 
 * @param account the account to add
 * @return true if account was successfully added
 */
public boolean addAccount(Account account) {
    if (account == null) {
        System.out.println("Cannot add null account.");
        return false;
    }
    accounts.add(account);
    System.out.println("Account #" + account.getAccountId() + " added to customer " + name);
    return true;
}
```

This method demonstrates three key principles:

- **Validation**: Check for null before adding to prevent runtime errors later.
- **Encapsulation**: External code can't manipulate the list directly; it must go through this method.
- **Feedback**: Returns a boolean indicating success and prints a confirmation message.

The `add()` method is provided by ArrayList and appends the new account to the end of the list. No manual array management required.

### Removing Accounts: The removeAccount Method

When customers close accounts, you need to remove them from the list:

```java
/**
 * Removes an account from this customer's account list.
 * 
 * @param account the account to remove
 * @return true if account was found and removed, false otherwise
 */
public boolean removeAccount(Account account) {
    if (accounts.remove(account)) {
        System.out.println("Account #" + account.getAccountId() + " removed from customer " + name);
        return true;
    } else {
        System.out.println("Account not found for customer " + name);
        return false;
    }
}
```

The ArrayList `remove()` method searches for the object and removes it if found, returning `true` on success and `false` if the object wasn't in the list. This handles the search and removal in one operation.

### Searching for Accounts: The findAccount Method

Customers need to interact with specific accounts, so you need a search method:

```java
/**
 * Searches for an account by its account ID.
 * 
 * @param accountId the ID of the account to find
 * @return the Account if found, null otherwise
 */
public Account findAccount(int accountId) {
    for (Account account : accounts) {
        if (account.getAccountId() == accountId) {
            return account;
        }
    }
    return null;
}
```

This uses the **enhanced for-each loop**, Java's clean syntax for iterating over collections:

```java
for (Account account : accounts) {
    // account is each element in the accounts list, one at a time
}
```

Read this as "for each account in accounts." It's much cleaner than traditional index-based loops and works with any collection type.

The method performs a linear search: it checks each account until it finds a matching ID, then returns that account. If no match is found, it returns `null`.

### Getting the Account Count

A simple utility method to check how many accounts a customer has:

```java
/**
 * Returns the total number of accounts owned by this customer.
 * 
 * @return the number of accounts
 */
public int getAccountCount() {
    return accounts.size();
}
```

ArrayList's `size()` method returns the current number of elements—no manual counting needed.

### Connecting Customer and Account: Delegation Methods

> Now comes the powerful part: customers can perform banking operations by delegating to their accounts. This demonstrates **composition**

Customer doesn't duplicate Account's deposit/withdraw logic; it coordinates access to the right account.

#### Depositing to a Specific Account

```java
/**
 * Deposits money into a specific account.
 * 
 * @param accountId the ID of the account to deposit into
 * @param amount the amount to deposit
 * @return true if deposit was successful, false otherwise
 */
public boolean deposit(int accountId, double amount) {
    Account account = findAccount(accountId);
    if (account == null) {
        System.out.println("Account #" + accountId + " not found for customer " + name);
        return false;
    }
    return account.deposit(amount);
}
```

This method:

1. Searches for the account using `findAccount()`
2. Validates that the account exists
3. Delegates the actual deposit to the Account object

The Customer class doesn't know or care about balance validation, freeze checks, or how deposits work—that's Account's job. Customer just routes the operation to the right place.

#### Withdrawing from a Specific Account

The withdrawal method follows the same pattern:

```java
/**
 * Withdraws money from a specific account.
 * 
 * @param accountId the ID of the account to withdraw from
 * @param amount the amount to withdraw
 * @return true if withdrawal was successful, false otherwise
 */
public boolean withdraw(int accountId, double amount) {
    Account account = findAccount(accountId);
    if (account == null) {
        System.out.println("Account #" + accountId + " not found for customer " + name);
        return false;
    }
    return account.withdraw(amount);
}
```

Again, Customer acts as a coordinator, not a controller. Each class maintains its own responsibilities: Customer manages the collection of accounts, while Account handles its own balance and validation rules.

### Calculating Total Balance Across All Accounts

Customers often want to see their total holdings across all accounts:

```java
/**
 * Calculates the total balance across all accounts.
 * 
 * @return the sum of all account balances
 */
public double getTotalBalance() {
    double total = 0.0;
    for (Account account : accounts) {
        total += account.getBalance();
    }
    return total;
}
```

This demonstrates **aggregation**, that is combining data from multiple related objects. The for-each loop iterates through all accounts, asking each for its balance and accumulating the sum.

Notice that Customer doesn't access Account's private `balance` field directly (it can't—it's private). Instead, it uses the public `getBalance()` getter. Encapsulation is maintained across the relationship.

### Displaying All Accounts

A utility method to show a customer's complete account portfolio:

```java
/**
 * Displays all accounts owned by this customer.
 */
public void displayAllAccounts() {
    System.out.println("\n--- Accounts for " + name + " ---");
    if (accounts.isEmpty()) {
        System.out.println("No accounts found.");
    } else {
        for (Account account : accounts) {
            System.out.println("  " + account);
        }
        System.out.println("Total Balance: €" + String.format("%.2f", getTotalBalance()));
    }
}
```

The `isEmpty()` method checks if the list has zero elements. If accounts exist, the loop prints each one (automatically calling Account's `toString()` method), then shows the total balance.

### Updating Customer's toString

The Customer's string representation should now show account information:

```java
/**
 * Returns a string representation of the customer.
 * 
 * @return a formatted string containing customer details
 */
@Override
public String toString() {
    return "Customer{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", age=" + age +
            ", address='" + address + '\'' +
            ", accounts=" + accounts.size() +
            ", totalBalance=€" + String.format("%.2f", getTotalBalance()) +
            '}';
}
```

This provides a compact summary: how many accounts and the total balance across all of them.

### Testing the Complete Relationship

Here's how Customer and Account work together:

```java
public class Main {
    public static void main(String[] args) {
        // Create a customer
        Customer ada = new Customer(1815, "Ada Lovelace", 36, "London, England");
        
        // Create and add accounts
        Account checking = new Account(1000.0);
        Account savings = new Account(5000.0);
        
        ada.addAccount(checking);  // Account #1001 added to customer Ada Lovelace
        ada.addAccount(savings);   // Account #1002 added to customer Ada Lovelace
        
        System.out.println(ada);  // Shows 2 accounts, total €6000.00
        
        // Deposit through Customer
        ada.deposit(1001, 500.0);  // Deposited: €500.0 to Account #1001
        
        // Withdraw through Customer
        ada.withdraw(1002, 200.0); // Withdrawn: €200.0 from Account #1002
        
        // Display complete account information
        ada.displayAllAccounts();
        // --- Accounts for Ada Lovelace ---
        //   Account{id=1001, balance=€1500.00, status=[ACTIVE]}
        //   Account{id=1002, balance=€4800.00, status=[ACTIVE]}
        // Total Balance: €6300.00
        
        // Find and freeze a specific account
        Account found = ada.findAccount(1001);
        if (found != null) {
            found.freezeAccount();  // Account #1001 has been frozen.
        }
        
        // Try to deposit to frozen account
        ada.deposit(1001, 100.0);  // Cannot deposit. Account #1001 is frozen.
    }
}
```

### Why This Design Works

- **Single Responsibility**: Customer manages the account collection; Account manages its own money and state.
- **Encapsulation**: Private list with public methods prevents unauthorized manipulation.
- **Delegation**: Customer doesn't duplicate Account logic; it routes operations to the right account.
- **Composition**: The Customer-Account relationship reflects real-world banking naturally.
- **Scalability**: ArrayList grows dynamically as customers open more accounts without code changes.

The Customer class has evolved from a simple data container into a sophisticated coordinator that manages a one-to-many relationship with accounts, all while maintaining clean separation of concerns and proper encapsulation.

## Summary

You've transformed a basic banking system into one that handles real-world complexity. The upgrades touched two critical areas: class-level identity management and dynamic object relationships.

### Key Concepts Mastered

#### **Static fields for shared data**
The `accountCounter` generates unique IDs at the class level, demonstrating when data belongs to the class rather than individual objects. Static fields are shared across all instances, making them perfect for ID generation and shared configuration.

#### **ArrayList for dynamic relationships**
The `Customer` class now manages a growing collection of accounts using `ArrayList<Account>`, eliminating the rigid constraints of arrays. Collections grow and shrink as customers open and close accounts, reflecting real banking operations.

#### One-to-many relationships 
Customer coordinates multiple accounts through encapsulated methods—`addAccount()`, `removeAccount()`, `findAccount()`, and `getTotalBalance()`—demonstrating how objects collaborate while maintaining clear responsibilities.

#### Delegation over duplication 
Customer doesn't reimplement deposit and withdrawal logic; it delegates to Account objects. This separation of concerns keeps each class focused on its own responsibilities.

#### Enhanced iteration patterns
The for-each loop provides clean, readable iteration over collections, making code easier to understand and maintain.

### Complete Code

Find the complete working code organized by version:

- [v1_basic_classes](github.com/evisp/java-oop-course/week2_classes_objects) - Starting point with simple Customer and Account
- [v2_basic_collections](github.com/evisp/java-oop-course/week3_collections) - Enhanced version with static IDs, ArrayList, and relationships

