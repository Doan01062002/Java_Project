package ra.edu.presentation;

import ra.edu.business.model.Course;
import ra.edu.business.service.course.CourseService;
import ra.edu.business.service.course.CourseServiceImp;
import ra.edu.validate.Validator;

import java.util.List;
import java.util.Scanner;

public class AdminUI {
    private final CourseService courseService = new CourseServiceImp();

    public void displayMenuAdmin(Scanner scanner) {
        do {
            System.out.println("========== MENU ADMIN ==========");
            System.out.println("1. Quản lý khóa học");
            System.out.println("2. Quản lý học viên");
            System.out.println("3. Quản lý đăng ký học");
            System.out.println("4. Thống kê học viên theo khóa học");
            System.out.println("5. Đăng xuất");
            System.out.println("================================");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = Validator.validateInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    displayMenuManagerCourses(scanner);
                    break;
                case 2:
                    displayMenuManagerStudents(scanner);
                    break;
                case 3:
                    displayMenuManagerRegistrationCourses(scanner);
                    break;
                case 4:
                    displayMenuStatistic(scanner);
                    break;
                case 5:
                    System.out.println("Đăng xuất thành công");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ vui lòng nhập lại.");
            }
        } while (true);
    }

    public void displayMenuManagerCourses(Scanner scanner) {
        do {
            System.out.println("========== MENU QUẢN LÝ KHÓA HỌC ==========");
            System.out.println("1. Hiển thị danh sách khóa học");
            System.out.println("2. Thêm mới khóa học");
            System.out.println("3. Chỉnh sửa thông tin khóa học");
            System.out.println("4. Xóa khóa học");
            System.out.println("5. Tìm kiếm khóa học theo tên");
            System.out.println("6. Sắp xếp theo tên hoặc id");
            System.out.println("7. Quay về menu chính");
            System.out.println("================================");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = Validator.validateInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    // Display all courses
                    List<Course> courses = courseService.findAll();
                    if (courses.isEmpty()) {
                        System.out.println("Chưa có khóa học nào.");
                    } else {
                        System.out.println("Danh sách khóa học:");
                        for (Course course : courses) {
                            System.out.println(course);
                        }
                    }
                    break;
                case 2:
                    // Add new course
                    System.out.print("Nhập tên khóa học: ");
                    String name = scanner.nextLine();
                    System.out.print("Nhập thời lượng (số giờ): ");
                    int duration = Validator.validateInt(scanner.nextLine());
                    Course newCourse = new Course(0, name, duration);
                    if (courseService.save(newCourse)) {
                        System.out.println("Thêm khóa học thành công.");
                    } else {
                        System.out.println("Thêm khóa học thất bại.");
                    }
                    break;
                case 3:
                    // Update course
                    System.out.print("Nhập mã khóa học cần sửa: ");
                    int id = Validator.validateInt(scanner.nextLine());
                    Course course = courseService.findById(id);
                    if (course == null) {
                        System.out.println("Khóa học không tồn tại.");
                    } else {
                        System.out.println("Thông tin hiện tại: " + course);
                        System.out.print("Nhập tên mới (nhấn Enter để giữ nguyên): ");
                        String newName = scanner.nextLine();
                        if (!newName.isEmpty()) {
                            course.setName(newName);
                        }
                        System.out.print("Nhập thời lượng mới (nhấn Enter để giữ nguyên): ");
                        String durationInput = scanner.nextLine();
                        if (!durationInput.isEmpty()) {
                            course.setDuration(Validator.validateInt(durationInput));
                        }
                        if (courseService.update(course)) {
                            System.out.println("Sửa khóa học thành công.");
                        } else {
                            System.out.println("Sửa khóa học thất bại.");
                        }
                    }
                    break;
                case 4:
                    // Delete course
                    System.out.print("Nhập mã khóa học cần xóa: ");
                    int deleteId = Validator.validateInt(scanner.nextLine());
                    Course courseToDelete = courseService.findById(deleteId);
                    if (courseToDelete == null) {
                        System.out.println("Khóa học không tồn tại.");
                    } else {
                        System.out.print("Xác nhận xóa (Y/N): ");
                        String confirm = scanner.nextLine();
                        if (confirm.equalsIgnoreCase("Y")) {
                            if (courseService.delete(courseToDelete)) {
                                System.out.println("Xóa khóa học thành công.");
                            } else {
                                System.out.println("Xóa khóa học thất bại.");
                            }
                        }
                    }
                    break;
                case 5:
                    // Search course by name
                    System.out.print("Nhập tên khóa học cần tìm: ");
                    String searchName = scanner.nextLine();
                    List<Course> foundCourses = courseService.findByName(searchName);
                    if (foundCourses.isEmpty()) {
                        System.out.println("Không tìm thấy khóa học.");
                    } else {
                        System.out.println("Kết quả tìm kiếm:");
                        for (Course c : foundCourses) {
                            System.out.println(c);
                        }
                    }
                    break;
                case 6:
                    // Sort courses
                    System.out.println("Chọn tiêu chí sắp xếp:");
                    System.out.println("1. Theo tên (tăng dần)");
                    System.out.println("2. Theo tên (giảm dần)");
                    System.out.println("3. Theo ID (tăng dần)");
                    System.out.println("4. Theo ID (giảm dần)");
                    int sortChoice = Validator.validateInt(scanner.nextLine());
                    List<Course> sortedCourses;
                    switch (sortChoice) {
                        case 1:
                            sortedCourses = courseService.sortByName(true);
                            break;
                        case 2:
                            sortedCourses = courseService.sortByName(false);
                            break;
                        case 3:
                            sortedCourses = courseService.sortById(true);
                            break;
                        case 4:
                            sortedCourses = courseService.sortById(false);
                            break;
                        default:
                            System.out.println("Lựa chọn không hợp lệ.");
                            continue;
                    }
                    if (sortedCourses.isEmpty()) {
                        System.out.println("Chưa có khóa học nào.");
                    } else {
                        System.out.println("Danh sách khóa học đã sắp xếp:");
                        for (Course c : sortedCourses) {
                            System.out.println(c);
                        }
                    }
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ vui lòng nhập lại.");
            }
        } while (true);
    }

    public void displayMenuManagerStudents(Scanner scanner){
        do {
            System.out.println("========== MENU QUẢN LÝ HỌC VIÊN ==========");
            System.out.println("1. Hiển thị danh sách học viên");
            System.out.println("2. Thêm mới học viên");
            System.out.println("3. Chỉnh sửa thông tin học viên");
            System.out.println("4. Xóa học viên");
            System.out.println("5. Tìm kiếm học viên theo tên, email hoặc id");
            System.out.println("6. Sắp xếp theo tên hoặc id");
            System.out.println("7. Quay về menu chính");
            System.out.println("================================");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = Validator.validateInt(scanner.nextLine());
            switch (choice){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ vui lòng nhập lại.");
            }
        }while (true);
    }

    public void displayMenuManagerRegistrationCourses(Scanner scanner){
        do {
            System.out.println("========== MENU QUẢN LÝ ĐĂNG KÝ KHÓA HỌC ==========");
            System.out.println("1. Hiển thị danh sách học viên theo từng khóa học");
            System.out.println("2. Duyệt học viên vào khóa học");
            System.out.println("3. Xóa học viên khỏi khóa học");
            System.out.println("4. Quay về menu chính");
            System.out.println("================================");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = Validator.validateInt(scanner.nextLine());
            switch (choice){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ vui lòng nhập lại.");
            }
        }while (true);
    }

    public void displayMenuStatistic(Scanner scanner){
        do {
            System.out.println("========== MENU THỐNG KÊ ==========");
            System.out.println("1. Thống kê tổng số lượng khóa học và học viên");
            System.out.println("2. Thống kê học viên theo từng khóa học");
            System.out.println("3. Top 5 khóa học đông học viên nhất");
            System.out.println("4. Liệt kê khóa học có trên 10 học viên");
            System.out.println("5. Quay về menu chính");
            System.out.println("================================");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = Validator.validateInt(scanner.nextLine());
            switch (choice){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ vui lòng nhập lại.");
            }
        }while (true);
    }
}
