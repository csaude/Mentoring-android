package mz.org.csaude.mentoring.service.partner;

import android.app.Application;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.partner.PartnerDao;
import mz.org.csaude.mentoring.dto.partner.PartnerDTO;
import mz.org.csaude.mentoring.model.partner.Partner;
import mz.org.csaude.mentoring.model.user.User;

public class PartnerServiceImpl extends BaseServiceImpl<Partner> implements PartnerService {

    PartnerDao partnerDao;

    public PartnerServiceImpl(Application application, User currentUser) {
        super(application, currentUser);
    }

    public PartnerServiceImpl(Application application) {
        super(application);
    }

    @Override
    public void init(Application application, User currentUser) throws SQLException{
        super.init(application, currentUser);
        this.partnerDao = getDataBaseHelper().getPartnerDao();
    }


    @Override
    public Partner save(Partner record) throws SQLException {
        this.partnerDao.create(record);
        return record;
    }

    @Override
    public Partner update(Partner record) throws SQLException {
        this.partnerDao.update(record);
        return record;
    }

    @Override
    public int delete(Partner record) throws SQLException {
        return this.partnerDao.delete(record);
    }

    @Override
    public List<Partner> getAll() throws SQLException {
        return this.partnerDao.queryForAll();
    }

    @Override
    public Partner getById(int id) throws SQLException {
        return this.getById(id);
    }

    @Override
    public void saveOrUpdatePartners(List<PartnerDTO> partnerDTOS) throws SQLException {

        for(PartnerDTO partnerDTO : partnerDTOS){

            boolean partinerExistance = this.partnerDao.checkPartnerExistance(partnerDTO.getUuid());
            if(partinerExistance){
                 this.partnerDao.createOrUpdate(new Partner(partnerDTO));
            }

        }

    }

    @Override
    public Partner savedOrUpdatePartner(Partner partner) throws SQLException {

       List<Partner> partners = this.partnerDao.queryForEq("uuid", partner.getUuid());

       if(partners.isEmpty()){
           this.partnerDao.createOrUpdate(partner);
           return partner;
       }
        return partners.get(0);
    }
}
