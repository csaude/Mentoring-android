package mz.org.csaude.mentoring.model.answer;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.answer.AnswerTypeDAOImpl;
import mz.org.csaude.mentoring.dao.setting.SettingDAOImpl;
import mz.org.csaude.mentoring.model.setting.Setting;

@Data
@DatabaseTable(tableName = AnswerType.TABLE_NAME, daoClass = AnswerTypeDAOImpl.class)
public class AnswerType extends BaseModel {

    public static final String TABLE_NAME = "answer_type";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_DESCRIPTION = "description";

    @DatabaseField(columnName = COLUMN_NAME)
    private String name;

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String Description;

    public AnswerType() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return Description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
