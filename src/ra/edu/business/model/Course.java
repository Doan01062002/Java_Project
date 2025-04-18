package ra.edu.business.model;

public class Course {
    private int id;
    private String courseCode;
    private String name;
    private String description;
    private Integer createdByAdminId;
    private int duration; // Duration in hours or days, define unit clearly
    private String instructor;
    private String createdAt;

    public Course() {}

    public Course(String courseCode, String name, String description, int duration, String instructor, String createdAt) {
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.instructor = instructor;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getCreatedByAdminId() { return createdByAdminId; }
    public void setCreatedByAdminId(Integer createdByAdminId) { this.createdByAdminId = createdByAdminId; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + (description == null ? "N/A" : description) + '\'' +
                ", duration=" + duration + " (units)" + // Specify units e.g., hours
                ", instructor='" + instructor + '\'' +
                ", createdByAdminId=" + (createdByAdminId == null ? "N/A" : createdByAdminId) +
                ", createdAt=" + createdAt +
                '}';
    }
}