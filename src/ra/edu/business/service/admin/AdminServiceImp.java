package ra.edu.business.service.admin;

import ra.edu.business.dao.admin.AdminDAO;
import ra.edu.business.dao.admin.AdminDAOImp;
import ra.edu.business.model.Admin;

import java.util.List;

public class AdminServiceImp implements AdminService {
    private final AdminDAO adminDAO;

    public AdminServiceImp() {
        this.adminDAO = new AdminDAOImp();
    }

    @Override
    public boolean save(Admin admin) {
        return adminDAO.save(admin);
    }

    @Override
    public boolean update(Admin admin) {
        return adminDAO.update(admin);
    }

    @Override
    public boolean delete(Admin admin) {
        return adminDAO.delete(admin);
    }

    @Override
    public Admin findById(int id) {
        return adminDAO.findById(id);
    }

    @Override
    public Admin login(String username, String password) {
        return adminDAO.login(username, password);
    }

    @Override
    public List<Admin> findAll() {
        return adminDAO.findAll();
    }
}