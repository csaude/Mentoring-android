package mz.org.csaude.mentoring.dao.mentorship;

import android.app.Application;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.application.MentoringApplication;
import mz.org.csaude.mentoring.base.dao.MentoringBaseDao;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.session.Session;

public interface MentorshipDAO extends MentoringBaseDao<Mentorship, Integer> {
     List<Mentorship> getMentorshipByTutor(Application application, String uuidTutor)throws SQLException;
     List<Mentorship> getAllNotSynced(Application application) throws SQLException;

    List<Mentorship> getAllOfRonda(Ronda ronda, MentoringApplication application);

    List<Mentorship> getAllOfSession(Session session) throws SQLException;

}
