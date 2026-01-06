import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CustomerFileReader {

    public static List<Customer> readCustomers(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path); // reads all lines into a List<String>
        List<Customer> customers = new ArrayList<>();

        boolean isFirst = true;
        for (String line : lines) {
            if (isFirst) {
                // Skip header line: CustomerID,Name,Email,Age,City
                isFirst = false;
                continue;
            }
            if (line.isBlank()) {
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length != 5) {
                System.err.println("Skipping malformed line: " + line);
                continue;
            }

            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String email = parts[2].trim();
            int age = Integer.parseInt(parts[3].trim());
            String city = parts[4].trim();

            Customer customer = new Customer(id, name, email, age, city);
            customers.add(customer);
        }

        return customers;
    }
}
