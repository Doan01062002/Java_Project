package ra.edu.business.service.enrollment;

import ra.edu.business.dao.enrollment.EnrollmentDAOImp;
import ra.edu.business.model.Enrollment;

import java.util.List;

public class EnrollmentServiceImp implements EnrollmentService {
    private final EnrollmentDAOImp enrollmentDAO = new EnrollmentDAOImp();

    @Override
    public boolean save(Enrollment enrollment) {
        return enrollmentDAO.save(enrollment);
    }

    @Override
    public boolean update(Enrollment enrollment) {
        return enrollmentDAO.update(enrollment);
    }

    @Override
    public boolean delete(Enrollment enrollment) {
        return enrollmentDAO.delete(enrollment);
    }

    @Override
    public Enrollment findById(int id) {
        return enrollmentDAO.findById(id);
    }

    @Override
    public List<Enrollment> findAll() {
        return enrollmentDAO.findAll();
    }

    @Override
    public List<Enrollment> findByCourseId(int courseId) {
        return enrollmentDAO.findByCourseId(courseId);
    }

    @Override
    public List<Enrollment> findByCourseIdWithPagination(int courseId, int page, int pageSize) {
        return enrollmentDAO.findByCourseIdWithPagination(courseId, page, pageSize);
    }

    @Override
    public List<Enrollment> findByStudentId(int studentId) {
        return enrollmentDAO.findByStudentId(studentId);
    }

    @Override
    public boolean existsByStudentIdAndCourseId(int studentId, int courseId) {
        return enrollmentDAO.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public boolean deleteById(int id) {
        return enrollmentDAO.deleteById(id);
    }
}