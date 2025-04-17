package ra.edu.presentation;

import ra.edu.business.model.Course;
import ra.edu.business.service.course.CourseService;
import ra.edu.business.service.course.CourseServiceImp;
import ra.edu.validate.Validator;
import ra.edu.validate.CourseValidator;

import java.util.List;
import java.util.Scanner;

public class StudentUI {
    private final CourseService courseService = new CourseServiceImp();

    public void displayMenuStudent(Scanner scanner) {
        do {
            System.out.println("========== MENU HỌC VIÊN ==========");
            System.out.println("1. Xem danh sách khóa học");
            System.out.println("2. Đăng ký khóa học");
            System.out.println("3. Xem khóa học đã đăng ký");
            System.out.println("4. Hủy đăng ký");
            System.out.println("5. Đổi mật khẩu");
            System.out.println("6. Đăng xuất");
            System.out.println("================================");
            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = Validator.validateInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    // View course list with recommendations
                    List<Course> courses = courseService.findAll();
                    if (courses.isEmpty()) {
                        System.out.println("Chưa có khóa học nào.");
                    } else {
                        System.out.println("Danh sách khóa học:");
                        for (Course course : courses) {
                            System.out.println(course);
                        }
                        // TODO: Implement course recommendations
                        System.out.println("Khóa học đề xuất: Chưa triển khai.");
                    }
                    break;
                case 2:
                    // Register for a course
                    System.out.print("Nhập mã khóa học cần đăng ký: ");
                    int courseId = Validator.validateInt(scanner.nextLine());
                    Course course = courseService.findById(courseId);
                    if (course == null) {
                        System.out.println("Khóa học không tồn tại.");
                    } else {
                        // TODO: Check if already registered and save registration
                        System.out.println("Đăng ký khóa học chưa triển khai.");
                    }
                    break;
                case 3:
                    // View registered courses
                    // TODO: Implement fetching registered courses
                    System.out.println("Xem khóa học đã đăng ký chưa triển khai.");
                    break;
                case 4:
                    // Cancel registration
                    // TODO: Implement canceling unconfirmed registrations
                    System.out.println("Hủy đăng ký chưa triển khai.");
                    break;
                case 5:
                    // Change password
                    System.out.print("Nhập mật khẩu cũ: ");
                    String oldPassword = scanner.nextLine();
                    // TODO: Verify old password
                    System.out.println("Chọn phương thức xác thực: 1. Email, 2. Số điện thoại");
                    int authChoice = Validator.validateInt(scanner.nextLine());
                    String verificationCode = "123456"; // Simulate code
                    System.out.println("Mã xác thực đã gửi: " + verificationCode);
                    System.out.print("Nhập mã xác thực: ");
                    String inputCode = scanner.nextLine();
                    if (!inputCode.equals(verificationCode)) {
                        System.out.println("Mã xác thực không đúng.");
                        break;
                    }
                    System.out.print("Nhập mật khẩu mới: ");
                    String newPassword = scanner.nextLine();
                    System.out.print("Xác nhận mật khẩu mới: ");
                    String confirmPassword = scanner.nextLine();
                    if (!newPassword.equals(confirmPassword)) {
                        System.out.println("Mật khẩu xác nhận không khớp.");
                        break;
                    }
                    String validatedPassword = CourseValidator.validatePassword(newPassword);
                    if (validatedPassword.isEmpty()) {
                        break;
                    }
                    // TODO: Update password in database
                    System.out.println("Đổi mật khẩu thành công.");
                    break;
                case 6:
                    System.out.println("Đăng xuất thành công.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ vui lòng nhập lại.");
            }
        } while (true);
    }
}