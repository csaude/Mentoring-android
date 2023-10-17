package mz.org.csaude.mentoring.service.form;

import java.sql.SQLException;

import mz.org.csaude.mentoring.base.service.BaseService;
import mz.org.csaude.mentoring.model.form.FormType;

public interface FormTypeService extends BaseService<FormType> {

    FormType savedOrUpdateFormType(FormType formType) throws SQLException;
}
