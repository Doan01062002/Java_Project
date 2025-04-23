package ra.edu.presentation;

import ra.edu.business.model.Course;
import ra.edu.business.model.Enrollment;
import ra.edu.business.model.EnrollmentStatus;
import ra.edu.business.model.Student;
import ra.edu.business.service.admin.AdminService;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.business.service.course.CourseService;
import ra.edu.business.service.course.CourseServiceImp;
import ra.edu.business.service.enrollment.EnrollmentService;
import ra.edu.business.service.enrollment.EnrollmentServiceImp;
import ra.edu.business.service.statistic.StatisticService;
import ra.edu.business.service.statistic.StatisticServiceImp;
import ra.edu.business.service.student.StudentService;
import ra.edu.business.service.student.StudentServiceImp;
import ra.edu.utils.UIUtils;
import ra.edu.validate.Validator;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class AdminUI {
    private final CourseService courseService = new CourseServiceImp();
    private final StudentService studentService = new StudentServiceImp();
    private final EnrollmentService enrollmentService = new EnrollmentServiceImp();
    private final StatisticService statisticService = new StatisticServiceImp();
    private final AdminService adminService = new AdminServiceImp();
    private static final int PAGE_SIZE = 5;

    public void displayMenuAdmin(Scanner scanner) {
        while (true) {
            UIUtils.printHeader("MENU ADMIN");
            System.out.println("│ 1. Quản lý khóa học                           │");
            System.out.println("│ 2. Quản lý học viên                           │");
            System.out.println("│ 3. Quản lý đăng ký học                        │");
            System.out.println("│ 4. Thống kê học viên theo khóa học            │");
            System.out.println("│ 5. Đăng xuất                                  │");
            UIUtils.printFooter();
            int choice = Validator.validateInt(scanner, "Nhập lựa chọn (1-5): ");
            switch (choice) {
                case 1:
                    manageCourses(scanner);
                    break;
                case 2:
                    manageStudents(scanner);
                    break;
                case 3:
                    manageRegistrations(scanner);
                    break;
                case 4:
                    showStatistics(scanner);
                    break;
                case 5:
                    if (confirmAction(scanner, "đăng xuất")) {
                        UIUtils.showSuccess("Đăng xuất thành công.");
                        return;
                    }
                    break;
                default:
                    UIUtils.showError("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private void manageCourses(Scanner scanner) {
        while (true) {
            UIUtils.printHeader("QUẢN LÝ KHÓA HỌC");
            System.out.println("│ 1. Hiển thị danh sách khóa học (phân trang)   │");
            System.out.println("│ 2. Thêm mới khóa học                          │");
            System.out.println("│ 3. Chỉnh sửa khóa học                         │");
            System.out.println("│ 4. Xóa khóa học                               │");
            System.out.println("│ 5. Tìm kiếm khóa học theo tên (phân trang)    │");
            System.out.println("│ 6. Sắp xếp khóa học (phân trang)              │");
            System.out.println("│ 7. Quay lại                                   │");
            UIUtils.printFooter();
            int choice = Validator.validateInt(scanner, "Nhập lựa chọn (1-7): ");
            switch (choice) {
                case 1:
                    displayCoursesWithPagination(scanner);
                    break;
                case 2:
                    addNewCourse(scanner);
                    break;
                case 3:
                    editCourse(scanner);
                    break;
                case 4:
                    deleteCourse(scanner);
                    break;
                case 5:
                    searchCourses(scanner);
                    break;
                case 6:
                    sortCourses(scanner);
                    break;
                case 7:
                    return;
                default:
                    UIUtils.showError("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private void addNewCourse(Scanner scanner) {
        UIUtils.printHeader("THÊM KHÓA HỌC MỚI");
        String courseCode;
        do {
            courseCode = Validator.validateCourseId(scanner, "Mã khóa học (VD: KSB001): ");
            if (courseService.existsByCourseCode(courseCode)) {
                UIUtils.showError("Mã khóa học đã tồn tại, vui lòng nhập mã khác.");
                courseCode = null;
            }
        } while (courseCode == null);

        String name;
        do {
            name = Validator.validateString(scanner, "Tên khóa học: ");
            if (courseService.existsByName(name)) {
                UIUtils.showError("Tên khóa học đã tồn tại, vui lòng nhập tên khác.");
                name = null;
            }
        } while (name == null);

        String description = Validator.validateString(scanner, "Mô tả khóa học: ");
        int duration = Validator.validateInt(scanner, "Thời lượng (số giờ): ");
        if (duration <= 0) {
            UIUtils.showError("Thời lượng không hợp lệ.");
            return;
        }

        String instructor = Validator.validateString(scanner, "Tên giảng viên: ");
        String startDate = Validator.validateDate(scanner, "Ngày bắt đầu (dd/MM/yyyy, Enter để bỏ qua): ");
        Integer createdByAdminId = null;
        int adminIdInput = Validator.validateInt(scanner, "ID admin tạo khóa học (Enter để bỏ qua): ");
        if (adminIdInput > 0) {
            createdByAdminId = adminIdInput;
        }

        Course course = new Course(courseCode, name, description, duration, instructor, startDate);
        course.setCreatedByAdminId(createdByAdminId);

        if (courseService.save(course)) {
            UIUtils.showSuccess("Thêm khóa học thành công.");
        } else {
            UIUtils.showError("Thêm khóa học thất bại.");
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void editCourse(Scanner scanner) {
        UIUtils.printHeader("CHỈNH SỬA KHÓA HỌC");
        int id = Validator.validateInt(scanner, "Nhập ID khóa học cần sửa: ");
        Course course = courseService.findById(id);
        if (course == null) {
            UIUtils.showError("Khóa học không tồn tại.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("Thông tin hiện tại:");
        UIUtils.displayCourseTable(List.of(course));

        String newName = Validator.validateString(scanner, "Tên mới (Enter để giữ nguyên): ");
        if (!newName.isEmpty()) course.setName(newName);

        String newDescription = Validator.validateString(scanner, "Mô tả mới (Enter để giữ nguyên): ");
        if (!newDescription.isEmpty()) course.setDescription(newDescription);

        int newDuration = Validator.validateInt(scanner, "Thời lượng mới (Enter để giữ nguyên): ");
        if (newDuration > 0) course.setDuration(newDuration);

        String newInstructor = Validator.validateString(scanner, "Tên giảng viên mới (Enter để giữ nguyên): ");
        if (!newInstructor.isEmpty()) course.setInstructor(newInstructor);

        String newStartDate = Validator.validateDate(scanner, "Ngày bắt đầu mới (dd/MM/yyyy, Enter để giữ nguyên): ");
        if (!newStartDate.isEmpty()) course.setCreatedAt(newStartDate);

        if (courseService.update(course)) {
            UIUtils.showSuccess("Cập nhật khóa học thành công.");
        } else {
            UIUtils.showError("Cập nhật khóa học thất bại.");
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void deleteCourse(Scanner scanner) {
        UIUtils.printHeader("XÓA KHÓA HỌC");
        int id = Validator.validateInt(scanner, "Nhập ID khóa học cần xóa: ");
        Course course = courseService.findById(id);
        if (course == null) {
            UIUtils.showError("Khóa học không tồn tại.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        if (confirmAction(scanner, "xóa khóa học")) {
            if (courseService.delete(course)) {
                UIUtils.showSuccess("Xóa khóa học thành công.");
            } else {
                UIUtils.showError("Xóa khóa học thất bại.");
            }
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void searchCourses(Scanner scanner) {
        UIUtils.printHeader("TÌM KIẾM KHÓA HỌC");
        String searchName = Validator.validateString(scanner, "Nhập tên khóa học cần tìm: ");
        int page = 1;
        while (true) {
            List<Course> courses = courseService.findByNameWithPagination(searchName, page, PAGE_SIZE);
            if (courses.isEmpty()) {
                UIUtils.showError("Không tìm thấy khóa học hoặc không có dữ liệu ở trang này.");
                break;
            }
            System.out.println("\nKết quả tìm kiếm (Trang " + page + "):");
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
                UIUtils.showError("Không có khóa học hoặc không có dữ liệu ở trang này.");
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

    private void displayCoursesWithPagination(Scanner scanner) {
        UIUtils.printHeader("DANH SÁCH KHÓA HỌC");
        int page = 1;
        while (true) {
            List<Course> courses = courseService.findAllWithPagination(page, PAGE_SIZE);
            if (courses.isEmpty()) {
                UIUtils.showError("Không có khóa học hoặc không có dữ liệu ở trang này.");
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

    private void manageStudents(Scanner scanner) {
        while (true) {
            UIUtils.printHeader("QUẢN LÝ HỌC VIÊN");
            System.out.println("│ 1. Hiển thị danh sách học viên (phân trang)   │");
            System.out.println("│ 2. Thêm mới học viên                          │");
            System.out.println("│ 3. Chỉnh sửa học viên                         │");
            System.out.println("│ 4. Xóa học viên                               │");
            System.out.println("│ 5. Tìm kiếm học viên (phân trang)             │");
            System.out.println("│ 6. Sắp xếp học viên (phân trang)              │");
            System.out.println("│ 7. Khóa/Mở tài khoản học viên                 │");
            System.out.println("│ 8. Quay lại                                   │");
            UIUtils.printFooter();
            int choice = Validator.validateInt(scanner, "Nhập lựa chọn (1-8): ");
            switch (choice) {
                case 1:
                    displayStudentsWithPagination(scanner);
                    break;
                case 2:
                    addNewStudent(scanner);
                    break;
                case 3:
                    editStudent(scanner);
                    break;
                case 4:
                    deleteStudent(scanner);
                    break;
                case 5:
                    searchStudents(scanner);
                    break;
                case 6:
                    sortStudents(scanner);
                    break;
                case 7:
                    lockUnlockStudent(scanner);
                    break;
                case 8:
                    return;
                default:
                    UIUtils.showError("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private void addNewStudent(Scanner scanner) {
        UIUtils.printHeader("THÊM HỌC VIÊN MỚI");
        String studentCode = Validator.validateStudentId(scanner, "Mã học viên (VD: SV001): ");
        String username = Validator.validateUsername(scanner, "Tên đăng nhập: ");
        String password = Validator.validatePassword(scanner, "Mật khẩu: ");
        String fullName = Validator.validateString(scanner, "Họ tên: ");
        String email = Validator.validateEmail(scanner, "Email: ");
        String phone = Validator.validatePhone(scanner, "Số điện thoại (Enter để bỏ qua): ");
        System.out.print("Giới tính (1: Nam, 0: Nữ, Enter để bỏ qua): ");
        String sexInput = scanner.nextLine().trim();
        Boolean sex = sexInput.equals("1") ? true : sexInput.equals("0") ? false : null;
        String dobInput = Validator.validateDate(scanner, "Ngày sinh (dd/MM/yyyy, Enter để bỏ qua): ");
        java.util.Date dob = null;
        if (!dobInput.isEmpty()) {
            try {
                dob = new SimpleDateFormat("dd/MM/yyyy").parse(dobInput);
            } catch (Exception e) {
                UIUtils.showError("Ngày sinh không hợp lệ.");
                return;
            }
        }

        Student student = new Student();
        student.setStudentCode(studentCode);
        student.setUsername(username);
        student.setPassword(password);
        student.setFullName(fullName);
        student.setEmail(email);
        student.setPhone(phone.isEmpty() ? null : phone);
        student.setSex(sex);
        student.setDob(dob);

        if (studentService.save(student)) {
            UIUtils.showSuccess("Thêm học viên thành công.");
        } else {
            UIUtils.showError("Thêm học viên thất bại.");
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void editStudent(Scanner scanner) {
        UIUtils.printHeader("CHỈNH SỬA HỌC VIÊN");
        int id = Validator.validateInt(scanner, "Nhập ID học viên cần sửa: ");
        Student student = studentService.findById(id);
        if (student == null) {
            UIUtils.showError("Học viên không tồn tại.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("Thông tin hiện tại:");
        UIUtils.displayStudentTable(List.of(student));

        String newFullName = Validator.validateString(scanner, "Họ tên mới (Enter để giữ nguyên): ");
        if (!newFullName.isEmpty()) student.setFullName(newFullName);

        String newEmail = Validator.validateEmail(scanner, "Email mới (Enter để giữ nguyên): ");
        if (!newEmail.isEmpty()) student.setEmail(newEmail);

        String newPhone = Validator.validatePhone(scanner, "Số điện thoại mới (Enter để giữ nguyên): ");
        if (!newPhone.isEmpty()) student.setPhone(newPhone);

        System.out.print("Giới tính mới (1: Nam, 0: Nữ, Enter để giữ nguyên): ");
        String sexInput = scanner.nextLine().trim();
        if (!sexInput.isEmpty()) {
            Boolean newSex = sexInput.equals("1") ? true : sexInput.equals("0") ? false : null;
            student.setSex(newSex);
        }

        String dobInput = Validator.validateDate(scanner, "Ngày sinh mới (dd/MM/yyyy, Enter để giữ nguyên): ");
        if (!dobInput.isEmpty()) {
            try {
                java.util.Date newDob = new SimpleDateFormat("dd/MM/yyyy").parse(dobInput);
                student.setDob(newDob);
            } catch (Exception e) {
                UIUtils.showError("Ngày sinh không hợp lệ.");
                return;
            }
        }

        if (studentService.update(student)) {
            UIUtils.showSuccess("Cập nhật học viên thành công.");
        } else {
            UIUtils.showError("Cập nhật học viên thất bại.");
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void deleteStudent(Scanner scanner) {
        UIUtils.printHeader("XÓA HỌC VIÊN");
        int id = Validator.validateInt(scanner, "Nhập ID học viên cần xóa: ");
        Student student = studentService.findById(id);
        if (student == null) {
            UIUtils.showError("Học viên không tồn tại.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        if (confirmAction(scanner, "xóa học viên")) {
            if (studentService.delete(student)) {
                UIUtils.showSuccess("Xóa học viên thành công.");
            } else {
                UIUtils.showError("Xóa học viên thất bại.");
            }
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void searchStudents(Scanner scanner) {
        UIUtils.printHeader("TÌM KIẾM HỌC VIÊN");
        String searchInput = Validator.validateString(scanner, "Nhập tên, email hoặc mã học viên: ");
        int page = 1;
        while (true) {
            List<Student> students = studentService.findByNameOrEmailOrCodeWithPagination(searchInput, page, PAGE_SIZE);
            if (students.isEmpty()) {
                UIUtils.showError("Không tìm thấy học viên hoặc không có dữ liệu ở trang này.");
                break;
            }
            System.out.println("\nKết quả tìm kiếm (Trang " + page + "):");
            UIUtils.displayStudentTable(students);
            page = Validator.validateInt(scanner, "Nhập số trang (0 để thoát): ");
            if (page <= 0) break;
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void sortStudents(Scanner scanner) {
        UIUtils.printHeader("SẮP XẾP HỌC VIÊN");
        System.out.println("│ 1. Theo tên (tăng dần)                        │");
        System.out.println("│ 2. Theo tên (giảm dần)                        │");
        System.out.println("│ 3. Theo mã (tăng dần)                         │");
        System.out.println("│ 4. Theo mã (giảm dần)                         │");
        UIUtils.printFooter();
        int sortChoice = Validator.validateInt(scanner, "Nhập lựa chọn (1-4): ");
        String sortBy;
        boolean ascending;
        switch (sortChoice) {
            case 1:
                sortBy = "full_name";
                ascending = true;
                break;
            case 2:
                sortBy = "full_name";
                ascending = false;
                break;
            case 3:
                sortBy = "student_code";
                ascending = true;
                break;
            case 4:
                sortBy = "student_code";
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
            List<Student> students = studentService.sortWithPagination(sortBy, ascending, page, PAGE_SIZE);
            if (students.isEmpty()) {
                UIUtils.showError("Không có học viên hoặc không có dữ liệu ở trang này.");
                break;
            }
            System.out.println("\nDanh sách học viên đã sắp xếp (Trang " + page + "):");
            UIUtils.displayStudentTable(students);
            page = Validator.validateInt(scanner, "Nhập số trang (0 để thoát): ");
            if (page <= 0) break;
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void lockUnlockStudent(Scanner scanner) {
        UIUtils.printHeader("KHÓA/MỞ TÀI KHOẢN HỌC VIÊN");
        int id = Validator.validateInt(scanner, "Nhập ID học viên: ");
        Student student = studentService.findById(id);
        if (student == null) {
            UIUtils.showError("Học viên không tồn tại.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("Thông tin hiện tại:");
        UIUtils.displayStudentTable(List.of(student));

        System.out.print("Khóa tài khoản? (Y/N): ");
        String lockInput = scanner.nextLine().trim();
        boolean isActive = !lockInput.equalsIgnoreCase("Y");
        student.setActive(isActive);

        if (adminService.lockStudent(id, isActive)) {
            UIUtils.showSuccess((isActive ? "Mở" : "Khóa") + " tài khoản thành công.");
        } else {
            UIUtils.showError((isActive ? "Mở" : "Khóa") + " tài khoản thất bại.");
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void displayStudentsWithPagination(Scanner scanner) {
        UIUtils.printHeader("DANH SÁCH HỌC VIÊN");
        int page = 1;
        while (true) {
            List<Student> students = studentService.findAllWithPagination(page, PAGE_SIZE);
            if (students.isEmpty()) {
                UIUtils.showError("Không có học viên hoặc không có dữ liệu ở trang này.");
                break;
            }
            System.out.println("\nDanh sách học viên (Trang " + page + "):");
            UIUtils.displayStudentTable(students);
            page = Validator.validateInt(scanner, "Nhập số trang (0 để thoát): ");
            if (page <= 0) break;
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void manageRegistrations(Scanner scanner) {
        while (true) {
            UIUtils.printHeader("QUẢN LÝ ĐĂNG KÝ HỌC");
            System.out.println("│ 1. Hiển thị danh sách đăng ký (phân trang)    │");
            System.out.println("│ 2. Phê duyệt/Hủy đăng ký                      │");
            System.out.println("│ 3. Xóa đơn đăng ký bị hủy                     │");
            System.out.println("│ 4. Quay lại                                   │");
            UIUtils.printFooter();
            int choice = Validator.validateInt(scanner, "Nhập lựa chọn (1-4): ");
            switch (choice) {
                case 1:
                    displayRegistrations(scanner);
                    break;
                case 2:
                    manageEnrollmentStatus(scanner);
                    break;
                case 3:
                    deleteCancelledEnrollments(scanner);
                    break;
                case 4:
                    return;
                default:
                    UIUtils.showError("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private void deleteCancelledEnrollments(Scanner scanner) {
        UIUtils.printHeader("XÓA ĐƠN ĐĂNG KÝ BỊ HỦY");
        List<Enrollment> cancelledEnrollments = enrollmentService.findAll()
                .stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.CANCEL)
                .toList();

        if (cancelledEnrollments.isEmpty()) {
            UIUtils.showError("Không có đơn đăng ký nào ở trạng thái bị hủy.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("Danh sách đơn đăng ký bị hủy:");
        UIUtils.displayEnrollmentTable(cancelledEnrollments, courseService);

        int id = Validator.validateInt(scanner, "Nhập ID đơn đăng ký cần xóa: ");
        Enrollment enrollment = enrollmentService.findById(id);
        if (enrollment == null) {
            UIUtils.showError("Đơn đăng ký không tồn tại.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }
        if (enrollment.getStatus() != EnrollmentStatus.CANCEL) {
            UIUtils.showError("Chỉ có thể xóa đơn đăng ký ở trạng thái bị hủy.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        if (confirmAction(scanner, "xóa đơn đăng ký")) {
            if (enrollmentService.deleteById(id)) {
                UIUtils.showSuccess("Xóa đơn đăng ký thành công.");
            } else {
                UIUtils.showError("Xóa đơn đăng ký thất bại.");
            }
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void displayRegistrations(Scanner scanner) {
        UIUtils.printHeader("DANH SÁCH ĐĂNG KÝ");
        int courseId = Validator.validateInt(scanner, "Nhập ID khóa học: ");
        Course course = courseService.findById(courseId);
        if (course == null) {
            UIUtils.showError("Khóa học không tồn tại.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        int page = 1;
        while (true) {
            List<Enrollment> enrollments = enrollmentService.findByCourseIdWithPagination(courseId, page, PAGE_SIZE);
            if (enrollments.isEmpty()) {
                UIUtils.showError("Không có đăng ký nào hoặc không có dữ liệu ở trang này.");
                break;
            }
            System.out.println("\nDanh sách đăng ký khóa học ID " + courseId + " (Trang " + page + "):");
            UIUtils.displayEnrollmentTable(enrollments, courseService);
            page = Validator.validateInt(scanner, "Nhập số trang (0 để thoát): ");
            if (page <= 0) break;
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void manageEnrollmentStatus(Scanner scanner) {
        UIUtils.printHeader("PHÊ DUYỆT/HỦY ĐĂNG KÝ");
        int id = Validator.validateInt(scanner, "Nhập ID đăng ký: ");
        Enrollment enrollment = enrollmentService.findById(id);
        if (enrollment == null) {
            UIUtils.showError("Đăng ký không tồn tại.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("Thông tin đăng ký hiện tại:");
        UIUtils.displayEnrollmentTable(List.of(enrollment), courseService);

        if (enrollment.getStatus() == EnrollmentStatus.CONFIRM) {
            UIUtils.showError("Đăng ký đã được phê duyệt, không thể thay đổi.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }
        if (enrollment.getStatus() == EnrollmentStatus.CANCEL) {
            UIUtils.showError("Đăng ký đã bị hủy, không thể thay đổi.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        System.out.println("│ 1. Phê duyệt                                  │");
        System.out.println("│ 2. Hủy                                        │");
        int action = Validator.validateInt(scanner, "Nhập lựa chọn (1-2): ");
        if (action == 1) {
            enrollment.setStatus(EnrollmentStatus.CONFIRM);
        } else if (action == 2) {
            enrollment.setStatus(EnrollmentStatus.CANCEL);
        } else {
            UIUtils.showError("Lựa chọn không hợp lệ.");
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
            return;
        }

        int adminId = Validator.validateInt(scanner, "Nhập ID admin xử lý (Enter để bỏ qua): ");
        if (adminId > 0) {
            enrollment.setAdminRefId(adminId);
        }

        if (enrollmentService.update(enrollment)) {
            UIUtils.showSuccess("Xử lý đăng ký thành công.");
        } else {
            UIUtils.showError("Xử lý đăng ký thất bại.");
        }
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void showStatistics(Scanner scanner) {
        while (true) {
            UIUtils.printHeader("THỐNG KÊ");
            System.out.println("│ 1. Tổng số khóa học và học viên               │");
            System.out.println("│ 2. Số học viên theo khóa học                  │");
            System.out.println("│ 3. Top 5 khóa học đông học viên               │");
            System.out.println("│ 4. Khóa học có trên 10 học viên               │");
            System.out.println("│ 5. Quay lại                                   │");
            UIUtils.printFooter();
            int choice = Validator.validateInt(scanner, "Nhập lựa chọn (1-5): ");
            switch (choice) {
                case 1:
                    int totalCourses = statisticService.getTotalCourses();
                    int totalStudents = statisticService.getTotalStudents();
                    System.out.println("Tổng số khóa học: " + totalCourses);
                    System.out.println("Tổng số học viên: " + totalStudents);
                    break;
                case 2:
                    int courseId = Validator.validateInt(scanner, "Nhập ID khóa học: ");
                    Course course = courseService.findById(courseId);
                    if (course == null) {
                        UIUtils.showError("Khóa học không tồn tại.");
                        break;
                    }
                    int studentCount = statisticService.countStudentsByCourse(courseId);
                    System.out.println("Khóa học " + course.getName() + " có " + studentCount + " học viên.");
                    break;
                case 3:
                    List<Course> topCourses = statisticService.getTop5CoursesByStudents();
                    if (topCourses.isEmpty()) {
                        UIUtils.showError("Không có khóa học nào.");
                        break;
                    }
                    System.out.println("\nTop 5 khóa học đông học viên nhất:");
                    System.out.println("┌───────┬──────────────────────────┬────────────┐");
                    System.out.println("│ ID    │ Tên khóa học             │ Số học viên│");
                    System.out.println("├───────┼──────────────────────────┼────────────┤");
                    for (Course topCourse : topCourses) {
                        int studentCountTop = statisticService.countStudentsByCourse(topCourse.getId());
                        System.out.printf("│ %-5d │ %-24s │ %-10d │%n",
                                topCourse.getId(),
                                UIUtils.truncateString(topCourse.getName(), 24),
                                studentCountTop);
                    }
                    System.out.println("└───────┴──────────────────────────┴────────────┘");
                    break;
                case 4:
                    List<Course> coursesWithMoreThan10Students = statisticService.getCoursesWithMoreThan10Students();
                    if (coursesWithMoreThan10Students.isEmpty()) {
                        UIUtils.showError("Không có khóa học nào có hơn 10 học viên.");
                        break;
                    }
                    System.out.println("\nKhóa học có hơn 10 học viên:");
                    System.out.println("┌───────┬──────────────────────────┬────────────┐");
                    System.out.println("│ ID    │ Tên khóa học             │ Số học viên│");
                    System.out.println("├───────┼──────────────────────────┼────────────┤");
                    for (Course courseWithMoreThan10 : coursesWithMoreThan10Students) {
                        int studentCountMoreThan10 = statisticService.countStudentsByCourse(courseWithMoreThan10.getId());
                        System.out.printf("│ %-5d │ %-24s │ %-10d │%n",
                                courseWithMoreThan10.getId(),
                                UIUtils.truncateString(courseWithMoreThan10.getName(), 24),
                                studentCountMoreThan10);
                    }
                    System.out.println("└───────┴──────────────────────────┴────────────┘");
                    break;
                case 5:
                    return;
                default:
                    UIUtils.showError("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
            System.out.println("Nhấn Enter để tiếp tục...");
            scanner.nextLine();
        }
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