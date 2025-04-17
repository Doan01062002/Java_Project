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
    public boolean save(Student student) {
        return false;
    }

    @Override
    public boolean update(Student student) {
        return false;
    }

    @Override
    public boolean delete(Student student) {
        return false;
    }

    @Override
    public Student findById(int id) {
        return null;
    }

    @Override
    public List<Student> findAll() {
        return List.of();
    }
}
