package mz.org.csaude.mentoring.service.form;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseService;
import mz.org.csaude.mentoring.dto.tutor.FormTypeDTO;
import mz.org.csaude.mentoring.model.form.FormType;

public interface FormTypeService extends BaseService<FormType> {
    void saveOrUpdateFormTypes(List<FormTypeDTO> formTypeDTOList) throws SQLException;

    FormType saveOrUpdateFormType(FormType formType) throws SQLException;
}
