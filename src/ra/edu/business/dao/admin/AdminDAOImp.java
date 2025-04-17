package ra.edu.business.dao.admin;

import ra.edu.business.config.DatabaseConnection;
import ra.edu.business.model.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImp implements AdminDAO {

    @Override
    public boolean save(Admin admin) {
        return false;
    }

    @Override
    public boolean update(Admin admin) {
        return false;
    }

    @Override
    public boolean delete(Admin admin) {
        return false;
    }

    @Override
    public Admin login(String username, String password) {
        String query = "{CALL AdminLogin(?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setString(1, username);
            callableStatement.setString(2, password);
            ResultSet resultSet = callableStatement.executeQuery();
            if (resultSet.next()) {
                return new Admin(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error during admin login: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Admin findById(int id) {
        return null;
    }

    @Override
    public List<Admin> findAll() {
        List<Admin> admins = new ArrayList<>();
        return admins;
    }
}