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
import ra.edu.validate.CourseValidator;
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
    private static final int PAGE_SIZE = 5; // Số mục trên mỗi trang

    public void displayMenuAdmin(Scanner scanner) {
        // Hiển thị menu chính của admin
        while (true) {
            printHeader("MENU ADMIN");
            System.out.println("│ 1. Quản lý khóa học                               │");
            System.out.println("│ 2. Quản lý học viên                               │");
            System.out.println("│ 3. Quản lý đăng ký học                            │");
            System.out.println("│ 4. Thống kê học viên theo khóa học                │");
            System.out.println("│ 5. Đăng xuất                                      │");
            printFooter();
            System.out.print("Nhập lựa chọn: ");
            int choice = Validator.validateInt(scanner.nextLine());
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
                        System.out.println("Đăng xuất thành công.");
                        return;
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private void manageCourses(Scanner scanner) {
        // Quản lý khóa học
        while (true) {
            printHeader("QUẢN LÝ KHÓA HỌC");
            System.out.println("│ 1. Hiển thị danh sách khóa học (phân trang)       │");
            System.out.println("│ 2. Thêm mới khóa học                              │");
            System.out.println("│ 3. Chỉnh sửa khóa học                             │");
            System.out.println("│ 4. Xóa khóa học                                   │");
            System.out.println("│ 5. Tìm kiếm khóa học theo tên (phân trang)        │");
            System.out.println("│ 6. Sắp xếp khóa học (phân trang)                  │");
            System.out.println("│ 7. Quay lại                                       │");
            printFooter();
            System.out.print("Nhập lựa chọn: ");
            int choice = Validator.validateInt(scanner.nextLine());
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
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private void addNewCourse(Scanner scanner) {
        // Thêm khóa học mới
        String courseCode;
        do {
            System.out.print("Mã khóa học (VD: KSB001): ");
            courseCode = CourseValidator.validateCourseId(scanner.nextLine());
            if (courseCode.isEmpty()) return;
            if (courseService.existsByCourseCode(courseCode)) {
                System.err.println("Mã khóa học đã tồn tại, vui lòng nhập mã khác.\n");
                courseCode = null;
            }
        } while (courseCode == null);

        String name;
        do {
            System.out.print("Tên khóa học: ");
            name = Validator.validateString(scanner.nextLine());
            if (name.isEmpty()) return;
            if (courseService.existsByName(name)) {
                System.err.println("Tên khóa học đã tồn tại, vui lòng nhập tên khác.");
                name = null;
            }
        } while (name == null);

        System.out.print("Nhập mô tả khóa học: ");
        String description = scanner.nextLine();

        System.out.print("Nhập thời lượng (số giờ): ");
        int duration = Validator.validateInt(scanner.nextLine());
        if (duration <= 0) return;

        System.out.print("Nhập tên giảng viên: ");
        String instructor = Validator.validateString(scanner.nextLine());
        if (instructor.isEmpty()) return;

        System.out.print("Nhập ngày bắt đầu (dd/MM/yyyy): ");
        String startDate = Validator.validateDate(scanner.nextLine());
        if (startDate.isEmpty()) return;

        System.out.print("Nhập ID admin tạo khóa học: ");
        String adminIdInput = scanner.nextLine();
        Integer createdByAdminId = adminIdInput.isEmpty() ? null : Validator.validateInt(adminIdInput);

        Course course = new Course(courseCode, name, description, duration, instructor, startDate);
        course.setCreatedByAdminId(createdByAdminId);

        if (courseService.save(course)) {
            System.out.println("Thêm khóa học thành công.");
        } else {
            System.err.println("Thêm khóa học thất bại.");
        }
    }

    private void editCourse(Scanner scanner) {
        // Chỉnh sửa khóa học
        System.out.print("Nhập ID khóa học cần sửa: ");
        int id = Validator.validateInt(scanner.nextLine());
        if (id <= 0) return;

        Course course = courseService.findById(id);
        if (course == null) {
            System.err.println("Khóa học không tồn tại.");
            return;
        }

        System.out.println("Thông tin hiện tại:");
        displayCourseTable(List.of(course));

        System.out.print("Nhập tên mới (Enter để giữ nguyên): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) course.setName(newName);

        System.out.print("Nhập mô tả mới (Enter để giữ nguyên): ");
        String newDescription = scanner.nextLine();
        if (!newDescription.isEmpty()) course.setDescription(newDescription);

        System.out.print("Nhập thời lượng mới (Enter để giữ nguyên): ");
        String durationInput = scanner.nextLine();
        if (!durationInput.isEmpty()) {
            int newDuration = Validator.validateInt(durationInput);
            if (newDuration > 0) course.setDuration(newDuration);
        }

        System.out.print("Nhập tên giảng viên mới (Enter để giữ nguyên): ");
        String newInstructor = scanner.nextLine();
        if (!newInstructor.isEmpty()) course.setInstructor(newInstructor);

        System.out.print("Nhập ngày bắt đầu mới (dd/MM/yyyy, Enter để giữ nguyên): ");
        String newStartDate = scanner.nextLine();
        if (!newStartDate.isEmpty()) {
            String validatedDate = Validator.validateDate(newStartDate);
            if (!validatedDate.isEmpty()) course.setCreatedAt(validatedDate);
        }

        if (courseService.update(course)) {
            System.out.println("Cập nhật khóa học thành công.");
        } else {
            System.out.println("Cập nhật khóa học thất bại.");
        }
    }

    private void deleteCourse(Scanner scanner) {
        // Xóa khóa học
        System.out.print("Nhập ID khóa học cần xóa: ");
        int id = Validator.validateInt(scanner.nextLine());
        if (id <= 0) return;

        Course course = courseService.findById(id);
        if (course == null) {
            System.out.println("Khóa học không tồn tại.");
            return;
        }

        if (confirmAction(scanner, "xóa khóa học")) {
            if (courseService.delete(course)) {
                System.out.println("Xóa khóa học thành công.");
            } else {
                System.out.println("Xóa khóa học thất bại.");
            }
        }
    }

    private void searchCourses(Scanner scanner) {
        // Tìm kiếm khóa học theo tên với phân trang
        System.out.print("Nhập tên khóa học cần tìm: ");
        String searchName = scanner.nextLine();
        if (searchName.isEmpty()) return;

        int page = 1;
        while (true) {
            List<Course> courses = courseService.findByNameWithPagination(searchName, page, PAGE_SIZE);
            if (courses.isEmpty()) {
                System.out.println("Không tìm thấy khóa học hoặc không có dữ liệu ở trang này.");
                return;
            }
            System.out.println("\nKết quả tìm kiếm (Trang " + page + "):");
            displayCourseTable(courses);
            System.out.print("\nNhập số trang (0 để thoát): ");
            page = Validator.validateInt(scanner.nextLine());
            if (page <= 0) break;
        }
    }

    private void sortCourses(Scanner scanner) {
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
                return;
        }

        int page = 1;
        while (true) {
            List<Course> courses = courseService.sortWithPagination(sortBy, ascending, page, PAGE_SIZE);
            if (courses.isEmpty()) {
                System.out.println("Không có khóa học hoặc không có dữ liệu ở trang này.");
                return;
            }
            System.out.println("\nDanh sách khóa học đã sắp xếp (Trang " + page + "):");
            displayCourseTable(courses);
            System.out.print("\nNhập số trang (0 để thoát): ");
            page = Validator.validateInt(scanner.nextLine());
            if (page <= 0) break;
        }
    }

    private void displayCoursesWithPagination(Scanner scanner) {
        // Hiển thị danh sách khóa học với phân trang
        int page = 1;
        while (true) {
            List<Course> courses = courseService.findAllWithPagination(page, PAGE_SIZE);
            if (courses.isEmpty()) {
                System.out.println("Không có khóa học hoặc không có dữ liệu ở trang này.");
                return;
            }
            System.out.println("\nDanh sách khóa học (Trang " + page + "):");
            displayCourseTable(courses);
            System.out.print("\nNhập số trang (0 để thoát): ");
            page = Validator.validateInt(scanner.nextLine());
            if (page <= 0) break;
        }
    }

    private void displayCourseTable(List<Course> courses) {
        // Hiển thị bảng khóa học
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

    private void manageStudents(Scanner scanner) {
        // Quản lý học viên
        while (true) {
            printHeader("QUẢN LÝ HỌC VIÊN");
            System.out.println("│ 1. Hiển thị danh sách học viên (phân trang)       │");
            System.out.println("│ 2. Thêm mới học viên                              │");
            System.out.println("│ 3. Chỉnh sửa học viên                             │");
            System.out.println("│ 4. Xóa học viên                                   │");
            System.out.println("│ 5. Tìm kiếm học viên (phân trang)                 │");
            System.out.println("│ 6. Sắp xếp học viên (phân trang)                  │");
            System.out.println("│ 7. Khóa/Mở tài khoản học viên                     │");
            System.out.println("│ 8. Quay lại                                       │");
            printFooter();
            System.out.print("Nhập lựa chọn: ");
            int choice = Validator.validateInt(scanner.nextLine());
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
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private void addNewStudent(Scanner scanner) {
        // Thêm học viên mới
        System.out.print("Nhập mã học viên (VD: SV001): ");
        String studentCode = Validator.validateStudentId(scanner.nextLine());
        if (studentCode.isEmpty()) return;

        System.out.print("Nhập tên đăng nhập: ");
        String username = Validator.validateUsername(scanner.nextLine());
        if (username.isEmpty()) return;

        System.out.print("Nhập mật khẩu: ");
        String password = Validator.validatePassword(scanner.nextLine());
        if (password.isEmpty()) return;

        System.out.print("Nhập họ tên: ");
        String fullName = Validator.validateString(scanner.nextLine());
        if (fullName.isEmpty()) return;

        System.out.print("Nhập email: ");
        String email = Validator.validateEmail(scanner.nextLine());
        if (email.isEmpty()) return;

        System.out.print("Nhập số điện thoại: ");
        String phone = Validator.validatePhone(scanner.nextLine());
        if (phone.isEmpty()) return;

        System.out.print("Nhập giới tính (1: Nam, 0: Nữ): ");
        String sexInput = scanner.nextLine();
        Boolean sex = sexInput.equals("1") ? true : sexInput.equals("0") ? false : null;

        System.out.print("Nhập ngày sinh (dd/MM/yyyy): ");
        String dobInput = scanner.nextLine();
        java.util.Date dob = null;
        if (!dobInput.isEmpty()) {
            try {
                dob = new SimpleDateFormat("dd/MM/yyyy").parse(dobInput);
            } catch (Exception e) {
                System.out.println("Ngày sinh không hợp lệ.");
                return;
            }
        }

        Student student = new Student();
        student.setStudentCode(studentCode);
        student.setUsername(username);
        student.setPassword(password);
        student.setFullName(fullName);
        student.setEmail(email);
        student.setPhone(phone);
        student.setSex(sex);
        student.setDob(dob);

        if (studentService.save(student)) {
            System.out.println("Thêm học viên thành công.");
        } else {
            System.out.println("Thêm học viên thất bại.");
        }
    }

    private void editStudent(Scanner scanner) {
        // Chỉnh sửa học viên
        System.out.print("Nhập ID học viên cần sửa: ");
        int id = Validator.validateInt(scanner.nextLine());
        if (id <= 0) return;

        Student student = studentService.findById(id);
        if (student == null) {
            System.out.println("Học viên không tồn tại.");
            return;
        }

        System.out.println("Thông tin hiện tại:");
        displayStudentTable(List.of(student));

        System.out.print("Nhập họ tên mới (Enter để giữ nguyên): ");
        String newFullName = scanner.nextLine();
        if (!newFullName.isEmpty()) student.setFullName(newFullName);

        System.out.print("Nhập email mới (Enter để giữ nguyên): ");
        String newEmail = scanner.nextLine();
        if (!newEmail.isEmpty()) {
            String validatedEmail = Validator.validateEmail(newEmail);
            if (validatedEmail.isEmpty()) return;
            student.setEmail(validatedEmail);
        }

        System.out.print("Nhập số điện thoại mới (Enter để giữ nguyên): ");
        String newPhone = scanner.nextLine();
        if (!newPhone.isEmpty()) {
            String validatedPhone = Validator.validatePhone(newPhone);
            if (validatedPhone.isEmpty()) return;
            student.setPhone(validatedPhone);
        }

        System.out.print("Nhập giới tính mới (1: Nam, 0: Nữ, Enter để giữ nguyên): ");
        String sexInput = scanner.nextLine();
        if (!sexInput.isEmpty()) {
            Boolean newSex = sexInput.equals("1") ? true : sexInput.equals("0") ? false : null;
            student.setSex(newSex);
        }

        System.out.print("Nhập ngày sinh mới (dd/MM/yyyy, Enter để giữ nguyên): ");
        String dobInput = scanner.nextLine();
        if (!dobInput.isEmpty()) {
            try {
                java.util.Date newDob = new SimpleDateFormat("dd/MM/yyyy").parse(dobInput);
                student.setDob(newDob);
            } catch (Exception e) {
                System.out.println("Ngày sinh không hợp lệ.");
                return;
            }
        }

        if (studentService.update(student)) {
            System.out.println("Cập nhật học viên thành công.");
        } else {
            System.out.println("Cập nhật học viên thất bại.");
        }
    }

    private void deleteStudent(Scanner scanner) {
        // Xóa học viên
        System.out.print("Nhập ID học viên cần xóa: ");
        int id = Validator.validateInt(scanner.nextLine());
        if (id <= 0) return;

        Student student = studentService.findById(id);
        if (student == null) {
            System.out.println("Học viên không tồn tại.");
            return;
        }

        if (confirmAction(scanner, "xóa học viên")) {
            if (studentService.delete(student)) {
                System.out.println("Xóa học viên thành công.");
            } else {
                System.out.println("Xóa học viên thất bại.");
            }
        }
    }

    private void searchStudents(Scanner scanner) {
        // Tìm kiếm học viên theo tên, email hoặc mã
        System.out.print("Nhập tên, email hoặc mã học viên: ");
        String searchInput = scanner.nextLine();

        int page = 1;
        while (true) {
            List<Student> students = studentService.findByNameOrEmailOrCodeWithPagination(searchInput, page, PAGE_SIZE);
            if (students.isEmpty()) {
                System.out.println("Không tìm thấy học viên hoặc không có dữ liệu ở trang này.");
                return;
            }
            System.out.println("\nKết quả tìm kiếm (Trang " + page + "):");
            displayStudentTable(students);
            System.out.print("\nNhập số trang (0 để thoát): ");
            page = Validator.validateInt(scanner.nextLine());
            if (page <= 0) break;
        }
    }

    private void sortStudents(Scanner scanner) {
        // Sắp xếp học viên
        System.out.println("Chọn tiêu chí sắp xếp:");
        System.out.println("1. Theo tên (tăng dần)");
        System.out.println("2. Theo tên (giảm dần)");
        System.out.println("3. Theo mã (tăng dần)");
        System.out.println("4. Theo mã (giảm dần)");
        int sortChoice = Validator.validateInt(scanner.nextLine());
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
                System.out.println("Lựa chọn không hợp lệ.");
                return;
        }

        int page = 1;
        while (true) {
            List<Student> students = studentService.sortWithPagination(sortBy, ascending, page, PAGE_SIZE);
            if (students.isEmpty()) {
                System.out.println("Không có học viên hoặc không có dữ liệu ở trang này.");
                return;
            }
            System.out.println("\nDanh sách học viên đã sắp xếp (Trang " + page + "):");
            displayStudentTable(students);
            System.out.print("\nNhập số trang (0 để thoát): ");
            page = Validator.validateInt(scanner.nextLine());
            if (page <= 0) break;
        }
    }

    private void lockUnlockStudent(Scanner scanner) {
        // Khóa hoặc mở tài khoản học viên
        System.out.print("Nhập ID học viên: ");
        int id = Validator.validateInt(scanner.nextLine());
        if (id <= 0) return;

        Student student = studentService.findById(id);
        if (student == null) {
            System.out.println("Học viên không tồn tại.");
            return;
        }

        System.out.println("Thông tin hiện tại:");
        displayStudentTable(List.of(student));

        System.out.print("Khóa tài khoản? (Y/N): ");
        String lockInput = scanner.nextLine();
        boolean isActive = !lockInput.equalsIgnoreCase("Y");
        student.setActive(isActive);

        if (studentService.update(student)) {
            System.out.println((isActive ? "Mở" : "Khóa") + " tài khoản thành công.");
        } else {
            System.out.println((isActive ? "Mở" : "Khóa") + " tài khoản thất bại.");
        }
    }

    private void displayStudentsWithPagination(Scanner scanner) {
        // Hiển thị danh sách học viên với phân trang
        int page = 1;
        while (true) {
            List<Student> students = studentService.findAllWithPagination(page, PAGE_SIZE);
            if (students.isEmpty()) {
                System.out.println("Không có học viên hoặc không có dữ liệu ở trang này.");
                return;
            }
            System.out.println("\nDanh sách học viên (Trang " + page + "):");
            displayStudentTable(students);
            System.out.print("\nNhập số trang (0 để thoát): ");
            page = Validator.validateInt(scanner.nextLine());
            if (page <= 0) break;
        }
    }

    private void displayStudentTable(List<Student> students) {
        // Hiển thị bảng học viên
        System.out.println("\n┌───────┬────────────┬──────────────────────────┬──────────────────────────┬────────────┬────────────┐");
        System.out.println("│ ID    │ Mã học viên│ Họ tên                   │ Email                    │ SĐT        │ Trạng thái │");
        System.out.println("├───────┼────────────┼──────────────────────────┼──────────────────────────┼────────────┼────────────┤");
        for (Student student : students) {
            System.out.printf("│ %-5d │ %-10s │ %-24s │ %-24s │ %-10s │ %-10s │%n",
                    student.getId(),
                    student.getStudentCode(),
                    truncateString(student.getFullName(), 24),
                    truncateString(student.getEmail(), 24),
                    student.getPhone() != null ? student.getPhone() : "",
                    student.isActive() ? "Hoạt động" : "Bị khóa");
        }
        System.out.println("└───────┴────────────┴──────────────────────────┴──────────────────────────┴────────────┴────────────┘");
    }

    private void manageRegistrations(Scanner scanner) {
        // Quản lý đăng ký học
        while (true) {
            printHeader("QUẢN LÝ ĐĂNG KÝ HỌC");
            System.out.println("│ 1. Hiển thị danh sách đăng ký (phân trang)        │");
            System.out.println("│ 2. Phê duyệt/Hủy đăng ký                          │");
            System.out.println("│ 3. Xóa đơn đăng ký bị hủy                          │");
            System.out.println("│ 4. Quay lại                                       │");
            printFooter();
            System.out.print("Nhập lựa chọn: ");
            int choice = Validator.validateInt(scanner.nextLine());
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
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private void deleteCancelledEnrollments(Scanner scanner) {
        printHeader("XÓA ĐƠN ĐĂNG KÝ BỊ HỦY");
        // Lấy danh sách đơn đăng ký có trạng thái CANCEL
        List<Enrollment> cancelledEnrollments = enrollmentService.findAll()
                .stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.CANCEL)
                .toList();

        if (cancelledEnrollments.isEmpty()) {
            System.out.println("Không có đơn đăng ký nào ở trạng thái bị hủy.");
            return;
        }

        System.out.println("Danh sách đơn đăng ký bị hủy:");
        displayEnrollmentTable(cancelledEnrollments);

        System.out.print("Nhập ID đơn đăng ký cần xóa: ");
        int id = Validator.validateInt(scanner.nextLine());
        if (id <= 0) {
            System.out.println("ID không hợp lệ.");
            return;
        }

        Enrollment enrollment = enrollmentService.findById(id);
        if (enrollment == null) {
            System.out.println("Đơn đăng ký không tồn tại.");
            return;
        }
        if (enrollment.getStatus() != EnrollmentStatus.CANCEL) {
            System.out.println("Chỉ có thể xóa đơn đăng ký ở trạng thái bị hủy.");
            return;
        }

        if (confirmAction(scanner, "xóa đơn đăng ký")) {
            if (enrollmentService.deleteById(id)) {
                System.out.println( "Xóa đơn đăng ký thành công.");
            } else {
                System.out.println( "Xóa đơn đăng ký thất bại." );
            }
        }
    }

    private void displayRegistrations(Scanner scanner) {
        // Hiển thị danh sách đăng ký theo khóa học
        System.out.print("Nhập ID khóa học: ");
        int courseId = Validator.validateInt(scanner.nextLine());
        if (courseId <= 0) return;

        Course course = courseService.findById(courseId);
        if (course == null) {
            System.out.println("Khóa học không tồn tại.");
            return;
        }

        int page = 1;
        while (true) {
            List<Enrollment> enrollments = enrollmentService.findByCourseIdWithPagination(courseId, page, PAGE_SIZE);
            if (enrollments.isEmpty()) {
                System.out.println("Không có đăng ký nào hoặc không có dữ liệu ở trang này.");
                return;
            }
            System.out.println("\nDanh sách đăng ký khóa học ID " + courseId + " (Trang " + page + "):");
            displayEnrollmentTable(enrollments);
            System.out.print("\nNhập số trang (0 để thoát): ");
            page = Validator.validateInt(scanner.nextLine());
            if (page <= 0) break;
        }
    }

    private void manageEnrollmentStatus(Scanner scanner) {
        // Phê duyệt hoặc hủy đăng ký
        System.out.print("Nhập ID đăng ký: ");
        int id = Validator.validateInt(scanner.nextLine());
        if (id <= 0) return;

        Enrollment enrollment = enrollmentService.findById(id);
        if (enrollment == null) {
            System.out.println("Đăng ký không tồn tại.");
            return;
        }

//        System.out.println("Thông tin đăng ký hiện tại:");
//        displayEnrollmentTable(List.of(enrollment));

        System.out.println("Chọn hành động:");
        System.out.println("1. Phê duyệt");
        System.out.println("2. Hủy");
        int action = Validator.validateInt(scanner.nextLine());
        if (enrollment.getStatus() == EnrollmentStatus.CONFIRM) {
            System.out.println("Đăng ký đã được phê duyệt trước đó.");
            return;
        } else if (enrollment.getStatus() == EnrollmentStatus.CANCEL) {
            System.out.println("Đăng ký đã bị hủy trước đó.");
            return;

        }
        if (action == 1) {
            enrollment.setStatus(EnrollmentStatus.CONFIRM);
        } else if (action == 2) {
            enrollment.setStatus(EnrollmentStatus.CANCEL);
        } else {
            System.out.println("Lựa chọn không hợp lệ.");
            return;
        }

        System.out.print("Nhập ID admin xử lý (Enter để bỏ qua): ");
        String adminIdInput = scanner.nextLine();
        if (!adminIdInput.isEmpty()) {
            int adminId = Validator.validateInt(adminIdInput);
            if (adminId > 0) enrollment.setAdminRefId(adminId);
        }

        if (enrollmentService.update(enrollment)) {
            System.out.println("Xử lý đăng ký thành công.");
        } else {
            System.out.println("Xử lý đăng ký thất bại.");
        }
    }

    private void displayEnrollmentTable(List<Enrollment> enrollments) {
        // Hiển thị bảng đăng ký
        System.out.println("\n┌───────┬────────────┬──────────────────────────┬────────────┬────────────┬─────────────────┐");
        System.out.println("│ ID    │ Mã đăng ký │ Họ tên học viên          │ Mã học viên│ Trạng thái │ Ngày đăng ký    │");
        System.out.println("├───────┼────────────┼──────────────────────────┼────────────┼────────────┼─────────────────┤");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Enrollment enrollment : enrollments) {
            Student student = enrollment.getStudent();
            String fullName = student != null ? student.getFullName() : "N/A";
            String studentCode = student != null ? student.getStudentCode() : "N/A";
            System.out.printf("│ %-5d │ %-10s │ %-24s │ %-10s │ %-10s │ %-15s │%n",
                    enrollment.getId(),
                    truncateString(enrollment.getEnrollmentCode(),10),
                    truncateString(fullName, 24),
                    studentCode,
                    enrollment.getStatus().name(),
                    enrollment.getRegistrationDate() != null ? sdf.format(enrollment.getRegistrationDate()) : "");
        }
        System.out.println("└───────┴────────────┴──────────────────────────┴────────────┴────────────┴─────────────────┘");
    }

    private void showStatistics(Scanner scanner) {
        // Thống kê học viên theo khóa học
        while (true) {
            printHeader("THỐNG KÊ");
            System.out.println("│ 1. Thống kê tổng số lượng khóa học và tổng số học viên       │");
            System.out.println("│ 2. Thống kê tổng số học viên theo khóa học       │");
            System.out.println("│ 3. Thống kê top 5 khóa học đông sinh viên nhất       │");
            System.out.println("│ 4. Liệt kê các khóa học có trên 10 học viên       │");
            System.out.println("│ 5. Quay lại                                       │");
            printFooter();
            System.out.print("Nhập lựa chọn: ");
            int choice = Validator.validateInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    int totalCourses = statisticService.getTotalCourses();
                    System.out.println("Tổng số lượng khóa học:" + totalCourses);
                    int totalStudents = statisticService.getTotalStudents();
                    System.out.println("Tổng số học viên:" + totalStudents);
                    break;
                case 2:
                    System.out.print("Nhập ID khóa học: ");
                    int courseId = Validator.validateInt(scanner.nextLine());
                    if (courseId <= 0) return;
                    Course course = courseService.findById(courseId);
                    if (course == null) {
                        System.out.println("Khóa học không tồn tại.");
                        break;
                    }
                    int studentCount = statisticService.countStudentsByCourse(courseId);
                    System.out.println("Khóa học " + course.getName() + " có " + studentCount + " học viên.");
                    break;
                case 3:
                    List<Course> topCourses = statisticService.getTop5CoursesByStudents();
                    if (topCourses.isEmpty()) {
                        System.out.println("Không có khóa học nào.");
                        break;
                    }
                    System.out.println("\nTop 5 khóa học đông sinh viên nhất:");
                    System.out.println("┌───────┬──────────────────────────┬────────────┐");
                    System.out.println("│ ID    │ Tên khóa học             │ Số học viên│");
                    System.out.println("├───────┼──────────────────────────┼────────────┤");
                    for (Course topCourse : topCourses) {
                        int studentCountTop = statisticService.countStudentsByCourse(topCourse.getId());
                        System.out.printf("│ %-5d │ %-24s │ %-10d │%n",
                                topCourse.getId(),
                                truncateString(topCourse.getName(), 24),
                                studentCountTop);
                    }
                    System.out.println("└───────┴──────────────────────────┴────────────┘");
                    break;
                case 4:
                    List<Course> coursesWithMoreThan10Students = statisticService.getCoursesWithMoreThan10Students();
                    if (coursesWithMoreThan10Students.isEmpty()) {
                        System.out.println("Không có khóa học nào có hơn 10 học viên.");
                        break;
                    }
                    System.out.println("\nDanh sách khóa học có hơn 10 học viên:");
                    System.out.println("┌───────┬──────────────────────────┬────────────┐");
                    System.out.println("│ ID    │ Tên khóa học             │ Số học viên│");
                    System.out.println("├───────┼──────────────────────────┼────────────┤");
                    for (Course courseWithMoreThan10 : coursesWithMoreThan10Students) {
                        int studentCountMoreThan10 = statisticService.countStudentsByCourse(courseWithMoreThan10.getId());
                        System.out.printf("│ %-5d │ %-24s │ %-10d │%n",
                                courseWithMoreThan10.getId(),
                                truncateString(courseWithMoreThan10.getName(), 24),
                                studentCountMoreThan10);
                    }
                    System.out.println("└───────┴──────────────────────────┴────────────┘");
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private boolean confirmAction(Scanner scanner, String action) {
        // Xác nhận hành động
        System.out.print("Xác nhận " + action + " (Y/N): ");
        return scanner.nextLine().equalsIgnoreCase("Y");
    }

    private void printHeader(String title) {
        // In tiêu đề menu
        System.out.println("\n═════════════════════ " + title + " ═════════════════════");
    }

    private void printFooter() {
        // In chân trang menu
        System.out.println("═════════════════════════════════════════════════════");
    }

    private String truncateString(String str, int maxLength) {
        // Cắt chuỗi nếu vượt quá độ dài
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }
}