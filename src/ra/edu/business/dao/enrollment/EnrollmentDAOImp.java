package ra.edu.business.dao.enrollment;

import ra.edu.business.config.DatabaseConnection;
import ra.edu.business.model.Enrollment;
import ra.edu.business.model.EnrollmentStatus;
import ra.edu.business.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAOImp implements EnrollmentDAO {

    @Override
    public boolean save(Enrollment enrollment) {
        if (enrollment == null || enrollment.getStudentRefId() <= 0 || enrollment.getCourseRefId() <= 0) {
            System.out.println("Dữ liệu đăng ký không hợp lệ.");
            return false;
        }
        String query = "{CALL EnrollmentSave(?, ?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, enrollment.getEnrollmentCode());
            stmt.setInt(2, enrollment.getStudentRefId());
            stmt.setInt(3, enrollment.getCourseRefId());
            stmt.setString(4, enrollment.getStatus().name());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi lưu đăng ký: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public boolean update(Enrollment enrollment) {
        if (enrollment == null || enrollment.getId() <= 0) {
            System.out.println("ID đăng ký không hợp lệ.");
            return false;
        }
        String query = "{CALL EnrollmentUpdate(?, ?, ?, ?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, enrollment.getId());
            stmt.setString(2, enrollment.getEnrollmentCode());
            stmt.setInt(3, enrollment.getStudentRefId());
            stmt.setInt(4, enrollment.getCourseRefId());
            stmt.setInt(5, enrollment.getAdminRefId() != null ? enrollment.getAdminRefId() : 0);
            stmt.setString(6, enrollment.getStatus().name());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật đăng ký: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public boolean delete(Enrollment enrollment) {
        if (enrollment == null || enrollment.getId() <= 0) {
            System.out.println("ID đăng ký không hợp lệ.");
            return false;
        }
        String query = "{CALL EnrollmentDelete(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, enrollment.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa đăng ký: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public Enrollment findById(int id) {
        String query = "{CALL EnrollmentFindById(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setId(rs.getInt("id"));
                enrollment.setEnrollmentCode(rs.getString("enrollment_code"));
                enrollment.setStudentRefId(rs.getInt("student_ref_id"));
                enrollment.setCourseRefId(rs.getInt("course_ref_id"));
                enrollment.setAdminRefId(rs.getInt("admin_ref_id") == 0 ? null : rs.getInt("admin_ref_id"));
                enrollment.setRegistrationDate(rs.getTimestamp("registration_date"));
                enrollment.setStatus(EnrollmentStatus.fromString(rs.getString("status")));
                return enrollment;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm đăng ký theo ID: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return null;
    }

    @Override
    public List<Enrollment> findAll() {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "{CALL EnrollmentFindAll()}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setId(rs.getInt("id"));
                enrollment.setEnrollmentCode(rs.getString("enrollment_code"));
                enrollment.setStudentRefId(rs.getInt("student_ref_id"));
                enrollment.setCourseRefId(rs.getInt("course_ref_id"));
                enrollment.setAdminRefId(rs.getInt("admin_ref_id") == 0 ? null : rs.getInt("admin_ref_id"));
                enrollment.setRegistrationDate(rs.getTimestamp("registration_date"));
                enrollment.setStatus(EnrollmentStatus.fromString(rs.getString("status")));
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách đăng ký: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return enrollments;
    }

    @Override
    public List<Enrollment> findByCourseId(int courseId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "{CALL EnrollmentFindByCourseId(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setId(rs.getInt("id"));
                enrollment.setEnrollmentCode(rs.getString("enrollment_code"));
                enrollment.setStudentRefId(rs.getInt("student_ref_id"));
                enrollment.setCourseRefId(rs.getInt("course_ref_id"));
                enrollment.setAdminRefId(rs.getInt("admin_ref_id") == 0 ? null : rs.getInt("admin_ref_id"));
                enrollment.setRegistrationDate(rs.getTimestamp("registration_date"));
                enrollment.setStatus(EnrollmentStatus.fromString(rs.getString("status")));
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm đăng ký theo ID khóa học: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return enrollments;
    }

    @Override
    public List<Enrollment> findByCourseIdWithPagination(int courseId, int page, int pageSize) {
        // Lấy danh sách đăng ký theo khóa học với phân trang
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "{CALL EnrollmentFindByCourseIdWithPagination(?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, courseId);
            stmt.setInt(2, page);
            stmt.setInt(3, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setId(rs.getInt("id"));
                enrollment.setEnrollmentCode(rs.getString("enrollment_code"));
                enrollment.setStudentRefId(rs.getInt("student_ref_id"));
                enrollment.setCourseRefId(rs.getInt("course_ref_id"));
                enrollment.setAdminRefId(rs.getInt("admin_ref_id") == 0 ? null : rs.getInt("admin_ref_id"));
                enrollment.setRegistrationDate(rs.getTimestamp("registration_date"));
                enrollment.setStatus(EnrollmentStatus.fromString(rs.getString("status")));
                // Lưu thông tin sinh viên
                Student student = new Student();
                student.setFullName(rs.getString("full_name"));
                student.setStudentCode(rs.getString("student_code"));
                student.setEmail(rs.getString("email"));
                enrollment.setStudent(student);
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm đăng ký theo ID khóa học với phân trang: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return enrollments;
    }

    @Override
    public List<Enrollment> findByStudentId(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "{CALL EnrollmentFindByStudentId(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setId(rs.getInt("id"));
                enrollment.setEnrollmentCode(rs.getString("enrollment_code"));
                enrollment.setStudentRefId(rs.getInt("student_ref_id"));
                enrollment.setCourseRefId(rs.getInt("course_ref_id"));
                enrollment.setAdminRefId(rs.getInt("admin_ref_id") == 0 ? null : rs.getInt("admin_ref_id"));
                enrollment.setRegistrationDate(rs.getTimestamp("registration_date"));
                enrollment.setStatus(EnrollmentStatus.fromString(rs.getString("status")));
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm đăng ký theo ID sinh viên: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return enrollments;
    }

    @Override
    public int countStudentsByCourse(int courseId) {
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_enrollments_count_students_by_course(?)}")) {
            cs.setInt(1, courseId);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return 0;
    }

    @Override
    public boolean existsByStudentIdAndCourseId(int studentId, int courseId) {
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_enrollment_exists_by_student_and_course(?,?,?)}")) {
            cs.setInt(1, studentId);
            cs.setInt(2, courseId);
            cs.registerOutParameter(3, Types.BOOLEAN);
            cs.execute();
            return cs.getBoolean(3);
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra đăng ký: " + e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_enrollments_delete_by_id(?)}")) {
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa đăng ký: " + e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }
}