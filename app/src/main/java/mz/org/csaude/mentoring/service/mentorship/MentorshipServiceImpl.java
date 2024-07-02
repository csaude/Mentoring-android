package mz.org.csaude.mentoring.service.mentorship;

import android.app.Application;

import com.j256.ormlite.misc.TransactionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.answer.AnswerDAO;
import mz.org.csaude.mentoring.dao.mentorship.MentorshipDAO;
import mz.org.csaude.mentoring.dao.ronda.RondaDAO;
import mz.org.csaude.mentoring.dao.session.SessionDAO;
import mz.org.csaude.mentoring.dao.session.SessionStatusDAO;
import mz.org.csaude.mentoring.dao.tutored.TutoredDao;
import mz.org.csaude.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.csaude.mentoring.dto.session.SessionDTO;
import mz.org.csaude.mentoring.dto.session.SessionStatusDTO;
import mz.org.csaude.mentoring.model.answer.Answer;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.model.session.SessionStatus;

public class MentorshipServiceImpl extends BaseServiceImpl<Mentorship> implements MentorshipService {


    MentorshipDAO mentorshipDAO;
    SessionDAO sessionDAO;
    SessionStatusDAO sessionStatusDAO;

    TutoredDao tutoredDao;

    RondaDAO rondaDAO;
    AnswerDAO answerDAO;

    public MentorshipServiceImpl(Application application) {
        super(application);
    }

    @Override
    public void init(Application application){
        try {
            super.init(application);
            this.mentorshipDAO = getDataBaseHelper().getMentorshipDAO();
            this.sessionDAO = getDataBaseHelper().getSessionDAO();
            this.sessionStatusDAO = getDataBaseHelper().getSessionStatusDAO();
            this.answerDAO = getDataBaseHelper().getAnswerDAO();
            this.rondaDAO = getDataBaseHelper().getRondaDAO();
            this.tutoredDao = getDataBaseHelper().getTutoredDao();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Mentorship save(Mentorship record) throws SQLException {
        TransactionManager.callInTransaction(getDataBaseHelper().getConnectionSource(), (Callable<Void>) () -> {
            if (record.getSession().getRonda().isRondaZero()) {
                tutoredDao.update(record.getTutored());
            }
            if(record.getSession().getRonda().isRondaCompleted()) {
                rondaDAO.update(record.getSession().getRonda());
            }
            if (record.getSession().getRonda().isRondaZero()) {
                sessionDAO.create(record.getSession());
            } else if (record.getSession().isCompleted()) {
                sessionDAO.update(record.getSession());
            }

            mentorshipDAO.create(record);
            for (Answer answer : record.getAnswers()) {
                answerDAO.create(answer);
            }

            return null;
        });

        return record;
    }

    @Override
    public Mentorship update(Mentorship record) throws SQLException {
        mentorshipDAO.update(record);
        return record;
    }

    @Override
    public int delete(Mentorship record) throws SQLException {
        return this.mentorshipDAO.delete(record);
    }

    @Override
    public List<Mentorship> getAll() throws SQLException {
        return this.mentorshipDAO.queryForAll();
    }

    @Override
    public Mentorship getById(int id) throws SQLException {
        return this.mentorshipDAO.queryForId(id);
    }

    @Override
    public List<Mentorship> getMentorshipByTutor(String uuidTutor) throws SQLException {
        return this.mentorshipDAO.getMentorshipByTutor(application ,uuidTutor);
    }

    @Override
    public void saveOrUpdateMentorships(List<MentorshipDTO> mentorshipDTOS) throws SQLException {
        for (MentorshipDTO mentorshipDTO: mentorshipDTOS) {
            this.saveOrUpdateMentorship(mentorshipDTO);
        }

    }

    @Override
    public Mentorship saveOrUpdateMentorship(MentorshipDTO mentorshipDTO) throws SQLException {
        SessionDTO sessionDTO = mentorshipDTO.getSession();

        SessionStatusDTO sessionStatusDTO = sessionDTO.getSessionStatus();
        SessionStatus ss = this.sessionStatusDAO.getByUuid(sessionStatusDTO.getUuid());
        SessionStatus sessionStatus = sessionStatusDTO.getSessionStatus();
        if(ss!=null) {
            sessionStatus.setId(ss.getId());
        }
        this.sessionStatusDAO.createOrUpdate(sessionStatus);

        Session s = this.sessionDAO.getByUuid(sessionDTO.getUuid());
        Session session = sessionDTO.getSession();
        if(s!=null) {
            session.setId(s.getId());
        }
        this.sessionDAO.createOrUpdate(session);

        Mentorship m = this.mentorshipDAO.getByUuid(mentorshipDTO.getUuid());
        Mentorship mentorship = mentorshipDTO.getMentorship();
        if(m!=null) {
            mentorship.setId(m.getId());
        }
        this.mentorshipDAO.createOrUpdate(mentorship);

        return mentorship;
    }

    @Override
    public List<Mentorship> getAllNotSynced(Application application) throws SQLException {
        return this.mentorshipDAO.getAllNotSynced(application);
    }

    @Override
    public List<Mentorship> getAllOfRonda(Ronda ronda) throws SQLException {
        List<Mentorship> mentorships = this.mentorshipDAO.getAllOfRonda(ronda, getApplication());
        for (Mentorship mentorship : mentorships) {
            mentorship.getSession().setRonda(this.rondaDAO.queryForId(mentorship.getSession().getRonda().getId()));
        }
        return mentorships;
    }

    @Override
    public List<Mentorship> getAllOfSession(Session session) throws SQLException {
        List<Mentorship> mentorships = mentorshipDAO.getAllOfSession(session);
        for (Mentorship mentorship : mentorships) {
            mentorship.getSession().setRonda(this.rondaDAO.queryForId(mentorship.getSession().getRonda().getId()));
        }
        return mentorships;
    }

}
