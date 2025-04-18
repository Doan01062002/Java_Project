package ra.edu.business.service.course;

import ra.edu.business.dao.course.CourseDAO;
import ra.edu.business.dao.course.CourseDAOImp;
import ra.edu.business.model.Course;

import java.util.List;

public class CourseServiceImp implements CourseService {
    private final CourseDAO courseDAO = new CourseDAOImp();

    @Override
    public boolean save(Course course) {
        return courseDAO.save(course);
    }

    @Override
    public boolean update(Course course) {
        return courseDAO.update(course);
    }

    @Override
    public boolean delete(Course course) {
        return courseDAO.delete(course);
    }

    @Override
    public Course findById(int id) {
        return courseDAO.findById(id);
    }

    @Override
    public List<Course> findAll() {
        return courseDAO.findAll();
    }

    @Override
    public List<Course> findByName(String name) {
        return courseDAO.findByName(name);
    }

    @Override
    public List<Course> findAllWithPagination(int page, int pageSize) {
        return courseDAO.findAllWithPagination(page, pageSize);
    }

    @Override
    public List<Course> findByNameWithPagination(String name, int page, int pageSize) {
        return courseDAO.findByNameWithPagination(name, page, pageSize);
    }

    @Override
    public List<Course> sortWithPagination(String sortBy, boolean ascending, int page, int pageSize) {
        return courseDAO.sortWithPagination(sortBy, ascending, page, pageSize);
    }

    @Override
    public List<Course> getTopCourses(int limit) {
        return courseDAO.getTopCourses(limit);
    }
}