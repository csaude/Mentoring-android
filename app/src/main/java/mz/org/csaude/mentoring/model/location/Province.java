package mz.org.csaude.mentoring.model.location;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.location.ProvinceDAOImpl;
import mz.org.csaude.mentoring.dto.location.ProvinceDTO;

@DatabaseTable(tableName = Province.COLUMN_TABLE_NAME, daoClass = ProvinceDAOImpl.class)
public class Province extends BaseModel implements Listble {

    public static final String COLUMN_TABLE_NAME = "province";

    public static final String COLUMN_DESIGNATION = "designation";

    @DatabaseField(columnName = COLUMN_DESIGNATION)
    private String designation;

    public String getDescription() {
        return designation;
    }

    @Override
    public int getDrawable() {
        return 0;
    }

    @Override
    public String getCode() {
        return null;
    }

    public void setDescription(String description) {
        this.designation = description;
    }

    public Province() {
    }

    public Province(ProvinceDTO provinceDTO) {
        super(provinceDTO);
        this.setDescription(provinceDTO.getDesignation());
    }


}
