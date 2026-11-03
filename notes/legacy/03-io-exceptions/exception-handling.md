# Exception Handling: Failing Fast and Recovering Gracefully

## Introduction - When Silent Failures Create Hidden Problems

In our current banking system (Version 5), operations handle errors inefficiently. Operational methods like `withdraw()` and `transfer()` return `boolean` and print messages:

```java
public boolean withdraw(double amount) {
    if (isFrozen()) {
        System.out.println("Cannot withdraw. Account is frozen.");
        return false;
    }
    if (amount <= 0) {
        System.out.println("Withdrawal amount must be positive.");
        return false;
    }
    // ...
}
```

This creates problems. The calling code sees only `false` without any indication of **why** it failed:

```java
boolean success = account.withdraw(500.0);
if (!success) {
    // What went wrong? Frozen account? Invalid amount? Insufficient funds?
    // No way to know - just a boolean!
}
```

Worse, the return value can be silently ignored:

```java
account.withdraw(1000000.0);  // Returns false, but who checks?
// Code continues as if withdrawal succeeded!
```

Real banking systems need structured error information. Insufficient funds requires different handling than a frozen account or invalid input. Each situation demands specific responses—offering overdraft protection, escalating to customer service, or rejecting invalid parameters.

**This tutorial introduces systematic exception handling for banking operations.** You'll replace boolean returns with exceptions, use standard Java exception types for error classification, and implement try-catch blocks for intelligent recovery. The goal: make failures impossible to ignore and provide context for proper handling.

## The Problems with Boolean Returns

### Silent Failures

The most dangerous issue: callers can ignore boolean returns.

```java
account.withdraw(5000.0);      // Maybe succeeds, maybe fails
customer.addAccount(null);     // Definitely fails, returns false
// Code continues executing with corrupted state!
```

Nothing forces checking the return value. The program continues, potentially with money not withdrawn or accounts not added.

### All Failures Look Identical

Look at `SavingsAccount.withdraw()`:

```java
public boolean withdraw(double amount) {
    if (isFrozen()) {
        System.out.println("Account is frozen.");
        return false;
    }
    if (amount <= 0) {
        System.out.println("Amount must be positive.");
        return false;
    }
    if (balance - amount < minimumBalance) {
        System.out.println("Would violate minimum balance.");
        return false;
    }
    // ... perform withdrawal
    return true;
}
```

Three completely different problems—frozen account (state error), invalid amount (programming error), insufficient funds (business rule); all return the same `false`. The caller has no way to distinguish them or respond appropriately.

### Lost Context

When `transfer()` fails deep in a call chain, diagnostic information disappears:

```java
// In Bank.transfer()
if (fromAccount.getBalance() < amount) {
    System.out.println("Insufficient funds.");
    return false;  // Lost: current balance, requested amount, shortfall
}
```

By the time `false` reaches the caller, all valuable data—balances, amounts, account IDs—has been discarded. This information could enable intelligent responses: "You're trying to withdraw €600 but can withdraw up to €400."

## Understanding Exceptions

### What Is an Exception?

An exception is an object representing an error. When thrown, it immediately stops normal execution and jumps to the nearest matching handler. Unlike boolean returns, exceptions **cannot be ignored**; if not caught, they terminate the program with a clear error message.

```java
throw new IllegalArgumentException("Something went wrong");
System.out.println("This never executes");  // Unreachable
```

### Java's Exception Hierarchy

```
Throwable
├── Error (system-level problems, don't catch)
└── Exception
    ├── RuntimeException (unchecked - programming errors)
    │   ├── IllegalArgumentException
    │   ├── IllegalStateException
    │   └── NullPointerException
    └── IOException, SQLException (checked - must handle)
```

For banking operations, we use **RuntimeException** subclasses. These are **unchecked**—callers aren't forced to catch them, but they can't be silently ignored. If uncaught, the program crashes with a stack trace pointing to the problem.

### Exception Types for Banking

**IllegalArgumentException**: Invalid input parameters
```java
throw new IllegalArgumentException("Withdrawal amount must be positive: " + amount);
```

**IllegalStateException**: Operation not allowed in current state
```java
throw new IllegalStateException("Cannot withdraw from frozen account #" + accountId);
```

**NullPointerException**: Null references where objects expected
```java
if (customer == null) {
    throw new NullPointerException("Customer cannot be null");
}
```

## Throwing Exceptions in Banking Operations

### Replacing Boolean Returns in withdraw()

**Before (Version 5):**
```java
public boolean withdraw(double amount) {
    if (isFrozen()) {
        System.out.println("Cannot withdraw. Account is frozen.");
        return false;
    }
    if (amount <= 0) {
        System.out.println("Withdrawal amount must be positive.");
        return false;
    }
    if (balance - amount < minimumBalance) {
        System.out.println("Would violate minimum balance.");
        return false;
    }
    // ... perform withdrawal
}
```

**After (Version 6 with Exceptions):**
```java
public void withdraw(double amount) {
    if (isFrozen()) {
        throw new IllegalStateException(
            "Cannot withdraw from frozen account #" + accountId
        );
    }
    
    if (amount <= 0) {
        throw new IllegalArgumentException(
            "Withdrawal amount must be positive. Provided: €" + amount
        );
    }
    
    if (balance - amount < minimumBalance) {
        throw new IllegalStateException(
            String.format("Insufficient funds. Balance: €%.2f, Minimum: €%.2f, Requested: €%.2f",
                         balance, minimumBalance, amount)
        );
    }
    
    // Perform withdrawal
    balance -= amount;
    Transaction transaction = new Transaction(
        Transaction.TransactionType.WITHDRAW,
        amount,
        accountId,
        balance,
        "Withdrawal"
    );
    transactionHistory.add(transaction);
}
```

**Key changes:**

- Return type changed from `boolean` to `void`
- Error messages replaced with `throw` statements
- Each error gets an appropriate exception type
- Messages include all relevant values (amounts, IDs, limits)
- No return value needed—if method completes, it succeeded

### Good Exception Messages

Bad messages:
```java
throw new IllegalArgumentException("Invalid input");
throw new IllegalStateException("Operation failed");
```

Good messages:
```java
throw new IllegalArgumentException(
    "Withdrawal amount must be positive. Provided: €" + amount
);

throw new IllegalStateException(
    String.format("Cannot withdraw €%.2f from account #%d. " +
                 "Balance: €%.2f, Minimum required: €%.2f",
                 amount, accountId, balance, minimumBalance)
);
```

Include: what went wrong, the values causing the problem, the constraint violated, and enough context to diagnose the issue.

## Changes to Core Operations

### SavingsAccount.withdraw() with Minimum Balance

```java
@Override
public void withdraw(double amount) {
    if (isFrozen()) {
        throw new IllegalStateException(
            "Cannot withdraw from frozen Savings Account #" + getAccountId()
        );
    }
    
    if (amount <= 0) {
        throw new IllegalArgumentException(
            "Withdrawal amount must be positive. Provided: €" + amount
        );
    }
    
    // Savings-specific check
    if (balance - amount < minimumBalance) {
        throw new IllegalStateException(
            String.format("Withdrawal would violate minimum balance. " +
                         "Current: €%.2f, Minimum: €%.2f, Maximum withdrawal: €%.2f",
                         balance, minimumBalance, balance - minimumBalance)
        );
    }
    
    balance -= amount;
    transactionHistory.add(new Transaction(
        Transaction.TransactionType.WITHDRAW, amount, getAccountId(), balance, "Withdrawal"
    ));
}
```

The detailed message tells the user exactly how much they **can** withdraw.

### CheckingAccount.withdraw() with Overdraft

```java
@Override
public void withdraw(double amount) {
    if (isFrozen()) {
        throw new IllegalStateException("Account #" + getAccountId() + " is frozen");
    }
    
    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive: €" + amount);
    }
    
    // Checking-specific: allow overdraft
    double availableBalance = balance + overdraftLimit;
    if (amount > availableBalance) {
        throw new IllegalStateException(
            String.format("Withdrawal exceeds available balance. " +
                         "Balance: €%.2f, Overdraft: €%.2f, Available: €%.2f",
                         balance, overdraftLimit, availableBalance)
        );
    }
    
    balance -= amount;
    transactionHistory.add(new Transaction(
        Transaction.TransactionType.WITHDRAW, amount, getAccountId(), balance,
        "Withdrawal" + (balance < 0 ? " (using overdraft)" : "")
    ));
}
```

### Bank.transfer() with Exceptions

```java
public void transfer(int fromCustomerId, int fromAccountId,
                    int toCustomerId, int toAccountId, double amount) {
    
    if (amount <= 0) {
        throw new IllegalArgumentException("Transfer amount must be positive: €" + amount);
    }
    
    Customer fromCustomer = findCustomer(fromCustomerId);
    if (fromCustomer == null) {
        throw new IllegalArgumentException("Source customer not found. ID: " + fromCustomerId);
    }
    
    Account fromAccount = fromCustomer.findAccount(fromAccountId);
    if (fromAccount == null) {
        throw new IllegalArgumentException(
            "Source account #" + fromAccountId + " not found for customer #" + fromCustomerId
        );
    }
    
    Customer toCustomer = findCustomer(toCustomerId);
    if (toCustomer == null) {
        throw new IllegalArgumentException("Destination customer not found. ID: " + toCustomerId);
    }
    
    Account toAccount = toCustomer.findAccount(toAccountId);
    if (toAccount == null) {
        throw new IllegalArgumentException(
            "Destination account #" + toAccountId + " not found for customer #" + toCustomerId
        );
    }
    
    if (fromAccount.isFrozen() || toAccount.isFrozen()) {
        throw new IllegalStateException("Cannot transfer: one or both accounts are frozen");
    }
    
    // Perform transfer - withdraw/deposit will throw if they fail
    fromAccount.withdraw(amount);
    toAccount.deposit(amount);
    
    System.out.println("Transfer successful: €" + amount + 
                      " from Account #" + fromAccountId + 
                      " to Account #" + toAccountId);
}
```

Notice `transfer()` doesn't check balance explicitly; `withdraw()` will throw if insufficient funds. Exceptions automatically propagate up the call stack.

## Catching Exceptions with Try-Catch

### Try-Catch Basics

The `try-catch` block handles exceptions gracefully:

```java
try {
    // Code that might throw exceptions
    account.withdraw(500.0);
    System.out.println("Withdrawal successful");
} catch (IllegalStateException e) {
    // Handle state errors (frozen account, insufficient funds)
    System.out.println("Withdrawal failed: " + e.getMessage());
} catch (IllegalArgumentException e) {
    // Handle input validation errors
    System.out.println("Invalid amount: " + e.getMessage());
}
```

**How it works:**

1. Java executes the `try` block
2. If an exception is thrown, Java immediately stops and looks for a matching `catch` block
3. The first matching `catch` executes
4. After the `catch`, execution continues normally

If no exception occurs, all `catch` blocks are skipped.

### Multiple Exception Types

> Order matters more specific exceptions first:

```java
try {
    bank.transfer(1815, 1001, 1912, 1002, 5000.0);
} catch (IllegalArgumentException e) {
    // Invalid input (bad IDs, negative amount)
    System.out.println("Invalid transfer: " + e.getMessage());
} catch (IllegalStateException e) {
    // State problems (frozen account, insufficient funds)
    System.out.println("Transfer not allowed: " + e.getMessage());
} catch (Exception e) {
    // Catch-all for unexpected exceptions
    System.out.println("Unexpected error: " + e.getMessage());
    e.printStackTrace();
}
```

### Practical Exception Handling in BankSystemMain

```java
public static void main(String[] args) {
    Bank bank = new Bank("Turing National Bank");
    
    try {
        Customer ada = new Customer(1815, "Ada Lovelace", 36, "London");
        bank.addCustomer(ada);
        
        SavingsAccount adaSavings = bank.openSavingsAccount(1815, 5000.0, 0.03);
        CheckingAccount adaChecking = bank.openCheckingAccount(1815, 2000.0, 500.0);
        
        // Successful operations
        try {
            adaSavings.deposit(1000.0);
            System.out.println("✓ Deposit successful\n");
        } catch (Exception e) {
            System.out.println("✗ Deposit failed: " + e.getMessage() + "\n");
        }
        
        // This fails - amount too large
        try {
            adaSavings.withdraw(10000.0);
        } catch (IllegalStateException e) {
            System.out.println("✗ Withdrawal failed: " + e.getMessage() + "\n");
        }
        
        // This fails - negative amount
        try {
            adaChecking.withdraw(-50.0);
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Invalid amount: " + e.getMessage() + "\n");
        }
        
        // Freeze and try operation
        adaSavings.freezeAccount();
        try {
            adaSavings.deposit(100.0);
        } catch (IllegalStateException e) {
            System.out.println("✗ Frozen account: " + e.getMessage() + "\n");
        }
        
        // Unfreeze and retry
        adaSavings.unfreezeAccount();
        adaSavings.deposit(100.0);
        System.out.println("✓ Deposit successful after unfreezing\n");
        
    } catch (IllegalArgumentException e) {
        System.out.println("Setup failed: " + e.getMessage());
    }
}
```

### Letting Exceptions Propagate

Sometimes the best strategy is **not to catch** at the low level. Let exceptions propagate to higher-level code with more context:

```java
// Low-level - just throws
public void processTransaction(int customerId, int accountId, double amount) {
    Customer customer = findCustomer(customerId);
    if (customer == null) {
        throw new IllegalArgumentException("Customer #" + customerId + " not found");
    }
    
    Account account = customer.findAccount(accountId);
    if (account == null) {
        throw new IllegalArgumentException("Account #" + accountId + " not found");
    }
    
    account.withdraw(amount);  // Let it propagate
}

// High-level - catches and handles
public void processUserRequest(int customerId, int accountId, double amount) {
    try {
        processTransaction(customerId, accountId, amount);
        System.out.println("Transaction processed successfully");
    } catch (IllegalArgumentException e) {
        System.out.println("Transaction rejected: " + e.getMessage());
        // Log, notify user, update UI
    } catch (IllegalStateException e) {
        System.out.println("Transaction failed: " + e.getMessage());
        // Offer alternatives (overdraft, partial withdrawal)
    }
}
```

## Exception Handling Best Practices

### 1. Be Specific with Exception Types

```java
// Bad
throw new Exception("Something went wrong");

// Good
throw new IllegalStateException("Account #" + accountId + " is frozen");
```

### 2. Include Context in Messages

```java
// Bad
throw new IllegalArgumentException("Invalid amount");

// Good
throw new IllegalArgumentException("Amount must be positive. Provided: €" + amount);
```

### 3. Don't Swallow Exceptions

```java
// Bad - silent failure
try {
    account.withdraw(amount);
} catch (Exception e) {
    // Nothing - worst possible!
}

// Good
try {
    account.withdraw(amount);
} catch (IllegalStateException e) {
    System.out.println("Failed: " + e.getMessage());
}
```

### 4. Catch Specific Exceptions First

```java
// Bad - too broad
try {
    bank.transfer(...);
} catch (Exception e) {
    System.out.println("Something failed");
}

// Good - specific handling
try {
    bank.transfer(...);
} catch (IllegalArgumentException e) {
    // Handle invalid input
} catch (IllegalStateException e) {
    // Handle state problems
}
```

## Summary

You've transformed the banking system from **silent failures** to **explicit error handling**. Operations that returned boolean now throw exceptions with rich context.

### Key Concepts Mastered

#### Exceptions cannot be ignored
Unlike boolean returns, uncaught exceptions crash the program. This forces explicit acknowledgment and handling. Silent bugs become loud crashes with clear error messages.

#### Exception types classify failures
`IllegalArgumentException` for invalid input, `IllegalStateException` for violated preconditions, `NullPointerException` for unexpected nulls. Each type signals a different problem requiring different handling.

#### Exception messages carry context
Instead of `false`, exceptions include all relevant data: account IDs, balances, amounts, limits. Callers can use these values for recovery strategies or user feedback.

#### Try-catch provides structured handling
Multiple `catch` blocks handle different exception types differently. Code explicitly states "here's how I handle each type of failure."

#### Fail-fast prevents corrupted state
Throwing exceptions immediately stops execution. Invalid operations never partially complete. Account balances never enter inconsistent states.

### Design Benefits

Exception-based error handling provides:

- **Safety**: Invalid operations cannot silently fail and corrupt data
- **Clarity**: Error types and messages explain exactly what went wrong
- **Testability**: Assert specific exception types instead of parsing console output
- **Robustness**: Calling code must acknowledge failures, cannot ignore them
- **Debuggability**: Stack traces show exact error location and call chain

### When to Use Exceptions

**Use exceptions for:**
- Invalid arguments that should never occur
- Violated preconditions (frozen accounts, insufficient funds)
- Unexpected states (null where object expected)
- Critical operations where failure needs attention

**Use return values for:**
- Expected "not found" cases where null is valid
- Optional operations where failure is normal
- Boolean predicates testing conditions

The banking system now uses exceptions consistently: constructors throw for invalid arguments, operations throw for precondition violations, and calling code uses try-catch for graceful recovery. Error messages include complete diagnostic information, making the system production-ready with robust error management.
