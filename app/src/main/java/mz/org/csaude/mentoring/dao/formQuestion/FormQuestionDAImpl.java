package mz.org.csaude.mentoring.dao.formQuestion;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

import mz.org.csaude.mentoring.dao.form.FormTargetDAO;
import mz.org.csaude.mentoring.model.formQuestion.FormQuestion;

public class FormQuestionDAImpl extends BaseDaoImpl<FormQuestion, Integer> implements FormQuestionDAO {

    public FormQuestionDAImpl(Class<FormQuestion> dataClass) throws SQLException {
        super(dataClass);
    }

    public FormQuestionDAImpl(ConnectionSource connectionSource, Class<FormQuestion> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public FormQuestionDAImpl(ConnectionSource connectionSource, DatabaseTableConfig<FormQuestion> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }
}
