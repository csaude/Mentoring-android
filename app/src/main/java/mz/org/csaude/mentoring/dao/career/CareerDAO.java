package mz.org.csaude.mentoring.dao.career;

import com.j256.ormlite.dao.Dao;

import mz.org.csaude.mentoring.model.career.Career;

import java.sql.SQLException;

public interface CareerDAO extends Dao<Career, Integer> {

    public boolean checkCareerExistance(final String uuid) throws SQLException;
    Career getCareerByUuid(final String uuid) throws SQLException;
}
