package ra.edu.business.dao.student;

import ra.edu.business.config.DatabaseConnection;
import ra.edu.business.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImp implements StudentDAO {

    @Override
    public boolean save(Student student) {
        if (student == null || student.getUsername() == null || student.getPassword() == null) {
            System.out.println("Dữ liệu sinh viên không hợp lệ.");
            return false;
        }
        String query = "{CALL StudentSave(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getUsername());
            stmt.setString(3, student.getPassword());
            stmt.setString(4, student.getFullName());
            stmt.setString(5, student.getEmail());
            stmt.setBoolean(6, student.getSex() != null ? student.getSex() : false);
            stmt.setString(7, student.getPhone());
            stmt.setDate(8, student.getDob() != null ? new java.sql.Date(student.getDob().getTime()) : null);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi lưu sinh viên: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public boolean update(Student student) {
        if (student == null || student.getId() <= 0) {
            System.out.println("ID sinh viên không hợp lệ.");
            return false;
        }
        String query = "{CALL StudentUpdate(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, student.getId());
            stmt.setString(2, student.getStudentCode());
            stmt.setString(3, student.getUsername());
            stmt.setString(4, student.getPassword().startsWith("$2a$") ? student.getPassword() : student.getPassword());
            stmt.setString(5, student.getFullName());
            stmt.setString(6, student.getEmail());
            stmt.setBoolean(7, student.getSex() != null ? student.getSex() : false);
            stmt.setString(8, student.getPhone());
            stmt.setDate(9, student.getDob() != null ? new java.sql.Date(student.getDob().getTime()) : null);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sinh viên: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public boolean delete(Student student) {
        if (student == null || student.getId() <= 0) {
            System.out.println("ID sinh viên không hợp lệ.");
            return false;
        }
        String query = "{CALL StudentDelete(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, student.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa sinh viên: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public Student findById(int id) {
        String query = "{CALL StudentFindById(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setDob(rs.getDate("dob"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                student.setActive(rs.getBoolean("is_active"));
                return student;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm sinh viên theo ID: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return null;
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String query = "{CALL StudentFindAll()}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setDob(rs.getDate("dob"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                student.setActive(rs.getBoolean("is_active"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sinh viên: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return students;
    }

    @Override
    public Student login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            System.out.println("Tên đăng nhập và mật khẩu không được để trống.");
            return null;
        }
        String query = "{CALL StudentLogin(?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setDob(rs.getDate("dob"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                student.setActive(rs.getBoolean("is_active"));
                return student;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đăng nhập sinh viên: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return null;
    }

    @Override
    public boolean register(Student student) {
        if (student == null || student.getUsername() == null || student.getPassword() == null) {
            System.out.println("Dữ liệu đăng ký không hợp lệ.");
            return false;
        }
        String query = "{CALL StudentRegister(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getUsername());
            stmt.setString(3, student.getPassword());
            stmt.setString(4, student.getFullName());
            stmt.setString(5, student.getEmail());
            stmt.setBoolean(6, student.getSex() != null ? student.getSex() : false);
            stmt.setString(7, student.getPhone());
            stmt.setDate(8, student.getDob() != null ? new java.sql.Date(student.getDob().getTime()) : null);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi đăng ký sinh viên: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public List<Student> findByNameOrEmailOrCode(String search) {
        List<Student> students = new ArrayList<>();
        String query = "{CALL StudentFindByNameOrEmailOrCode(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, "%" + search + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setDob(rs.getDate("dob"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                student.setActive(rs.getBoolean("is_active"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm sinh viên: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return students;
    }

    @Override
    public List<Student> findAllWithPagination(int page, int pageSize) {
        // Lấy danh sách sinh viên với phân trang
        List<Student> students = new ArrayList<>();
        String query = "{CALL StudentFindAllWithPagination(?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, page);
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setDob(rs.getDate("dob"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                student.setActive(rs.getBoolean("is_active"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sinh viên với phân trang: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return students;
    }

    @Override
    public List<Student> findByNameOrEmailOrCodeWithPagination(String search, int page, int pageSize) {
        // Tìm kiếm sinh viên theo tên, email hoặc mã với phân trang
        List<Student> students = new ArrayList<>();
        String query = "{CALL StudentSearchByNameOrEmailOrCodeWithPagination(?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, "%" + search + "%");
            stmt.setInt(2, page);
            stmt.setInt(3, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setDob(rs.getDate("dob"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                student.setActive(rs.getBoolean("is_active"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm sinh viên với phân trang: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return students;
    }

    @Override
    public List<Student> sortWithPagination(String sortBy, boolean ascending, int page, int pageSize) {
        // SắpHH xếp sinh viên với phân trang
        List<Student> students = new ArrayList<>();
        String query = "{CALL StudentSortWithPagination(?, ?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, sortBy);
            stmt.setString(2, ascending ? "ASC" : "DESC");
            stmt.setInt(3, page);
            stmt.setInt(4, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setDob(rs.getDate("dob"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                student.setActive(rs.getBoolean("is_active"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi sắp xếp sinh viên với phân trang: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return students;
    }
}