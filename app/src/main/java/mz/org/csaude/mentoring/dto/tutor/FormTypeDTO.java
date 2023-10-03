package mz.org.csaude.mentoring.dto.tutor;

import java.io.Serializable;

public class FormTypeDTO implements Serializable {

    private String uuid;
    private String description;

    private  String code;

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
