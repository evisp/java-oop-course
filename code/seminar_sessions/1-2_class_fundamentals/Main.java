public class Main {
    public static void main(String[] args) {
        // Create Students
        Student ada = new Student("1842", "Ada Lovelace", 3.9, 1840);
        Student turing = new Student("1936", "Alan Turing", 3.8, 1934);

        // Create Professors
        Professor babbage = new Professor("PROF01", "Charles Babbage", "Mechanical Computation", 20);
        Professor wiles = new Professor("PROF02", "Andrew Wiles", "Number Theory", 15);

        // Create Courses
        Course analyticalEngines = new Course("CS101", "Analytical Engines", 3, "Fall 1842");
        Course computability = new Course("CS201", "Computability Theory", 3, "Fall 1936");

        // Create a Classroom
        Classroom lectureHall = new Classroom("A1", "Babbage Hall", 100, true);

        // Create a Department
        Department csDept = new Department("Computer Science", "Babbage Hall", 10, "Charles Babbage");
        csDept.addFaculty(); // Add a new faculty member

        System.out.println("--- University Management System ---");

        // Display Student Info
        System.out.println("\n--- Student Details ---");
        System.out.println(ada.toString() + " - Standing: " + ada.calculateAcademicStanding());
        System.out.println(turing.toString() + " - Standing: " + turing.calculateAcademicStanding());

        // Display Professor Info
        System.out.println("\n--- Professor Details ---");
        System.out.println(babbage.getName() + " - Title: " + babbage.getAcademicTitle());
        System.out.println(wiles.getName() + " - Experienced: " + wiles.isExperienced());

        // Display Course Info
        System.out.println("\n--- Course Details ---");
        analyticalEngines.displayCourseInfo();
        System.out.println(computability.getFullCourseName() + " - Full Course: " + computability.isFullCourse());

        // Display Classroom and Department Info
        System.out.println("\n--- Infrastructure ---");
        lectureHall.displayClassroomInfo();
        System.out.println("Can accommodate 80 students: " + lectureHall.canAccommodate(80));
        csDept.displayDepartmentInfo();
        System.out.println("Is it a large department? " + csDept.isLargeDepartment());
    }
}
