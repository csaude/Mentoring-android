package mz.org.csaude.mentoring.dao.answer;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

import mz.org.csaude.mentoring.model.answer.Answer;
import mz.org.csaude.mentoring.model.answer.AnswerType;

public class AnswerTypeDAOImpl extends BaseDaoImpl<AnswerType, Integer> implements AnswerTypeDAO{
    public AnswerTypeDAOImpl(Class<AnswerType> dataClass) throws SQLException {
        super(dataClass);
    }

    public AnswerTypeDAOImpl(ConnectionSource connectionSource, Class<AnswerType> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public AnswerTypeDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<AnswerType> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }
}
