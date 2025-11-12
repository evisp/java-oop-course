import java.util.ArrayList;

public class Course {
    private String courseCode;
    private String courseName;
    private int credits;
    private String semester;
    private Professor instructor;
    private Classroom location;
    private ArrayList<Student> enrolledStudents;

    public Course() {
        this.courseCode = "";
        this.courseName = "";
        this.credits = 0;
        this.semester = "";
        this.instructor = null;
        this.location = null;
        this.enrolledStudents = new ArrayList<Student>();
    }

    public Course(String courseCode, String courseName, int credits, String semester) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.semester = semester;
        this.instructor = null;
        this.location = null;
        this.enrolledStudents = new ArrayList<Student>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void assignProfessor(Professor professor) {
        this.instructor = professor;
    }

    public Professor getInstructor() {
        return instructor;
    }

    public String getInstructorName() {
        if (instructor != null) {
            return instructor.getName();
        } else {
            return "Not Assigned";
        }
    }

    public void assignClassroom(Classroom classroom) {
        this.location = classroom;
    }

    public Classroom getClassroom() {
        return location;
    }

    public String getClassroomLocation() {
        if (location != null) {
            return location.getFullLocation();
        } else {
            return "Not Assigned";
        }
    }

    public void enrollStudent(Student student) {
        this.enrolledStudents.add(student);
    }

    public void dropStudent(String studentID) {
        for (int i = enrolledStudents.size() - 1; i >= 0; i--) {
            if (enrolledStudents.get(i).getStudentID().equals(studentID)) {
                enrolledStudents.remove(i);
            }
        }
    }

    public int getEnrollmentCount() {
        return enrolledStudents.size();
    }

    public void listEnrolledStudents() {
        System.out.println("Students enrolled in " + getFullCourseName() + ":");
        for (int i = 0; i < enrolledStudents.size(); i++) {
            Student s = enrolledStudents.get(i);
            System.out.println("  - " + s.getName() + " (ID: " + s.getStudentID() + ", GPA: " + s.getGPA() + ")");
        }
    }

    public boolean isStudentEnrolled(String studentID) {
        for (int i = 0; i < enrolledStudents.size(); i++) {
            if (enrolledStudents.get(i).getStudentID().equals(studentID)) {
                return true;
            }
        }
        return false;
    }

    public void displayCourseInfo() {
        System.out.println("=== Course Information ===");
        System.out.println("Course: " + getFullCourseName());
        System.out.println("Credits: " + credits);
        System.out.println("Semester: " + semester);
        System.out.println("Instructor: " + getInstructorName());
        System.out.println("Location: " + getClassroomLocation());
        System.out.println("Enrollment: " + getEnrollmentCount() + " students");
    }

    public boolean isFullCourse() {
        return credits >= 3;
    }

    public String getFullCourseName() {
        return courseCode + ": " + courseName;
    }

    @Override
    public String toString() {
        return "Course[" + getFullCourseName() + ", Credits=" + credits + ", Instructor=" + getInstructorName() + "]";
    }
}
