package ra.edu.business.dao.admin;


import ra.edu.business.dao.AppDAO;
import ra.edu.business.model.Admin;

public interface AdminDAO extends AppDAO<Admin> {
    Admin login(String username, String password);
}