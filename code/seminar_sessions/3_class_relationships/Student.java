public class Student {
    private String studentID;
    private String name;
    private double GPA;
    private int enrollmentYear;

    public Student() {
        this.studentID = "";
        this.name = "";
        this.GPA = 0.0;
        this.enrollmentYear = 0;
    }

    public Student(String studentID, String name, double GPA, int enrollmentYear) {
        this.studentID = studentID;
        this.name = name;
        this.GPA = GPA;
        this.enrollmentYear = enrollmentYear;
    }

    public String getStudentID() {
        return studentID;
    }

    public int getEnrollmentYear() {
        return enrollmentYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGPA() {
        return GPA;
    }

    public void updateGPA(double newGPA) {
        if (newGPA >= 0.0 && newGPA <= 4.0) {
            this.GPA = newGPA;
        }
    }

    public String calculateAcademicStanding() {
        if (GPA >= 2.0) {
            return "Good Standing";
        } else if (GPA >= 1.5) {
            return "Academic Probation";
        } else {
            return "Academic Warning";
        }
    }

    @Override
    public String toString() {
        return "Student[ID=" + studentID + ", Name=" + name + ", GPA=" + GPA + ", Year=" + enrollmentYear + "]";
    }
}
