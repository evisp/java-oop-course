package week2_basic_classes;

/**
 * Main class to test the Customer and Account classes.
 * Features famous computer scientists as customer examples.
 * 
 * @author Evis Plaku
 * @version 1.0
 */
public class BankSystemMain {
    
    /**
     * Main method to test Customer and Account functionality.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== Bank Management System - Week 1 Demo ===");
        System.out.println("Featuring Computer Science Legends!\n");
        
        // Testing Customer class with legendary computer scientists
        System.out.println("--- Testing Customer Class ---");
        
        // Create customer objects with famous computer scientists
        Customer customer1 = new Customer(1815, "Ada Lovelace", 36, "10 St. James Square, London, England");
        Customer customer2 = new Customer(1912, "Alan Turing", 41, "Sherborne School, Dorset, England");
        Customer customer3 = new Customer(1906, "Grace Hopper", 85, "Yale University, New Haven, CT");
        
        // Display customer information
        System.out.println("Customer 1: " + customer1);
        System.out.println("Customer 2: " + customer2);  
        System.out.println("Customer 3: " + customer3);
        
        // Test getter methods on Ada Lovelace
        System.out.println("\nAda Lovelace Details (First Computer Programmer):");
        System.out.println("ID: " + customer1.getId());
        System.out.println("Name: " + customer1.getName());
        System.out.println("Age: " + customer1.getAge());
        System.out.println("Address: " + customer1.getAddress());
        
        // Test setter methods - Alan Turing moves to Bletchley Park
        customer2.setAge(30);
        customer2.setAddress("Bletchley Park, Buckinghamshire, England");
        System.out.println("\nAlan Turing moves to Bletchley Park (Enigma Code Breaker):");
        System.out.println(customer2);
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Testing Account class with tech-themed scenarios
        System.out.println("--- Testing Account Class ---");
        
        // Create accounts for our computer science legends
        Account adaAccount = new Account();  // Ada starts from scratch
        Account alanAccount = new Account(2500.00);  // Alan has wartime savings
        Account graceAccount = new Account(1500.00);  // Grace has Navy pay
        
        System.out.println("Ada's Account (Analytical Engine Fund): " + adaAccount);
        System.out.println("Alan's Account (Codebreaking Savings): " + alanAccount);
        System.out.println("Grace's Account (COBOL Development Fund): " + graceAccount);
        
        // Test deposit operations
        System.out.println("\n--- Testing Deposits ---");
        System.out.println("Ada receives royalties from her mathematical work:");
        adaAccount.deposit(1000.00);
        
        System.out.println("Grace gets paid for compiler development:");
        graceAccount.deposit(750.00);
        
        System.out.println("Invalid deposit attempt:");
        alanAccount.deposit(-100.00);  // Invalid deposit
        
        // Test withdrawal operations  
        System.out.println("\n--- Testing Withdrawals ---");
        System.out.println("Alan funds his theoretical computer research:");
        alanAccount.withdraw(500.00);
        
        System.out.println("Grace tries to withdraw more than available:");
        graceAccount.withdraw(3000.00);  // Insufficient funds
        
        System.out.println("Ada withdraws for lab equipment:");
        adaAccount.withdraw(200.00);
        
        // Final account states
        System.out.println("\n--- Final Account States ---");
        System.out.println("Ada Lovelace (First Programmer): " + adaAccount);
        System.out.println("Alan Turing (Father of CS): " + alanAccount);
        System.out.println("Grace Hopper (Debugging Pioneer): " + graceAccount);
        
        System.out.println("\n=== End of Computer Science Legends Demo ===");
        System.out.println("Fun Fact: These pioneers laid the foundation for everything we code today!");
    }
}
