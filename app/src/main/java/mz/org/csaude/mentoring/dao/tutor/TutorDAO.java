package mz.org.csaude.mentoring.dao.tutor;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.user.User;

public interface TutorDAO extends Dao<Tutor, Integer> {
   public boolean checkTutorExistance(final String uuid) throws SQLException;

  Tutor getTutorByUuid(final String uuid) throws SQLException;

    Tutor getTutorByUser(User user) throws SQLException;
}
