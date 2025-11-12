public class Main {
    public static void main(String[] args) {
        System.out.println("=== University Management System ===\n");

        // Create Department
        Department computerScience = new Department("Computer Science", "Turing Hall", "Charles Babbage");

        // Create Professors and assign to department
        Professor alanTuring = new Professor("T1912", "Alan Turing", "Computability Theory", 12);
        Professor graceHopper = new Professor("H1906", "Grace Hopper", "Compilers", 15);
        
        alanTuring.assignDepartment(computerScience);
        computerScience.addProfessor(alanTuring);
        computerScience.addProfessor(graceHopper);

        // Create Classroom
        Classroom lectureHall = new Classroom("101", "Turing Hall", 50, true);

        // Create Course and assign professor and classroom
        Course algorithms = new Course("CS301", "Advanced Algorithms", 3, "Fall 2025");
        algorithms.assignProfessor(alanTuring);
        algorithms.assignClassroom(lectureHall);
        alanTuring.assignCourse(algorithms);

        // Create and enroll students
        Student ada = new Student("S1815", "Ada Lovelace", 3.95, 2023);
        Student donald = new Student("S1934", "Donald Knuth", 3.88, 2023);
        Student margaret = new Student("S1936", "Margaret Hamilton", 3.92, 2024);
        
        algorithms.enrollStudent(ada);
        algorithms.enrollStudent(donald);
        algorithms.enrollStudent(margaret);

        // Display course information
        System.out.println("--- Course Information ---");
        algorithms.displayCourseInfo();
        System.out.println();

        // List enrolled students
        algorithms.listEnrolledStudents();
        System.out.println();

        // Drop a student
        System.out.println("--- After dropping Donald Knuth ---");
        algorithms.dropStudent("S1934");
        System.out.println("Current enrollment: " + algorithms.getEnrollmentCount() + " students\n");

        // Show professor's courses
        System.out.println("--- Professor Information ---");
        System.out.println(alanTuring.getName() + " (" + alanTuring.getAcademicTitle() + ")");
        alanTuring.listCourses();
        System.out.println();

        // Show department faculty
        System.out.println("--- Department Faculty ---");
        computerScience.listAllFaculty();
    }
}
