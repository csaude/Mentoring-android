package mz.org.csaude.mentoring.service.tutor;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseService;
import mz.org.csaude.mentoring.dto.tutor.TutorDTO;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.user.User;

public interface TutorService extends BaseService<Tutor> {

    public void saveOrUpdateTutors(List<TutorDTO> tutorDTOS) throws SQLException;

    public Tutor getTutorByUuid(String uuid) throws SQLException;

    public Tutor getTutorByUser(User user) throws SQLException;
}
