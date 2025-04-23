package ra.edu.business.dao.course;

import ra.edu.business.model.Course;

import java.util.List;

public interface CourseDAO {
    boolean save(Course course);
    boolean update(Course course);
    boolean delete(Course course);
    Course findById(int id);
    List<Course> findAll();
    List<Course> findAllWithPagination(int page, int pageSize); // Thêm phân trang
    List<Course> findByName(String name);
    List<Course> findByNameWithPagination(String name, int page, int pageSize); // Thêm tìm kiếm với phân trang
    List<Course> sortWithPagination(String sortBy, boolean ascending, int page, int pageSize); // Thêm sắp xếp với phân trang
    List<Course> getTopCourses(int limit);
    boolean existsByCourseCode(String courseCode); // Kiểm tra mã khóa học trùng
    boolean existsByName(String name); // Kiểm tra tên khóa học trùng
}