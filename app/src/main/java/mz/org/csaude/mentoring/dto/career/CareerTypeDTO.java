package mz.org.csaude.mentoring.dto.career;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.model.career.CareerType;

import java.io.Serializable;

/**
 * @author Jose Julai Ritsure
 */
@Data
@NoArgsConstructor
public class CareerTypeDTO implements Serializable {
    private String uuid;
    private String code;
    private String description;

    public CareerTypeDTO(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public CareerTypeDTO(String uuid, String code, String description) {
        this.uuid = uuid;
        this.code = code;
        this.description = description;
    }

    public CareerTypeDTO(CareerType careerType) {
        this.setUuid(careerType.getUuid());
        this.setCode(careerType.getCode());
        this.setDescription(careerType.getDescription());
    }

    public CareerType getCareerType() {
        return  new CareerType(this);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
