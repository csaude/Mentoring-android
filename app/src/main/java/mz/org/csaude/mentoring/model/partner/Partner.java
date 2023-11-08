package mz.org.csaude.mentoring.model.partner;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.partner.PartnerDaoImpl;
import mz.org.csaude.mentoring.dto.partner.PartnerDTO;
import mz.org.csaude.mentoring.dto.tutor.TutorDTO;
import mz.org.csaude.mentoring.model.user.UserIndividual;

@Data
@DatabaseTable(tableName = Partner.TABLE_NAME, daoClass = PartnerDaoImpl.class)
@EqualsAndHashCode(callSuper=false)
public class Partner extends BaseModel implements UserIndividual {

    public static final String TABLE_NAME = "partner";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_DESCRIPTION = "description";

    @DatabaseField(columnName = COLUMN_NAME, unique = true)
    private String name;

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    public Partner() {
    }

    public Partner(String name, String description, String uuid) {
        this.name = name;
        this.description = description;
        this.setUuid(uuid);
    }
    public Partner(PartnerDTO partnerDTO) {
        this.name = partnerDTO.getName();
        this.description = partnerDTO.getDescription();
        this.setUuid(partnerDTO.getUuid());
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
