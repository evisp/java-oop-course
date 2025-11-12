import java.util.ArrayList;

public class Department {
    private String departmentName;
    private String location;
    private String departmentHead;
    private ArrayList<Professor> facultyList;

    public Department() {
        this.departmentName = "";
        this.location = "";
        this.departmentHead = "";
        this.facultyList = new ArrayList<Professor>();
    }

    public Department(String departmentName, String location, String departmentHead) {
        this.departmentName = departmentName;
        this.location = location;
        this.departmentHead = departmentHead;
        this.facultyList = new ArrayList<Professor>();
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(String departmentHead) {
        this.departmentHead = departmentHead;
    }

    public void addProfessor(Professor professor) {
        this.facultyList.add(professor);
    }

    public void removeProfessor(String employeeID) {
        for (int i = facultyList.size() - 1; i >= 0; i--) {
            if (facultyList.get(i).getEmployeeID().equals(employeeID)) {
                facultyList.remove(i);
            }
        }
    }

    public int getFacultyCount() {
        return facultyList.size();
    }

    public void listAllFaculty() {
        System.out.println("Faculty in " + departmentName + ":");
        for (int i = 0; i < facultyList.size(); i++) {
            Professor p = facultyList.get(i);
            System.out.println("  - " + p.getName() + " (" + p.getSpecialization() + ", " + p.getYearsOfExperience() + " years)");
        }
    }

    public Professor findProfessor(String employeeID) {
        for (int i = 0; i < facultyList.size(); i++) {
            Professor p = facultyList.get(i);
            if (p.getEmployeeID().equals(employeeID)) {
                return p;
            }
        }
        return null;
    }

    public void displayDepartmentInfo() {
        System.out.println("Department: " + departmentName + ", Head: " + departmentHead + ", Faculty Count: " + getFacultyCount());
    }

    public boolean isLargeDepartment() {
        return getFacultyCount() > 20;
    }

    @Override
    public String toString() {
        return "Department[Name=" + departmentName + ", Head=" + departmentHead + ", Faculty=" + getFacultyCount() + "]";
    }
}
