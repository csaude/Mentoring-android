package mz.org.csaude.mentoring.dao.programmaticArea;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import mz.org.csaude.mentoring.model.programmaticArea.ProgrammaticArea;

public interface ProgrammaticAreaDAO extends Dao<ProgrammaticArea, Integer> {
    boolean checkProgrammaticAreaExistance(final String uuid) throws SQLException;
}
