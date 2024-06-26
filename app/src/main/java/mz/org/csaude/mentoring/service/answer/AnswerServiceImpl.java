package mz.org.csaude.mentoring.service.answer;

import android.app.Application;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.answer.AnswerDAO;
import mz.org.csaude.mentoring.dao.answer.AnswerTypeDAO;
import mz.org.csaude.mentoring.model.answer.Answer;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;

public class AnswerServiceImpl extends BaseServiceImpl<Answer> implements AnswerService {

    AnswerDAO answerDAO;


    public AnswerServiceImpl(Application application) {
        super(application);
    }
    @Override
    public void init(Application application) throws SQLException {
        super.init(application);
        this.answerDAO = getDataBaseHelper().getAnswerDAO();
    }

    @Override
    public Answer save(Answer record) throws SQLException {
        this.answerDAO.create(record);
        return record;
    }

    @Override
    public Answer update(Answer record) throws SQLException {
        this.answerDAO.update(record);
        return record;
    }

    @Override
    public int delete(Answer record) throws SQLException {
        return this.answerDAO.delete(record);
    }

    @Override
    public List<Answer> getAll() throws SQLException {
        return this.answerDAO.queryForAll();
    }

    @Override
    public Answer getById(int id) throws SQLException {
        return this.answerDAO.queryForId(id);
    }

    @Override
    public List<Answer> getAllOfMentorship(Mentorship mentorship) throws SQLException {
        return this.answerDAO.queryForMentorship(mentorship);
    }
}
