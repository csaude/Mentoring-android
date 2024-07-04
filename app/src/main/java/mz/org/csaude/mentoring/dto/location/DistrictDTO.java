package mz.org.csaude.mentoring.dto.location;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.model.location.District;
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DistrictDTO extends BaseEntityDTO {

    private String description;
    private ProvinceDTO provinceDTO;

    public DistrictDTO(District district) {
        super(district);
        this.setDescription(district.getDescription());
        if (district.getProvince() != null)  this.setProvinceDTO(new ProvinceDTO(district.getProvince()));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProvinceDTO getProvinceDTO() {
        return provinceDTO;
    }

    public void setProvinceDTO(ProvinceDTO provinceDTO) {
        this.provinceDTO = provinceDTO;
    }
    public District getDistrict() {
        return new District(this);
    }
}
