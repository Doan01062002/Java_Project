package ra.edu.business.service.enrollment;

import ra.edu.business.model.Enrollment;
import ra.edu.business.service.AppService;

import java.util.List;

public interface EnrollmentService extends AppService<Enrollment> {
    Enrollment findById(int id);
    List<Enrollment> findByCourseId(int courseId);
    List<Enrollment> findByCourseIdWithPagination(int courseId, int page, int pageSize); // Thêm phân trang
    List<Enrollment> findByStudentId(int studentId);
    boolean existsByStudentIdAndCourseId(int studentId, int courseId);
    boolean deleteById(int id);
}