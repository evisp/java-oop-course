# File I/O and Persistence: Surviving Program Restarts

## Introduction - When Memory Isn't Enough

In your current banking system (Version 5), all data lives in RAM. Create customers, open accounts, perform transactions—everything works perfectly. Until the program ends:

```java
Bank bank = new Bank("Turing National Bank");
Customer ada = new Customer(1815, "Ada Lovelace", 36, "London");
bank.addCustomer(ada);

SavingsAccount adaSavings = bank.openSavingsAccount(1815, 5000.0, 0.03);
adaSavings.deposit(1000.0);
adaSavings.withdraw(500.0);

// Program ends - EVERYTHING IS GONE
// All customers, all accounts, all transaction history - lost forever
```

Run the program again, and the bank is empty. Ada Lovelace never existed. Her €5500 balance? Vanished. The transaction showing she deposited €1000? No record. The entire banking history exists only as long as the program runs.

This is the **volatility of RAM**. Random Access Memory is temporary working storage. It requires continuous power. When the program terminates—normally or through a crash; every variable, every object, every data structure disappears. For a toy program, this might be acceptable. For a real banking system, it's catastrophic.

Real banks persist data. When you check your balance today, you see yesterday's deposits. When the bank's systems restart, your account still exists. Transaction history from months ago remains accessible. The system maintains **state across sessions**—the technical term for "remembering things after turning off and on again."

> Programs achieve persistence through **file I/O**

Input/Output operations that read from and write to disk storage. Unlike RAM, disk storage is **persistent**: data written to disk remains until explicitly deleted, surviving program termination, system restarts, even power failures.

Your banking system needs two capabilities:

1. **Save the complete bank state** to disk before the program ends
2. **Load the saved state** when the program starts again

But there's a design choice: **how** to save the data. You could write code that manually converts every Customer, every Account, every Transaction into text format, then reverse the process when loading. This is tedious, error-prone, and breaks every time you add a field to a class.

Or you could use Java's **serialization**; a built-in mechanism that automatically converts entire object graphs (Bank containing Customers containing Accounts containing Transactions) into bytes and back. For human-readable exports, e.g., CSV files that open in Excel, or text reports for auditing, you'll use text file formats.

**This tutorial introduces file I/O for banking persistence.** You'll see how serialization saves complex object graphs, how text formats provide human-readable exports, and how try-with-resources ensures files close properly even when exceptions occur. The goal: transform your banking system from a disposable toy into a production system that survives restarts.

## Understanding File Types: Text vs Binary

Before writing file I/O code, you need to understand the two fundamental file categories.


![Text vs Binary](https://i.imgur.com/LT8vm86.png)

### Text Files: Human-Readable Characters

Text files store data as **characters** encoded in a standard format (UTF-8, ASCII, etc.). You can open them with any text editor (Notepad, VS Code, TextEdit) and read the contents. Every byte represents a printable character, newline, or space.

Example CSV file (text):
```bash 
CustomerID,Name,Email,Balance
1815,Ada Lovelace,ada@example.com,5500.00
1912,Alan Turing,alan@example.com,3200.00
```

Advantages of text files:

- **Human-readable**: Open in any text editor or spreadsheet program
- **Portable**: Works across different programming languages and systems
- **Debuggable**: Inspect file contents directly to verify correctness
- **Parseable**: Easy to process with standard text-processing tools

Disadvantages:

- **Verbose**: Numbers like `5500.00` take 7 bytes as text vs 4 bytes as binary
- **Requires parsing**: Must convert string `"5500.00"` back to double `5500.0`
- **Structure is manual**: You write code to format and parse each field
- **Relationship complexity**: Nested objects (Bank → Customer → Account → Transaction) require complex formatting

### Binary Files: Compact Machine Format

Binary files store data as **raw bytes** optimized for efficiency, not readability. Open them in a text editor and you see gibberish—unprintable characters, strange symbols. But programs can read them quickly and compactly.

Example serialized Bank object (binary—this is hexadecimal representation):
```bash
AC ED 00 05 73 72 00 0D 76 36 5F 66 69 6C 65 5F ...
```

Advantages of binary files:

- **Compact**: Numbers stored in native format (4 bytes for int, 8 for double)
- **Fast**: No conversion between text and numbers—direct memory representation
- **Structure preserves**: Java serialization automatically saves object relationships
- **No manual formatting**: Serialization handles the entire object graph automatically

Disadvantages:

- **Not human-readable**: Cannot inspect contents without specialized tools
- **Language-specific**: Java serialization creates files only Java can read
- **Version-sensitive**: Changing class structure can break deserialization
- **Opaque errors**: Corrupted binary files fail mysteriously

### When to Use Each

**Use binary serialization when:**

- Saving complete program state (entire Bank object)
- Need to preserve complex object graphs with relationships
- Want automatic handling of nested objects
- Priority is convenience and completeness

**Use text files when:**

- Exporting data for humans to read (reports, audit trails)
- Sharing data with other systems (Excel, databases, web services)
- Need to inspect or edit data manually
- Want format that survives program changes

Your banking system uses **both**: serialization for complete saves/loads, text files for exports and reports.

## Making Classes Serializable

To save objects using Java serialization, classes must implement the `Serializable` interface.

### The Serializable Interface

`Serializable` is a **marker interface**; it has no methods. It simply tells Java "this class permits serialization":

```java
public class Bank implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String bankName;
    private ArrayList<Customer> customers;
    
    // All methods unchanged
}
```

Key points:

1. **Add `implements Serializable`** to the class declaration
2. **Add `serialVersionUID`**—a version identifier for the class structure
3. **All fields must be serializable**—primitives, Strings, and other Serializable objects

The `serialVersionUID` is crucial for version control:

```java
private static final long serialVersionUID = 1L;
```

When deserializing, Java checks if the saved `serialVersionUID` matches the current class. If they differ, deserialization fails with `InvalidClassException`. Increment this when you make incompatible changes (removing fields, changing types).

### Cascading Serialization

> When you serialize a `Bank` object, Java automatically serializes everything it references:

```
Bank (serializable)
 ├── ArrayList<Customer> customers
 │    ├── Customer (must be serializable)
 │    │    ├── ArrayList<Account> accounts
 │    │    │    ├── Account (must be serializable)
 │    │    │    │    ├── ArrayList<Transaction> transactionHistory
 │    │    │    │    │    └── Transaction (must be serializable)
```



If **any** class in this graph isn't serializable, serialization fails with `NotSerializableException`. So all domain classes need `implements Serializable`:

```java
public class Bank implements Serializable { ... }
public class Customer implements Serializable { ... }
public abstract class Account implements Auditable, Serializable { ... }
public class SavingsAccount extends Account { ... }  // Inherits Serializable
public class CheckingAccount extends Account { ... } // Inherits Serializable
public class Transaction implements Serializable { ... }
```

Once marked serializable, a single `writeObject(bank)` call saves the entire object graph.

## Serialization: Saving Complete Bank State

The `BankDataManager` class centralizes all file operations. Let's examine serialization methods.

### Saving a Bank Object

```java
public static void saveBank(Bank bank, String filename) throws IOException {
    // Ensure directory exists
    File dir = new File(DATA_DIR);  // DATA_DIR = "bank_data/"
    if (!dir.exists()) {
        dir.mkdirs();
    }
    
    String fullPath = DATA_DIR + filename;
    try (ObjectOutputStream out = new ObjectOutputStream(
            new BufferedOutputStream(new FileOutputStream(fullPath)))) {
        out.writeObject(bank);
        System.out.println("✓ Bank data saved successfully to: " + fullPath);
    }
}
```

**How it works:**

1. **Create directory** if it doesn't exist—`mkdirs()` creates all parent directories
2. **Open `ObjectOutputStream`** wrapping a `FileOutputStream`
   - `FileOutputStream` writes bytes to the file
   - `BufferedOutputStream` adds buffering for efficiency
   - `ObjectOutputStream` converts objects to bytes
3. **Write the object** with `writeObject(bank)`—serializes entire Bank graph
4. **Automatic closure** via try-with-resources (more on this below)

The `throws IOException` declaration means this method doesn't catch I/O exceptions—it lets them propagate to the caller. The caller decides how to handle file errors.

### Loading a Bank Object

```java
public static Bank loadBank(String filename) throws IOException, ClassNotFoundException {
    String fullPath = DATA_DIR + filename;
    try (ObjectInputStream in = new ObjectInputStream(
            new BufferedInputStream(new FileInputStream(fullPath)))) {
        Bank bank = (Bank) in.readObject();
        System.out.println("Bank data loaded successfully from: " + fullPath);
        return bank;
    }
}
```

**How it works:**

1. **Open `ObjectInputStream`** wrapping a `FileInputStream`
   - `FileInputStream` reads bytes from the file
   - `BufferedInputStream` adds buffering for efficiency
   - `ObjectInputStream` converts bytes back to objects
2. **Read the object** with `readObject()`—returns `Object`, must cast to `Bank`
3. **Return the reconstructed Bank**—complete with all customers, accounts, transactions

This method throws **two** exceptions:

- `IOException`: File doesn't exist, permission denied, disk full, etc.
- `ClassNotFoundException`: The saved class isn't available at runtime (rare)

### Creating Timestamped Backups

```java
public static String backupBank(Bank bank) throws IOException {
    String timestamp = LocalDateTime.now().format(FORMATTER);
    String backupFilename = "bank_backup_" + timestamp + ".ser";
    saveBank(bank, backupFilename);
    return backupFilename;
}
```

This creates backups with timestamps in the filename:

```bash
bank_backup_2025-11-29_14-30-00.ser
bank_backup_2025-11-29_14-35-00.ser
bank_backup_2025-11-29_14-40-00.ser
```

Each backup is a complete snapshot. If recent changes corrupt data, load an earlier backup.

## Text File Exports: CSV Format

For human-readable data sharing, CSV (Comma-Separated Values) is ideal. Spreadsheet programs like Excel and Google Sheets open CSV files natively.

### Exporting Transaction History to CSV

```java
public static void exportTransactionsToCSV(Account account, String filename) throws IOException {
    File dir = new File(DATA_DIR);
    if (!dir.exists()) {
        dir.mkdirs();
    }
    
    String fullPath = DATA_DIR + filename;
    try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {
        // Write CSV header
        writer.println("Transaction ID,Type,Amount,Account ID,Balance After,Timestamp,Description");
        
        // Write each transaction
        for (Transaction t : account.getAuditTrail()) {
            writer.printf("%d,%s,%.2f,%d,%.2f,%s,\"%s\"%n",
                t.getTransactionId(),
                t.getType(),
                t.getAmount(),
                t.getAccountId(),
                t.getBalanceAfter(),
                t.getTimestamp(),
                t.getDescription()
            );
        }
        
        System.out.println("  Transactions exported to: " + fullPath);
        System.out.println("  Total transactions: " + account.getTransactionCount());
    }
}
```

**Key points:**

1. **PrintWriter** wraps `FileWriter` for convenient text output methods
2. **CSV header** describes columns—first line is field names
3. **Each transaction** becomes one line with comma-separated values
4. **Quotes around description** handle descriptions containing commas
5. **printf formatting** ensures numbers display with two decimal places

Generated CSV file:
```bash
Transaction ID,Type,Amount,Account ID,Balance After,Timestamp,Description
10001,DEPOSIT,5000.00,1001,5000.00,2025-11-29T14:30:15,"Initial deposit"
10002,DEPOSIT,1000.00,1001,6000.00,2025-11-29T14:31:22,"Deposit"
10003,WITHDRAW,500.00,1001,5500.00,2025-11-29T14:32:45,"Withdrawal"
```

Open this in Excel and you see a formatted table with sortable columns.

### Exporting All Customer Transactions

```java
public static void exportCustomerTransactions(Customer customer, String filename) throws IOException {
    String fullPath = DATA_DIR + filename;
    try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {
        writer.println("Account ID,Account Type,Transaction ID,Type,Amount,Balance After,Timestamp,Description");
        
        for (Account account : customer.getAccounts()) {
            for (Transaction t : account.getAuditTrail()) {
                writer.printf("%d,%s,%d,%s,%.2f,%.2f,%s,\"%s\"%n",
                    account.getAccountId(),
                    account.getAccountTypeName(),
                    t.getTransactionId(),
                    t.getType(),
                    t.getAmount(),
                    t.getBalanceAfter(),
                    t.getTimestamp(),
                    t.getDescription()
                );
            }
        }
        
        System.out.println("  Customer transactions exported to: " + fullPath);
    }
}
```

This creates a comprehensive CSV showing all transactions across all accounts for one customer.

## Text File Exports: Formatted Reports

For audit trails and formal documentation, formatted text reports are more readable than CSV.

### Generating Account Audit Reports

```java
public static void generateAuditReportFile(Account account, String filename) throws IOException {
    String fullPath = DATA_DIR + filename;
    try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {
        writer.println("═══════════════════════════════════════════════════════════════");
        writer.println("               ACCOUNT AUDIT REPORT                            ");
        writer.println("═══════════════════════════════════════════════════════════════");
        writer.println("Generated: " + LocalDateTime.now().format(FORMATTER));
        writer.println();
        
        writer.println("Account Type: " + account.getAccountTypeName());
        writer.println("Account ID: " + account.getAccountId());
        writer.println("Current Balance: €" + String.format("%.2f", account.getBalance()));
        writer.println("Account Status: " + (account.isFrozen() ? "FROZEN" : "ACTIVE"));
        writer.println("Total Transactions: " + account.getTransactionCount());
        writer.println();
        
        writer.println("═══════════════════════════════════════════════════════════════");
        writer.println("              TRANSACTION HISTORY                              ");
        writer.println("═══════════════════════════════════════════════════════════════");
        
        if (account.getTransactionCount() == 0) {
            writer.println("No transactions recorded.");
        } else {
            for (Transaction t : account.getAuditTrail()) {
                writer.println();
                writer.println("Transaction #" + t.getTransactionId());
                writer.println("  Type: " + t.getType());
                writer.println("  Amount: €" + String.format("%.2f", t.getAmount()));
                writer.println("  Balance After: €" + String.format("%.2f", t.getBalanceAfter()));
                writer.println("  Timestamp: " + t.getTimestamp());
                writer.println("  Description: " + t.getDescription());
            }
        }
        
        writer.println();
        writer.println("═══════════════════════════════════════════════════════════════");
        writer.println("                   END OF REPORT                              ");
        writer.println("═══════════════════════════════════════════════════════════════");
        
        System.out.println("  Audit report generated: " + fullPath);
    }
}
```

This formatted report is professional, easy to read, and suitable for archiving or printing.

## Try-With-Resources: Automatic Resource Management

Notice the pattern in all file operations:

```java
try (ObjectOutputStream out = new ObjectOutputStream(...)) {
    // Use the stream
}  // Automatically closed here
```

This is **try-with-resources**—Java's mechanism for automatic resource cleanup.

### The Problem Without Try-With-Resources

Before Java 7, you had to manually close resources in a `finally` block:

```java
ObjectOutputStream out = null;
try {
    out = new ObjectOutputStream(new FileOutputStream("bank.ser"));
    out.writeObject(bank);
} catch (IOException e) {
    System.out.println("Error: " + e.getMessage());
} finally {
    if (out != null) {
        try {
            out.close();  // Must close in finally
        } catch (IOException e) {
            // What do we do with THIS exception?
        }
    }
}
```

This is verbose and error-prone. If you forget the `finally` block, the file remains open; a **resource leak**. Operating systems limit the number of open files; leak enough and your program cannot open new files.

### Try-With-Resources Solution

Try-with-resources handles cleanup automatically:

```java
try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("bank.ser"))) {
    out.writeObject(bank);
}  // out.close() called automatically, even if exception occurs
```

**How it works:**

1. **Declare resource** in parentheses after `try` keyword
2. **Use resource** in the try block
3. **Automatic closure** when block exits—whether normally or via exception

Multiple resources:

```java
try (FileInputStream fis = new FileInputStream("input.ser");
     ObjectInputStream in = new ObjectInputStream(fis)) {
    Bank bank = (Bank) in.readObject();
}  // Both streams closed automatically, in reverse order
```

Resources must implement `AutoCloseable` interface (all I/O streams do). The compiler generates `finally` blocks automatically, guaranteeing `close()` is called.

## Exception Handling in File Operations

File I/O operations throw checked exceptions that must be handled.

### IOException: The General File Exception

`IOException` is the parent class for all I/O exceptions:

```java
try {
    BankDataManager.saveBank(bank, "bank.ser");
} catch (IOException e) {
    System.out.println("Failed to save bank: " + e.getMessage());
    e.printStackTrace();
}
```

Specific `IOException` subclasses:
- **FileNotFoundException**: File doesn't exist or cannot be accessed
- **EOFException**: End of file reached unexpectedly
- **FileSystemException**: Permission denied, disk full, etc.

### ClassNotFoundException: The Deserialization Exception

When loading serialized objects, Java must find the class definitions:

```java
try {
    Bank bank = BankDataManager.loadBank("bank.ser");
} catch (IOException e) {
    System.out.println("I/O error: " + e.getMessage());
} catch (ClassNotFoundException e) {
    System.out.println("Cannot find class: " + e.getMessage());
}
```

`ClassNotFoundException` occurs when:

- The serialized class isn't in the classpath
- The class was renamed or moved
- The serialized data is corrupted

### Handling Missing Files Gracefully

When loading bank data, a missing file isn't necessarily an error—it might be the first run:

```java
public static void main(String[] args) {
    Bank bank;
    
    try {
        bank = BankDataManager.loadBank("bank.ser");
        System.out.println("Loaded existing bank data");
    } catch (FileNotFoundException e) {
        // First run - create new bank
        bank = new Bank("Turing National Bank");
        System.out.println("No existing data - created new bank");
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error loading bank: " + e.getMessage());
        return;  // Cannot continue
    }
    
    // Use bank...
    
    // Save before exiting
    try {
        BankDataManager.saveBank(bank, "bank.ser");
    } catch (IOException e) {
        System.out.println("Warning: Could not save bank data");
        e.printStackTrace();
    }
}
```

This pattern:

1. **Try to load** existing data
2. **Create new** if file doesn't exist
3. **Perform operations** on the bank
4. **Save** before exiting

## Using BankDataManager in Practice

Let's see complete usage in `BankSystemMain`.

### First Run: Creating and Saving Data

```java
public static void main(String[] args) {
    System.out.println("=== Turing National Bank ===\n");
    
    // Create new bank
    Bank bank = new Bank("Turing National Bank");
    
    // Add customers
    Customer ada = new Customer(1815, "Ada Lovelace", 36, "London");
    Customer alan = new Customer(1912, "Alan Turing", 41, "Manchester");
    bank.addCustomer(ada);
    bank.addCustomer(alan);
    
    // Open accounts
    SavingsAccount adaSavings = bank.openSavingsAccount(1815, 5000.0, 0.03);
    CheckingAccount adaChecking = bank.openCheckingAccount(1815, 2000.0, 500.0);
    
    // Perform transactions
    adaSavings.deposit(1000.0);
    adaSavings.withdraw(500.0);
    adaChecking.deposit(800.0);
    
    // Save everything
    try {
        BankDataManager.saveBank(bank, "bank.ser");
        System.out.println("\n✓ All data saved successfully");
    } catch (IOException e) {
        System.out.println("\n✗ Failed to save: " + e.getMessage());
    }
}
```

This creates `bank_data/bank.ser` containing the entire bank state.

### Second Run: Loading and Continuing

```java
public static void main(String[] args) {
    System.out.println("=== Turing National Bank ===\n");
    
    Bank bank;
    
    // Load existing data
    try {
        bank = BankDataManager.loadBank("bank.ser");
        System.out.println("✓ Loaded existing bank with " + 
                          bank.getCustomerCount() + " customers\n");
    } catch (FileNotFoundException e) {
        System.out.println("No existing data - creating new bank\n");
        bank = new Bank("Turing National Bank");
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error loading bank: " + e.getMessage());
        return;
    }
    
    // Bank is now loaded with all previous data!
    // Ada Lovelace still exists with her accounts and transaction history
    
    Customer ada = bank.findCustomer(1815);
    if (ada != null) {
        System.out.println("Found customer: " + ada.getName());
        System.out.println("Total balance: €" + ada.getTotalBalance());
        
        // Perform new transactions
        Account savings = ada.findAccount(1001);
        if (savings != null) {
            savings.deposit(250.0);
            System.out.println("New transaction added");
        }
    }
    
    // Save updated state
    try {
        BankDataManager.saveBank(bank, "bank.ser");
        System.out.println("\n✓ Updated data saved");
    } catch (IOException e) {
        System.out.println("\n✗ Failed to save: " + e.getMessage());
    }
}
```

### Exporting Data for Analysis

```java
// Export Ada's savings account transactions to CSV
try {
    Account adaSavings = ada.findAccount(1001);
    BankDataManager.exportTransactionsToCSV(adaSavings, "ada_savings.csv");
    System.out.println("✓ CSV exported - open in Excel");
} catch (IOException e) {
    System.out.println("Export failed: " + e.getMessage());
}

// Generate audit report for Alan's checking account
try {
    Customer alan = bank.findCustomer(1912);
    Account alanChecking = alan.findAccount(1002);
    BankDataManager.generateAuditReportFile(alanChecking, "alan_audit.txt");
    System.out.println("✓ Audit report generated");
} catch (IOException e) {
    System.out.println("Report failed: " + e.getMessage());
}

// Create timestamped backup
try {
    String backupFile = BankDataManager.backupBank(bank);
    System.out.println("✓ Backup created: " + backupFile);
} catch (IOException e) {
    System.out.println("Backup failed: " + e.getMessage());
}
```

This creates:

- `bank_data/ada_savings.csv` (open in Excel)
- `bank_data/alan_audit.txt` (formatted report)
- `bank_data/bank_backup_2025-11-29_14-30-00.ser` (timestamped backup)

## Summary

You've transformed the banking system from **volatile memory** to **persistent storage**. Data now survives program termination and system restarts.

### Key Concepts Mastered

#### Text vs binary file formats
Text files (CSV, formatted reports) are human-readable and portable but verbose. Binary files (serialization) are compact and automatic but opaque. Use both: serialization for complete state, text for exports and reports.

#### Serialization preserves object graphs
Marking classes `Serializable` lets Java automatically save entire object hierarchies. One `writeObject(bank)` call saves Bank, all Customers, all Accounts, all Transactions—complete with relationships.

#### Try-with-resources guarantees cleanup
Declaring resources in try parentheses ensures automatic closure. Files close even if exceptions occur, preventing resource leaks and ensuring data flushes to disk.

#### IOException handling enables graceful degradation
Catching `FileNotFoundException` separately from general `IOException` lets programs handle missing files (first run) differently from I/O errors (disk full, permission denied).

#### BankDataManager centralizes file operations
Creating a dedicated class for file I/O separates persistence concerns from business logic. Bank, Customer, and Account classes remain unchanged—only `BankSystemMain` uses `BankDataManager`.


### Complete File Operations

Your banking system now supports:

**Serialization (Binary):**
- `saveBank()` - Save complete bank state
- `loadBank()` - Restore complete bank state
- `backupBank()` - Create timestamped backup

**CSV Export (Text):**
- `exportTransactionsToCSV()` - Export account transactions
- `exportCustomerTransactions()` - Export all customer transactions

**Formatted Reports (Text):**
- `generateAuditReportFile()` - Detailed account audit
- `generateBankReportFile()` - Bank summary report

**Utilities:**
- `fileExists()` - Check if file exists
- `deleteFile()` - Remove old files
- `listSaveFiles()` - Show available backups

The banking system is now production-ready with persistent storage. Customers, accounts, and transaction histories survive across sessions. The combination of binary serialization (for complete saves) and text formats (for human-readable exports) provides both convenience and transparency. Try-with-resources ensures files close properly, and exception handling enables graceful recovery from I/O errors.

