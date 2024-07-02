package mz.org.csaude.mentoring.model.answer;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.EqualsAndHashCode;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.answer.AnswerDAOImpl;
import mz.org.csaude.mentoring.dto.answer.AnswerDTO;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.formQuestion.FormQuestion;
import mz.org.csaude.mentoring.model.indicator.Indicator;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;
import mz.org.csaude.mentoring.model.question.Question;


@DatabaseTable(tableName = Answer.TABLE_NAME, daoClass = AnswerDAOImpl.class)
@EqualsAndHashCode(callSuper=false)
public class Answer extends BaseModel {

    public static final String TABLE_NAME = "answer";

    public static final String COLUMN_FORM = "form_id";

    public static final String COLUMN_MENTORSHIP = "mentorship_id";

    public static final String COLUMN_QUESTION = "question_id";

    public static final String COLUMN_VALUE = "value";

    @DatabaseField(columnName = COLUMN_FORM, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Form form;

    @DatabaseField(columnName = COLUMN_MENTORSHIP, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Mentorship mentorship;

    @DatabaseField(columnName = COLUMN_QUESTION, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Question question;

    @DatabaseField(columnName = COLUMN_VALUE)
    private String value;

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public Answer() {
    }
    public Answer(AnswerDTO answerDTO) {
        super(answerDTO);
        this.setValue(answerDTO.getValue());
        if(answerDTO.getForm()!=null) {
            this.setForm(answerDTO.getForm().getForm());
        }
        if(answerDTO.getQuestion()!=null) {
            this.setQuestion(answerDTO.getQuestion().getQuestionObj());
        }
        if(answerDTO.getMentorship()!=null) {
            this.setMentorship(new Mentorship(answerDTO.getMentorship()));
        }
    }

    public Form getForm() {
        return form;
    }

    public Mentorship getMentorship() {
        return mentorship;
    }

    public Question getQuestion() {
        return question;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setMentorship(Mentorship mentorship) {
        this.mentorship = mentorship;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}
