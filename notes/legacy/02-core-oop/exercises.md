# Core OOP Principles

## 1. Inheritance and Polymorphism: University Login System

In real-world systems, different user types (students, instructors, administrators) share common features but require different security levels. This exercise demonstrates **inheritance** (code reuse through parent-child class relationships) and **polymorphism** (same method call, different behavior) using password generation and validation.

### Class Requirements

#### User Class (Parent Class)

**Variables:** username, password, email (all `String`)

**Methods:**

- Constructors (default and parameterized)
- Getters/setters for all variables
- `generatePassword()` - Returns `Basic password requirements: 6+ characters`
- `validatePassword(String password)` - Returns true if `length >= 6`
- `getAccessLevel()` - Returns `Basic User`
- `login(String enteredPassword)` - Returns true if `password` matches
- `displayUserInfo()` - Prints all user details


#### Student Class (extends User)

**Additional Variables:** `studentID`, `major` (both String)

**Constructor:** Use `super()` to call parent constructor, then initialize `studentID` and `major`

**Override these methods:**

- `generatePassword()` - "Student: `6+` characters, `1` number required"
- `validatePassword(String)` - Check: `length >= 6` AND has at least `1` digit
- `getAccessLevel()` - `Student Access`
- `displayUserInfo()` - Call `super.displayUserInfo()`, then print `studentID` and `major`


#### Instructor Class (extends User)

**Additional Variables:** `employeeID`, `department` (both `String`, private)

**Constructor:** Use `super()`, then initialize `employeeID` and `department`

**Override these methods:**

- `generatePassword()` - "Instructor: `8+` characters, uppercase, lowercase, `1` number"
- `validatePassword(String)` - Check: `length >= 8 AND has uppercase AND lowercase AND digit`
- `getAccessLevel()` - `Instructor Access`
- `displayUserInfo()` - Call `super.displayUserInfo()`, then print `employeeID` and department


#### Administrator Class (extends User)

**Additional Variables:** `adminID`, `securityClearance` (both String, private)

**Constructor:** Use `super()`, then initialize `adminID` and `securityClearance`

**Override these methods:**

- `generatePassword()` - "Admin: `10+` characters, uppercase, lowercase, number, special char (`!@#$%^&`)
- `validatePassword(String)` - Check: `length >= 10 AND uppercase AND lowercase AND digit AND special char`
- `getAccessLevel()` - `Administrator Access - Full Control`
- `displayUserInfo()` - Call `super.displayUserInfo()`, then print `adminID` and clearance


#### Main Class Requirements

1. Create three users (`Student`, `Instructor`, `Administrator`). 
2. Store all three in a `User[]` array
3. Loop through array and for each user:
   - Call `displayUserInfo()`
   - Call `generatePassword()` and print result
   - Call `getAccessLevel()` and print result
4. Test password validation with different passwords:
   - Weak: "`pass123`"
   - Moderate: "`Pass123`"
   - Strong: "`Pass123!@#`"
5. Set valid passwords and test `login()` method


### Implementation Tips

- Use `@Override` annotation when overriding methods
- Check character types: `Character.isUpperCase()`, `Character.isLowerCase()`, `Character.isDigit()`
- Check special chars: `"!@#$%^&".indexOf(password.charAt(i)) >= 0`
- Loop through password with: `for (int i = 0; i < password.length(); i++)`

