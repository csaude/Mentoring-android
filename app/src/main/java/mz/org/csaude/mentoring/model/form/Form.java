package mz.org.csaude.mentoring.model.form;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.form.FormDAOImpl;
import mz.org.csaude.mentoring.dao.session.SessionStatusDAOImpl;
import mz.org.csaude.mentoring.dto.tutor.FormDTO;
import mz.org.csaude.mentoring.model.career.Career;
import mz.org.csaude.mentoring.model.partner.Partner;
import mz.org.csaude.mentoring.model.programmaticArea.ProgrammaticArea;

@Data
@NoArgsConstructor
@DatabaseTable(tableName = Form.TABLE_NAME, daoClass = FormDAOImpl.class)
@EqualsAndHashCode(callSuper=false)
public class Form extends BaseModel {

    public static final String TABLE_NAME = "form";
    public static final String COLUMN_NAME= "name";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PROGRAMMATIC_AREA= "programmatic_area_type_id";
    public static final String COLUMN_FORM_TYPE = "form_type_id";
    public static final String COLUMN_TARGET_PATIENT = "target_patient";
    public static final String COLUMN_TARGET_FILE = "target_file";
    public static final String COLUMN_PARTNER = "partner_id";

    @DatabaseField(columnName = COLUMN_NAME)
    private String name;

    @DatabaseField(columnName = COLUMN_CODE)
    private String code;

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COLUMN_PROGRAMMATIC_AREA, canBeNull = false, foreign = true, foreignAutoRefresh = true )
    private ProgrammaticArea programmaticArea;

    @DatabaseField(columnName = COLUMN_FORM_TYPE, canBeNull = false, foreign = true, foreignAutoRefresh = true )
    private FormType formType;

    @DatabaseField(columnName = COLUMN_TARGET_PATIENT)
    private int targetPatient;

    @DatabaseField(columnName = COLUMN_TARGET_FILE)
    private int targetFile;

    public Form(FormDTO formDTO) {
        this.name = formDTO.getName();
        this.code = formDTO.getCode();
        this.description = formDTO.getDescription();
        this.targetPatient = formDTO.getTargetPatient();
        this.targetFile = formDTO.getTargetFile();
    }

    @DatabaseField(columnName = COLUMN_PARTNER, canBeNull = false, foreign = true, foreignAutoRefresh = true )
    private Partner partner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProgrammaticArea getProgrammaticArea() {
        return programmaticArea;
    }

    public void setProgrammaticArea(ProgrammaticArea programmaticArea) {
        this.programmaticArea = programmaticArea;
    }

    public FormType getFormType() {
        return formType;
    }

    public void setFormType(FormType formType) {
        this.formType = formType;
    }

    public int getTargetPatient() {
        return targetPatient;
    }

    public void setTargetPatient(int targetPatient) {
        this.targetPatient = targetPatient;
    }

    public int getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(int targetFile) {
        this.targetFile = targetFile;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }
}
