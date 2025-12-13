public class Professor {
    private String employeeID;
    private String name;
    private String specialization;
    private int yearsOfExperience;

    public Professor() {
        this.employeeID = "";
        this.name = "";
        this.specialization = "";
        this.yearsOfExperience = 0;
    }

    public Professor(String employeeID, String name, String specialization, int yearsOfExperience) {
        this.employeeID = employeeID;
        this.name = name;
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
    }

    // Getters and Setters for all variables
    public String getEmployeeID() { return employeeID; }
    public void setEmployeeID(String employeeID) { this.employeeID = employeeID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }


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
        return "Professor[ID=" + employeeID + ", Name=" + name + ", Specialization=" + specialization + "]";
    }
}
