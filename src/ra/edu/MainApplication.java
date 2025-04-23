package ra.edu;

import ra.edu.business.model.Admin;
import ra.edu.business.model.Student;
import ra.edu.business.service.admin.AdminService;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.business.service.student.StudentServiceImp;
import ra.edu.presentation.AdminUI;
import ra.edu.presentation.StudentUI;
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
                    if (student != null) {
                        UIUtils.showSuccess("Đăng nhập thành công!");
                        StudentUI studentUI = new StudentUI();
                        studentUI.displayMenuStudent(scanner, student);
                    } else {
                        UIUtils.showError("Tên đăng nhập hoặc mật khẩu không đúng.");
                    }
                    System.out.println("Nhấn Enter để tiếp tục...");
                    scanner.nextLine();
                    break;
                case 3:
                    UIUtils.printHeader("ĐĂNG KÝ HỌC VIÊN");
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
                            System.out.println("Nhấn Enter để tiếp tục...");
                            scanner.nextLine();
                            break;
                        }
                    }

                    Student newStudent = new Student();
                    newStudent.setStudentCode(studentCode);
                    newStudent.setUsername(username);
                    newStudent.setPassword(password);
                    newStudent.setFullName(fullName);
                    newStudent.setEmail(email);
                    newStudent.setPhone(phone.isEmpty() ? null : phone);
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
        System.out.println("│ 4. Thoát                                      │");
        UIUtils.printFooter();
    }
}