import java.nio.file.*;
import java.io.*;
import java.util.*;

/**
 * Centralized File I/O class handling all file operations
 * - Reading customers from file (2 text methods + 1 binary method)
 * - Writing customers to file (2 text methods + 1 binary method)
 * - Filtering data
 * - Printing data
 */
public class FileIO {
    
    // ==================== READING METHODS (TEXT) ====================
    
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
     * METHOD 2: Read customers using BufferedReader - MOST EFFICIENT
     * Best for: Large files, streaming, memory efficiency
     */
    public static List<Customer> readCustomersEfficient(String filename) {
        List<Customer> customers = new ArrayList<>();
        int lineNumber = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                // Skip header
                if (lineNumber == 1) continue;
                
                try {
                    Customer customer = parseCustomerLine(line);
                    if (customer != null) {
                        customers.add(customer);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Line " + lineNumber + " error: " + e.getMessage());
                }
            }
            
            System.out.println("Loaded " + customers.size() + " customers (Efficient method)");
            
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
    
    
    // ==================== READING METHODS (BINARY) ====================
    
    /**
     * METHOD 3: Read customers using ObjectInputStream - BINARY FORMAT
     * Best for: Saving entire object collections with full Java object preservation
     * NOTE: Customer class must implement Serializable interface
     */
    @SuppressWarnings("unchecked")
    public static List<Customer> readCustomersBinary(String filename) {
        List<Customer> customers = new ArrayList<>();
        
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(filename))) {
            
            // Read the entire List<Customer> object from binary file
            customers = (List<Customer>) input.readObject();
            
            System.out.println("Loaded " + customers.size() + " customers (Binary method)");
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (ClassNotFoundException e) {
            System.out.println("Customer class not found - incompatible file format");
        } catch (IOException e) {
            System.out.println("Error reading binary file: " + e.getMessage());
        }
        
        return customers;
    }
    
    
    // ==================== WRITING METHODS (TEXT) ====================
    
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            
            // Write header
            writer.write("CustomerID,Name,Email,Age,City");
            writer.newLine();
            
            // Write each customer
            for (Customer customer : customers) {
                String line = String.format("%d,%s,%s,%d,%s",
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getAge(),
                    customer.getCity()
                );
                writer.write(line);
                writer.newLine();
            }
            
            System.out.println("Wrote " + customers.size() + " customers to " + filename + " (Efficient method)");
            
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
    
    
    // ==================== WRITING METHODS (BINARY) ====================
    
    /**
     * METHOD 3: Write customers using ObjectOutputStream - BINARY FORMAT
     * Best for: Complete object serialization, fastest I/O, smallest file size
     * NOTE: Customer class must implement Serializable interface
     */
    public static void writeCustomersBinary(List<Customer> customers, String filename) {
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            
            // Write the entire List<Customer> object to binary file
            output.writeObject(customers);
            
            System.out.println("Wrote " + customers.size() + " customers to " + filename + " (Binary method)");
            
        } catch (IOException e) {
            System.out.println("Error writing binary file: " + e.getMessage());
        }
    }
    
    
    // ==================== FILTERING METHODS ====================
    
    /**
     * Filter customers older than specified age
     */
    public static List<Customer> filterOlderThan(List<Customer> customers, int age) {
        List<Customer> filtered = new ArrayList<>();
        
        for (Customer customer : customers) {
            if (customer.getAge() > age) {
                filtered.add(customer);
            }
        }
        
        return filtered;
    }
    
    /**
     * Filter customers from specific city
     */
    public static List<Customer> filterByCity(List<Customer> customers, String city) {
        List<Customer> filtered = new ArrayList<>();
        
        for (Customer customer : customers) {
            if (customer.getCity().equalsIgnoreCase(city)) {
                filtered.add(customer);
            }
        }
        
        return filtered;
    }
    
    
    // ==================== PRINTING METHODS ====================
    
    /**
     * Print all customers in formatted list
     */
    public static void printCustomers(List<Customer> customers) {
        if (customers.isEmpty()) {
            System.out.println("No customers to display.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("CUSTOMER LIST (" + customers.size() + " total)");
        System.out.println("=".repeat(70));
        
        for (Customer customer : customers) {
            System.out.println(customer);
        }
        
        System.out.println("=".repeat(70) + "\n");
    }
    
    /**
     * Print customers filtered by age with description
     */
    public static void printCustomersOlderThan(List<Customer> customers, int age) {
        List<Customer> filtered = filterOlderThan(customers, age);
        
        System.out.println("\nCustomers older than " + age + ":");
        System.out.println("-".repeat(70));
        
        if (filtered.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer customer : filtered) {
                System.out.println(customer);
            }
        }
    }
    
    /**
     * Print customers from specific city with description
     */
    public static void printCustomersByCity(List<Customer> customers, String city) {
        List<Customer> filtered = filterByCity(customers, city);
        
        System.out.println("\nCustomers from " + city + ":");
        System.out.println("-".repeat(70));
        
        if (filtered.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer customer : filtered) {
                System.out.println(customer);
            }
        }
    }
}
