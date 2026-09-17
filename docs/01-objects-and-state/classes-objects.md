
## Introduction - Why Classes Matter

Imagine you are tasked with building a digital banking system for a growing financial institution. The system needs to handle customers, their personal information, multiple account types, and secure financial transactions. Traditional programming approaches using separate variables and functions quickly become inefficient when dealing with complex, interconnected data like customer profiles linked to multiple accounts with different balances and transaction histories.

**Object-oriented programming provides an elegant solution by letting you model real-world entities directly in code**. Instead of juggling separate arrays and variables, you create classes that represent actual things: a `Customer` class that knows its own name, ID, and contact information, and an `Account` class that manages its own balance and validates its own transactions.

**We will build a complete bank account management system step by step**, starting with fundamental concepts and ending with a working application. Today, you'll create `Customer` and `Account` classes that interact naturally.

Through this practical project, **you will learn the core building blocks of object-oriented programming**: how to define classes as blueprints for objects, use constructors to create properly initialized objects, implement encapsulation with getters and setters to protect sensitive data, and override the `toString()` method for clear object representation. 

![Bank System](https://i.imgur.com/nESWRQy.jpeg)

## The Blueprint Concept - Understanding Classes

Think of a class as an architect's blueprint for a house. The blueprint itself isn't a house you can live in—it's a detailed plan that specifies where the rooms go, how big they should be, and what features each room should have. But from that single blueprint, a construction company can build dozens of identical houses, each one a real, physical structure where families can actually live.

In programming, classes work exactly the same way. 

> A class is a blueprint that defines what data an object should contain and what actions it can perform. 

**The class itself doesn't hold any actual data—it's just the template**.

> Objects are the real instances built from that class blueprint, each containing actual values and able to perform real operations.

Let's see this concept in action with our banking system. Here's our first empty `Customer` class—our blueprint:

```java
public class Customer {
    // This is our blueprint - it defines the structure
    // but doesn't contain any actual customer data yet
}
```


This `Customer` class is like our architectural blueprint. It exists, but it doesn't represent any specific customer. To create actual customers (objects), we instantiate the class:

``` java
public class Main {
public static void main(String[] args) {
    // Creating actual Customer objects from our blueprint
    Customer customer1 = new Customer(); 
    Customer customer2 = new Customer(); 
    // customer1 and customer2 are separate objects (houses)
    // built from the same Customer class (blueprint)
}
```


Notice the key terminology here: `Customer` (with capital C) is the **class** - our blueprint. `customer1` and `customer2` are **objects** or **instances** - actual customers created from that blueprint. Each object exists independently and can hold different data, just like two houses built from the same blueprint can have different families living in them and different furniture inside.

The power of this blueprint concept becomes clear when you realize that from one well-designed class, you can create hundreds or thousands of objects, each managing its own data safely and consistently.

## Building Our First Class - Customer

### Class Definition and Fields

Now let's add substance to our blueprint. A real customer has essential information: their name, a unique ID, and an email address. But here's where object-oriented programming gets interesting—we need to decide who can access this information and how.

```java
public class Customer {
    // Private fields - the customer's sensitive data
    private int id;
    private String name;
    private int age;
    private String address;
    // This data is now protected inside the class
}
```

Notice the `private` keyword before each field. This is encapsulation in action—we're building walls around our data. Think of it like a bank vault: the money (data) is stored inside, but you can't just walk in and grab it. You need to go through proper channels (methods) to access or modify it.

### Private vs Public Access: The Security Difference

In banking, data protection isn't just good practice—it's legally required. Imagine if anyone could directly change a customer's account balance or ID number:


With `private` fields, this direct manipulation becomes impossible. The compiler will throw an error if external code tries to access private data directly. This forces all interactions to go through controlled methods that we design, where we can add validation, logging, and business logic.

### Why Encapsulation Matters in Banking

Banks handle sensitive financial data that requires multiple layers of protection. Encapsulation provides the first line of defense by ensuring that:

- **Data Integrity**: Customer IDs can't be accidentally set to invalid values
- **Business Rules**: Email addresses must follow proper formats  
- **Audit Trails**: All data changes go through trackable methods
- **Security**: Sensitive information is never exposed directly to external code

When you encapsulate data properly, you create a controlled environment where every interaction with customer information goes through your carefully designed security checkpoints. 

### The Constructor - Bringing Objects to Life

A constructor is a special method that runs automatically when you create a new object. Think of it as the "birth certificate" process for objects; it ensures every new customer gets properly registered with valid information before they can interact with the banking system.

```java
// Default constructor - creates empty customer
public Customer() {
    // Java provides this automatically if we don't write one
    // But it leaves all fields with default values (0, null)
}

// Parameterized constructor - creates customer with data
public Customer(int id, String name, int age, String address) {
    // Validation before assignment
    if (id <= 0) {
        System.out.println("Customer ID must be positive");
    }
    if (name == null || name.isEmpty()) {
        System.out.println("Customer name cannot be empty");
    }
    if (age < 18 || age > 120) {
        System.out.println("Invalid age for banking customer");
    }
    if (address == null || address.isEmpty()) {
        System.out.println("Address is required");
    }
    
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
}
```

Notice how the parameterized constructor validates each piece of data before storing it. In banking, you can't have customers with negative IDs, empty names, or unrealistic ages. The constructor acts as a gatekeeper, ensuring only valid customers enter your system.

Here's how you create actual customer objects:

```java
public class Main {
    public static void main(String[] args) {
        // Creating customers with validated data
        Customer customer1 = new Customer(1815, "Ada Lovelace", 36, "10 St. Jame Square, London, England");
        Customer customer2 = new Customer(1912, "Alan Turing", 41, "Sherborne School, Dorset, England");
        // This would throw an error due to validation:
        // Customer badCustomer = new Customer(-1, "", 15, "");
    }
}
```


### Getters and Setters - Controlled Access

Even with proper construction, we need controlled ways to access and modify customer data. Direct field access in banking would be like giving everyone the vault combination—technically possible, but catastrophically dangerous.

```java
public class Customer {
    private int id;
    private String name;
    private int age;
    private String address;

    // Constructor code here...

    // Constructor code here...

    // Getters - safe data retrieval
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    // Setters - controlled data modification
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Name cannot be empty");
        }
        this.name = name;
    }

    public void setAge(int age) {
        if (age < 18 || age > 120) {
            System.out.println("Invalid age for banking customer");
        }
        this.age = age;
    }

    public void setAddress(String address) {
        if (address == null || address.isEmpty()) {
            System.out.println("Address cannot be empty");
        }
        this.address = address;
    }

    // Note: No setter for ID - customer IDs should never change!
```


### Why This Protection Matters

Getters provide read-only access to data without exposing the internal structure. Setters act as security checkpoints, validating every change before it's applied. Notice there's no `setId()` method; customer IDs are permanent identifiers that should never change after creation.

```java
public class Main {
    public static void main(String[] args) {
        Customer customer2 = new Customer(1912, "Alan Turing", 41, "Sherborne School, Dorset, England");

         // Safe data access
        System.out.println("Customer: " + customer2.getName());
        System.out.println("Age: " + customer2.getAge());

        // Test setter methods - Alan Turing moves to Bletchley Park
        customer2.setAge(30);
        customer2.setAddress("Bletchley Park, Buckinghamshire, England");
        System.out.println("Age: " + customer2.getAge());
        System.out.println("Address: " + customer2.getAdress()());
    }
}
```


This encapsulation creates a robust system where customer data remains consistent and valid throughout the object's lifetime, exactly as real banking systems require for regulatory compliance and data integrity.


## Adding Behavior - Methods and toString

### The toString Method

Every object in Java inherits a `toString()` method from the `Object` class, but by default it returns something cryptic like `Customer@2a84aee7`; hardly useful when you need to understand what's in your object. In banking software, being able to clearly display customer information is crucial for debugging, logging, and user interfaces.

Let's override the `toString()` method to provide meaningful output:


```java
class Customer {
    // private variables here
    // Constructor and getter/setter methods here...

    @Override
    public String toString() {
        return String.format("Customer{id=%d, name='%s', age=%d, address='%s'}", 
                        id, name, age, address);
    }
}
```


The `@Override` annotation tells the compiler we're intentionally replacing the parent class method. This provides compile-time safety—if we accidentally misspell the method name, we'll get an error instead of creating a new method.

Now when you print a customer object, you get professional, readable output:

```java
public class Main {
    public static void main(String[] args) {

        Customer customer2 = new Customer(1912, "Alan Turing", 41, "Sherborne School, Dorset, England");    
        System.out.println(customer2);  // Automatically calls toString()
        // Output: Customer{id=1912, name='Alan Turing', age=41, address='herborne School, Dorset, England'}
    
        // Great for logging
        System.out.println("Customer created: " + customer2.toString());
    }
}
```


### Business Logic Methods

Beyond basic data access, objects should encapsulate business behavior. Let's add methods that make sense for a banking customer:

```java

class Customer {

    public void displayCustomerInfo() {
        System.out.println("=== Customer Information ===");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Address: " + address);
        System.out.println("==========================");
    }

    // Business logic method - check if eligible for senior discount
    public boolean isEligibleForSeniorDiscount() {
        return age >= 65;
    }
}
```


These methods transform your `Customer` class from a simple data container into an intelligent object that knows how to display itself, validate its own data, and apply business rules. This encapsulation of both data and behavior is the essence of object-oriented programming; objects that are responsible for their own operations and maintain their own integrity.


---

## Building the Account Class

### Account Structure

Now that we have customers, we need accounts to hold their money. An `Account` class represents the financial core of our banking system, handling sensitive data that requires even stricter protection than customer information.


```java
public class Account {


    /** Current balance in the account */
    private double balance;

    /**
     * Constructs a new Account with zero balance.
     */
    public Account() {
        this.balance = 0.0;
    }

    /**
     * Constructs a new Account with the specified initial balance.
     * 
     * @param initialBalance the starting balance for the account
     */
    public Account(double initialBalance) {
        this.balance = initialBalance;
    }
}
```


Notice we have two constructors here—this is called constructor overloading. The default constructor creates an account with zero balance, while the parameterized constructor allows you to specify an initial balance. Java automatically chooses which constructor to use based on the arguments you provide when creating an object.

The `balance` field is marked `private`, demonstrating encapsulation at its most critical level. In banking, the account balance is the most sensitive piece of data—no external code should be able to directly modify it. All changes must go through controlled methods that validate the operations.

### Core Banking Operations

The real power of the `Account` class lies in its methods that safely handle money transactions. Each method includes validation and provides clear feedback about the operation's success or failure:

```java

public class Account {
    
    /** Current balance in the account */
    private double balance;
    
    /**
     * Constructs a new Account with zero balance.
     */
    public Account() {
        this.balance = 0.0;
    }
    
    /**
     * Constructs a new Account with the specified initial balance.
     * 
     * @param initialBalance the starting balance for the account
     */
    public Account(double initialBalance) {
        this.balance = initialBalance;
    }
    
    /**
     * Deposits the specified amount into the account.
     * 
     * @param amount the amount to deposit (must be positive)
     * @return true if deposit was successful, false otherwise
     */
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
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
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        
        if (amount > balance) {
            System.out.println("Insufficient funds. Current balance: $" + balance);
            return false;
        }
        
        balance -= amount;
        System.out.println("Withdrawn: $" + amount);
        return true;
    }
    
    /**
     * Returns the current account balance.
     * 
     * @return the current balance
     */
    public double getBalance() {
        return balance;
    }
    
    /**
     * Returns a string representation of the account.
     * 
     * @return a formatted string showing the account balance
     */
    @Override
    public String toString() {
        return "Account{balance=€" + String.format("%.2f", balance) + "}";
    }
}
```


### Why This Design Works

This `Account` implementation demonstrates several key object-oriented principles:

- **Encapsulation**: The `balance` field is private and can only be modified through controlled methods
- **Validation**: Both `deposit()` and `withdraw()` methods validate inputs before making changes
- **Feedback**: Methods return boolean values to indicate success/failure and print informative messages
- **Data Integrity**: The balance can never be set to invalid values through direct access
- **Professional Representation**: The `toString()` method provides a clean, formatted display


The beauty of this design is its simplicity combined with safety. The `Account` class protects its most critical data (the balance) while providing intuitive methods for common banking operations. This demonstrates how object-oriented programming creates robust, maintainable code that mirrors real-world business processes.

---

## Main Class - Putting It All Together

### The Application Entry Point

The `main` method is the entry point of any Java application—it's where your program starts executing. Think of it as the director of a play, orchestrating all the objects and demonstrating how they work together. Our `BankSystemMain` class serves as both a test environment and a showcase of our object-oriented banking system.

```java
public class BankingSystemMain {
    /**
     * Main method to test Customer and Account functionality.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== Bank Management System - Week 1 Demo ===");
        System.out.println("Featuring Computer Science Legends!\n");
        
        // The main method coordinates everything
    }

}
```


Notice how the `main` method is `public static void`—this special signature tells the Java Virtual Machine where to start your program. The `String[] args` parameter allows the program to receive command-line arguments, though we're not using them here.

The real magic happens when we start creating and using our objects:

```java
// Create customer objects with famous computer scientists
Customer customer1 = new Customer(1815, "Ada Lovelace", 36, "10 St. James Square, London, England");
Customer customer2 = new Customer(1912, "Alan Turing", 41, "Sherborne School, Dorset, England");
Customer customer3 = new Customer(1906, "Grace Hopper", 85, "Yale University, New Haven, CT");

// Display customer information using toString()
System.out.println("Customer 1: " + customer1);
System.out.println("Customer 2: " + customer2);
System.out.println("Customer 3: " + customer3);
```


Each `new Customer()` call invokes our constructor, creating three separate objects in memory. When we print them, Java automatically calls the `toString()` method we implemented, demonstrating how our encapsulated objects present themselves to the outside world.

### Demonstrating Encapsulation and Methods

The main method serves as a comprehensive test of all the functionality we've built:

```java
// Test getter methods on Ada Lovelace
System.out.println("Ada Lovelace Details (First Computer Programmer):");
System.out.println("ID: " + customer1.getId());
System.out.println("Name: " + customer1.getName());
System.out.println("Age: " + customer1.getAge());
System.out.println("Address: " + customer1.getAddress());

// Test setter methods - Alan Turing moves to Bletchley Park
customer2.setAge(30);
customer2.setAddress("Bletchley Park, Buckinghamshire, England");
System.out.println("Alan Turing moves to Bletchley Park (Enigma Code Breaker):");
System.out.println(customer2);
```


This demonstrates the power of encapsulation: we can safely access and modify customer data through our controlled getter and setter methods, while the actual fields remain protected from direct manipulation.

### Real-World Simulation

The main method creates a complete banking scenario, showing how multiple objects interact in a real system:

```java
// Create accounts for our computer science legends
Account adaAccount = new Account(); // Ada starts from scratch
Account alanAccount = new Account(2500.00); // Alan has wartime savings
Account graceAccount = new Account(1500.00); // Grace has Navy pay

// Demonstrate constructor overloading
System.out.println("Ada's Account (Analytical Engine Fund): " + adaAccount);
System.out.println("Alan's Account (Codebreaking Savings): " + alanAccount);
System.out.println("Grace's Account (COBOL Development Fund): " + graceAccount);
```


Here we see constructor overloading in action: Ada's account uses the default constructor (starting with €0.00), while Alan and Grace use the parameterized constructor with initial balances.

### Testing Banking Operations

The simulation includes comprehensive testing of our banking methods, including both successful operations and error conditions:

```java
// Test successful operations
System.out.println("Ada receives royalties from her mathematical work:");
adaAccount.deposit(1000.00);

System.out.println("Alan funds his theoretical computer research:");
alanAccount.withdraw(500.00);

// Test validation and error handling
System.out.println("Invalid deposit attempt:");
alanAccount.deposit(-100.00); // Invalid deposit

System.out.println("Grace tries to withdraw more than available:");
graceAccount.withdraw(3000.00); // Insufficient funds
```


This demonstrates how our methods handle both valid and invalid operations, showing the protective power of encapsulation and validation. The account balance remains safe because it can only be modified through our controlled methods.

### System State Display

Finally, the main method shows the current state of all objects using the `toString()` methods:

```java
// Final account states
System.out.println("Ada Lovelace (First Programmer): " + adaAccount);
System.out.println("Alan Turing (Father of CS): " + alanAccount);
System.out.println("Grace Hopper (Debugging Pioneer): " + graceAccount);
```


The beauty of this approach is that the main method doesn't need to know the internal structure of `Customer` or `Account` objects. It simply calls their public methods and lets each object manage its own representation and behavior.

When you run this program, you'll see a complete demonstration of object-oriented programming in action: objects being created, methods being called, data being safely modified through encapsulation, and professional output being generated through well-designed `toString()` methods. The main method orchestrates this entire symphony of objects, proving that our classes work together as a cohesive banking system.

---

## Summary

You've successfully built a complete bank management system while mastering the core concepts of object-oriented programming. Through the `Customer` and `Account` classes, you've learned how encapsulation protects sensitive data, constructors ensure valid object creation, and methods provide controlled behavior.

### Key Concepts Mastered

- **Classes and Objects**: Blueprints vs. actual instances
- **Encapsulation**: Private fields with public getter/setter methods for data protection
- **Constructors**: Safe object initialization with validation
- **Method Design**: Smart behavior like `deposit()` and `withdraw()` with proper validation
- **toString()**: Professional object representation for debugging and display

Your banking system validates all inputs, prevents common errors (negative deposits, overdrafts), and provides clear feedback—demonstrating the safety principles of real banking software.

### Complete Code

Find the complete working code in three organized files:

- [`Customer.java`](#) - Customer management with encapsulation
- [`Account.java`](#) - Banking account with transaction methods  
- [`BankSystemMain.java`](#) - Complete system demonstration

### Next Steps

The next tutorial will enhance this system using **Collections (Lists)** to create more realistic banking relationships. You'll learn how to:

- Give customers multiple accounts using `ArrayList<Account>`
- Connect the `Customer` and `Account` classes properly
- Implement methods like `addAccount()`, `getTotalBalance()`, and `findAccountByNumber()`

This will introduce you to one-to-many relationships in object-oriented design and the powerful Java Collections framework.

