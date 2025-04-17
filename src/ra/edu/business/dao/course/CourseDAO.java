package ra.edu.business.dao.course;

import ra.edu.business.model.Course;

import java.util.List;

public interface CourseDAO {
    boolean save(Course course);
    boolean update(Course course);
    boolean delete(Course course);
    Course findById(int id);
    List<Course> findAll();
    List<Course> findByName(String name);
    List<Course> sortByName(boolean ascending);
    List<Course> sortById(boolean ascending);
}