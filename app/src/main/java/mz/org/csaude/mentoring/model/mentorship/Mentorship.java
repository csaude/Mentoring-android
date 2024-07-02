package mz.org.csaude.mentoring.model.mentorship;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.mentorship.MentorshipDAOImpl;
import mz.org.csaude.mentoring.dto.answer.AnswerDTO;
import mz.org.csaude.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.csaude.mentoring.model.answer.Answer;
import mz.org.csaude.mentoring.model.evaluationType.EvaluationType;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.location.Cabinet;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.tutored.Tutored;

@Data
@DatabaseTable(tableName = Mentorship.TABLE_NAME, daoClass = MentorshipDAOImpl.class)
@EqualsAndHashCode(callSuper=true)
public class Mentorship extends BaseModel {

    public static final String TABLE_NAME = "mentorship";

    public static final String COLUMN_START_DATE = "start_date";

    public static final String COLUMN_END_DATE = "end_date";

    public static final String COLUMN_PERFORMED_DATE = "performed_date";

    public static final String COLUMN_TUTOR = "tutor_id";

    public static final String COLUMN_DEMOSTRATION_DETAILS = "demonstration_details";

    public static final String COLUMN_TUTORED = "tutored_id";

    public static final String COLUMN_FORM = "form_id";

    public static final String COLUMN_SESSION = "session_id";

    public static final String COLUMN_DEMOSTRATION = "demonstration";
    public static final String COLUMN_CABINET = "cabinet_id";

    public static final String COLUMN_ITERATION_TYPE = "iteration_type_id";

    public static final String COLUMN_ITERATION_NUMBER = "iteration_number";

    public static final String COLUMN_DOOR = "door_id";

    @DatabaseField(columnName = COLUMN_START_DATE, canBeNull = false)
    private Date startDate;

    @DatabaseField(columnName = COLUMN_END_DATE, canBeNull = false)
    private Date endDate;

    @DatabaseField(columnName = COLUMN_PERFORMED_DATE, canBeNull = false)
    private Date performedDate;

    @DatabaseField(columnName = COLUMN_TUTOR, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Tutor tutor;

    @DatabaseField(columnName = COLUMN_TUTORED, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Tutored tutored;

    @DatabaseField(columnName = COLUMN_FORM, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Form form;

    @DatabaseField(columnName = COLUMN_SESSION, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Session session;

    @DatabaseField(columnName = COLUMN_CABINET, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Cabinet cabinet;

    @DatabaseField(columnName = COLUMN_ITERATION_TYPE, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private EvaluationType evaluationType;

    @DatabaseField(columnName = COLUMN_ITERATION_NUMBER, canBeNull = false)
    private Integer iterationNumber;

    @DatabaseField(columnName = COLUMN_DOOR, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Door door;

    @DatabaseField(columnName = COLUMN_DEMOSTRATION)
    private boolean demonstration;

    @DatabaseField(columnName = COLUMN_DEMOSTRATION_DETAILS)
    private String demonstrationDetails;
    private List<Answer> answers;

    public Mentorship() {
    }

    public Mentorship(MentorshipDTO mentorshipDTO) {
        super(mentorshipDTO);
        this.setStartDate(this.getStartDate());
        this.setEndDate(this.getEndDate());
        this.setIterationNumber(this.getIterationNumber());
        this.setDemonstration(mentorshipDTO.isDemonstration());
        this.setDemonstrationDetails(mentorshipDTO.getDemonstrationDetails());

        if(mentorshipDTO.getMentor()!=null) {
            this.setTutor(new Tutor(mentorshipDTO.getMentor()));
        }
        if(mentorshipDTO.getMentee()!=null) {
            this.setTutored(new Tutored(mentorshipDTO.getMentee()));
        }
        if(mentorshipDTO.getSession()!=null) {
            this.setSession(new Session(mentorshipDTO.getSession()));
        }
        if(mentorshipDTO.getForm()!=null) {
            this.setForm(new Form(mentorshipDTO.getForm()));
        }
        if(mentorshipDTO.getCabinet()!=null) {
            this.setCabinet(new Cabinet(mentorshipDTO.getCabinet()));
        }
        if(mentorshipDTO.getDoor()!=null) {
            this.setDoor(new Door(mentorshipDTO.getDoor()));
        }
        if(mentorshipDTO.getEvaluationType()!=null) {
            this.setEvaluationType(new EvaluationType(mentorshipDTO.getEvaluationType()));
        }
        if(mentorshipDTO.getAnswers()!=null) {
            List<Answer> answerList = new ArrayList<>();
            for (AnswerDTO answerDTO: mentorshipDTO.getAnswers()) {
                answerList.add(new Answer(answerDTO));
                this.setAnswers(answerList);
            }
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getPerformedDate() {
        return performedDate;
    }

    public void setPerformedDate(Date performedDate) {
        this.performedDate = performedDate;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Tutored getTutored() {
        return tutored;
    }

    public void setTutored(Tutored tutored) {
        this.tutored = tutored;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Cabinet getCabinet() {
        return cabinet;
    }

    public void setCabinet(Cabinet cabinet) {
        this.cabinet = cabinet;
    }


    public EvaluationType getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(EvaluationType evaluationType) {
        this.evaluationType = evaluationType;
    }

    public Integer getIterationNumber() {
        return iterationNumber;
    }

    public void setIterationNumber(Integer iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        if (answers == null) answers = new ArrayList<>();
        answers.add(answer);
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Door getDoor() {
        return door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public String getEvaluationTypeDestription(){
        return "Avaliação de " + evaluationType.getDescription();
    }
    @Override
    public String toString() {
        return "Mentorship{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", performedDate=" + performedDate +
                ", tutor=" + tutor +
                ", tutored=" + tutored +
                ", form=" + form +
                ", cabinet=" + cabinet +
                ", evaluationType=" + evaluationType +
                ", iterationNumber=" + iterationNumber +
                ", door=" + door +
                '}';
    }

    public boolean isCompleted() {
        return this.endDate != null;
    }

    public boolean isPatientEvaluation() {
        return this.evaluationType.isPatientEvaluation();
    }

    public boolean isFileEvaluation() {
        return this.evaluationType.isFichaEvaluation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mentorship)) return false;
        if (!super.equals(o)) return false;
        Mentorship that = (Mentorship) o;
        return Objects.equals(tutor, that.tutor) && Objects.equals(tutored, that.tutored) && Objects.equals(form, that.form) && Objects.equals(session, that.session) && Objects.equals(evaluationType, that.evaluationType) && Objects.equals(iterationNumber, that.iterationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tutor, tutored, form, session, evaluationType, iterationNumber);
    }

    public boolean isDemonstration() {
        return demonstration;
    }

    public void setDemonstration(boolean demonstration) {
        this.demonstration = demonstration;
    }

    public String getDemonstrationDetails() {
        return demonstrationDetails;
    }

    public void setDemonstrationDetails(String demonstrationDetails) {
        this.demonstrationDetails = demonstrationDetails;
    }
}
