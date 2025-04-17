package ra.edu.business.service.admin;

import ra.edu.business.model.Admin;
import ra.edu.business.service.AppService;

public interface AdminService extends AppService <Admin> {
    Admin login(String username, String password);
}
