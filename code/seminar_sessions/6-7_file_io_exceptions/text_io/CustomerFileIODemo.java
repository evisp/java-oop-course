import java.util.*;

public class CustomerFileIODemo {
    
    public static void main(String[] args) {
        String inputFile = "customers.txt";
        String outputFiltered = "customers_filtered.txt";
        
        System.out.println("=== CUSTOMER FILE I/O DEMO ===\n");
        
        // 1. READ using simple method
        System.out.println("1. READING CUSTOMERS (Simple):");
        List<Customer> customers = FileIO.readCustomersSimple(inputFile);
        
        // 2. PRINT ALL
        System.out.println("\n2. ALL CUSTOMERS:");
        FileIO.printCustomers(customers);
        
        // 3. FILTER and print
        System.out.println("3. FILTERING (Age > 40):");
        FileIO.printCustomersOlderThan(customers, 40);
        
        // 4. FILTER by city and print
        System.out.println("4. FILTERING (City):");
        FileIO.printCustomersByCity(customers, "Göttingen");
        
        // 5. WRITE filtered data
        System.out.println("5. WRITING FILTERED DATA:");
        List<Customer> filtered = FileIO.filterOlderThan(customers, 40);
        FileIO.writeCustomersSimple(filtered, outputFiltered);
        
        // 6. VERIFY by reading with efficient method
        System.out.println("\n6. VERIFYING FILTERED FILE (Efficient):");
        List<Customer> verified = FileIO.readCustomersEfficient(outputFiltered);
        FileIO.printCustomers(verified);
    }
}
