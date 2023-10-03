package mz.org.csaude.mentoring.dao.partner;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import mz.org.csaude.mentoring.model.partner.Partner;

public interface PartnerDao extends Dao<Partner, Integer> {
    boolean checkPartnerExistance(String uuid) throws SQLException;
}
