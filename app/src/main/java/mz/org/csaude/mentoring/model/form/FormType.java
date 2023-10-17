package mz.org.csaude.mentoring.model.form;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.career.CareerDAOImpl;
import mz.org.csaude.mentoring.dao.form.FormTypeDAOImpl;
import mz.org.csaude.mentoring.dto.form.FormTypeDTO;
import mz.org.csaude.mentoring.model.career.CareerType;

@Data
@NoArgsConstructor
@DatabaseTable(tableName = FormType.COLUMN_TABLE_NAME, daoClass = FormTypeDAOImpl.class)
@EqualsAndHashCode(callSuper=false)
public class FormType extends BaseModel {

    public static final String COLUMN_TABLE_NAME = "career_type";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CODE = "code";

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String descripion;

    @DatabaseField(columnName = COLUMN_CODE)
    private String code;

    public FormType(FormTypeDTO formTypeDTO) {
        this.descripion = formTypeDTO.getDescription();
        this.code = formTypeDTO.getCode();
    }

    public String getDescripion() {
        return descripion;
    }

    public void setDescripion(String descripion) {
        this.descripion = descripion;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
