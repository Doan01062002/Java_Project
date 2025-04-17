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
    public List<Course> sortByName(boolean ascending) {
        return courseDAO.sortByName(ascending);
    }

    @Override
    public List<Course> sortById(boolean ascending) {
        return courseDAO.sortById(ascending);
    }
}