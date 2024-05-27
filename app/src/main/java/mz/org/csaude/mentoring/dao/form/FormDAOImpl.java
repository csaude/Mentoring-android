package mz.org.csaude.mentoring.dao.form;

import android.app.Application;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.dao.MentoringBaseDaoImpl;
import mz.org.csaude.mentoring.base.databasehelper.MentoringDataBaseHelper;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.programmaticArea.TutorProgrammaticArea;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.util.SyncSatus;

public class FormDAOImpl extends MentoringBaseDaoImpl<Form, Integer> implements FormDAO {

    public FormDAOImpl(Class<Form> dataClass) throws SQLException {
        super(dataClass);
    }

    public FormDAOImpl(ConnectionSource connectionSource, Class<Form> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public FormDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<Form> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public List<Form> getAllOfTutor(Tutor tutor, Application application) throws SQLException {
        QueryBuilder<TutorProgrammaticArea, Integer> tutorPAreaQb =  MentoringDataBaseHelper.getInstance(application.getApplicationContext()).getTutorProgrammaticAreaDAO().queryBuilder();
        tutorPAreaQb.where().eq(TutorProgrammaticArea.COLUMN_TUTOR, tutor.getId());

        QueryBuilder<Form, Integer> formQb =  MentoringDataBaseHelper.getInstance(application.getApplicationContext()).getFormDAO().queryBuilder();
        //formQb.where().eq(Form.COLUMN_PROGRAMMATIC_AREA, new ColumnArg(TutorProgrammaticArea.TABLE_NAME, TutorProgrammaticArea.COLUMN_PROGRAMMATIC_AREA));
        //formQb.join(tutorPAreaQb);

        return formQb.query();
    }

    @Override
    public List<Form> getAllNotSynced() throws SQLException {
        return queryForEq(Form.COLUMN_SYNC_STATUS, SyncSatus.PENDING);
    }

    @Override
    public List<Form> getAllSynced(Application application) throws SQLException {
        return queryForEq(Form.COLUMN_SYNC_STATUS, SyncSatus.SENT);
    }
}
