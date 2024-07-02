package mz.org.csaude.mentoring.model.evaluationType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.evaluation.EvaluationTypeDAOImpl;
import mz.org.csaude.mentoring.dto.evaluationType.EvaluationTypeDTO;

@Data
@DatabaseTable(tableName = EvaluationType.TABLE_NAME, daoClass = EvaluationTypeDAOImpl.class)
@EqualsAndHashCode(callSuper=false)
public class EvaluationType extends BaseModel {

    public static final String TABLE_NAME = "evaluation_type";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_CODE = "code";

    public static final String CONSULTA = "Consulta";
    public static final String FICHA = "Ficha";

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COLUMN_CODE, unique = true)
    private String code;

    public EvaluationType() {
    }

    public EvaluationType(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public EvaluationType(EvaluationTypeDTO evaluationType) {
        super(evaluationType);
        this.description = evaluationType.getDescription();
        this.code = evaluationType.getCode();
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

    @JsonIgnore
    public boolean isPatientEvaluation() {
        return this.code.equals(EvaluationType.CONSULTA);
    }
    @JsonIgnore
    public boolean isFichaEvaluation() {
        return this.code.equals(EvaluationType.FICHA);
    }
}
