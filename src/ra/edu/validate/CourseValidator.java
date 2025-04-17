package ra.edu.validate;

public class CourseValidator {
    public static String validateStudentId(String input) {
        String studentIdRegex = "^SV\\d{3}$";
        if (input == null || !input.toUpperCase().matches(studentIdRegex)) {
            System.err.println("Lỗi: Mã sinh viên phải có định dạng SV + 3 chữ số (VD: SV001)");
            return "";
        }
        return input;
    }

    public static String validateUsername(String input) {
        String usernameRegex = "^[a-z0-9]{10,45}$";
        if (input == null || !input.matches(usernameRegex)) {
            System.err.println("Lỗi: Username phải dài 10-45 ký tự, viết liền, không dấu, không chứa ký tự đặc biệt và chỉ dùng chữ thường + số!");
            return "";
        }
        return input;
    }

    public static String validatePassword(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.err.println("Lỗi: Mật khẩu không được để trống!");
            return "";
        }

        if (input.contains(" ")) {
            System.err.println("Lỗi: Mật khẩu không được chứa dấu cách!");
            return "";
        }

        // Regex: ít nhất 1 chữ thường, 1 chữ hoa, 1 số, 1 ký tự đặc biệt, dài 10–100 ký tự
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{10,100}$";

        if (!input.matches(passwordRegex)) {
            System.err.println("Lỗi: Mật khẩu phải từ 10–100 ký tự, chứa ít nhất 1 chữ thường, 1 chữ hoa, 1 số và 1 ký tự đặc biệt!");
            return "";
        }
        return input;
    }

}
