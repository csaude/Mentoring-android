package mz.org.csaude.mentoring.dto.location;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.model.location.HealthFacility;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HealthFacilityDTO extends BaseEntityDTO {

    private String healthFacility;

    private DistrictDTO districtDTO;

    public HealthFacilityDTO(HealthFacility healthFacility){
        super(healthFacility);
        this.setHealthFacility(healthFacility.getDescription());
        if (healthFacility.getDistrict() != null) this.setDistrictDTO(new DistrictDTO(healthFacility.getDistrict()));

    }

    public String getHealthFacility() {
        return healthFacility;
    }

    public void setHealthFacility(String healthFacility) {
        this.healthFacility = healthFacility;
    }

    public DistrictDTO getDistrictDTO() {
        return districtDTO;
    }

    public void setDistrictDTO(DistrictDTO districtDTO) {
        this.districtDTO = districtDTO;
    }
    public HealthFacility getHealthFacilityObj() {
        return new HealthFacility(this);
    }
}
