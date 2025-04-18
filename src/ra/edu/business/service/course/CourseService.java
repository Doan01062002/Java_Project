package ra.edu.business.service.course;

import ra.edu.business.model.Course;
import ra.edu.business.service.AppService;

import java.util.List;

public interface CourseService extends AppService<Course> {
    List<Course> findByName(String name);
    List<Course> findAllWithPagination(int page, int pageSize); // Thêm phân trang
    List<Course> findByNameWithPagination(String name, int page, int pageSize); // Thêm tìm kiếm với phân trang
    List<Course> sortWithPagination(String sortBy, boolean ascending, int page, int pageSize); // Thêm sắp xếp với phân trang
    List<Course> getTopCourses(int limit);
}