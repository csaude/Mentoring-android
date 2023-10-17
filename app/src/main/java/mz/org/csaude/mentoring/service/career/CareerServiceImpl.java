package mz.org.csaude.mentoring.service.career;

import android.app.Application;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.career.CareerDAO;
import mz.org.csaude.mentoring.dao.career.CareerTypeDAO;
import mz.org.csaude.mentoring.dto.career.CareerDTO;
import mz.org.csaude.mentoring.dto.career.CareerTypeDTO;
import mz.org.csaude.mentoring.model.career.Career;
import mz.org.csaude.mentoring.model.career.CareerType;
import mz.org.csaude.mentoring.model.user.User;

public class CareerServiceImpl extends BaseServiceImpl<Career> implements CareerService {

    CareerDAO careerDAO;

    CareerTypeDAO careerTypeDAO;

    CareerTypeService careerTypeService;

    public CareerServiceImpl(Application application, User currentUser) {
        super(application, currentUser);
    }

    public CareerServiceImpl(Application application) {
        super(application);
    }

    @Override
    public void init(Application application, User currentUser) throws SQLException {
        super.init(application, currentUser);
        this.careerDAO = getDataBaseHelper().getCareerDAO();
        this.careerTypeDAO = getDataBaseHelper().getCareerTypeDAO();
        this.careerTypeService = new CareerTypeServiceImpl(application);
    }

    @Override
    public Career save(Career record) throws SQLException {

        this.careerDAO.create(record);
        return record;
    }

    @Override
    public Career update(Career record) throws SQLException {
        this.careerDAO.update(record);
        return record;
    }

    @Override
    public int delete(Career record) throws SQLException {
        return this.careerDAO.delete(record);
    }

    @Override
    public List<Career> getAll() throws SQLException {
        return this.careerDAO.queryForAll();
    }

    @Override
    public Career getById(int id) throws SQLException {
        return this.careerDAO.queryForId(id);
    }

    @Override
    public void savedOrUpdateCareers(List<CareerDTO> careerDTOS) throws SQLException {
        for (CareerDTO careerDTO : careerDTOS) {
            boolean doesCareerExist = this.careerDAO.checkCareerExistance(careerDTO.getUuid());
            if (!doesCareerExist) {
                CareerTypeDTO careerTypeDTO = careerDTO.getCareerTypeDTO();
                CareerType careerType = this.careerTypeService.savedOrUpdateCareerTypes(new CareerType(careerTypeDTO));
                Career career = new Career(careerDTO);
                career.setCareerType(careerType);
                this.careerDAO.createOrUpdate(career);
            }
        }
    }

    @Override
    public List<Career> getCareersByCareerType(CareerType careerType) throws SQLException {
        List<Career> careers = this.careerDAO.findByCareerType(careerType);
        return careers;
    }

    @Override
    public Listble getAllCareers() {
        return null;
    }

    public Career savedOrUpdateCareer(Career career) throws SQLException {

        List<Career> careers = this.careerDAO.queryForEq("uuid", career.getUuid());

        if(careers.isEmpty()){
          this.careerDAO.createOrUpdate(career);
          return career;
        }
        return careers.get(0);
    }

    @Override
    public Career getCareerByUuid(String uuid) throws SQLException {

        return this.careerDAO.getCareerByUuid(uuid);
    }

}
