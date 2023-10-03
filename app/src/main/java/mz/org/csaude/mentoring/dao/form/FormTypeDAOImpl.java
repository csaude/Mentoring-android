package mz.org.csaude.mentoring.dao.form;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.model.form.FormType;

public class FormTypeDAOImpl extends BaseDaoImpl<FormType, Integer> implements FormTypeDAO {

    public FormTypeDAOImpl(Class<FormType> dataClass) throws SQLException {
        super(dataClass);
    }

    public FormTypeDAOImpl(ConnectionSource connectionSource, Class<FormType> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public FormTypeDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<FormType> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public boolean checkFormTypeExistance(String uuid) throws SQLException {

        List<FormType> formTypes = this.queryForEq("uuid", uuid);
        return formTypes.isEmpty();
    }
}
