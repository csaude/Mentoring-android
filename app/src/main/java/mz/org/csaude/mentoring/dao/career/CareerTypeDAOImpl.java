package mz.org.csaude.mentoring.dao.career;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

import mz.org.csaude.mentoring.model.career.CareerType;

public class CareerTypeDAOImpl extends BaseDaoImpl<CareerType, Integer> implements ICareerTypeDAO {

    protected CareerTypeDAOImpl(Class<CareerType> dataClass) throws SQLException {
        super(dataClass);
    }

    protected CareerTypeDAOImpl(ConnectionSource connectionSource, Class<CareerType> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    protected CareerTypeDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<CareerType> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }
}