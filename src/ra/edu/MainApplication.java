package ra.edu;

import ra.edu.business.model.Admin;
import ra.edu.business.model.Student;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.business.service.student.StudentServiceImp;
import ra.edu.presentation.AdminUI;
import ra.edu.presentation.StudentUI;
import ra.edu.validate.Validator;

import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AdminServiceImp adminService = new AdminServiceImp();
        StudentServiceImp studentService = new StudentServiceImp();

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
                        studentUI.displayMenuStudent(scanner);
                    } else {
                        System.out.println("Tên đăng nhập hoặc mật khẩu không đúng.");
                    }
                    break;
                case 0:
                    System.out.println("Tạm biệt!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n--- Menu Chính ---");
        System.out.println("1. Đăng nhập Admin");
        System.out.println("2. Đăng nhập Học viên");
        System.out.println("0. Thoát");
        System.out.print("Nhập lựa chọn của bạn: ");
    }
}