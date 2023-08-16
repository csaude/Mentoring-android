package mz.org.csaude.mentoring.dao.career;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.model.career.Career;

public class CareerDAOImpl extends BaseDaoImpl<Career, Integer> implements CareerDAO {

    public CareerDAOImpl(Class<Career> dataClass) throws SQLException {
        super(dataClass);
    }

    public CareerDAOImpl(ConnectionSource connectionSource, Class<Career> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public CareerDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<Career> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public boolean checkCareerExistance(String uuid) throws SQLException {
        List<Career> careers = this.queryForEq("uuid", uuid);
        return !careers.isEmpty();
    }
}
