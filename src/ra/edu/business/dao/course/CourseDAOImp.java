package ra.edu.business.dao.course;

import ra.edu.business.config.DatabaseConnection;
import ra.edu.business.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImp implements CourseDAO {
    @Override
    public boolean save(Course course) {
        String query = "{CALL CourseSave(?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, course.getName());
            stmt.setInt(2, course.getDuration());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving course: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Course course) {
        String query = "{CALL CourseUpdate(?, ?, ?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, course.getId());
            stmt.setString(2, course.getName());
            stmt.setInt(3, course.getDuration());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Course course) {
        String query = "{CALL CourseDelete(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, course.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
            return false;
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
                return new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding course: " + e.getMessage());
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
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all courses: " + e.getMessage());
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
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding courses by name: " + e.getMessage());
        }
        return courses;
    }

    @Override
    public List<Course> sortByName(boolean ascending) {
        List<Course> courses = new ArrayList<>();
        String query = "{CALL CourseSortByName(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, ascending ? "ASC" : "DESC");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error sorting courses by name: " + e.getMessage());
        }
        return courses;
    }

    @Override
    public List<Course> sortById(boolean ascending) {
        List<Course> courses = new ArrayList<>();
        String query = "{CALL CourseSortById(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, ascending ? "ASC" : "DESC");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error sorting courses by id: " + e.getMessage());
        }
        return courses;
    }
}