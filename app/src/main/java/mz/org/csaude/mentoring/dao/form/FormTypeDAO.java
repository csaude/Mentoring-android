package mz.org.csaude.mentoring.dao.form;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import mz.org.csaude.mentoring.model.form.FormType;

public interface FormTypeDAO extends Dao<FormType, Integer> {
    boolean checkFormTypeExistance(String uuid) throws SQLException;
}
