package mz.org.csaude.mentoring.dao.ronda;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.dao.MentoringBaseDao;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.ronda.RondaMentee;

public interface RondaMenteeDAO extends MentoringBaseDao<RondaMentee, Integer> {

    List<RondaMentee> getAllOfRonda(Ronda ronda) throws SQLException;
}
