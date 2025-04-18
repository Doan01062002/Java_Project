package ra.edu.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Validator {
    public static int validateInt(String input){
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.err.println("Lỗi: Giá trị phải là số nguyên hợp lệ!");
        }
        return -1;
    }

    public static String validateString(String input){
        if (input == null || input.trim().isEmpty()){
            System.err.println("Lỗi: Chuỗi không được để trống!");
            return "";
        }
        return input.trim();
    }

    public static String validateEmail(String input){
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!Pattern.matches(emailRegex,input)) {
            System.err.println("Lỗi: Email không hợp lệ!");
            return "";
        }
        return input;
    }

    public static String validateDate(String input){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            sdf.parse(input);
            return input;
        } catch (ParseException e) {
            System.err.println("Lỗi: Ngày không hợp lệ, định dạng đúng là: dd/MM/yyyy");
            return "";
        }
    }

    public static String validatePhone(String input){
        String phoneRegex = "^(0[3|5|7|8|9])+([0-9]{8})$";
        if (!Pattern.matches(phoneRegex,input)){
            System.err.println("Lỗi: Số điện thoại Việt Nam không hợp lệ!");
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
}
