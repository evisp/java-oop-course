import java.util.*;
import java.io.*;
import java.nio.file.*;

public class BinaryVsTextDemo {
    public static void main(String[] args) {
        // Read from text file
        List<Customer> customers = FileIO.readCustomersSimple("customers.txt");
        
        // Save to BINARY format
        FileIO.writeCustomersBinary(customers, "customers.dat");
         
        // Load from BINARY format
        List<Customer> fromBinary = FileIO.readCustomersBinary("customers.dat");
        FileIO.printCustomers(fromBinary);
        
        
    }
}
