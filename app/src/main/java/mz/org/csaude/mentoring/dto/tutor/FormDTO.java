package mz.org.csaude.mentoring.dto.tutor;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import mz.org.csaude.mentoring.dto.partner.PartnerDTO;
import mz.org.csaude.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;

public class FormDTO implements Serializable {

    private String uuid;

    private String code;

    private String name;

    private String description;

    @JsonProperty(value = "partner")
    private PartnerDTO partnerDTO;

    @JsonProperty(value = "programmaticArea")
    private ProgrammaticAreaDTO programmaticAreaDTO;

    @JsonProperty(value = "formType")
    private FormTypeDTO formTypeDTO;

    private Integer targetPatient;

    private Integer targetFile;

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPartnerDTO(PartnerDTO partnerDTO) {
        this.partnerDTO = partnerDTO;
    }

    public void setProgrammaticAreaDTO(ProgrammaticAreaDTO programmaticAreaDTO) {
        this.programmaticAreaDTO = programmaticAreaDTO;
    }

    public void setFormTypeDTO(FormTypeDTO formTypeDTO) {
        this.formTypeDTO = formTypeDTO;
    }

    public void setTargetPatient(Integer targetPatient) {
        this.targetPatient = targetPatient;
    }

    public void setTargetFile(Integer targetFile) {
        this.targetFile = targetFile;
    }

    public String getUuid() {
        return uuid;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public PartnerDTO getPartnerDTO() {
        return partnerDTO;
    }

    public ProgrammaticAreaDTO getProgrammaticAreaDTO() {
        return programmaticAreaDTO;
    }

    public FormTypeDTO getFormTypeDTO() {
        return formTypeDTO;
    }

    public Integer getTargetPatient() {
        return targetPatient;
    }

    public Integer getTargetFile() {
        return targetFile;
    }
}
