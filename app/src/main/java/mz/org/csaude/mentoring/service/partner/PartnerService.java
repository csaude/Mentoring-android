package mz.org.csaude.mentoring.service.partner;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseService;
import mz.org.csaude.mentoring.dto.partner.PartnerDTO;
import mz.org.csaude.mentoring.model.partner.Partner;

public interface PartnerService extends BaseService<Partner> {

    void saveOrUpdatePartners(List<PartnerDTO> partnerDTOS) throws SQLException;
    Partner savedOrUpdatePartner(Partner partner) throws SQLException;
}
