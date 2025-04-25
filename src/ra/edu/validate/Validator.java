package ra.edu.validate;

import ra.edu.utils.UIUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Validator {
    public static int validateInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                UIUtils.showError("Giá trị phải là số nguyên hợp lệ!");
            }
        }
    }

    public static String validateString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            UIUtils.showError("Chuỗi không được để trống!");
        }
    }

    public static String validateEmail(Scanner scanner, String prompt) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (Pattern.matches(emailRegex, input)) {
                return input;
            }
            UIUtils.showError("Email không hợp lệ!");
        }
    }

    public static String validateDate(Scanner scanner, String prompt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return input; // Cho phép để trống
            }
            try {
                sdf.parse(input);
                return input;
            } catch (ParseException e) {
                UIUtils.showError("Ngày không hợp lệ, định dạng đúng là: dd/MM/yyyy");
            }
        }
    }

    public static String validatePhone(Scanner scanner, String prompt) {
        String phoneRegex = "^(0[3|5|7|8|9])+([0-9]{8})$";
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return input; // Cho phép để trống
            }
            if (Pattern.matches(phoneRegex, input)) {
                return input;
            }
            UIUtils.showError("Số điện thoại Việt Nam không hợp lệ!");
        }
    }

    public static String validatePassword(Scanner scanner, String prompt) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{10,100}$";
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                UIUtils.showError("Mật khẩu không được để trống!");
                continue;
            }
            if (input.contains(" ")) {
                UIUtils.showError("Mật khẩu không được chứa dấu cách!");
                continue;
            }
            if (input.matches(passwordRegex)) {
                return input;
            }
            UIUtils.showError("Mật khẩu phải từ 10–100 ký tự, chứa ít nhất 1 chữ thường, 1 chữ hoa, 1 số và 1 ký tự đặc biệt!");
        }
    }

    public static String validateStudentId(Scanner scanner, String prompt) {
        String studentIdRegex = "^SV\\d{3}$";
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.matches(studentIdRegex)) {
                return input;
            }
            UIUtils.showError("Mã sinh viên phải có định dạng SV + 3 chữ số (VD: SV001)");
        }
    }

    public static String validateCourseId(Scanner scanner, String prompt) {
        String courseIdRegex = "^KSB\\d{3}$";
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.matches(courseIdRegex)) {
                return input;
            }
            UIUtils.showError("Mã khóa học phải có định dạng KSB + 3 chữ số (VD: KSB001)");
        }
    }

    public static String validateUsername(Scanner scanner, String prompt) {
        String usernameRegex = "^[a-z0-9]{10,45}$";
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches(usernameRegex)) {
                return input;
            }
            UIUtils.showError("Username phải dài 10-45 ký tự, viết liền, không dấu, chỉ dùng chữ thường và số!");
        }
    }

    // Validate name (only letters and spaces, minimum 2 characters)
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]{2,}$");
    }

    // Validate email using a regex pattern
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email != null && Pattern.matches(emailRegex, email);
    }

    // Validate phone number (only digits, 10-15 characters)
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^(0[3|5|7|8|9])+([0-9]{8})$");
    }
}