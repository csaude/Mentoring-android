package mz.org.csaude.mentoring.model.session;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.session.SessionDAOImpl;
import mz.org.csaude.mentoring.dto.session.SessionDTO;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.tutored.Tutored;

@Data
@DatabaseTable(tableName = Session.TABLE_NAME, daoClass = SessionDAOImpl.class)
@EqualsAndHashCode(callSuper=true)
public class Session extends BaseModel {

    public static final String TABLE_NAME = "session";

    public static final String COLUMN_START_DATE = "start_date";

    public static final String COLUMN_END_DATE = "end_date";

    public static final String COLUMN_PERFORMED_DATE = "performed_date";

    public static final String COLUMN_STATUS = "session_status_id";

    public static final String COLUMN_RONDA = "ronda_id";

    public static final String COLUMN_MENTEE = "mentee_id";

    public static final String COLUMN_FORM = "form_id";


    public static final String COLUMN_STRONG_POINTS = "strong_points";
    public static final String COLUMN_WEAK_POINTS = "points_to_improve";
    public static final String COLUMN_WORK_PLAN = "work_plan";
    public static final String COLUMN_OBSERVATIONS = "observations";

    @DatabaseField(columnName = COLUMN_START_DATE, canBeNull = false)
    private Date startDate;

    @DatabaseField(columnName = COLUMN_END_DATE)
    private Date endDate;

    @DatabaseField(columnName = COLUMN_PERFORMED_DATE)
    private Date performedDate;

    @DatabaseField(columnName = COLUMN_STATUS, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private SessionStatus status;

    @DatabaseField(columnName = COLUMN_RONDA, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Ronda ronda;

    @DatabaseField(columnName = COLUMN_MENTEE, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Tutored tutored;

    @DatabaseField(columnName = COLUMN_FORM, canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Form form;
    private List<Mentorship> mentorships;


    @DatabaseField(columnName = COLUMN_STRONG_POINTS)
    private String strongPoints;
    @DatabaseField(columnName = COLUMN_WEAK_POINTS)
    private String pointsToImprove;
    @DatabaseField(columnName = COLUMN_WORK_PLAN)
    private String workPlan;
    @DatabaseField(columnName = COLUMN_OBSERVATIONS)
    private String obsevations;

    public Session() {
    }

    public Session(SessionDTO sessionDTO) {
        super(sessionDTO);
        this.setStartDate(sessionDTO.getStartDate());
        this.setEndDate(sessionDTO.getEndDate());
        this.setPerformedDate(sessionDTO.getPerformedDate());
        this.setPointsToImprove(sessionDTO.getPointsToImprove());
        this.setStrongPoints(sessionDTO.getStrongPoints());
        this.setObsevations(sessionDTO.getObsevations());
        if(sessionDTO.getSessionStatus()!=null) {
            this.setStatus(new SessionStatus(sessionDTO.getSessionStatus()));
        }
        if(sessionDTO.getMentee()!=null) {
            this.setTutored(new Tutored(sessionDTO.getMentee()));
        }
        if(sessionDTO.getRonda()!=null) {
            this.setRonda(new Ronda(sessionDTO.getRonda()));
        }
        if(sessionDTO.getForm()!=null) {
            this.setForm(new Form(sessionDTO.getForm()));
        }
    }

    public Session(Date startDate, Date endDate, Date performedDate, SessionStatus status) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.performedDate = performedDate;
        this.status = status;
    }

    public Ronda getRonda() {
        return ronda;
    }

    public void setRonda(Ronda ronda) {
        this.ronda = ronda;
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

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public List<Mentorship> getMentorships() {
        return mentorships;
    }
    public void addMentorship(Mentorship mentorship) {
        if (mentorships == null) {
            mentorships = new ArrayList<>();
        }
        mentorships.add(mentorship);
    }

    public void setMentorships(List<Mentorship> mentorships) {
        this.mentorships = mentorships;
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

    public boolean isCompleted() {
        return this.status.isCompleted();
    }

    public boolean canBeClosed() {
        if (mentorships == null) {
            // Handle the null case, possibly return false or handle as per your requirement
            return false;
        }

        int completedPatient = 0;
        int completedFile = 0;

        for (Mentorship mentorship : mentorships) {
            if (mentorship.isCompleted()) {
                if (mentorship.isPatientEvaluation()) {
                    completedPatient++;
                } else if (mentorship.isFileEvaluation()) {
                    completedFile++;
                }
            }
        }

        return completedPatient == form.getTargetPatient() && completedFile == form.getTargetFile();
    }


    public void addMentorships(List<Mentorship> completedMentorships) {
        for (Mentorship mentorship : completedMentorships) {
            if (!this.mentorships.contains(mentorship)) {
                this.mentorships.add(mentorship);
            }
        }
    }



    public String getStrongPoints() {
        return strongPoints;
    }

    public void setStrongPoints(String strongPoints) {
        this.strongPoints = strongPoints;
    }

    public String getPointsToImprove() {
        return pointsToImprove;
    }

    public void setPointsToImprove(String pointsToImprove) {
        this.pointsToImprove = pointsToImprove;
    }

    public String getWorkPlan() {
        return workPlan;
    }

    public void setWorkPlan(String workPlan) {
        this.workPlan = workPlan;
    }

    public String getObsevations() {
        return obsevations;
    }

    public void setObsevations(String obsevations) {
        this.obsevations = obsevations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Session session = (Session) o;
        return Objects.equals(ronda, session.ronda) && Objects.equals(tutored, session.tutored) && Objects.equals(form, session.form);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ronda, tutored, form);
    }
}
