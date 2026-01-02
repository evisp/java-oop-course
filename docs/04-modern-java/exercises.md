
# HashMap Practice - Customers Dataset

## Context (why this matters)
HashMaps are one of the most useful structures in programming because they allow fast lookups, grouping, counting, and building indexes from real data.  
In real projects (and interviews), it's common to load a dataset (like customers) and then build maps such as "ID → Customer", "City → List of Customers", or "Domain → Customers".


## Given code (already provided)
The following classes are already implemented and **must not be rewritten**:

- `Customer`  
  - Represents one customer with fields like `id`, `name`, `email`, `age`, and `city`. 
  - Implements `Comparable` with a natural order based on customer `id` (ascending). 

- `CustomerFileReader`  
  - Reads customer data from a text/CSV file and returns a list of customers. 
  - The file has a header line like: `CustomerID,Name,Email,Age,City`. 

## Input file
You will work with a file called `customers.txt`

```text
CustomerID,Name,Email,Age,City
1,Alan Turing,alan.turing@bletchley.uk,41,London
2,Ada Lovelace,ada.lovelace@analytical.engine,36,London
3,Grace Hopper,grace.hopper@navy.mil,85,New York
...
```


## Your tasks
Create a new class:

- `CustomerCollectionsDemo`

This class will contain methods that practice HashMaps. Then create a `main` class to read from `customers.txt` and demonstrate that each method works.


### Task 1 - Index customers by ID
**Method:** `indexById(List<Customer> customers)`  
**Return:** `Map<Integer, Customer>`  

Goal: Build a map where the key is `customerId` and the value is the `Customer` object.

Example idea: `map.get(1912)` should return the customer with ID 1912.


### Task 2 - Count customers per city
**Method:** `countByCity(List<Customer> customers)`  
**Return:** `Map<String, Integer>`  

Goal: Create a map like:
- `"London" -> 12`
- `"Paris" -> 6`

### Task 3 - Group customers by city (key → list)
**Method:** `groupByCity(List<Customer> customers)`  
**Return:** `Map<String, List<Customer>>`  

Goal: Store a city as a key, and as value store all customers from that city in a list.


### Task 4 - Find all customers from a specific city
**Method:** `getCustomersFromCity(Map<String, List<Customer>> byCity, String city)`  
**Return:** `List<Customer>`  

Goal: Using the grouped map from Task 3, return the list for one city.  
If the city does not exist in the map, return an empty list.


### Task 5 - Nested grouping: city → age → customers
**Method:** `groupByCityThenAge(List<Customer> customers)`  
**Return:** `Map<String, Map<Integer, List<Customer>>>`  

Goal: Create a nested structure such as:
- `"London" -> { 36 -> [Ada Lovelace] }`
- `"Stanford" -> { 87 -> [Donald Knuth], 84 -> [John McCarthy] }`

This is a very common "real data" pattern: grouping by multiple attributes.


### Task 6 - Group customers by email domain
**Method:** `groupByEmailDomain(List<Customer> customers)`  
**Return:** `Map<String, List<Customer>>`  

Goal: Extract the domain from emails (everything after `@`) and group customers:
- `"gmail.com" -> [customers...]`
- `"yahoo.com" -> [customers...]`

If an email is malformed (no `@`), skip that customer.


### Task 7 - Sort customers using Comparable
**Method:** `sortCustomersNatural(List<Customer> customers)`  
**Return:** `List<Customer>`  

Goal: Return a new list sorted using the natural ordering from `Customer.compareTo`, which is based on customer `id`. 

--- 

## Main program requirements
Create a `Main` class with a `main` method that:

1. Reads customers from `customers.txt` using `CustomerFileReader`. 
2. Calls each task method (1–7).  
3. Prints results clearly (examples: print the map size, print one group, print sorted list).  

> Remember: Programs must be written for people to read, and only incidentally for machines to execute.
