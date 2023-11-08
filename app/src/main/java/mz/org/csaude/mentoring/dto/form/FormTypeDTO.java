package mz.org.csaude.mentoring.dto.form;

import java.io.Serializable;

import mz.org.csaude.mentoring.model.form.FormType;

public class FormTypeDTO implements Serializable {

    private String uuid;
    private String description;

    private  String code;

    public FormTypeDTO() {
    }

    public FormTypeDTO(FormType formType) {
        this.description = formType.getDescripion();
        this.code = formType.getCode();
        this.uuid = formType.getUuid();
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getUuid() {
        return uuid;
    }
}
