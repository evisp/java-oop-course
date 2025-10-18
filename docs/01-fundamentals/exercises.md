# Thinking Like an Object-Oriented Designer and Programmer

When designing software, imagine building real-world things as software objects. Each object represents a **thing** in the system with its own information and actions.

To practice this way of thinking, focus on these questions when given a system:

- What are the main things (entities) involved?
- What details (attributes) does each thing have?
- What actions (behaviors) can each thing perform?
- How do these things connect or relate to each other?
- Are there any important details that should be kept private or protected?

## 1. Introduction to OOP

### Scenario 1: Library Management System

![Library Management System](https://i.imgur.com/hWltn6k.jpeg)

Imagine you are designing software to help a library keep track of its books and users.

Consider these questions:

- What are the main things in the library system? Think about both physical things and people.
- For each main thing, what details are important to store?
- What actions will each thing need to perform? For example, what can happen to a book?
- How do the things interact or work together? Which things rely on or connect with others?
- Which details should be kept private or secure? Why might that be necessary?


 
### Scenario 2: University Management System

![Library Management System](https://i.imgur.com/vhEfvXP.jpeg)
 

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
    - Provide methods or functions to safely get (read) and set (modify) each property
    - Make sure the number of copies cannot be set to a negative number.

4. **Methods (Behaviors)**
    - A method to **borrow** a book: this should reduce the number of available copies by one, but only if there is at least one copy available.
    - A method to **return** a book: this should increase the number of available copies by one.


## 2. Fundamentals of Classes and Objects

### Project Goal

The goal of this project is to practice Object-Oriented Programming fundamentals by developing a simple University Management System. Each group will design and implement one class independently. After all groups complete their classes, we will integrate them in a main program to demonstrate how individual components can work together to form a complete software system.

### General Requirements for All Classes

Each class must include:

- **Private variables** as specified in your class requirements
- **Two constructors**: a default constructor and a parameterized constructor
- **Getter and setter methods** for all private variables
- **toString() method** that returns a formatted string representation of the object
- **Additional methods** as specified for your class 

---


### Class 1: Student

#### Private Variables

- `studentID` (String or int) - Unique identifier for the student
- `name` (String) - Full name of the student
- `GPA` (double) - Grade Point Average (0.0 to 4.0)
- `enrollmentYear` (int) - Year the student enrolled at the university

#### Constructors

1. **Default constructor**: Initialize all variables with default values (empty strings, 0, etc.)
2. **Parameterized constructor**: Accept and initialize studentID, name, GPA, and enrollmentYear

#### Required Methods

- **Getter methods only** for studentID and enrollmentYear (these cannot be changed after creation)
- **Getter and setter methods** for name
- **Getter method only** for GPA (no setter - use updateGPA method instead)
- **updateGPA(double newGPA)**: Update the student's GPA with validation (must be between 0.0 and 4.0)
- **toString()**: Return a formatted string containing all student information
- **calculateAcademicStanding()**: Return a String indicating academic standing based on GPA:
    - "Good Standing" if GPA >= 2.0
    - "Academic Probation" if GPA >= 1.5 and GPA < 2.0
    - "Academic Warning" if GPA < 1.5

### Class 2: Professor

#### Private Variables

- `employeeID` (String or int) - Unique identifier for the professor
- `name` (String) - Full name of the professor
- `specialization` (String) - Subject area of expertise (e.g., "Computer Science", "Mathematics")
- `yearsOfExperience` (int) - Total years of teaching experience

#### Constructors

1. **Default constructor**: Initialize all variables with default values
2. **Parameterized constructor**: Accept and initialize employeeID, name, specialization, and yearsOfExperience

#### Required Methods

- **Getter and setter methods** for all four variables
- **toString()**: Return a formatted string containing all professor information
- **isExperienced()**: Return true if `yearsOfExperience` is greater than `5`, false otherwise
- **getAcademicTitle()**: Return a String with academic title based on experience (e.g., "Assistant Professor" for 0-5 years, "Associate Professor" for 6-10 years, "Professor" for 11+ years)

---

### Class 3: Course

#### Private Variables

- `courseCode` (String) - Unique course identifier (e.g., "CS101", "MATH201")
- `courseName` (String) - Full name of the course
- `credits` (int) - Number of credit hours (typically 1-6)
- `semester` (String) - When the course is offered (e.g., "Fall 2025", "Spring 2026")

#### Constructors

1. **Default constructor**: Initialize all variables with default values
2. **Parameterized constructor**: Accept and initialize courseCode, courseName, credits, and semester

#### Required Methods

- **Getter and setter methods** for all four variables
- **toString()**: Return a formatted string containing all course information
- **displayCourseInfo()**: Print complete course details in a readable format
- **isFullCourse()**: Return true if credits >= 3, false otherwise
- **getFullCourseName()**: Return a String combining `courseCode` and `courseName` (e.g., "CS101: Introduction to Programming")

--- 

### Class 4: Classroom

#### Private Variables

- `roomNumber` (String) - Room identifier (e.g., "101", "A205")
- `buildingName` (String) - Name or code of the building
- `capacity` (int) - Maximum number of students the room can hold
- `hasProjector` (boolean) - Whether the room has a projector

#### Constructors

1. **Default constructor**: Initialize all variables with default values
2. **Parameterized constructor**: Accept and initialize roomNumber, buildingName, capacity, and hasProjector

#### Required Methods

- **Getter and setter methods** for all four variables
- **toString()**: Return a formatted string containing all classroom information
- **displayClassroomInfo()**: Print complete classroom details in a readable format
- **isLargeRoom()**: Return true if `capacity > 50`, false otherwise
- **canAccommodate(int numberOfStudents)**: Accept a number of students and return true if the classroom capacity can accommodate them, false otherwise
- **getFullLocation()**: Return a String combining building name and room number (e.g., "Building A, Room 205")

--- 

### Class 5: Department

#### Private Variables

- `departmentName` (String) - Name of the department (e.g., "Computer Science", "Mathematics")
- `location` (String) - Building or floor where department offices are located
- `numberOfFaculty` (int) - Total number of faculty members in the department
- `departmentHead` (String) - Name of the department chairperson

#### Constructors

1. **Default constructor**: Initialize all variables with default values
2. **Parameterized constructor**: Accept and initialize departmentName, location, numberOfFaculty, and departmentHead

#### Required Methods

- **Getter and setter methods** for all four variables
- **toString()**: Return a formatted string containing all department information
- **displayDepartmentInfo()**: Print complete department details in a readable format
- **addFaculty()**: Increment the numberOfFaculty by 1
- **removeFaculty()**: Decrement the numberOfFaculty by 1 (ensure it doesn't go below 0)
- **isLargeDepartment()**: Return true if numberOfFaculty > 20, false otherwise

---

### Submission Guidelines

- Each group must submit a single `.java` file containing their class
- Include proper comments documenting your class, variables, and methods
- Test your class by creating objects and calling all methods to ensure they work correctly
- Your class name must match exactly as specified above (Student, Professor, Course, Classroom, or Department)

### Integration

Once all groups have completed their classes, we will create a main program that:

- Creates objects from each class
- Demonstrates interactions between objects (e.g., a Professor teaching a Course in a Classroom)
- Shows how independent components combine to form a functional system