package mz.org.csaude.mentoring.dto.location;

import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.dto.employee.EmployeeDTO;
import mz.org.csaude.mentoring.model.location.District;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.location.Location;
import mz.org.csaude.mentoring.model.location.Province;

public class LocationDTO extends BaseEntityDTO {

    private String uuid;
    private EmployeeDTO employeeDTO;
    private ProvinceDTO provinceDTO;
    private DistrictDTO districtDTO;
    private HealthFacilityDTO healthFacilityDTO;
    private String locationLevel;

    public LocationDTO() {
    }

    public LocationDTO(Location location) {
        super(location);
        this.setLocationLevel(location.getLocationLevel());
       if(location.getProvince() != null) this.setProvinceDTO(new ProvinceDTO((Province) location.getProvince()));
       if(location.getDistrict() != null) this.setDistrictDTO(new DistrictDTO((District) location.getDistrict()));
        this.setHealthFacilityDTO(new HealthFacilityDTO((HealthFacility) location.getHealthFacility()));

    }

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    public ProvinceDTO getProvinceDTO() {
        return provinceDTO;
    }

    public void setProvinceDTO(ProvinceDTO provinceDTO) {
        this.provinceDTO = provinceDTO;
    }

    public DistrictDTO getDistrictDTO() {
        return districtDTO;
    }

    public void setDistrictDTO(DistrictDTO districtDTO) {
        this.districtDTO = districtDTO;
    }

    public HealthFacilityDTO getHealthFacilityDTO() {
        return healthFacilityDTO;
    }

    public void setHealthFacilityDTO(HealthFacilityDTO healthFacilityDTO) {
        this.healthFacilityDTO = healthFacilityDTO;
    }

    public String getLocationLevel() {
        return locationLevel;
    }

    public void setLocationLevel(String locationLevel) {
        this.locationLevel = locationLevel;
    }

    public Location getLocation() {
        return new Location(this);
    }
}
