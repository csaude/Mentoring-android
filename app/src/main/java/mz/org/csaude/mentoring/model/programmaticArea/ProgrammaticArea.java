package mz.org.csaude.mentoring.model.programmaticArea;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.programmaticArea.ProgrammaticAreaDAOImpl;

@Data
@DatabaseTable(tableName = ProgrammaticArea.TABLE_NAME, daoClass = ProgrammaticAreaDAOImpl.class)
@EqualsAndHashCode(callSuper=false)
public class ProgrammaticArea extends BaseModel {

    public static final String TABLE_NAME = "programmatic_area";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COLUMN_CODE, unique = true)
    private String code;

    @DatabaseField(columnName = COLUMN_NAME, canBeNull = false)
    private String name;

    public ProgrammaticArea() {
    }

    public ProgrammaticArea(String description, String code, String name) {
        this.description = description;
        this.code = code;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
