package ra.edu.business.dao.student;

import ra.edu.business.config.DatabaseConnection;
import ra.edu.business.model.Student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImp implements StudentDAO {
    @Override
    public boolean delete(Student student) {
        return false;
    }

    @Override
    public boolean save(Student student) {
        return false;
    }

    @Override
    public boolean update(Student student) {
        return false;
    }

    @Override
    public Student findById(int id) {
        return null;
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        return students;
    }

    @Override
    public Student login(String username, String password) {
        String query = "{CALL StudentLogin(?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setString(1, username);
            callableStatement.setString(2, password);
            ResultSet resultSet = callableStatement.executeQuery();
            if (resultSet.next()) {
                return new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error during student login: " + e.getMessage());
        }
        return null;
    }
}