package ra.edu;

import ra.edu.business.model.Admin;
import ra.edu.business.model.Student;
import ra.edu.business.service.admin.AdminService;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.business.service.student.StudentServiceImp;
import ra.edu.presentation.AdminUI;
import ra.edu.presentation.StudentUI;
import ra.edu.validate.Validator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentServiceImp studentService = new StudentServiceImp();
        AdminService adminService = new AdminServiceImp();

        System.out.println("Chào mừng đến với Hệ thống Quản lý Khóa học!");

        while (true) {
            showMainMenu();
            int choice = Validator.validateInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Đăng nhập Admin:");
                    System.out.print("Tên đăng nhập: ");
                    String adminUsername = scanner.nextLine();
                    System.out.print("Mật khẩu: ");
                    String adminPassword = scanner.nextLine();
                    Admin admin = adminService.login(adminUsername, adminPassword);
                    if (admin != null) {
                        System.out.println("Đăng nhập thành công!");
                        AdminUI adminUI = new AdminUI();
                        adminUI.displayMenuAdmin(scanner);
                    } else {
                        System.out.println("Tên đăng nhập hoặc mật khẩu không đúng.");
                    }
                    break;
                case 2:
                    System.out.println("Đăng nhập Học viên:");
                    System.out.print("Tên đăng nhập: ");
                    String studentUsername = scanner.nextLine();
                    System.out.print("Mật khẩu: ");
                    String studentPassword = scanner.nextLine();
                    Student student = studentService.login(studentUsername, studentPassword);
                    if (student != null) {
                        System.out.println("Đăng nhập thành công!");
                        StudentUI studentUI = new StudentUI();
                        studentUI.displayMenuStudent(scanner, student);
                    } else {
                        System.out.println("Tên đăng nhập hoặc mật khẩu không đúng.");
                    }
                    break;
                case 3:
                    System.out.println("Đăng ký tài khoản học viên:");
                    System.out.print("Nhập mã học viên (SVxxx): ");
                    String studentCode = Validator.validateStudentId(scanner.nextLine());
                    if (studentCode.isEmpty()) break;
                    System.out.print("Nhập tên đăng nhập: ");
                    String username = Validator.validateUsername(scanner.nextLine());
                    if (username.isEmpty()) break;
                    System.out.print("Nhập mật khẩu: ");
                    String password = Validator.validatePassword(scanner.nextLine());
                    if (password.isEmpty()) break;
                    System.out.print("Nhập họ tên: ");
                    String fullName = Validator.validateString(scanner.nextLine());
                    if (fullName.isEmpty()) break;
                    System.out.print("Nhập email: ");
                    String email = Validator.validateEmail(scanner.nextLine());
                    if (email.isEmpty()) break;
                    System.out.print("Nhập số điện thoại: ");
                    String phone = Validator.validatePhone(scanner.nextLine());
                    if (phone.isEmpty()) break;
                    System.out.print("Nhập giới tính (1: Nam, 0: Nữ): ");
                    String sexInput = scanner.nextLine();
                    Boolean sex = sexInput.equals("1") ? true : sexInput.equals("0") ? false : null;
                    System.out.print("Nhập ngày sinh (dd/MM/yyyy): ");
                    String dobInput = scanner.nextLine();
                    Date dob = null;
                    if (!dobInput.isEmpty()) {
                        try {
                            dob = new SimpleDateFormat("dd/MM/yyyy").parse(dobInput);
                        } catch (ParseException e) {
                            System.out.println("Ngày sinh không hợp lệ.");
                            break;
                        }
                    }
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
                        System.out.println("Đăng ký tài khoản học viên thành công.");
                    } else {
                        System.out.println("Đăng ký tài khoản học viên thất bại.");
                    }
                    break;
                case 4:
                    System.out.println("Thoát chương trình. Tạm biệt!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng nhập lại.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("========== HỆ THỐNG QUẢN LÝ KHÓA HỌC ==========");
        System.out.println("| 1. Đăng nhập Admin                          |");
        System.out.println("| 2. Đăng nhập Học viên                       |");
        System.out.println("| 3. Đăng ký tài khoản Học viên               |");
        System.out.println("| 4. Thoát                                    |");
        System.out.println("===============================================");
        System.out.print("Nhập lựa chọn của bạn: ");
    }
}