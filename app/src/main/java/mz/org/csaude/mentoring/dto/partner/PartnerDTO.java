package mz.org.csaude.mentoring.dto.partner;


import java.io.Serializable;

import mz.org.csaude.mentoring.model.partner.Partner;

public class PartnerDTO implements Serializable {

    private String uuid;
    private String name;

    private String description;

    public PartnerDTO() {
    }

    public PartnerDTO(Partner partner) {
        this.name = partner.getName();
        this.description = partner.getDescription();
        this.uuid = partner.getUuid();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
