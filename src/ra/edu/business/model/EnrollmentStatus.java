package ra.edu.business.model;

public enum EnrollmentStatus {
    WAITING, CANCEL, CONFIRM;

    // Helper để chuyển đổi String từ DB sang Enum
    public static EnrollmentStatus fromString(String statusStr) {
        if (statusStr == null) {
            return null;
        }
        try {
            return EnrollmentStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Giá trị trạng thái không hợp lệ từ DB: " + statusStr);
            return null; // Hoặc trả về một giá trị mặc định/ném ngoại lệ tùy logic
        }
    }
}
