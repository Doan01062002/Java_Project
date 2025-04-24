package ra.edu.business.service.student;


import ra.edu.business.dao.student.StudentDAOImp;
import ra.edu.business.model.Student;

import java.util.List;

public class StudentServiceImp implements StudentService{
    private final StudentDAOImp studentDAO = new StudentDAOImp();
    private final List<Student> students = findAll();

    @Override
    public Student login(String username, String password) {
        return studentDAO.login(username, password);
    }

    @Override
    public boolean register(Student student) {
        return studentDAO.register(student);
    }

    @Override
    public List<Student> findByNameOrEmailOrCode(String search) {
        return studentDAO.findByNameOrEmailOrCode(search);
    }

    @Override
    public List<Student> findAllWithPagination(int page, int pageSize) {
        return studentDAO.findAllWithPagination(page, pageSize);
    }

    @Override
    public List<Student> findByNameOrEmailOrCodeWithPagination(String search, int page, int pageSize) {
        return studentDAO.findByNameOrEmailOrCodeWithPagination(search, page, pageSize);
    }

    @Override
    public List<Student> sortWithPagination(String sortBy, boolean ascending, int page, int pageSize) {
        return studentDAO.sortWithPagination(sortBy, ascending, page, pageSize);
    }

    @Override
    public boolean existsByUsername(String username) {
        return students.stream().anyMatch(student -> student.getUsername().equalsIgnoreCase(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return students.stream().anyMatch(student -> student.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public boolean existsByPhone(String phone) {
        return students.stream().anyMatch(student -> student.getPhone().equals(phone));
    }

    @Override
    public boolean existsByStudentCode(String studentCode) {
        return students.stream().anyMatch(student -> student.getStudentCode().equalsIgnoreCase(studentCode));
    }

    @Override
    public boolean save(Student student) {
        return studentDAO.save(student);
    }

    @Override
    public boolean update(Student student) {
        return studentDAO.update(student);
    }

    @Override
    public boolean delete(Student student) {
        return studentDAO.delete(student);
    }

    @Override
    public Student findById(int id) {
        return studentDAO.findById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentDAO.findAll();
    }
}
