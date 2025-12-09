import java.nio.file.*;
import java.io.*;
import java.util.*;

/**
 * Centralized File I/O class handling all file operations
 * - Reading customers from file (2 methods)
 * - Writing customers to file (2 methods)
 * - Filtering data
 * - Printing data
 */
public class FileIO {
    
    // ==================== READING METHODS ====================
    
    /**
     * METHOD 1: Read customers using Files.readAllLines() - SIMPLEST
     * Best for: Small files, one-time complete read
     */
    public static List<Customer> readCustomersSimple(String filename) {
        List<Customer> customers = new ArrayList<>();
        
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            
            // Skip header line (line 0)
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                Customer customer = parseCustomerLine(line);
                if (customer != null) {
                    customers.add(customer);
                }
            }
            
            System.out.println("Loaded " + customers.size() + " customers (Simple method)");
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        
        return customers;
    }

    /**
     * Helper: Parse single customer line from CSV
     */
    private static Customer parseCustomerLine(String line) {
        try {
            String[] parts = line.split(",");
            
            if (parts.length != 5) {
                System.out.println("Invalid line format (expected 5 fields): " + line);
                return null;
            }
            
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String email = parts[2].trim();
            int age = Integer.parseInt(parts[3].trim());
            String city = parts[4].trim();
            
            return new Customer(id, name, email, age, city);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid number in line: " + line);
            return null;
        }
    }
    

    /**
     * METHOD 2: Read customers using BufferedReader - MOST EFFICIENT
     * Best for: Large files, streaming, memory efficiency
     */
    public static List<Customer> readCustomersEfficient(String filename) {
       // implement this method
       return null;
    }
    
    
    // ==================== WRITING METHODS ====================
    
    /**
     * METHOD 1: Write customers using Files.write() - SIMPLEST
     * Best for: Simple writes, small datasets
     */
    public static void writeCustomersSimple(List<Customer> customers, String filename) {
        try {
            List<String> lines = new ArrayList<>();
            
            // Write header
            lines.add("CustomerID,Name,Email,Age,City");
            
            // Write each customer
            for (Customer customer : customers) {
                String line = String.format("%d,%s,%s,%d,%s",
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getAge(),
                    customer.getCity()
                );
                lines.add(line);
            }
            
            // Write all lines at once
            Files.write(Paths.get(filename), lines);
            System.out.println("Wrote " + (lines.size() - 1) + " customers to " + filename + " (Simple method)");
            
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
    
    /**
     * METHOD 2: Write customers using BufferedWriter - MOST EFFICIENT
     * Best for: Large writes, streaming, performance
     */
    public static void writeCustomersEfficient(List<Customer> customers, String filename) {
        // implement this method
    }
    
    
    // ==================== FILTERING METHODS ====================
    
    /**
     * Filter customers older than specified age
     */
    public static List<Customer> filterOlderThan(List<Customer> customers, int age) {
        // implement this method
        return null;
    }
    
    /**
     * Filter customers from specific city
     */
    public static List<Customer> filterByCity(List<Customer> customers, String city) {
        // implement this method
        return null;

    }
    
    
    // ==================== PRINTING METHODS ====================
    
    /**
     * Print all customers in formatted list
     */
    public static void printCustomers(List<Customer> customers) {
        // implement this method
    }
    
    /**
     * Print customers filtered by age with description
     */
    public static void printCustomersOlderThan(List<Customer> customers, int age) {
        // implement this method
    }
    
    /**
     * Print customers from specific city with description
     */
    public static void printCustomersByCity(List<Customer> customers, String city) {
        // implement this method

    }
}
