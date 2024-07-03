package mz.org.csaude.mentoring.service.user;

import java.sql.SQLException;

import mz.org.csaude.mentoring.base.service.BaseService;
import mz.org.csaude.mentoring.model.user.User;

public interface UserService extends BaseService<User> {

    User login(User user) throws SQLException;
    User savedOrUpdateUser(User user) throws SQLException;
    User getByUserNameAndPassword(User currentUser);

    void updatePassword(User relatedRecord) throws SQLException;

    User getCurrentUser() throws SQLException;
}
