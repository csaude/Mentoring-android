package mz.org.csaude.mentoring.dto.mentorship;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.common.Syncable;
import mz.org.csaude.mentoring.dto.answer.AnswerDTO;
import mz.org.csaude.mentoring.dto.evaluationType.EvaluationTypeDTO;
import mz.org.csaude.mentoring.dto.form.FormDTO;
import mz.org.csaude.mentoring.dto.location.CabinetDTO;
import mz.org.csaude.mentoring.dto.location.HealthFacilityDTO;
import mz.org.csaude.mentoring.dto.session.SessionDTO;
import mz.org.csaude.mentoring.dto.tutor.TutorDTO;
import mz.org.csaude.mentoring.dto.tutored.TutoredDTO;
import mz.org.csaude.mentoring.model.answer.Answer;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;
import mz.org.csaude.mentoring.util.SyncSatus;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MentorshipDTO extends BaseEntityDTO implements Syncable {
    private Integer iterationNumber;
    private Date startDate;
    private Date endDate;
    private TutorDTO mentor;
    private TutoredDTO mentee;
    private SessionDTO session;
    private FormDTO form;
    private CabinetDTO cabinet;
    private DoorDTO door;
    private EvaluationTypeDTO evaluationType;
    private boolean demonstration;
    private String demonstrationDetails;
    private List<AnswerDTO> answers;
    public MentorshipDTO(Mentorship mentorship) {
        super(mentorship);
        this.setStartDate(mentorship.getStartDate());
        this.setEndDate(mentorship.getEndDate());
        this.setIterationNumber(mentorship.getIterationNumber());
        this.setDemonstration(mentorship.isDemonstration());
        this.setDemonstrationDetails(mentorship.getDemonstrationDetails());
        if(mentorship.getTutor()!=null) {
            this.setMentor(new TutorDTO(mentorship.getTutor()));
        }
        if(mentorship.getTutored()!=null) {
            this.setMentee(new TutoredDTO(mentorship.getTutored()));
        }
        if(mentorship.getSession()!=null) {
            this.setSession(new SessionDTO(mentorship.getSession()));
        }
        if(mentorship.getForm()!=null) {
            this.setForm(new FormDTO(mentorship.getForm()));
        }
        if(mentorship.getCabinet()!=null) {
            this.setCabinet(new CabinetDTO(mentorship.getCabinet()));
        }
        if(mentorship.getDoor()!=null) {
            this.setDoor(new DoorDTO(mentorship.getDoor()));
        }
        if(mentorship.getEvaluationType()!=null) {
            this.setEvaluationType(new EvaluationTypeDTO(mentorship.getEvaluationType()));
        }
        if(mentorship.getAnswers()!=null) {
            List<AnswerDTO> answerDTOS = new ArrayList<>();
            for (Answer answer: mentorship.getAnswers()) {
                 answerDTOS.add(new AnswerDTO(answer));
                 this.setAnswers(answerDTOS);
            }
        }
    }

    public Integer getIterationNumber() {
        return iterationNumber;
    }

    public void setIterationNumber(Integer iterationNumber) {
        this.iterationNumber = iterationNumber;
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

    public TutorDTO getMentor() {
        return mentor;
    }

    public void setMentor(TutorDTO mentor) {
        this.mentor = mentor;
    }

    public TutoredDTO getMentee() {
        return mentee;
    }

    public void setMentee(TutoredDTO mentee) {
        this.mentee = mentee;
    }

    public SessionDTO getSession() {
        return session;
    }

    public void setSession(SessionDTO session) {
        this.session = session;
    }

    public FormDTO getForm() {
        return form;
    }

    public void setForm(FormDTO form) {
        this.form = form;
    }

    public CabinetDTO getCabinet() {
        return cabinet;
    }

    public void setCabinet(CabinetDTO cabinet) {
        this.cabinet = cabinet;
    }

    public DoorDTO getDoor() {
        return door;
    }

    public void setDoor(DoorDTO door) {
        this.door = door;
    }

    public EvaluationTypeDTO getEvaluationType() {
        return evaluationType;
    }
    public void setEvaluationType(EvaluationTypeDTO evaluationType) {
        this.evaluationType = evaluationType;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    public Mentorship getMentorship() {
        return new Mentorship(this);
    }

    @Override
    public void setSyncSatus(SyncSatus syncSatus) {
        this.syncSatus = syncSatus;
    }

    @Override
    public SyncSatus getSyncSatus() {
        return this.syncSatus;
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
