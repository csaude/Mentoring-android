package mz.org.csaude.mentoring.service.career;

import mz.org.csaude.mentoring.base.service.BaseService;
import mz.org.csaude.mentoring.dto.career.CareerDTO;
import mz.org.csaude.mentoring.model.career.Career;

import java.sql.SQLException;
import java.util.List;

public interface CareerService extends BaseService<Career> {

    void savedOrUpdateCareers(List<CareerDTO> careerDTOS) throws SQLException;

    Career savedOrUpdateCareer(Career career) throws SQLException;
    public Career getCareerByUuid(final String uuid) throws SQLException;
}
