package mz.org.csaude.mentoring.model.location;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.location.LocationDAOImpl;
import mz.org.csaude.mentoring.dto.location.LocationDTO;
import mz.org.csaude.mentoring.model.employee.Employee;


@DatabaseTable(tableName = Location.COLUMN_TABLE_NAME, daoClass = LocationDAOImpl.class)
public class Location extends BaseModel implements Listble {

    public static final String COLUMN_TABLE_NAME = "location";
    public static final String COLUMN_EMPLOYEE = "employee_id";
    public static final String COLUMN_PROVINCE = "province_id";
    public static final String COLUMN_DISTRICT = "district_id";
    public static final String COLUMN_HEALTH_FACILITY = "health_hacility_id";
    public static final String COLUMN_LOCATION_LEVEL = "location_Level";

    @DatabaseField(columnName = COLUMN_EMPLOYEE, foreign = true, foreignAutoRefresh = true)
    private Employee employee;
    @DatabaseField(columnName = COLUMN_PROVINCE, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Province province;
    @DatabaseField(columnName = COLUMN_DISTRICT, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private District district;
    @DatabaseField(columnName = COLUMN_HEALTH_FACILITY, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private HealthFacility healthFacility;
    @DatabaseField(columnName = COLUMN_LOCATION_LEVEL)
    private String locationLevel;

    public Location() {
    }

    public Location(Province province, District district, HealthFacility healthFacility, String locationLevel) {
        this.province = province;
        this.district = district;
        this.healthFacility = healthFacility;
        this.locationLevel = locationLevel;
    }

    public Location(LocationDTO location) {
        this.setUuid(location.getUuid());
        this.setLocationLevel(location.getLocationLevel());
       if(location.getEmployeeDTO() != null) this.setEmployee(new Employee(location.getEmployeeDTO()));
       if(location.getProvinceDTO() != null) this.setProvince(new Province(location.getProvinceDTO()));
       if(location.getDistrictDTO() != null) this.setDistrict(new District(location.getDistrictDTO()));
       if(location.getHealthFacilityDTO() != null) this.setHealthFacility(new HealthFacility(location.getHealthFacilityDTO()));
    }

    public Location(LocationDTO location, Employee employee) {
        super(location);
        this.setLocationLevel(location.getLocationLevel());
        this.setEmployee(employee);
        if(location.getProvinceDTO() != null) this.setProvince(new Province(location.getProvinceDTO()));
        if(location.getDistrictDTO() != null) this.setDistrict(new District(location.getDistrictDTO()));
        if(location.getHealthFacilityDTO() != null) this.setHealthFacility(new HealthFacility(location.getHealthFacilityDTO()));
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public HealthFacility getHealthFacility() {
        return healthFacility;
    }

    public void setHealthFacility(HealthFacility healthFacility) {
        this.healthFacility = healthFacility;
    }

    public String getLocationLevel() {
        return locationLevel;
    }

    public void setLocationLevel(String locationLevel) {
        this.locationLevel = locationLevel;
    }


    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getDrawable() {
        return 0;
    }

    @Override
    public String getCode() {
        return null;
    }
}
