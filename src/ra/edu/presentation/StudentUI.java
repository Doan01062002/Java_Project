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
import ra.edu.utils.UIUtils;
import ra.edu.validate.Validator;

import java.util.List;
import java.util.Scanner;

public class StudentUI {
    private final CourseService courseService = new CourseServiceImp();
    private final EnrollmentService enrollmentService = new EnrollmentServiceImp();
    private final StudentService studentService = new StudentServiceImp();
    private static final int PAGE_SIZE = 5;

    public void displayMenuStudent(Scanner scanner, Student student) {
        while (true) {
            UIUtils.printHeader("MENU SINH VIÊN");
            System.out.println("│ Xin chào, " + UIUtils.truncateString(student.getFullName(), 35) + " │");
            System.out.println("├────────────────────────────────────────────────────────┤");
            System.out.println("│ 1. Xem danh sách khóa học (phân trang)                 │");
            System.out.println("│ 2. Đăng ký khóa học                                    │");
            System.out.println("│ 3. Hủy đăng ký khóa học                                │");
            System.out.println("│ 4. Xem danh sách khóa học đã đăng ký                   │");
            System.out.println("│ 5. Tìm kiếm khóa học theo tên (phân trang)             │");
            System.out.println("│ 6. Sắp xếp khóa học (phân trang)                       │");
            System.out.println("│ 7. Cập nhật thông tin cá nhân                          │");
            System.out.println("│ 8. Đăng xuất                                           │");
            UIUtils.printFooter();
            int choice = Validator.validateInt(scanner, "Nhập lựa chọn (1-8): ");
            switch (choice) {
                case 1:
                    displayCoursesWithPagination(scanner);
                    break;
                case 2:
                    registerCourse(scanner, student);
                    break;
                case 3:
                    cancelRegistration(scanner, student);
                    break;
                case 4:
                    List<Enrollment> enrollments = enrollmentService.findByStudentId(student.getId());
                    if (enrollments.isEmpty()) {
                        UIUtils.showError("Bạn chưa đăng ký khóa học nào.");
                    } else {
                        System.out.println("\nDanh sách khóa học đã đăng ký:");
                        UIUtils.displayEnrollmentTable(enrollments, courseService);
                    }
                    System.out.println("Nhấn Enter để tiếp tục...");
                    scanner.nextLine();
                    break;
                case 5:
                    String searchName = Validator.validateString(scanner, "Nhập tên khóa học cần tìm: ");
                    displayCoursesSearchWithPagination(scanner, searchName);
                    break;
                case 6:
                    sortCourses(scanner);
                    break;
                case 7:
                    updatePersonalInfo(scanner, student);
                    break;
                case 8:
                    if (confirmAction(scanner, "đăng xuất")) {
                        UIUtils.showSuccess("Đăng xuất thành công.");
                        return;
                    }
                    break;
                default:
                    UIUtils.showError("Lựa chọn không hợp lệ, vui lòng nhập lại.");
            }
        }
    }

    private void registerCourse(Scanner scanner, Student student) {
        UIUtils.printHeader("ĐĂNG KÝ KHÓA HỌC");
        int courseId = Validator.validateInt(scanner, "Nhập ID khóa học: ");
        Course course = courseService.findById(courseId);
        if (course == null) {
            UIUtils.showError("Khóa học không tồn tại.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        if (enrollmentService.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            UIUtils.showError("Bạn đã đăng ký hoặc gửi đơn đăng ký khóa học này rồi.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("Thông tin khóa học:");
        UIUtils.displayCourseTable(List.of(course));

        if (confirmAction(scanner, "đăng ký khóa học")) {
            Enrollment enrollment = new Enrollment();
            enrollment.setEnrollmentCode("ENR" + System.currentTimeMillis());
            enrollment.setStudentRefId(student.getId());
            enrollment.setCourseRefId(courseId);
            enrollment.setStatus(EnrollmentStatus.WAITING);
            if (enrollmentService.save(enrollment)) {
                UIUtils.showSuccess("Đăng ký khóa học thành công, đang chờ phê duyệt.");
            } else {
                UIUtils.showError("Đăng ký khóa học thất bại.");
            }
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void cancelRegistration(Scanner scanner, Student student) {
        UIUtils.printHeader("HỦY ĐĂNG KÝ KHÓA HỌC");
        int id = Validator.validateInt(scanner, "Nhập ID đăng ký: ");
        Enrollment enrollment = enrollmentService.findById(id);
        if (enrollment == null) {
            UIUtils.showError("Đăng ký không tồn tại.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }
        if (enrollment.getStudentRefId() != student.getId()) {
            UIUtils.showError("Bạn không có quyền hủy đăng ký này.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }
        if (enrollment.getStatus() != EnrollmentStatus.WAITING) {
            UIUtils.showError("Không thể hủy: Đăng ký đang ở trạng thái " + enrollment.getStatus());
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("Thông tin đăng ký:");
        UIUtils.displayEnrollmentTable(List.of(enrollment), courseService);

        if (confirmAction(scanner, "hủy đăng ký")) {
            enrollment.setStatus(EnrollmentStatus.CANCEL);
            if (enrollmentService.update(enrollment)) {
                UIUtils.showSuccess("Hủy đăng ký thành công.");
            } else {
                UIUtils.showError("Hủy đăng ký thất bại.");
            }
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void updatePersonalInfo(Scanner scanner, Student student) {
        UIUtils.printHeader("CẬP NHẬT THÔNG TIN CÁ NHÂN");
        System.out.println("Thông tin hiện tại:");
        UIUtils.displayStudentTable(List.of(student));

        String newFullName = Validator.validateString(scanner, "Họ tên mới (Enter để giữ nguyên): ");
        if (!newFullName.isEmpty()) student.setFullName(newFullName);

        String newEmail = Validator.validateEmail(scanner, "Email mới (Enter để giữ nguyên): ");
        if (!newEmail.isEmpty()) student.setEmail(newEmail);

        String newPhone = Validator.validatePhone(scanner, "Số điện thoại mới (Enter để giữ nguyên): ");
        if (!newPhone.isEmpty()) student.setPhone(newPhone);

        String newPassword = Validator.validatePassword(scanner, "Mật khẩu mới (Enter để giữ nguyên): ");
        if (!newPassword.isEmpty()) student.setPassword(newPassword);

        if (studentService.update(student)) {
            UIUtils.showSuccess("Cập nhật thông tin cá nhân thành công.");
        } else {
            UIUtils.showError("Cập nhật thông tin cá nhân thất bại.");
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void displayCoursesWithPagination(Scanner scanner) {
        UIUtils.printHeader("DANH SÁCH KHÓA HỌC");
        int page = 1;
        while (true) {
            List<Course> courses = courseService.findAllWithPagination(page, PAGE_SIZE);
            if (courses.isEmpty()) {
                UIUtils.showError("Chưa có khóa học nào hoặc không có dữ liệu ở trang này.");
                break;
            }
            System.out.println("\nDanh sách khóa học (Trang " + page + "):");
            UIUtils.displayCourseTable(courses);
            page = Validator.validateInt(scanner, "Nhập số trang (0 để thoát): ");
            if (page <= 0) break;
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void displayCoursesSearchWithPagination(Scanner scanner, String searchName) {
        UIUtils.printHeader("TÌM KIẾM KHÓA HỌC");
        int page = 1;
        while (true) {
            List<Course> courses = courseService.findByNameWithPagination(searchName, page, PAGE_SIZE);
            if (courses.isEmpty()) {
                UIUtils.showError("Không tìm thấy khóa học hoặc không có dữ liệu ở trang này.");
                break;
            }
            System.out.println("\nKết quả tìm kiếm khóa học (Trang " + page + "):");
            UIUtils.displayCourseTable(courses);
            page = Validator.validateInt(scanner, "Nhập số trang (0 để thoát): ");
            if (page <= 0) break;
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void sortCourses(Scanner scanner) {
        UIUtils.printHeader("SẮP XẾP KHÓA HỌC");
        System.out.println("│ 1. Theo tên (tăng dần)                        │");
        System.out.println("│ 2. Theo tên (giảm dần)                        │");
        System.out.println("│ 3. Theo ID (tăng dần)                         │");
        System.out.println("│ 4. Theo ID (giảm dần)                         │");
        UIUtils.printFooter();
        int sortChoice = Validator.validateInt(scanner, "Nhập lựa chọn (1-4): ");
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
                UIUtils.showError("Lựa chọn không hợp lệ.");
                System.out.println("Nhấn Enter để tiếp tục...");
                scanner.nextLine();
                return;
        }

        int page = 1;
        while (true) {
            List<Course> courses = courseService.sortWithPagination(sortBy, ascending, page, PAGE_SIZE);
            if (courses.isEmpty()) {
                UIUtils.showError("Chưa có khóa học nào hoặc không có dữ liệu ở trang này.");
                break;
            }
            System.out.println("\nDanh sách khóa học đã sắp xếp (Trang " + page + "):");
            UIUtils.displayCourseTable(courses);
            page = Validator.validateInt(scanner, "Nhập số trang (0 để thoát): ");
            if (page <= 0) break;
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private boolean confirmAction(Scanner scanner, String action) {
        while (true) {
            System.out.print("Xác nhận " + action + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) return true;
            if (input.equals("N")) return false;
            UIUtils.showError("Vui lòng nhập Y hoặc N.");
        }
    }
}