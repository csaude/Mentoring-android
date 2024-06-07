package mz.org.csaude.mentoring.dao.session;

import java.util.List;

import mz.org.csaude.mentoring.base.application.MentoringApplication;
import mz.org.csaude.mentoring.base.dao.MentoringBaseDao;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.model.tutored.Tutored;

public interface SessionDAO extends MentoringBaseDao<Session, Integer> {
    List<Session> queryForAllOfRondaAndMentee(Ronda currRonda, Tutored selectedMentee, MentoringApplication application);
    List<Session> queryForAllOfRondaAndMentor(Ronda currRonda, Tutored selectedMentor);
    List<Session> queryForAllOfRonda(Ronda currRonda);
    List<Session> queryForAllOfMentee(Tutored selectedMentee);
    List<Session> queryForAllOfMentor(Tutored selectedMentor);
}
