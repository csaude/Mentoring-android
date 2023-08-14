package mz.org.csaude.mentoring.dao.indicator;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

import mz.org.csaude.mentoring.model.indicator.Indicator;

public class IndicatorDAOImpl extends BaseDaoImpl<Indicator, Integer> implements IndicatorDAO {

    public IndicatorDAOImpl(Class<Indicator> dataClass) throws SQLException {
        super(dataClass);
    }

    public IndicatorDAOImpl(ConnectionSource connectionSource, Class<Indicator> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public IndicatorDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<Indicator> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }
}
