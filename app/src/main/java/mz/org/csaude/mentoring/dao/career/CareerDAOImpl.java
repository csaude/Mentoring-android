package mz.org.csaude.mentoring.dao.career;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.model.career.Career;
import mz.org.csaude.mentoring.model.career.CareerType;

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

    @Override
    public Career getCareerByUuid(String uuid) throws SQLException {
        List<Career> careers = this.queryForEq("uuid", uuid);
        return careers.get(0);
    }
    @Override
    public List<Career> findByCareerType(CareerType careerType) throws SQLException {
        List<Career> careers = this.queryForEq("career_type_id", careerType);
        return careers;
    }
}
