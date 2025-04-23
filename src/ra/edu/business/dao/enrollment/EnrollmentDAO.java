package ra.edu.business.dao.enrollment;

import ra.edu.business.dao.AppDAO;
import ra.edu.business.model.Enrollment;

import java.util.List;

public interface EnrollmentDAO extends AppDAO<Enrollment> {
    Enrollment findById(int id);
    List<Enrollment> findByCourseId(int courseId);
    List<Enrollment> findByCourseIdWithPagination(int courseId, int page, int pageSize); // Thêm phân trang
    List<Enrollment> findByStudentId(int studentId);
    int countStudentsByCourse(int courseId);
    boolean existsByStudentIdAndCourseId(int studentId, int courseId);
    boolean deleteById(int id);
}