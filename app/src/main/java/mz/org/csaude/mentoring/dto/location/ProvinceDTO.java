package mz.org.csaude.mentoring.dto.location;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.model.location.Province;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProvinceDTO extends BaseEntityDTO {

    private String designation;
    public ProvinceDTO(Province province) {
        super(province);
        this.setDesignation(province.getDescription());
    }
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Province getProvince() {
        return new Province(this);
    }
}
