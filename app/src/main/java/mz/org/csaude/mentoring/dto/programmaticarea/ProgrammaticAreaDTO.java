package mz.org.csaude.mentoring.dto.programmaticarea;



import java.io.Serializable;


public class ProgrammaticAreaDTO implements Serializable {

    private String uuid;

    private String code;

    private String description;

    private String name;

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
}
