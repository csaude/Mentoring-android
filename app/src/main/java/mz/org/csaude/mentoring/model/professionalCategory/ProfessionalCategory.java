package mz.org.csaude.mentoring.model.professionalCategory;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.professionalCategoryDAO.ProfessionalCategoryDAOImpl;
import mz.org.csaude.mentoring.dto.professionalcategory.ProfessionalCategoryDTO;


@DatabaseTable(tableName = ProfessionalCategory.TABLE_NAME, daoClass = ProfessionalCategoryDAOImpl.class)
public class ProfessionalCategory extends BaseModel implements Listble{

    public static final String TABLE_NAME = "professional_category";

    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CODE = "code";

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    public ProfessionalCategory() {
    }

    @DatabaseField(columnName = COLUMN_CODE)
    private  String code;


    public ProfessionalCategory(ProfessionalCategoryDTO professionalCategoryDTO) {
        super(professionalCategoryDTO);
        this.setDescription(professionalCategoryDTO.getDescription());
        this.setCode(professionalCategoryDTO.getCode());
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getDrawable() {
        return 0;
    }

    @Override
    public String getCode() {
        return null;
    }
}
