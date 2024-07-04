package mz.org.csaude.mentoring.dao.mentorship;

import android.app.Application;

import com.j256.ormlite.stmt.ColumnArg;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.application.MentoringApplication;
import mz.org.csaude.mentoring.base.dao.MentoringBaseDaoImpl;
import mz.org.csaude.mentoring.base.databasehelper.MentoringDataBaseHelper;
import mz.org.csaude.mentoring.model.evaluationType.EvaluationType;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.location.Cabinet;
import mz.org.csaude.mentoring.model.mentorship.Door;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.util.SyncSatus;

public class MentorshipDAOImpl extends MentoringBaseDaoImpl<Mentorship, Integer> implements MentorshipDAO{

    public MentorshipDAOImpl(Class<Mentorship> dataClass) throws SQLException {
        super(dataClass);
    }

    public MentorshipDAOImpl(ConnectionSource connectionSource, Class<Mentorship> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public MentorshipDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<Mentorship> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public List<Mentorship> getMentorshipByTutor(Application application, String uuidTutor) throws SQLException {

        QueryBuilder<Tutor, Integer> tutorQueryBuilder = MentoringDataBaseHelper.getInstance(application.getApplicationContext()).getTutorDAO().queryBuilder();
        tutorQueryBuilder.where().eq(Tutor.COLUMN_UUID, uuidTutor);

        QueryBuilder<Mentorship, Integer> mentorshipQueryBuilder = MentoringDataBaseHelper.getInstance(application.getApplicationContext()).getMentorshipDAO().queryBuilder();
        mentorshipQueryBuilder.where().eq(Mentorship.COLUMN_TUTOR, new ColumnArg(Tutor.COLUMN_TABLE_NAME, Tutor.COLUMN_ID));
        mentorshipQueryBuilder.join(tutorQueryBuilder);

        return mentorshipQueryBuilder.query();
    }

    @Override
    public List<Mentorship> getAllNotSynced(Application application) throws SQLException {
        QueryBuilder<Mentorship, Integer> mentorshipQueryBuilder = MentoringDataBaseHelper.getInstance(application.getApplicationContext()).getMentorshipDAO().queryBuilder();
        mentorshipQueryBuilder.where().eq(Form.COLUMN_SYNC_STATUS, SyncSatus.PENDING).and().isNotNull(Mentorship.COLUMN_END_DATE);
        return mentorshipQueryBuilder.query();
    }

    @Override
    public List<Mentorship> getAllOfRonda(Ronda ronda, MentoringApplication application) {
        List<Mentorship> mentorshipList = new ArrayList<>();
        try {
            // Build the query to get Sessions for the given Ronda
            QueryBuilder<Session, Integer> sessionQb = MentoringDataBaseHelper.getInstance(application).getSessionDAO().queryBuilder();
            sessionQb.where().eq(Session.COLUMN_RONDA, ronda.getId());

            // Get the list of Sessions
            List<Session> sessionList = sessionQb.query();

            if (!sessionList.isEmpty()) {
                // Build the query to get Mentorship records for the list of Sessions
                QueryBuilder<Mentorship, Integer> mentorshipQb = this.queryBuilder();
                Where<Mentorship, Integer> where = mentorshipQb.where();
                where.in(Mentorship.COLUMN_SESSION, sessionList);

                // Execute the query and return the result
                mentorshipList = mentorshipQb.query();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mentorshipList;
    }

    @Override
    public List<Mentorship> getAllOfSession(Session session) throws SQLException {
        return queryForEq(Mentorship.COLUMN_SESSION, session.getId());
    }
}
