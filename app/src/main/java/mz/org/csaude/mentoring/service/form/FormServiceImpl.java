package mz.org.csaude.mentoring.service.form;

import android.app.Application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.form.FormDAO;
import mz.org.csaude.mentoring.dao.programmaticArea.TutorProgrammaticAreaDAO;
import mz.org.csaude.mentoring.dto.form.FormDTO;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.form.FormType;
import mz.org.csaude.mentoring.model.partner.Partner;
import mz.org.csaude.mentoring.model.programmaticArea.ProgrammaticArea;
import mz.org.csaude.mentoring.model.programmaticArea.TutorProgrammaticArea;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.ProgrammaticArea.ProgrammaticAreaService;
import mz.org.csaude.mentoring.service.ProgrammaticArea.ProgrammaticAreaServiceImpl;
import mz.org.csaude.mentoring.service.partner.PartnerService;
import mz.org.csaude.mentoring.service.partner.PartnerServiceImpl;

public class FormServiceImpl extends BaseServiceImpl<Form> implements FormService{

    FormDAO formDAO;

    TutorProgrammaticAreaDAO tutorProgrammaticAreaDAO;

    ProgrammaticAreaService programmaticAreaService;

    FormTypeService formTypeService;

    PartnerService partnerService;

    public FormServiceImpl(Application application, User currentUser) {
        super(application, currentUser);
        init(application, currentUser);
    }

    public FormServiceImpl(Application application) {
        super(application);
        init(application, null);
    }

    @Override
    public void init(Application application, User currentUser) {
        try {
            super.init(application, currentUser);
            this.formDAO = getDataBaseHelper().getFormDAO();
            this.tutorProgrammaticAreaDAO = getDataBaseHelper().getTutorProgrammaticAreaDAO();
            this.programmaticAreaService = new ProgrammaticAreaServiceImpl(application);
            this.formTypeService = new FormTypeServiceImpl(application);
            this.partnerService = new PartnerServiceImpl(application);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Form save(Form record) throws SQLException {
        this.formDAO.create(record);
        return record;
    }

    @Override
    public Form update(Form record) throws SQLException {
        this.formDAO.update(record);
        return record;
    }

    @Override
    public int delete(Form record) throws SQLException {
        return this.formDAO.delete(record);
    }

    @Override
    public List<Form> getAll(){

        List<Form> forms = new ArrayList<>();
        try {
            forms = this.formDAO.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return forms;
    }

    @Override
    public Form getById(int id) throws SQLException {
        return this.formDAO.queryForId(id);
    }

    @Override
    public List<Form> getAllOfTutor(Tutor tutor) throws SQLException {
        return formDAO.getAllOfTutor(tutor, application);
    }

    @Override
    public void savedOrUpdateForms(List<FormDTO> formDTOS) throws SQLException {

        for(FormDTO formDTO : formDTOS){

            boolean formExist = this.formDAO.checkFormExistance(formDTO.getUuid());
            if(formExist){
                ProgrammaticArea programmaticArea = this.programmaticAreaService.saveOrUpdateProgrammaticArea(new ProgrammaticArea(formDTO.getProgrammaticAreaDTO()));
                FormType formType = this.formTypeService.savedOrUpdateFormType(new FormType(formDTO.getFormTypeDTO()));
                Partner partner = this.partnerService.savedOrUpdatePartner(new Partner(formDTO.getPartnerDTO()));

                Form form = new Form(formDTO);
                form.setProgrammaticArea(programmaticArea);
                form.setFormType(formType);
                form.setPartner(partner);

                this.formDAO.createOrUpdate(form);
            }

        }

    }

    @Override
    public void savedOrUpdateForm(FormDTO formDTO) throws SQLException {

    }


}
