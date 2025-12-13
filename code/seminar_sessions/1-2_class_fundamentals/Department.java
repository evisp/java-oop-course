public class Department {
    private String departmentName;
    private String location;
    private int numberOfFaculty;
    private String departmentHead;

    public Department() {
        this.departmentName = "";
        this.location = "";
        this.numberOfFaculty = 0;
        this.departmentHead = "";
    }

    public Department(String departmentName, String location, int numberOfFaculty, String departmentHead) {
        this.departmentName = departmentName;
        this.location = location;
        this.numberOfFaculty = numberOfFaculty;
        this.departmentHead = departmentHead;
    }

    // Getters and Setters for all variables
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getNumberOfFaculty() { return numberOfFaculty; }
    public void setNumberOfFaculty(int numberOfFaculty) { this.numberOfFaculty = numberOfFaculty; }
    public String getDepartmentHead() { return departmentHead; }
    public void setDepartmentHead(String departmentHead) { this.departmentHead = departmentHead; }

    public void displayDepartmentInfo() {
        System.out.println("Department: " + departmentName + ", Head: " + departmentHead + ", Faculty: " + numberOfFaculty);
    }

    public void addFaculty() {
        this.numberOfFaculty++;
    }

    public void removeFaculty() {
        if (this.numberOfFaculty > 0) {
            this.numberOfFaculty--;
        }
    }

    public boolean isLargeDepartment() {
        return numberOfFaculty > 20;
    }

    @Override
    public String toString() {
        return "Department[" + departmentName + "]";
    }
}
