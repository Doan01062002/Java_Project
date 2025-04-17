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
}
