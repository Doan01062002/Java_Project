package ra.edu.business.service.student;

import ra.edu.business.model.Student;
import ra.edu.business.service.AppService;

import java.util.List;

public interface StudentService extends AppService<Student> {
    Student login(String username, String password);
    boolean register(Student student);
    List<Student> findByNameOrEmailOrCode(String search);
    List<Student> findAllWithPagination(int page, int pageSize); // Thêm phân trang
    List<Student> findByNameOrEmailOrCodeWithPagination(String search, int page, int pageSize); // Thêm tìm kiếm với phân trang
    List<Student> sortWithPagination(String sortBy, boolean ascending, int page, int pageSize); // Thêm sắp xếp với phân trang
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByStudentCode(String studentCode);
}