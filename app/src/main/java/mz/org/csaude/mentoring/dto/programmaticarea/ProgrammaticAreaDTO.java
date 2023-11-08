package mz.org.csaude.mentoring.dto.programmaticarea;

import java.io.Serializable;

import mz.org.csaude.mentoring.model.programmaticArea.ProgrammaticArea;

public class ProgrammaticAreaDTO implements Serializable {

    private String uuid;

    private String code;

    private String description;

    private String name;

    public ProgrammaticAreaDTO() {
    }

    public ProgrammaticAreaDTO(ProgrammaticArea programmaticArea) {
        this.uuid = programmaticArea.getUuid();
        this.code = programmaticArea.getCode();
        this.description = programmaticArea.getDescription();
        this.name = programmaticArea.getName();
    }

    public String getUuid() {
        return uuid;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
}