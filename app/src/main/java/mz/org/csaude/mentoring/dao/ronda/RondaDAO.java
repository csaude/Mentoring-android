package mz.org.csaude.mentoring.dao.ronda;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.application.MentoringApplication;
import mz.org.csaude.mentoring.base.dao.MentoringBaseDao;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.rondatype.RondaType;
import mz.org.csaude.mentoring.model.tutor.Tutor;

public interface RondaDAO extends MentoringBaseDao<Ronda, Integer> {
    List<Ronda> getAllByHealthFacilityAndMentor(HealthFacility healthFacility, Tutor tutor, MentoringApplication mentoringApplication) throws SQLException;
    List<Ronda> getAllNotSynced() throws SQLException;
    List<Ronda> getAllByRondaType(RondaType rondaType, MentoringApplication mentoringApplication) throws SQLException;
    List<Ronda> getAllByMentor(Tutor tutor, MentoringApplication mentoringApplication) throws SQLException;
}
