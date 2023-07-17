package mz.org.csaude.mentoring.service.location;

import android.app.Application;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseService;
import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.location.CabinetDAO;
import mz.org.csaude.mentoring.model.location.Cabinet;
import mz.org.csaude.mentoring.model.user.User;

public class CabinetServiceImpl extends BaseServiceImpl<Cabinet> implements CabinetService {

    CabinetDAO cabinetDAO;

    public CabinetServiceImpl(Application application, User currentUser) {
        super(application, currentUser);
    }

    public CabinetServiceImpl(Application application) {
        super(application);
    }

    @Override
    public void init(Application application, User currentUser) throws SQLException {
        super.init(application, currentUser);
        this.cabinetDAO = getDataBaseHelper().getCabinetDAO();
    }

    @Override
    public Cabinet save(Cabinet record) throws SQLException {
        this.cabinetDAO.create(record);
        return record;
    }

    @Override
    public Cabinet update(Cabinet record) throws SQLException {
        this.cabinetDAO.update(record);
        return record;
    }

    @Override
    public int delete(Cabinet record) throws SQLException {
        return this.cabinetDAO.delete(record);
    }

    @Override
    public List<Cabinet> getAll() throws SQLException {
        return this.cabinetDAO.queryForAll();
    }

    @Override
    public Cabinet getById(int id) throws SQLException {
        return this.cabinetDAO.queryForId(id);
    }
}
