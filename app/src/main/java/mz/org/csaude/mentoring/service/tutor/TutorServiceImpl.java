package mz.org.csaude.mentoring.service.tutor;

import android.app.Application;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.base.service.BaseService;
import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.tutor.TutorDAO;
import mz.org.csaude.mentoring.dto.tutor.TutorDTO;
import mz.org.csaude.mentoring.model.career.Career;
import mz.org.csaude.mentoring.model.partner.Partner;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.career.CareerService;
import mz.org.csaude.mentoring.service.career.CareerServiceImpl;
import mz.org.csaude.mentoring.service.partner.PartnerService;
import mz.org.csaude.mentoring.service.partner.PartnerServiceImpl;
import mz.org.csaude.mentoring.service.user.UserService;
import mz.org.csaude.mentoring.service.user.UserServiceImpl;

public class TutorServiceImpl extends BaseServiceImpl<Tutor> implements TutorService {

    TutorDAO tutorDAO;

    CareerService careerService;

    PartnerService partnerService;

    UserService userService;

    public TutorServiceImpl(Application application, User currentUser) {
        super(application, currentUser);
    }

    public TutorServiceImpl(Application application) {
        super(application);
    }

    @Override
    public void init(Application application, User currentUser) throws SQLException {
        super.init(application, currentUser);
        this.tutorDAO = getDataBaseHelper().getTutorDAO();
        this.careerService = new CareerServiceImpl(application);
        this.partnerService = new PartnerServiceImpl(application);
        this.userService = new UserServiceImpl(application);
    }

    @Override
    public Tutor save(Tutor record) throws SQLException {
        this.tutorDAO.create(record);
        return record;
    }

    @Override
    public Tutor update(Tutor record) throws SQLException {
        tutorDAO.update(record);
        return record;
    }

    @Override
    public int delete(Tutor record) throws SQLException {
        return this.tutorDAO.delete(record);
    }

    @Override
    public List<Tutor> getAll() throws SQLException {
        return this.tutorDAO.queryForAll();
    }

    @Override
    public Tutor getById(int id) throws SQLException {
        return this.tutorDAO.queryForId(id);
    }

    @Override
    public void saveOrUpdateTutors(List<TutorDTO> tutorDTOS) throws SQLException {

        for(TutorDTO tutorDTO : tutorDTOS){
            boolean doesTutorExiste = this.tutorDAO.checkTutorExistance(tutorDTO.getUuid());

            if(!doesTutorExiste){

                Tutor tutor = new Tutor(tutorDTO);

                tutor.setCareer(careerService.getCareerByUuid(tutor.getCareer().getUuid()));
                Partner partner = this.partnerService.savedOrUpdatePartner(new Partner(tutorDTO.getPartnerDTO()));

                tutor.setPartner(partner);
                tutor.setUser(getCurrentUser());

                this.tutorDAO.createOrUpdate(tutor);
            }
        }

    }
}
