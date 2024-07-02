package mz.org.csaude.mentoring.model.location;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.location.CabinetDAOImpl;
import mz.org.csaude.mentoring.dto.location.CabinetDTO;

@Data
@DatabaseTable(tableName = Cabinet.TABLE_NAME, daoClass = CabinetDAOImpl.class)
@EqualsAndHashCode(callSuper=false)
public class Cabinet extends BaseModel {
    public static final String TABLE_NAME = "cabinet";

    public static final String COLUMN_NAME = "name";

    @DatabaseField(columnName = COLUMN_NAME, unique = true, canBeNull = false)
    private String name;

    public Cabinet() {
    }

    public Cabinet(String name) {
        this.name = name;
    }
    public Cabinet(CabinetDTO dto) {
        super(dto);
        this.setName(dto.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return name;
    }
}
