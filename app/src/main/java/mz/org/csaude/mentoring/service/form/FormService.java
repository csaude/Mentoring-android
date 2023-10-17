package mz.org.csaude.mentoring.service.form;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseService;
import mz.org.csaude.mentoring.dto.form.FormDTO;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.tutor.Tutor;

public interface FormService extends BaseService<Form> {
    List<Form> getAllOfTutor(Tutor tutor) throws SQLException;
    void savedOrUpdateForms(List<FormDTO> formDTOS) throws SQLException;
    void savedOrUpdateForm(FormDTO formDTO) throws SQLException;
}
