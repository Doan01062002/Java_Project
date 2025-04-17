package ra.edu.business.dao.student;

import ra.edu.business.dao.AppDAO;
import ra.edu.business.model.Student;

public interface StudentDAO extends AppDAO<Student> {
    Student login(String username, String password);
}
