package ra.edu;

import ra.edu.business.model.Admin;
import ra.edu.business.model.Student;
import ra.edu.business.service.admin.AdminService;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.business.service.student.StudentServiceImp;
import ra.edu.presentation.AdminUI;
import ra.edu.presentation.StudentUI;
import ra.edu.utils.EmailService;
import ra.edu.utils.UIUtils;
import ra.edu.validate.Validator;

import java.text.SimpleDateFormat;
import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentServiceImp studentService = new StudentServiceImp();
        AdminService adminService = new AdminServiceImp();

        while (true) {
            showMainMenu();
            int choice = Validator.validateInt(scanner, "Nhập lựa chọn (1-4): ");
            switch (choice) {
                case 1:
                    UIUtils.printHeader("ĐĂNG NHẬP ADMIN");
                    String adminUsername = Validator.validateString(scanner, "Tên đăng nhập: ");
                    String adminPassword = Validator.validatePassword(scanner, "Mật khẩu: ");
                    Admin admin = adminService.login(adminUsername, adminPassword);
                    if (admin != null) {
                        UIUtils.showSuccess("Đăng nhập thành công!");
                        AdminUI adminUI = new AdminUI();
                        adminUI.displayMenuAdmin(scanner);
                    } else {
                        UIUtils.showError("Tên đăng nhập hoặc mật khẩu không đúng.");
                    }
                    System.out.println("Nhấn Enter để tiếp tục...");
                    scanner.nextLine();
                    break;
                case 2:
                    UIUtils.printHeader("ĐĂNG NHẬP HỌC VIÊN");
                    String studentUsername = Validator.validateString(scanner, "Tên đăng nhập: ");
                    String studentPassword = Validator.validatePassword(scanner, "Mật khẩu: ");
                    Student student = studentService.login(studentUsername, studentPassword);

                    if (student == null) {
                        UIUtils.showError("Tên đăng nhập hoặc mật khẩu không đúng.");
                    } else if (!student.isActive()) {
                        UIUtils.showError("Tài khoản của bạn đã bị khóa.");
                    } else if (!student.getPassword().equals(studentPassword)) {
                        UIUtils.showError("Tên đăng nhập hoặc mật khẩu không đúng.");
                    } else {
                        UIUtils.showSuccess("Đăng nhập thành công!");
                        StudentUI studentUI = new StudentUI();
                        studentUI.displayMenuStudent(scanner, student);
                    }

                    System.out.println("Nhấn Enter để tiếp tục...");
                    scanner.nextLine();
                    break;
                case 3:
                    UIUtils.printHeader("ĐĂNG KÝ HỌC VIÊN");

                    // Generate unique student code
                    String studentCode;
                    do {
                        studentCode = "SV" + (int) (Math.random() * 900 + 100); // Random 3-digit number
                    } while (studentService.existsByStudentCode(studentCode));

                    System.out.println("Mã học viên: " + studentCode);

                    // Validate unique username
                    String username;
                    while (true) {
                        username = Validator.validateUsername(scanner, "Tên đăng nhập: ");
                        if (studentService.existsByUsername(username)) {
                            UIUtils.showError("Tên đăng nhập đã tồn tại. Vui lòng nhập lại.");
                        } else {
                            break;
                        }
                    }

                    // Validate unique email
                    String email;
                    while (true) {
                        email = Validator.validateEmail(scanner, "Email: ");
                        if (studentService.existsByEmail(email)) {
                            UIUtils.showError("Email đã tồn tại. Vui lòng nhập lại.");
                        } else {
                            break;
                        }
                    }

                    // Validate unique phone number
                    String phone;
                    while (true) {
                        phone = Validator.validatePhone(scanner, "Số điện thoại: ");
                        if (studentService.existsByPhone(phone)) {
                            UIUtils.showError("Số điện thoại đã tồn tại. Vui lòng nhập lại.");
                        } else {
                            break;
                        }
                    }

                    // Validate gender
                    Boolean sex;
                    while (true) {
                        System.out.print("Giới tính (1: Nam, 0: Nữ): ");
                        String sexInput = scanner.nextLine().trim();
                        if (sexInput.equals("1")) {
                            sex = true;
                            break;
                        } else if (sexInput.equals("0")) {
                            sex = false;
                            break;
                        } else {
                            UIUtils.showError("Vui lòng nhập 1 (Nam) hoặc 0 (Nữ).");
                        }
                    }

                    // Validate date of birth
                    java.util.Date dob;
                    while (true) {
                        String dobInput = Validator.validateDate(scanner, "Ngày sinh (dd/MM/yyyy): ");
                        try {
                            dob = new SimpleDateFormat("dd/MM/yyyy").parse(dobInput);
                            break;
                        } catch (Exception e) {
                            UIUtils.showError("Ngày sinh không hợp lệ. Vui lòng nhập lại.");
                        }
                    }

                    // Create and register the student
                    String password = Validator.validatePassword(scanner, "Mật khẩu: ");
                    String fullName = Validator.validateString(scanner, "Họ tên: ");

                    Student newStudent = new Student();
                    newStudent.setStudentCode(studentCode);
                    newStudent.setUsername(username);
                    newStudent.setPassword(password);
                    newStudent.setFullName(fullName);
                    newStudent.setEmail(email);
                    newStudent.setPhone(phone);
                    newStudent.setSex(sex);
                    newStudent.setDob(dob);

                    if (studentService.register(newStudent)) {
                        UIUtils.showSuccess("Đăng ký tài khoản học viên thành công.");
                    } else {
                        UIUtils.showError("Đăng ký tài khoản học viên thất bại.");
                    }
                    System.out.println("Nhấn Enter để tiếp tục...");
                    scanner.nextLine();
                    break;
                case 4:
                    UIUtils.printHeader("QUÊN MẬT KHẨU HỌC VIÊN");

                    // Prompt for email
                    String emailRe = Validator.validateEmail(scanner, "Nhập email đã đăng ký: ");
                    Student existingStudent = studentService.findByEmail(emailRe);

                    if (existingStudent == null) {
                        UIUtils.showError("Không tìm thấy tài khoản với email đã nhập.");
                    } else {
                        // Generate reset code
                        String resetCode = String.valueOf((int) (Math.random() * 900000 + 100000)); // 6-digit code
                        EmailService.sendResetCode(emailRe, resetCode); // Send email with reset code
                        UIUtils.showSuccess("Mã đặt lại mật khẩu đã được gửi đến email của bạn.");

                        // Validate reset code
                        String enteredCode;
                        do {
                            System.out.print("Nhập mã đặt lại mật khẩu: ");
                            enteredCode = scanner.nextLine().trim();
                            if (!enteredCode.equals(resetCode)) {
                                UIUtils.showError("Mã đặt lại mật khẩu không chính xác. Vui lòng thử lại.");
                            }
                        } while (!enteredCode.equals(resetCode));

                        // Allow password reset
                        String newPassword = Validator.validatePassword(scanner, "Mật khẩu mới: ");
                        existingStudent.setPassword(newPassword);

                        if (studentService.update(existingStudent)) {
                            UIUtils.showSuccess("Đặt lại mật khẩu thành công.");
                        } else {
                            UIUtils.showError("Đặt lại mật khẩu thất bại.");
                        }
                    }

                    System.out.println("Nhấn Enter để tiếp tục...");
                    scanner.nextLine();
                    break;
                case 5:
                    UIUtils.showSuccess("Thoát chương trình. Tạm biệt!");
                    scanner.close();
                    System.exit(0);
                default:
                    UIUtils.showError("Lựa chọn không hợp lệ, vui lòng nhập lại.");
            }
        }
    }

    private static void showMainMenu() {
        UIUtils.printHeader("HỆ THỐNG QUẢN LÝ KHÓA HỌC");
        System.out.println("│ 1. Đăng nhập Admin                            │");
        System.out.println("│ 2. Đăng nhập Học viên                         │");
        System.out.println("│ 3. Đăng ký tài khoản Học viên                 │");
        System.out.println("│ 4. Quên mật khẩu Học viên                     │");
        System.out.println("│ 5. Thoát                                      │");
        UIUtils.printFooter();
    }
}