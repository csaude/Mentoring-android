package mz.org.csaude.mentoring.dao.programmaticArea;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

import mz.org.csaude.mentoring.base.dao.MentoringBaseDaoImpl;
import mz.org.csaude.mentoring.model.programmaticArea.ProgrammaticArea;

public class ProgrammaticAreaDAOImpl extends MentoringBaseDaoImpl<ProgrammaticArea, Integer> implements ProgrammaticAreaDAO {
    public ProgrammaticAreaDAOImpl(Class<ProgrammaticArea> dataClass) throws SQLException {
        super(dataClass);
    }

    public ProgrammaticAreaDAOImpl(ConnectionSource connectionSource, Class<ProgrammaticArea> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public ProgrammaticAreaDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<ProgrammaticArea> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }
}
