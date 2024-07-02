package mz.org.csaude.mentoring.model.mentorship;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.mentorship.DoorDAOImpl;
import mz.org.csaude.mentoring.dto.mentorship.DoorDTO;

@Data
@DatabaseTable(tableName = Door.TABLE_NAME, daoClass = DoorDAOImpl.class)
@EqualsAndHashCode(callSuper=false)
public class Door extends BaseModel {

    public static final String TABLE_NAME = "door";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_CODE = "code";

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COLUMN_CODE, unique = true)
    private String code;
    public Door() {
    }

    public Door(DoorDTO doorDTO) {
        super(doorDTO);
        this.setDescription(doorDTO.getDescription());
        this.setCode(doorDTO.getCode());
    }

    public Door(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
