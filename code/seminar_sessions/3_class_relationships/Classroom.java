public class Classroom {
    private String roomNumber;
    private String buildingName;
    private int capacity;
    private boolean hasProjector;

    public Classroom() {
        this.roomNumber = "";
        this.buildingName = "";
        this.capacity = 0;
        this.hasProjector = false;
    }

    public Classroom(String roomNumber, String buildingName, int capacity, boolean hasProjector) {
        this.roomNumber = roomNumber;
        this.buildingName = buildingName;
        this.capacity = capacity;
        this.hasProjector = hasProjector;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean getHasProjector() {
        return hasProjector;
    }

    public void setHasProjector(boolean hasProjector) {
        this.hasProjector = hasProjector;
    }

    public void displayClassroomInfo() {
        System.out.println("Location: " + getFullLocation() + ", Capacity: " + capacity + ", Projector: " + (hasProjector ? "Yes" : "No"));
    }

    public boolean isLargeRoom() {
        return capacity > 50;
    }

    public boolean canAccommodate(int numberOfStudents) {
        return capacity >= numberOfStudents;
    }

    public String getFullLocation() {
        return buildingName + ", Room " + roomNumber;
    }

    @Override
    public String toString() {
        return "Classroom[" + getFullLocation() + ", Capacity=" + capacity + "]";
    }
}
