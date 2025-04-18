package ra.edu.business.dao.student;

import ra.edu.business.dao.AppDAO;
import ra.edu.business.model.Student;

import java.util.List;

public interface StudentDAO extends AppDAO<Student> {
    Student login(String username, String password);
    boolean register(Student student);
    List<Student> findByNameOrEmailOrCode(String search);
    List<Student> findAllWithPagination(int page, int pageSize); // Thêm phân trang
    List<Student> findByNameOrEmailOrCodeWithPagination(String search, int page, int pageSize); // Thêm tìm kiếm với phân trang
    List<Student> sortWithPagination(String sortBy, boolean ascending, int page, int pageSize); // Thêm sắp xếp với phân trang
}