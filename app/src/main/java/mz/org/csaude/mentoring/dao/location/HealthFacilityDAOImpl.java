package mz.org.csaude.mentoring.dao.location;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

import mz.org.csaude.mentoring.model.location.HealthFacility;

public class HealthFacilityDAOImpl extends BaseDaoImpl<HealthFacility, Integer> implements HealthFacilityDAO{


    public HealthFacilityDAOImpl(Class<HealthFacility> dataClass) throws SQLException {
        super(dataClass);
    }

    public HealthFacilityDAOImpl(ConnectionSource connectionSource, Class<HealthFacility> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public HealthFacilityDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<HealthFacility> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }
}
