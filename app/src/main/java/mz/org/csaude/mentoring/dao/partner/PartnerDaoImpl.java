package mz.org.csaude.mentoring.dao.partner;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.model.partner.Partner;

public class PartnerDaoImpl extends BaseDaoImpl<Partner, Integer> implements PartnerDao {


    public PartnerDaoImpl(Class<Partner> dataClass) throws SQLException {
        super(dataClass);
    }

    public PartnerDaoImpl(ConnectionSource connectionSource, Class<Partner> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public PartnerDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig<Partner> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }


    @Override
    public boolean checkPartnerExistance(String uuid) throws SQLException {

            List<Partner> partners = this.queryForEq("uuid", uuid);
            return partners.isEmpty();
    }
}
