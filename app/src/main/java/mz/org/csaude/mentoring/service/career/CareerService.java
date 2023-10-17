package mz.org.csaude.mentoring.service.career;

import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.service.BaseService;
import mz.org.csaude.mentoring.dto.career.CareerDTO;
import mz.org.csaude.mentoring.model.career.Career;
import mz.org.csaude.mentoring.model.career.CareerType;

import java.sql.SQLException;
import java.util.List;

public interface CareerService extends BaseService<Career> {

    void savedOrUpdateCareers(List<CareerDTO> careerDTOS) throws SQLException;

    List<Career> getCareersByCareerType(CareerType careerType) throws SQLException;

    Listble getAllCareers();

    Career savedOrUpdateCareer(Career career) throws SQLException;
    public Career getCareerByUuid(final String uuid) throws SQLException;

}
