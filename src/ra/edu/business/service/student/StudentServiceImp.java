package ra.edu.business.service.student;


import ra.edu.business.dao.student.StudentDAOImp;
import ra.edu.business.model.Student;

import java.util.List;

public class StudentServiceImp implements StudentService{
    private final StudentDAOImp studentDAO = new StudentDAOImp();

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
