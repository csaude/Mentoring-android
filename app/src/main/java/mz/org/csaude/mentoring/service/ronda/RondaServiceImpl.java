package mz.org.csaude.mentoring.service.ronda;

import android.app.Application;

import com.j256.ormlite.misc.TransactionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import mz.org.csaude.mentoring.base.application.MentoringApplication;
import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.location.DistrictDAO;
import mz.org.csaude.mentoring.dao.location.HealthFacilityDAO;
import mz.org.csaude.mentoring.dao.ronda.RondaDAO;
import mz.org.csaude.mentoring.dao.ronda.RondaMenteeDAO;
import mz.org.csaude.mentoring.dao.ronda.RondaMentorDAO;
import mz.org.csaude.mentoring.dao.rondatype.RondaTypeDAO;
import mz.org.csaude.mentoring.dao.tutor.TutorDAO;
import mz.org.csaude.mentoring.dao.tutored.TutoredDao;
import mz.org.csaude.mentoring.dto.location.HealthFacilityDTO;
import mz.org.csaude.mentoring.dto.ronda.RondaDTO;
import mz.org.csaude.mentoring.dto.ronda.RondaMenteeDTO;
import mz.org.csaude.mentoring.dto.ronda.RondaMentorDTO;
import mz.org.csaude.mentoring.model.location.District;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.ronda.RondaMentee;
import mz.org.csaude.mentoring.model.ronda.RondaMentor;
import mz.org.csaude.mentoring.model.rondatype.RondaType;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.tutored.Tutored;

public class RondaServiceImpl extends BaseServiceImpl<Ronda> implements RondaService {
    RondaDAO rondaDAO;
    HealthFacilityDAO healthFacilityDAO;
    RondaMentorDAO rondaMentorDAO;
    RondaMenteeDAO rondaMenteeDAO;
    DistrictDAO districtDAO;
    RondaTypeDAO rondaTypeDAO;
    TutorDAO tutorDAO;
    TutoredDao tutoredDao;

    public RondaServiceImpl(Application application) {
        super(application);
    }

    @Override
    public void init(Application application) throws SQLException {
        super.init(application);
        this.rondaDAO = getDataBaseHelper().getRondaDAO();
        this.healthFacilityDAO = getDataBaseHelper().getHealthFacilityDAO();
        this.rondaMenteeDAO = getDataBaseHelper().getRondaMenteeDAO();
        this.rondaMentorDAO = getDataBaseHelper().getRondaMentorDAO();
        this.districtDAO = getDataBaseHelper().getDistrictDAO();
        this.rondaTypeDAO = getDataBaseHelper().getRondaTypeDAO();
        this.tutorDAO = getDataBaseHelper().getTutorDAO();
        this.tutoredDao = getDataBaseHelper().getTutoredDao();
    }

    @Override
    public Ronda savedOrUpdateRonda(Ronda ronda) throws SQLException {
        TransactionManager.callInTransaction(getDataBaseHelper().getConnectionSource(), (Callable<Void>) () -> {
            Ronda r = this.rondaDAO.getByUuid(ronda.getUuid());
            if(r!=null) {
                ronda.setId(r.getId());
            }
            this.rondaDAO.createOrUpdate(ronda);

            for (RondaMentor rondaMentor: ronda.getRondaMentors()) {
                this.rondaMentorDAO.createOrUpdate(rondaMentor);
            }
            for (RondaMentee rondaMentee: ronda.getRondaMentees()) {
                this.rondaMenteeDAO.createOrUpdate(rondaMentee);
            }
            return null;
        });
        return ronda;
    }

    @Override
    public List<Ronda> getAllByHealthFacilityAndMentor(HealthFacility healthFacility, Tutor tutor, MentoringApplication mentoringApplication) throws SQLException {
        return this.rondaDAO.getAllByHealthFacilityAndMentor(healthFacility, tutor, mentoringApplication);
    }

    @Override
    public List<Ronda> getAllNotSynced() throws SQLException {
        return this.rondaDAO.getAllNotSynced();
    }

    @Override
    public List<Ronda> doSearch(long offset, long limit) {
        return null;
    }

    @Override
    public int countRondas() throws SQLException {
        return this.rondaDAO.queryForAll().size();
    }

    @Override
    public List<Ronda> getAllByRondaType(RondaType rondaType) throws SQLException {
        return this.rondaDAO.getAllByRondaType(rondaType, this.getApplication());
    }

    @Override
    public void saveOrUpdateRondas(List<RondaDTO> rondaDTOS) throws SQLException {
        for (RondaDTO rondaDTO: rondaDTOS) {
            this.saveOrUpdateRonda(rondaDTO);
        }
    }

    @Override
    public Ronda saveOrUpdateRonda(RondaDTO rondaDTO) throws SQLException {
        HealthFacilityDTO healthFacilityDTO = rondaDTO.getHealthFacility();
        HealthFacility hf = this.healthFacilityDAO.getByUuid(healthFacilityDTO.getUuid());
        HealthFacility healthFacility = healthFacilityDTO.getHealthFacilityObj();
        if(hf!=null) {
            healthFacility.setId(hf.getId());
        }
        District district = districtDAO.getByUuid(healthFacilityDTO.getDistrictDTO().getUuid());
        if(district!=null) {
            healthFacility.setDistrict(district);
        }
        this.healthFacilityDAO.createOrUpdate(healthFacility);

        Ronda r = this.rondaDAO.getByUuid(rondaDTO.getUuid());
        Ronda ronda = rondaDTO.getRonda();
        if(r!=null) {
            ronda.setId(r.getId());
        }
        ronda.setHealthFacility(healthFacility);
        RondaType rondaType = this.rondaTypeDAO.getByUuid(rondaDTO.getRondaType().getUuid());
        if(rondaType!=null) {
            ronda.setRondaType(rondaType);
        }
        this.rondaDAO.createOrUpdate(ronda);

        saveRondaMentors(rondaDTO.getRondaMentors(), ronda);

        saveRondaMentees(rondaDTO.getRondaMentees(), ronda);

        return ronda;
    }

    @Override
    public Ronda getFullyLoadedRonda(Ronda ronda) throws SQLException {
        Ronda r = this.rondaDAO.getByUuid(ronda.getUuid());
        r.setRondaMentors(this.rondaMentorDAO.getRondaMentors(r));
        r.setRondaMentees(this.rondaMenteeDAO.getAllOfRonda(r));
        r.setSessions(getApplication().getSessionService().getAllOfRonda(r));
        return r;
    }

    private void saveRondaMentors(List<RondaMentorDTO> rondaMentorDTOS, Ronda ronda) throws SQLException {
        List<RondaMentor> rondaMentors = new ArrayList<>();
        for (RondaMentorDTO rondaMentorDTO: rondaMentorDTOS) {
            RondaMentor rm = this.rondaMentorDAO.getByUuid(rondaMentorDTO.getUuid());
            RondaMentor rondaMentor = rondaMentorDTO.getRondaMentor();
            if(rm!=null) {
                rondaMentor.setId(rm.getId());
            }
            rondaMentor.setRonda(ronda);
            Tutor mentor = this.tutorDAO.getByUuid(rondaMentorDTO.getMentor().getUuid());
            if(mentor!=null) {
                rondaMentor.setTutor(mentor);
            }
            this.rondaMentorDAO.createOrUpdate(rondaMentor);
            rondaMentors.add(rondaMentor);
        }
        ronda.setRondaMentors(rondaMentors);
    }
    private void saveRondaMentees(List<RondaMenteeDTO> rondaMenteeDTOS, Ronda ronda) throws SQLException {
        List<RondaMentee> rondaMentees = new ArrayList<>();
        for (RondaMenteeDTO rondaMenteeDTO: rondaMenteeDTOS) {
            RondaMentee rm = this.rondaMenteeDAO.getByUuid(rondaMenteeDTO.getUuid());
            RondaMentee rondaMentee = rondaMenteeDTO.getRondaMentee();
            if(rm!=null) {
                rondaMentee.setId(rm.getId());
            }
            rondaMentee.setRonda(ronda);
            Tutored mentee = this.tutoredDao.getByUuid(rondaMenteeDTO.getMentee().getUuid());
            if(mentee!=null) {
                rondaMentee.setTutored(mentee);
            }
            this.rondaMenteeDAO.createOrUpdate(rondaMentee);
            rondaMentees.add(rondaMentee);
        }
            ronda.setRondaMentees(rondaMentees);
    }

    @Override
    public Ronda save(Ronda record) throws SQLException {
        this.rondaDAO.create(record);
        return record;
    }

    @Override
    public Ronda update(Ronda record) throws SQLException {
        this.rondaDAO.update(record);
        return record;
    }

    @Override
    public int delete(Ronda record) throws SQLException {
        return this.rondaDAO.delete(record);
    }

    @Override
    public List<Ronda> getAll() throws SQLException {
        return this.rondaDAO.queryForAll();
    }

    @Override
    public Ronda getById(int id) throws SQLException {
        return this.rondaDAO.queryForId(id);
    }
    @Override
    public List<Ronda> getAllByMentor(Tutor tutor, MentoringApplication mentoringApplication) throws SQLException {
        return this.rondaDAO.getAllByMentor(tutor, mentoringApplication);
    }

}
