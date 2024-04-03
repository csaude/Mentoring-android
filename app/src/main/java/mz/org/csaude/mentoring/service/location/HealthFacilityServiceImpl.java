package mz.org.csaude.mentoring.service.location;

import android.app.Application;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.location.HealthFacilityDAO;
import mz.org.csaude.mentoring.dto.location.DistrictDTO;
import mz.org.csaude.mentoring.dto.location.HealthFacilityDTO;
import mz.org.csaude.mentoring.model.location.District;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.user.User;

public class HealthFacilityServiceImpl extends BaseServiceImpl<HealthFacility> implements HealthFacilityService {

    HealthFacilityDAO healthFacilityDAO;

    DistrictService districtService;

    public HealthFacilityServiceImpl(Application application) {
        super(application);
    }

    @Override
    public void init(Application application ) throws SQLException {
        super.init(application );
        this.healthFacilityDAO = getDataBaseHelper().getHealthFacilityDAO();
        this.districtService = new DistrictServiceImpl(application );
    }
    @Override
    public HealthFacility save(HealthFacility record) throws SQLException {
        this.healthFacilityDAO.create(record);
        return record;
    }

    @Override
    public HealthFacility update(HealthFacility record) throws SQLException {
        this.healthFacilityDAO.update(record);
        return record;
    }

    @Override
    public int delete(HealthFacility record) throws SQLException {
        return this.healthFacilityDAO.delete(record);
    }

    @Override
    public List<HealthFacility> getAll() throws SQLException {
        return this.healthFacilityDAO.queryForAll();
    }

    @Override
    public HealthFacility getById(int id) throws SQLException {
        return this.healthFacilityDAO.queryForId(id);
    }

    @Override
    public void savedOrUpdatHealthFacilitys(List<HealthFacility> healthFacilityDTOs) throws SQLException {

        for(HealthFacility healthFacility : healthFacilityDTOs){
            this.savedOrUpdatHealthFacility(healthFacility);
        }
    }

    @Override
    public HealthFacility savedOrUpdatHealthFacility(HealthFacility healthFacility) throws SQLException {

        HealthFacility h = this.healthFacilityDAO.getByUuid(healthFacility.getUuid());
        healthFacility.setDistrict(getApplication().getDistrictService().getByuuid(healthFacility.getDistrict().getUuid()));
        if(h != null){
            healthFacility.setId(h.getId());
        }
        this.healthFacilityDAO.createOrUpdate(healthFacility);
        return healthFacility;
    }

    @Override
    public List<HealthFacility> getHealthFacilityByDistrict(District district) throws SQLException {
        return this.healthFacilityDAO.getHealthFacilityByDistrict(district);
    }

    @Override
    public List<HealthFacility> getHealthFacilityByDistrictAndMentor(District district, Tutor mentor) throws SQLException {
        return this.healthFacilityDAO.getHealthFacilityByDistrictAndMentor(district, mentor);
    }
}
