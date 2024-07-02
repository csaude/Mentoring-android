package mz.org.csaude.mentoring.dto.partner;

import lombok.Data;
import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.model.partner.Partner;
@Data
public class PartnerDTO extends BaseEntityDTO {
    private String name;
    private String description;

    public PartnerDTO() {

    }

    public PartnerDTO(Partner partner) {
        this.setUuid(partner.getUuid());
        this.name = partner.getName();
        this.description = partner.getDescription();
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Partner getPartner() {
        return new Partner(this);
    }
}
