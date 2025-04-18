package ra.edu.business.model;

import java.sql.Timestamp;
import java.util.Date;

public class Student {
    private int id;
    private String studentCode;
    private String username;
    private String password; // Should be hashed
    private String fullName;
    private String email;
    private Boolean sex; // true for male, false for female, null if not specified
    private String phone;
    private Date dob; // Date of Birth
    private Timestamp createdAt;
    private boolean isActive;

    public Student() {
        this.isActive = true;
    }

    public Student(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password; // Should be hashed
        this.email = email;
        this.isActive = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Boolean getSex() { return sex; }
    public void setSex(Boolean sex) { this.sex = sex; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentCode='" + studentCode + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + (sex == null ? "N/A" : (sex ? "Nam" : "Nữ")) +
                ", phone='" + (phone == null ? "N/A" : phone) + '\'' +
                ", dob=" + (dob == null ? "N/A" : dob) +
                ", createdAt=" + createdAt +
                ", isActive=" + isActive +
                '}';
    }
}