public class Course {
    private String courseCode;
    private String courseName;
    private int credits;
    private String semester;

    public Course() {
        this.courseCode = "";
        this.courseName = "";
        this.credits = 0;
        this.semester = "";
    }

    public Course(String courseCode, String courseName, int credits, String semester) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.semester = semester;
    }

    // Getters and Setters for all variables
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public void displayCourseInfo() {
        System.out.println("Course: " + getFullCourseName() + ", Credits: " + credits + ", Semester: " + semester);
    }

    public boolean isFullCourse() {
        return credits >= 3;
    }

    public String getFullCourseName() {
        return courseCode + ": " + courseName;
    }

    @Override
    public String toString() {
        return "Course[" + getFullCourseName() + "]";
    }
}
