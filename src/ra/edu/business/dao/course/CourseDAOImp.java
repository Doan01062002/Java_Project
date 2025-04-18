package ra.edu.business.dao.course;

import ra.edu.business.config.DatabaseConnection;
import ra.edu.business.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImp implements CourseDAO {

    @Override
    public boolean save(Course course) {
        if (course == null || course.getName() == null || course.getName().trim().isEmpty()) {
            System.out.println("Tên khóa học không được để trống.");
            return false;
        }
        String query = "{CALL CourseSave(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getName());
            stmt.setString(3, course.getDescription());
            stmt.setInt(4, course.getCreatedByAdminId() != null ? course.getCreatedByAdminId() : 0);
            stmt.setInt(5, course.getDuration());
            stmt.setString(6, course.getInstructor());
            stmt.setString(7, course.getCreatedAt());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi lưu khóa học: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public boolean update(Course course) {
        if (course == null || course.getId() <= 0) {
            System.out.println("ID khóa học không hợp lệ.");
            return false;
        }
        String query = "{CALL CourseUpdate(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, course.getId());
            stmt.setString(2, course.getCourseCode());
            stmt.setString(3, course.getName());
            stmt.setString(4, course.getDescription());
            stmt.setInt(5, course.getCreatedByAdminId() != null ? course.getCreatedByAdminId() : 0);
            stmt.setInt(6, course.getDuration());
            stmt.setString(7, course.getInstructor());
            stmt.setString(8, course.getCreatedAt());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật khóa học: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public boolean delete(Course course) {
        if (course == null || course.getId() <= 0) {
            System.out.println("ID khóa học không hợp lệ.");
            return false;
        }
        String query = "{CALL CourseDelete(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, course.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa khóa học: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public Course findById(int id) {
        String query = "{CALL CourseFindById(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setCreatedByAdminId(rs.getInt("created_by_admin_id") == 0 ? null : rs.getInt("created_by_admin_id"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreatedAt(rs.getString("created_at"));
                return course;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khóa học theo ID: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return null;
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String query = "{CALL CourseFindAll()}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setCreatedByAdminId(rs.getInt("created_by_admin_id") == 0 ? null : rs.getInt("created_by_admin_id"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreatedAt(rs.getString("created_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách khóa học: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return courses;
    }

    @Override
    public List<Course> findAllWithPagination(int page, int pageSize) {
        List<Course> courses = new ArrayList<>();
        String query = "{CALL CourseFindAllWithPagination(?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, page);
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setCreatedByAdminId(rs.getInt("created_by_admin_id") == 0 ? null : rs.getInt("created_by_admin_id"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreatedAt(rs.getString("created_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách khóa học với phân trang: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return courses;
    }

    @Override
    public List<Course> findByName(String name) {
        List<Course> courses = new ArrayList<>();
        String query = "{CALL CourseFindByName(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setCreatedByAdminId(rs.getInt("created_by_admin_id") == 0 ? null : rs.getInt("created_by_admin_id"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreatedAt(rs.getString("created_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm khóa học theo tên: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return courses;
    }

    @Override
    public List<Course> findByNameWithPagination(String name, int page, int pageSize) {
        // Tìm kiếm khóa học theo tên với phân trang
        List<Course> courses = new ArrayList<>();
        String query = "{CALL CourseSearchByNameWithPagination(?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, "%" + name + "%");
            stmt.setInt(2, page);
            stmt.setInt(3, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setCreatedByAdminId(rs.getInt("created_by_admin_id") == 0 ? null : rs.getInt("created_by_admin_id"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreatedAt(rs.getString("created_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm khóa học theo tên với phân trang: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return courses;
    }

    @Override
    public List<Course> sortWithPagination(String sortBy, boolean ascending, int page, int pageSize) {
        // Sắp xếp khóa học với phân trang
        List<Course> courses = new ArrayList<>();
        String query = "{CALL CourseSortWithPagination(?, ?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, sortBy);
            stmt.setString(2, ascending ? "ASC" : "DESC");
            stmt.setInt(3, page);
            stmt.setInt(4, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setCreatedByAdminId(rs.getInt("created_by_admin_id") == 0 ? null : rs.getInt("created_by_admin_id"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreatedAt(rs.getString("created_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi sắp xếp khóa học với phân trang: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return courses;
    }

    @Override
    public List<Course> getTopCourses(int limit) {
        List<Course> courses = new ArrayList<>();
        String query = "{CALL GetTopCourses(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setCreatedByAdminId(rs.getInt("created_by_admin_id") == 0 ? null : rs.getInt("created_by_admin_id"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreatedAt(rs.getString("created_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách khóa học phổ biến: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return courses;
    }
}