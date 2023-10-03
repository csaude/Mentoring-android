package mz.org.csaude.mentoring.dao.user;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.model.user.User;


public class UserDaoImpl extends BaseDaoImpl<User, Integer> implements UserDao {

    public UserDaoImpl(Class<User> dataClass) throws SQLException {
        super(dataClass);
    }

    public UserDaoImpl(ConnectionSource connectionSource, Class<User> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public UserDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig<User> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public boolean checkUserExistance(String uuid) throws SQLException {
        List<User> users = this.queryForEq("uuid", uuid);
        return users.isEmpty();
    }
}
