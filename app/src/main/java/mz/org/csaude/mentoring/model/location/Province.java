package mz.org.csaude.mentoring.model.location;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.location.ProvinceDAOImpl;

@Data
@NoArgsConstructor
@DatabaseTable(tableName = Province.COLUMN_TABLE_NAME, daoClass = ProvinceDAOImpl.class)
@EqualsAndHashCode(callSuper = false)
public class Province extends BaseModel {

    public static final String COLUMN_TABLE_NAME = "province";

    public static final String COLUMN_DESIGNATION = "designation";

    @DatabaseField(columnName = COLUMN_DESIGNATION, canBeNull = false, unique = true)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
