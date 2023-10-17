package mz.org.csaude.mentoring.service.form;

import android.app.Application;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.form.FormTypeDAO;
import mz.org.csaude.mentoring.model.form.FormType;
import mz.org.csaude.mentoring.model.user.User;

public class FormTypeServiceImpl extends BaseServiceImpl<FormType> implements FormTypeService {

    FormTypeDAO formTypeDAO;

    public FormTypeServiceImpl(Application application, User currentUser) {
        super(application, currentUser);
    }

    public FormTypeServiceImpl(Application application) {
        super(application);
    }

    @Override
    public void init(Application application, User currentUser) throws SQLException {
        super.init(application, currentUser);
        this.formTypeDAO = getDataBaseHelper().getFormType();
    }

    @Override
    public FormType save(FormType record) throws SQLException {
        this.formTypeDAO.create(record);
        return record;
    }

    @Override
    public FormType update(FormType record) throws SQLException {
        this.formTypeDAO.update(record);
        return record;
    }

    @Override
    public int delete(FormType record) throws SQLException {
        return this.formTypeDAO.delete(record);
    }

    @Override
    public List<FormType> getAll() throws SQLException {
        return this.formTypeDAO.queryForAll();
    }

    @Override
    public FormType getById(int id) throws SQLException {
        return this.formTypeDAO.queryForId(id);
    }

    @Override
    public FormType savedOrUpdateFormType(FormType formType) throws SQLException {

        List<FormType> formTypeExist = this.formTypeDAO.queryForEq("uuid", formType.getUuid());

        if(formTypeExist.isEmpty()){
            this.formTypeDAO.createOrUpdate(formType);
            return formType;
        }
        return formTypeExist.get(0);
    }
}
