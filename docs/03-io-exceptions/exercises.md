# File and Exception Handling 

## Programming Tasks: Text Files I/O Operations

### Overview

In this exercise, you will practice reading and writing customer data from/to CSV text files using different Java I/O techniques. You'll implement efficient file reading methods, data filtering operations, and formatted output methods.

![Files](https://i.imgur.com/3iE4NXV.png)

### What Is Provided to You


You are provided with **starter code and sample data** available at [this link](https://github.com/evisp/java-oop-course/tree/main/code/seminar_sessions/5_file_io_exceptions). The starter package includes:

1. **`Customer.java`** - A complete data class (DO NOT MODIFY) with fields for id, name, email, age, and city, along with getters and a formatted `toString()` method.

2. **`customers.txt`** - A sample CSV file containing customer data for Ada Lovelace, Alan Turing, Grace Hopper, Emmy Noether, and David Hilbert, with header row: `CustomerID,Name,Email,Age,City`.

3. **`FileIO.java` (Partial)** - Starter code with **two methods already implemented**: `readCustomersSimple()` (uses `Files.readAllLines()`), `writeCustomersSimple()` (uses `Files.write()`), and a helper method `parseCustomerLine()` that you can use in your implementations.


### Your Task: Implement the Following Methods

#### Part 1: Efficient File Reading 

**Method Signature:**
```java
public static List<Customer> readCustomersEfficient(String filename)
```

**Requirements:**

- Use `BufferedReader` wrapped around `FileReader` for efficient line-by-line reading
- Use **try-with-resources** to ensure automatic resource cleanup
- Skip the header line (first line of CSV)
- Use the provided `parseCustomerLine(line)` helper to parse each line
- Handle exceptions:
  - `FileNotFoundException` - print "File not found: [filename]"
  - `IOException` - print "Error reading file: [message]"
- Print confirmation message: "Loaded X customers (Efficient method)"
- Return `List<Customer>` (empty list if file not found)

**Why this approach?**

- More memory efficient for large files (reads line-by-line)
- Suitable for streaming data processing
- Industry standard for processing large CSV files



#### Part 2: Efficient File Writing 

**Method Signature:**
```java
public static void writeCustomersEfficient(List<Customer> customers, String filename)
```

**Requirements:**

- Use `BufferedWriter` wrapped around `FileWriter` for efficient writing
- Use **try-with-resources** to ensure automatic resource cleanup
- Write CSV header first: "CustomerID,Name,Email,Age,City"
- Use `writer.newLine()` after each line (don't use `\n` directly)
- Format each customer as: `id,name,email,age,city`
- Handle `IOException` - print "Error writing file: [message]"
- Print confirmation message: "Wrote X customers to [filename] (Efficient method)"

**Why this approach?**

- More efficient for large datasets (writes incrementally)
- Better control over buffering and flushing
- Standard approach for generating large CSV files



#### Part 3: Data Filtering Methods 

Implement two filtering methods that return new lists containing only customers meeting specific criteria:

##### 3a. Filter by Age 

**Method Signature:**
```java
public static List<Customer> filterOlderThan(List<Customer> customers, int age)
```

**Requirements:**

- Return a new `ArrayList<Customer>` containing customers with `age > specified age`
- Do not modify the original list
- Return empty list if no customers match



##### 3b. Filter by City 

**Method Signature:**
```java
public static List<Customer> filterByCity(List<Customer> customers, String city)
```

**Requirements:**

- Return a new `ArrayList<Customer>` containing customers from the specified city
- Use **case-insensitive** comparison (`equalsIgnoreCase()`)
- Do not modify the original list
- Return empty list if no customers match



#### Part 4: Formatted Output Methods

Implement three methods that display customer data in formatted output:

##### 4a. Print All Customers

**Method Signature:**
```java
public static void printCustomers(List<Customer> customers)
```

**Requirements:**

- If list is empty, print "No customers to display." and return
- Otherwise, print formatted output:


##### 4b. Print Customers Filtered by Age 

**Method Signature:**
```java
public static void printCustomersOlderThan(List<Customer> customers, int age)
```

**Requirements:**

- Use your `filterOlderThan()` method to get filtered list
- Print header: "Customers older than [age]:"
- If no matches, print "No customers found."
- Otherwise, print each matching customer using `customer.toString()`


##### 4c. Print Customers Filtered by City 

**Method Signature:**
```java
public static void printCustomersByCity(List<Customer> customers, String city)
```

**Requirements:**
- Use your `filterByCity()` method to get filtered list
- Print header: "Customers from [city]:"
- If no matches, print "No customers found."
- Otherwise, print each matching customer using `customer.toString()`



### Testing Your Implementation

Use the provided `CustomerFileIODemo.java` to test all your methods:

```java
public static void main(String[] args) {
    String inputFile = "customers.txt";
    String outputFiltered = "customers_filtered.txt";
    
    // 1. READ using simple method (provided)
    List<Customer> customers = FileIO.readCustomersSimple(inputFile);
    
    // 2. PRINT ALL (your implementation)
    FileIO.printCustomers(customers);
    
    // 3. FILTER and print by age (your implementation)
    FileIO.printCustomersOlderThan(customers, 40);
    
    // 4. FILTER and print by city (your implementation)
    FileIO.printCustomersByCity(customers, "Göttingen");
    
    // 5. WRITE filtered data (your implementation)
    List<Customer> filtered = FileIO.filterOlderThan(customers, 40);
    FileIO.writeCustomersSimple(filtered, outputFiltered);
    
    // 6. VERIFY by reading with your efficient method
    List<Customer> verified = FileIO.readCustomersEfficient(outputFiltered);
    FileIO.printCustomers(verified);
}
```


### Key Learning Objectives

After completing this exercise, you will understand:

1. **Two approaches to file reading:**
   - `Files.readAllLines()` - simple, loads entire file
   - `BufferedReader` - efficient, streams line-by-line

2. **Two approaches to file writing:**
   - `Files.write()` - simple, writes all at once
   - `BufferedWriter` - efficient, writes incrementally

3. **Try-with-resources** for automatic resource cleanup

4. **Exception handling** for file operations (IOException, FileNotFoundException)

5. **Data filtering** and transformation operations on collections

6. **Formatted console output** for user-friendly display



## Programming Tasks: Binary Files I/O Operations


###  Holiday Roulette

There are some **important questions hidden inside a mysterious binary file**. Your goal is to wake them up, use Java to read them as objects, and then play with them: show them all, pick one at random, and filter them by difficulty level. By the end, you should feel comfortable reading data from a file into an `ArrayList` and then asking simple, useful questions about that data.

### What you have

- `holiday_roulette.bin` – A binary file containing an list of questions
- `Question.java` – A data class with question text, difficulty level (1–3), and category. *This java class is almost complete*. Only few minor details are missing. 
- `HolidayRouletteStarter.java` – Your starter file with four **TODO** sections to complete

You may find the starter code [here](https://github.com/evisp/java-oop-course/tree/main/code/seminar_sessions/5_file_io_exceptions/binary_io)

### Tasks

**Task 1: Load questions from file**  

Implement `loadQuestions(String filename)` using `ObjectInputStream` with try-with-resources. Read the mysterious data and store it in an *appropriate object*. 

**Task 2: Print all questions**  

Implement `printAllQuestions()` to loop through the `questions` list and print each one using `System.out.println()`.

**Task 3: Return a random question**  

Implement `getRandomQuestion()` to use the `Random` field to pick and return a random `Question` from the list, or `null` if empty.

**Task 4: Filter by difficulty level** 

Implement `getQuestionsByLevel(int level)` to create and return a new `ArrayList` containing only questions matching the given level (1 = Easy, 2 = Medium, 3 = Hard).

**Task 5: Test in main()**  

Write a `main` method that creates a `HolidayRouletteStarter` object, loads the file (handling `FileNotFoundException`, `IOException`, `ClassNotFoundException`), then calls your methods to verify they work: print total count, print all questions, print one random question, and print all Easy questions.

### How to verify

Run the program. You should see:

- Questions loaded successfully
- All questions printed with category and level
- One random question displayed
- All Easy questions filtered and shown

Have fun! 
