# Thinking Like an Object-Oriented Designer and Programmer

When designing software, imagine building real-world things as software objects. Each object represents a **thing** in the system with its own information and actions.

To practice this way of thinking, focus on these questions when given a system:

- What are the main things (entities) involved?
- What details (attributes) does each thing have?
- What actions (behaviors) can each thing perform?
- How do these things connect or relate to each other?
- Are there any important details that should be kept private or protected?

## Introduction to OOP

### Scenario 1: Library Management System

Imagine you are designing software to help a library keep track of its books and users.

Consider these questions:
- What are the main things in the library system? Think about both physical things and people.
- For each main thing, what details are important to store?
- What actions will each thing need to perform? For example, what can happen to a book?
- How do the things interact or work together? Which things rely on or connect with others?
- Which details should be kept private or secure? Why might that be necessary?

### Scenario 2: University Management System

Now think about software for managing courses, students, and staff at a university.

Ask yourself:
- What are the key things in a university system?
- What important details would each thing need to keep track of?
- What kinds of actions or tasks must those things be able to do?
- How would these things be connected or related to one another?
- Are there details that need to be kept confidential? Who should have access to them?

This way of breaking down problems helps you see the system as a group of interacting parts, each with its own role. Taking these steps before writing any code sets a strong foundation for building clear and flexible software.


### Hands-On Coding

Let's get practical. Create a class named `Book` that models a real book with the following features:

1. **Properties (Variables)**
   - Title (text)
   - Author (text)
   - ISBN number (text or number)
   - Number of copies available (whole number)

2. **Constructor**
   - Create a way to set all these properties when a new `Book` object is created.

3. **Getters and Setters**
   - Provide methods or functions to safely get (read) and set (modify) each property.
   - Make sure the number of copies cannot be set to a negative number.

4. **Methods (Behaviors)**
   - A method to **borrow** a book: this should reduce the number of available copies by one, but only if there is at least one copy available.
   - A method to **return** a book: this should increase the number of available copies by one.
