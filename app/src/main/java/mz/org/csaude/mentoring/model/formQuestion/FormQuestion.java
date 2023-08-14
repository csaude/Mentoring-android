package mz.org.csaude.mentoring.model.formQuestion;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.answer.AnswerTypeDAOImpl;
import mz.org.csaude.mentoring.dao.formQuestion.FormQuestionDAImpl;
import mz.org.csaude.mentoring.model.Question.Question;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.setting.Setting;

@Data
@DatabaseTable(tableName = FormQuestion.TABLE_NAME, daoClass = FormQuestionDAImpl.class)
public class FormQuestion extends BaseModel {

    public static final String TABLE_NAME = "form_question";
    public static final String COLUMN_FORM = "form";

    public static final String COLUMN_QUESTION = "question";

    public static final String COLUMN_MANDATORY = "mandatory";

    public static final String COLUMN_SEQUENCE = "sequence";

    public static final String COLUMN_APPLICABLE = "applicable";

    @DatabaseField(columnName = COLUMN_FORM)
    private Form form;

    @DatabaseField(columnName = COLUMN_QUESTION)
    private Question question;

    @DatabaseField(columnName = COLUMN_MANDATORY)
    private boolean mandatory;

    @DatabaseField(columnName = COLUMN_SEQUENCE)
    private Integer sequence;

    @DatabaseField(columnName = COLUMN_APPLICABLE)
    private Boolean applicable;

    public FormQuestion() {
    }

    public Form getForm() {
        return form;
    }

    public Question getQuestion() {
        return question;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public Integer getSequence() {
        return sequence;
    }

    public Boolean getApplicable() {
        return applicable;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public void setApplicable(Boolean applicable) {
        this.applicable = applicable;
    }
}
