import java.util.ArrayList;

public class Professor {
    private String employeeID;
    private String name;
    private String specialization;
    private int yearsOfExperience;
    private Department department;
    private ArrayList<Course> coursesTeaching;

    public Professor() {
        this.employeeID = "";
        this.name = "";
        this.specialization = "";
        this.yearsOfExperience = 0;
        this.department = null;
        this.coursesTeaching = new ArrayList<Course>();
    }

    public Professor(String employeeID, String name, String specialization, int yearsOfExperience) {
        this.employeeID = employeeID;
        this.name = name;
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
        this.department = null;
        this.coursesTeaching = new ArrayList<Course>();
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public void assignDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public String getDepartmentName() {
        if (department != null) {
            return department.getDepartmentName();
        } else {
            return "No Department";
        }
    }

    public void assignCourse(Course course) {
        this.coursesTeaching.add(course);
    }

    public void removeCourse(String courseCode) {
        for (int i = coursesTeaching.size() - 1; i >= 0; i--) {
            if (coursesTeaching.get(i).getCourseCode().equals(courseCode)) {
                coursesTeaching.remove(i);
            }
        }
    }

    public int getTeachingLoad() {
        return coursesTeaching.size();
    }

    public void listCourses() {
        System.out.println(name + " is teaching:");
        for (int i = 0; i < coursesTeaching.size(); i++) {
            Course c = coursesTeaching.get(i);
            System.out.println("  - " + c.getFullCourseName());
        }
    }

    public boolean isTeaching(String courseCode) {
        for (int i = 0; i < coursesTeaching.size(); i++) {
            if (coursesTeaching.get(i).getCourseCode().equals(courseCode)) {
                return true;
            }
        }
        return false;
    }

    public boolean isExperienced() {
        return yearsOfExperience > 5;
    }

    public String getAcademicTitle() {
        if (yearsOfExperience <= 5) {
            return "Assistant Professor";
        } else if (yearsOfExperience <= 10) {
            return "Associate Professor";
        } else {
            return "Professor";
        }
    }

    @Override
    public String toString() {
        return "Professor[ID=" + employeeID + ", Name=" + name + ", Specialization=" + specialization + ", Department=" + getDepartmentName() + "]";
    }
}
