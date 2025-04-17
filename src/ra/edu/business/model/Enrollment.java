package ra.edu.business.model;

import java.sql.Timestamp;

public class Enrollment {
    private int id;
    private String enrollmentCode;
    private int studentRefId;
    private int courseRefId;
    private Integer adminRefId;
    private Timestamp registrationDate;
    private EnrollmentStatus status;

    private Student student;
    private Course course;
    private Admin approverAdmin;

    public Enrollment() {
        this.status = EnrollmentStatus.WAITING;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEnrollmentCode() { return enrollmentCode; }
    public void setEnrollmentCode(String enrollmentCode) { this.enrollmentCode = enrollmentCode; }
    public int getStudentRefId() { return studentRefId; }
    public void setStudentRefId(int studentRefId) { this.studentRefId = studentRefId; }
    public int getCourseRefId() { return courseRefId; }
    public void setCourseRefId(int courseRefId) { this.courseRefId = courseRefId; }
    public Integer getAdminRefId() { return adminRefId; }
    public void setAdminRefId(Integer adminRefId) { this.adminRefId = adminRefId; }
    public Timestamp getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Timestamp registrationDate) { this.registrationDate = registrationDate; }
    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Admin getApproverAdmin() { return approverAdmin; }
    public void setApproverAdmin(Admin approverAdmin) { this.approverAdmin = approverAdmin;}


    @Override
    public String toString() {
        String studentInfo = student != null ? student.getFullName() + " (" + student.getStudentCode() + ")" : "ID: " + studentRefId;
        String courseInfo = course != null ? course.getName() + " (" + course.getCourseCode() + ")" : "ID: " + courseRefId;
        String adminInfo = adminRefId != null ? "ID: " + adminRefId : "Chưa duyệt";

        return "Enrollment{" +
                "id=" + id +
                ", enrollmentCode='" + enrollmentCode + '\'' +
                ", student=" + studentInfo +
                ", course=" + courseInfo +
                ", registrationDate=" + registrationDate +
                ", status=" + status +
                ", approvedByAdmin=" + adminInfo +
                '}';
    }
}
