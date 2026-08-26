package mz.org.csaude.mentoring.dto.location;

import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.dto.employee.EmployeeDTO;
import mz.org.csaude.mentoring.model.location.District;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.location.Location;
import mz.org.csaude.mentoring.model.location.Province;

public class LocationDTO extends BaseEntityDTO {

    private EmployeeDTO employeeDTO;
    private ProvinceDTO provinceDTO;
    private DistrictDTO districtDTO;
    private HealthFacilityDTO healthFacilityDTO;
    private String locationLevel;

    // ✅ NOVO
    private Boolean interno;

    public LocationDTO() {
    }

    public LocationDTO(Location location) {
        super(location);
        this.setLocationLevel(location.getLocationLevel());
        this.setInterno(location.getInterno()); // ✅ NOVO

        if (location.getProvince() != null) this.setProvinceDTO(new ProvinceDTO((Province) location.getProvince()));
        if (location.getDistrict() != null) this.setDistrictDTO(new DistrictDTO((District) location.getDistrict()));
        if (location.getHealthFacility() != null) this.setHealthFacilityDTO(new HealthFacilityDTO(location.getHealthFacility()));
        if (location.getEmployee() != null) this.setEmployeeDTO(new EmployeeDTO(location.getEmployee()));
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

    // ✅ NOVO
    public Boolean getInterno() {
        return interno;
    }

    public void setInterno(Boolean interno) {
        this.interno = interno;
    }

    public Location getLocation() {
        Location location = new Location();
        location.setUuid(this.getUuid());
        location.setLocationLevel(this.getLocationLevel());

        // ✅ NOVO (default false se vier null)
        location.setInterno(this.getInterno() != null ? this.getInterno() : Boolean.FALSE);

        if (this.getEmployeeDTO() != null) {
            location.setEmployee(this.employeeDTO.getEmployee());
        }
        if (this.getDistrictDTO() != null) {
            location.setDistrict(this.getDistrictDTO().getDistrict());
        }
        if (this.getProvinceDTO() != null) {
            location.setProvince(this.getProvinceDTO().getProvince());
        }
        if (this.getHealthFacilityDTO() != null) {
            location.setHealthFacility(this.getHealthFacilityDTO().getHealthFacilityObj());
        }

        return location;
    }
}
