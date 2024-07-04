package mz.org.csaude.mentoring.dto.answer;

import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.dto.form.FormDTO;
import mz.org.csaude.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.csaude.mentoring.dto.question.QuestionDTO;
import mz.org.csaude.mentoring.model.answer.Answer;

public class AnswerDTO extends BaseEntityDTO {

    private String value;

    private FormDTO form;

    private MentorshipDTO mentorship;

    private QuestionDTO question;

    public AnswerDTO() {

    }
    public AnswerDTO(Answer answer) {
        super(answer);
        this.setValue(answer.getValue());
        /*if(answer.getMentorship()!=null) {
            this.setMentorship(new MentorshipDTO(answer.getMentorship()));
        }*/
        if(answer.getForm()!=null) {
            this.setForm(new FormDTO(answer.getForm()));
        }
        if(answer.getQuestion()!=null) {
            this.setQuestion(new QuestionDTO(answer.getQuestion()));
        }
    }

    public Answer getAnswer() {
        return new Answer(this);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FormDTO getForm() {
        return form;
    }

    public void setForm(FormDTO form) {
        this.form = form;
    }

    public MentorshipDTO getMentorship() {
        return mentorship;
    }

    public void setMentorship(MentorshipDTO mentorship) {
        this.mentorship = mentorship;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }
}
