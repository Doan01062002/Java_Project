package ra.edu.utils;

import ra.edu.business.model.Course;
import ra.edu.business.model.Enrollment;
import ra.edu.business.model.Student;
import ra.edu.business.service.course.CourseService;

import java.text.SimpleDateFormat;
import java.util.List;

public class UIUtils {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void printHeader(String title) {
        System.out.println("\n╒═══════════════════════════════════════════════╕");
        System.out.println("│ " + centerString(title.toUpperCase(), 45) + " │");
        System.out.println("╞═══════════════════════════════════════════════╡");
    }

    public static void printFooter() {
        System.out.println("╘═══════════════════════════════════════════════╛");
    }

    public static void showError(String message) {
        System.err.println(ANSI_RED + message + ANSI_RESET);
    }

    public static void showSuccess(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }

    public static String centerString(String str, int width) {
        int padding = (width - str.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + str + " ".repeat(Math.max(0, width - str.length() - padding));
    }

    public static String truncateString(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }

    public static void displayCourseTable(List<Course> courses) {
        System.out.println("\n┌───────┬────────────┬──────────────────────────┬────────────┬─────────────────┐");
        System.out.println("│ ID    │ Mã khóa    │ Tên khóa học             │ Thời lượng │ Giảng viên      │");
        System.out.println("├───────┼────────────┼──────────────────────────┼────────────┼─────────────────┤");
        for (Course course : courses) {
            System.out.printf("│ %-5d │ %-10s │ %-24s │ %-10d │ %-15s │%n",
                    course.getId(),
                    truncateString(course.getCourseCode(), 10),
                    truncateString(course.getName(), 24),
                    course.getDuration(),
                    truncateString(course.getInstructor(), 15));
        }
        System.out.println("└───────┴────────────┴──────────────────────────┴────────────┴─────────────────┘");
    }

    public static void displayStudentTable(List<Student> students) {
        System.out.println("\n┌───────┬────────────┬──────────────────────────┬──────────────────────────┬────────────┬────────────┐");
        System.out.println("│ ID    │ Mã học viên│ Họ tên                   │ Email                    │ SĐT        │ Trạng thái │");
        System.out.println("├───────┼────────────┼──────────────────────────┼──────────────────────────┼────────────┼────────────┤");
        for (Student student : students) {
            System.out.printf("│ %-5d │ %-10s │ %-24s │ %-24s │ %-10s │ %-10s │%n",
                    student.getId(),
                    truncateString(student.getStudentCode(), 10),
                    truncateString(student.getFullName(), 24),
                    truncateString(student.getEmail(), 24),
                    student.getPhone() != null ? truncateString(student.getPhone(), 10) : "",
                    student.isActive() ? "Hoạt động" : "Bị khóa");
        }
        System.out.println("└───────┴────────────┴──────────────────────────┴──────────────────────────┴────────────┴────────────┘");
    }

    public static void displayEnrollmentTable(List<Enrollment> enrollments, CourseService courseService) {
        System.out.println("\n┌───────┬────────────┬──────────────────────────┬────────────┬────────────┬─────────────────┐");
        System.out.println("│ ID    │ Mã đăng ký │ Tên khóa học             │ Mã học viên│ Trạng thái │ Ngày đăng ký    │");
        System.out.println("├───────┼────────────┼──────────────────────────┼────────────┼────────────┼─────────────────┤");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Enrollment enrollment : enrollments) {
            Course course = courseService.findById(enrollment.getCourseRefId());
            String courseName = course != null ? course.getName() : "N/A";
            Student student = enrollment.getStudent();
            String studentCode = student != null ? student.getStudentCode() : "N/A";
            System.out.printf("│ %-5d │ %-10s │ %-24s │ %-10s │ %-10s │ %-15s │%n",
                    enrollment.getId(),
                    truncateString(enrollment.getEnrollmentCode(), 10),
                    truncateString(courseName, 24),
                    studentCode,
                    enrollment.getStatus().name(),
                    enrollment.getRegistrationDate() != null ? sdf.format(enrollment.getRegistrationDate()) : "");
        }
        System.out.println("└───────┴────────────┴──────────────────────────┴────────────┴────────────┴─────────────────┘");
    }
}