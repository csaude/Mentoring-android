package mz.org.csaude.mentoring.dao.programmaticArea;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.model.career.Career;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.programmaticArea.ProgrammaticArea;

public class ProgrammaticAreaDAOImpl extends BaseDaoImpl<ProgrammaticArea, Integer> implements ProgrammaticAreaDAO {
    public ProgrammaticAreaDAOImpl(Class<ProgrammaticArea> dataClass) throws SQLException {
        super(dataClass);
    }

    public ProgrammaticAreaDAOImpl(ConnectionSource connectionSource, Class<ProgrammaticArea> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public ProgrammaticAreaDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<ProgrammaticArea> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public boolean checkProgrammaticAreaExistance(String uuid) throws SQLException{

        List<ProgrammaticArea> programmaticAreas = this.queryForEq("uuid", uuid);
        return programmaticAreas.isEmpty();
    }
}
