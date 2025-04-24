package ra.edu.business.dao.statistic;

import ra.edu.business.config.DatabaseConnection;
import ra.edu.business.model.Course;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticDAOImp implements StatisticDAO {

    @Override
    public int getTotalCourses() {
        String query = "{CALL GetTotalCourses()}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total courses: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return 0;
    }

    @Override
    public int getTotalStudents() {
        String query = "{CALL GetTotalStudents()}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total students: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return 0;
    }

    @Override
    public List<Course> getCoursesWithStudentCount() {
        List<Course> courses = new ArrayList<>();
        String query = "{CALL GetCoursesWithStudentCount()}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreatedAt(rs.getString("created_at"));
                // Add student count as a custom field if needed
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error getting courses with student count: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return courses;
    }

    @Override
    public List<Course> getTop5CoursesByStudents() {
        List<Course> courses = new ArrayList<>();
        String query = "{CALL GetTop5CoursesByStudents()}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreatedAt(rs.getString("created_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error getting top 5 courses: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return courses;
    }

    @Override
    public List<Course> getCoursesWithMoreThan10Students() {
        List<Course> courses = new ArrayList<>();
        String query = "{CALL GetCoursesWithMoreThan10Students()}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreatedAt(rs.getString("created_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error getting courses with more than 10 students: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return courses;
    }

    @Override
    public int countStudentsByCourse(int courseId) {
        String query = "{CALL CountStudentsByCourse(?)}";
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting students by course: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getConnection());
        }
        return 0;
    }
}