package ra.edu.business.dao.admin;

import ra.edu.business.config.DatabaseConnection;
import ra.edu.business.model.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImp implements AdminDAO {

    @Override
    public boolean save(Admin admin) {
        if (admin == null || admin.getUsername() == null || admin.getPassword() == null) {
            System.out.println("Dữ liệu admin không hợp lệ.");
            return false;
        }
        String query = "{CALL AdminSave(?, ?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getFullName());
            stmt.setString(4, admin.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi lưu admin: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public boolean update(Admin admin) {
        if (admin == null || admin.getId() <= 0) {
            System.out.println("ID admin không hợp lệ.");
            return false;
        }
        String query = "{CALL AdminUpdate(?, ?, ?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, admin.getId());
            stmt.setString(2, admin.getUsername());
            stmt.setString(3, admin.getPassword());
            stmt.setString(4, admin.getFullName());
            stmt.setString(5, admin.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật admin: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public boolean delete(Admin admin) {
        if (admin == null || admin.getId() <= 0) {
            System.out.println("ID admin không hợp lệ.");
            return false;
        }
        String query = "{CALL AdminDelete(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, admin.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa admin: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }

    @Override
    public Admin findById(int id) {
        String query = "{CALL AdminFindById(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm admin theo ID: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return null;
    }

    @Override
    public List<Admin> findAll() {
        List<Admin> admins = new ArrayList<>();
        String query = "{CALL AdminFindAll()}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                admins.add(new Admin(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách admin: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return admins;
    }

    @Override
    public Admin login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            System.out.println("Tên đăng nhập và mật khẩu không được để trống.");
            return null;
        }
        String query = "{CALL AdminLogin(?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đăng nhập admin: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return null;
    }

    @Override
    public boolean lockStudent(int studentId, boolean isActive) {
        String query = "{CALL LockStudent(?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, studentId);
            stmt.setBoolean(2, isActive);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi khóa/mở tài khoản sinh viên: " + e.getMessage());
            return false;
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
    }
}