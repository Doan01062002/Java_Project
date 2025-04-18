package ra.edu.presentation;

import ra.edu.business.model.Course;
import ra.edu.business.model.Enrollment;
import ra.edu.business.model.EnrollmentStatus;
import ra.edu.business.model.Student;
import ra.edu.business.service.course.CourseService;
import ra.edu.business.service.course.CourseServiceImp;
import ra.edu.business.service.enrollment.EnrollmentService;
import ra.edu.business.service.enrollment.EnrollmentServiceImp;
import ra.edu.business.service.student.StudentService;
import ra.edu.business.service.student.StudentServiceImp;
import ra.edu.validate.Validator;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class StudentUI {
    private final CourseService courseService = new CourseServiceImp();
    private final EnrollmentService enrollmentService = new EnrollmentServiceImp();
    private final StudentService studentService = new StudentServiceImp();
    private static final int PAGE_SIZE = 5; // Số mục trên mỗi trang

    public void displayMenuStudent(Scanner scanner, Student student) {
        // Cải thiện giao diện menu sinh viên
        do {
            System.out.println("\n═════════════════════ MENU SINH VIÊN ═════════════════════");
            System.out.println("│ Xin chào, " + truncateString(student.getFullName(), 30) + " │");
            System.out.println("├────────────────────────────────────────────────────────┤");
            System.out.println("│ 1. Xem danh sách khóa học (phân trang)                 │");
            System.out.println("│ 2. Đăng ký khóa học                                    │");
            System.out.println("│ 3. Xem danh sách khóa học đã đăng ký                   │");
            System.out.println("│ 4. Tìm kiếm khóa học theo tên (phân trang)             │");
            System.out.println("│ 5. Sắp xếp khóa học (phân trang)                       │");
            System.out.println("│ 6. Cập nhật thông tin cá nhân                          │");
            System.out.println("│ 7. Đăng xuất                                           │");
            System.out.println("═════════════════════════════════════════════════════════");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = Validator.validateInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    // Xem danh sách khóa học với phân trang
                    displayCoursesWithPagination(scanner);
                    break;
                case 2:
                    // Đăng ký khóa học
                    System.out.print("Nhập ID khóa học cần đăng ký: ");
                    int courseId = Validator.validateInt(scanner.nextLine());
                    if (courseId <= 0) break;
                    Course course = courseService.findById(courseId);
                    if (course == null) {
                        System.out.println("Khóa học không tồn tại.");
                        break;
                    }
                    System.out.println("Thông tin khóa học:");
                    displayCourseTable(List.of(course));
                    System.out.print("Xác nhận đăng ký (Y/N): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("Y")) {
                        Enrollment enrollment = new Enrollment();
                        enrollment.setEnrollmentCode("ENR" + System.currentTimeMillis());
                        enrollment.setStudentRefId(student.getId());
                        enrollment.setCourseRefId(courseId);
                        enrollment.setStatus(EnrollmentStatus.WAITING);
                        if (enrollmentService.save(enrollment)) {
                            System.out.println("Đăng ký khóa học thành công, đang chờ phê duyệt.");
                        } else {
                            System.out.println("Đăng ký khóa học thất bại.");
                        }
                    }
                    break;
                case 3:
                    // Xem danh sách khóa học đã đăng ký
                    List<Enrollment> enrollments = enrollmentService.findByStudentId(student.getId());
                    if (enrollments.isEmpty()) {
                        System.out.println("Bạn chưa đăng ký khóa học nào.");
                    } else {
                        System.out.println("\nDanh sách khóa học đã đăng ký:");
                        displayEnrolledCoursesTable(enrollments);
                    }
                    break;
                case 4:
                    // Tìm kiếm khóa học theo tên với phân trang
                    System.out.print("Nhập tên khóa học cần tìm: ");
                    String searchName = Validator.validateString(scanner.nextLine());
                    if (searchName.isEmpty()) break;
                    displayCoursesSearchWithPagination(scanner, searchName);
                    break;
                case 5:
                    // Sắp xếp khóa học với phân trang
                    System.out.println("Chọn tiêu chí sắp xếp:");
                    System.out.println("1. Theo tên (tăng dần)");
                    System.out.println("2. Theo tên (giảm dần)");
                    System.out.println("3. Theo ID (tăng dần)");
                    System.out.println("4. Theo ID (giảm dần)");
                    int sortChoice = Validator.validateInt(scanner.nextLine());
                    String sortBy;
                    boolean ascending;
                    switch (sortChoice) {
                        case 1:
                            sortBy = "name";
                            ascending = true;
                            break;
                        case 2:
                            sortBy = "name";
                            ascending = false;
                            break;
                        case 3:
                            sortBy = "id";
                            ascending = true;
                            break;
                        case 4:
                            sortBy = "id";
                            ascending = false;
                            break;
                        default:
                            System.out.println("Lựa chọn không hợp lệ.");
                            continue;
                    }
                    displayCoursesSortWithPagination(scanner, sortBy, ascending);
                    break;
                case 6:
                    // Cập nhật thông tin cá nhân
                    System.out.println("Thông tin hiện tại:");
                    displayStudentTable(List.of(student));
                    System.out.print("Nhập họ tên mới (nhấn Enter để giữ nguyên): ");
                    String newFullName = scanner.nextLine();
                    if (!newFullName.isEmpty()) {
                        student.setFullName(newFullName);
                    }
                    System.out.print("Nhập email mới (nhấn Enter để giữ nguyên): ");
                    String newEmail = scanner.nextLine();
                    if (!newEmail.isEmpty()) {
                        if (Validator.validateEmail(newEmail).isEmpty()) break;
                        student.setEmail(newEmail);
                    }
                    System.out.print("Nhập số điện thoại mới (nhấn Enter để giữ nguyên): ");
                    String newPhone = scanner.nextLine();
                    if (!newPhone.isEmpty()) {
                        if (Validator.validatePhone(newPhone).isEmpty()) break;
                        student.setPhone(newPhone);
                    }
                    System.out.print("Nhập mật khẩu mới (nhấn Enter để giữ nguyên): ");
                    String newPassword = scanner.nextLine();
                    if (!newPassword.isEmpty()) {
                        if (Validator.validatePassword(newPassword).isEmpty()) break;
                        student.setPassword(newPassword);
                    }
                    if (studentService.update(student)) {
                        System.out.println("Cập nhật thông tin cá nhân thành công.");
                    } else {
                        System.out.println("Cập nhật thông tin cá nhân thất bại.");
                    }
                    break;
                case 7:
                    System.out.print("Xác nhận đăng xuất (Y/N): ");
                    String logoutConfirm = scanner.nextLine();
                    if (logoutConfirm.equalsIgnoreCase("Y")) {
                        System.out.println("Đăng xuất thành công.");
                        return;
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng nhập lại.");
            }
        } while (true);
    }

    private void displayCoursesWithPagination(Scanner scanner) {
        // Hiển thị danh sách khóa học với phân trang
        int page = 1;
        while (true) {
            List<Course> courses = courseService.findAllWithPagination(page, PAGE_SIZE);
            if (courses.isEmpty()) {
                System.out.println("Chưa có khóa học nào hoặc không có dữ liệu ở trang này.");
                return;
            }
            System.out.println("\nDanh sách khóa học (Trang " + page + "):");
            displayCourseTable(courses);
            System.out.println("\nNhập số trang (0 để thoát): ");
            page = Validator.validateInt(scanner.nextLine());
            if (page <= 0) break;
        }
    }

    private void displayCoursesSearchWithPagination(Scanner scanner, String searchName) {
        // Hiển thị kết quả tìm kiếm khóa học với phân trang
        int page = 1;
        while (true) {
            List<Course> courses = courseService.findByNameWithPagination(searchName, page, PAGE_SIZE);
            if (courses.isEmpty()) {
                System.out.println("Không tìm thấy khóa học hoặc không có dữ liệu ở trang này.");
                return;
            }
            System.out.println("\nKết quả tìm kiếm khóa học (Trang " + page + "):");
            displayCourseTable(courses);
            System.out.println("\nNhập số trang (0 để thoát): ");
            page = Validator.validateInt(scanner.nextLine());
            if (page <= 0) break;
        }
    }

    private void displayCoursesSortWithPagination(Scanner scanner, String sortBy, boolean ascending) {
        // Hiển thị danh sách khóa học đã sắp xếp với phân trang
        int page = 1;
        while (true) {
            List<Course> courses = courseService.sortWithPagination(sortBy, ascending, page, PAGE_SIZE);
            if (courses.isEmpty()) {
                System.out.println("Chưa có khóa học nào hoặc không có dữ liệu ở trang này.");
                return;
            }
            System.out.println("\nDanh sách khóa học đã sắp xếp (Trang " + page + "):");
            displayCourseTable(courses);
            System.out.println("\nNhập số trang (0 để thoát): ");
            page = Validator.validateInt(scanner.nextLine());
            if (page <= 0) break;
        }
    }

    private void displayCourseTable(List<Course> courses) {
        // Hiển thị danh sách khóa học dưới dạng bảng ASCII
        System.out.println("\n┌───────┬────────────┬──────────────────────────┬────────────┬─────────────────┐");
        System.out.println("│ ID    │ Mã khóa    │ Tên khóa học             │ Thời lượng │ Giảng viên      │");
        System.out.println("├───────┼────────────┼──────────────────────────┼────────────┼─────────────────┤");
        for (Course course : courses) {
            System.out.printf("│ %-5d │ %-10s │ %-24s │ %-10d │ %-15s │%n",
                    course.getId(),
                    course.getCourseCode(),
                    truncateString(course.getName(), 24),
                    course.getDuration(),
                    truncateString(course.getInstructor(), 15));
        }
        System.out.println("└───────┴────────────┴──────────────────────────┴────────────┴─────────────────┘");
    }

    private void displayEnrolledCoursesTable(List<Enrollment> enrollments) {
        // Hiển thị danh sách khóa học đã đăng ký dưới dạng bảng ASCII
        System.out.println("\n┌───────┬────────────┬──────────────────────────┬────────────┬─────────────────┐");
        System.out.println("│ ID    │ Mã đăng ký │ Tên khóa học             │ Trạng thái │ Ngày đăng ký    │");
        System.out.println("├───────┼────────────┼──────────────────────────┼────────────┼─────────────────┤");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Enrollment enrollment : enrollments) {
            Course course = courseService.findById(enrollment.getCourseRefId());
            String courseName = course != null ? course.getName() : "N/A";
            System.out.printf("│ %-5d │ %-10s │ %-24s │ %-10s │ %-15s │%n",
                    enrollment.getId(),
                    enrollment.getEnrollmentCode(),
                    truncateString(courseName, 24),
                    enrollment.getStatus().name(),
                    enrollment.getRegistrationDate() != null ? sdf.format(enrollment.getRegistrationDate()) : "");
        }
        System.out.println("└───────┴────────────┴──────────────────────────┴────────────┴─────────────────┘");
    }

    private void displayStudentTable(List<Student> students) {
        // Hiển thị thông tin sinh viên dưới dạng bảng ASCII
        System.out.println("\n┌───────┬────────────┬──────────────────────────┬──────────────────────────┬────────────┐");
        System.out.println("│ ID    │ Mã học viên│ Họ tên                   │ Email                    │ SĐT        │");
        System.out.println("├───────┼────────────┼──────────────────────────┼──────────────────────────┼────────────┤");
        for (Student student : students) {
            System.out.printf("│ %-5d │ %-10s │ %-24s │ %-24s │ %-10s │%n",
                    student.getId(),
                    student.getStudentCode(),
                    truncateString(student.getFullName(), 24),
                    truncateString(student.getEmail(), 24),
                    student.getPhone() != null ? student.getPhone() : "");
        }
        System.out.println("└───────┴────────────┴──────────────────────────┴──────────────────────────┴────────────┘");
    }

    private String truncateString(String str, int maxLength) {
        // Cắt chuỗi nếu vượt quá độ dài tối đa
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }
}