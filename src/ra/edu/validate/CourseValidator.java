package ra.edu.validate;

public class CourseValidator {
    public static String validateCourseId(String input) {
        String courseIdRegex = "^KSB\\d{3}$";
        if (input == null || !input.toUpperCase().matches(courseIdRegex)) {
            System.err.println("Lỗi: Mã khóa học phải có định dạng KSB + 3 chữ số (VD: KSB001)");
            return "KSB";
        }
        return input;
    }
}
