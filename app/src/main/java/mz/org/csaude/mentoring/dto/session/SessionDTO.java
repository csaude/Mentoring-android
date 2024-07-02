package mz.org.csaude.mentoring.dto.session;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.dto.form.FormDTO;
import mz.org.csaude.mentoring.dto.ronda.RondaDTO;
import mz.org.csaude.mentoring.dto.tutored.TutoredDTO;
import mz.org.csaude.mentoring.model.session.Session;
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SessionDTO extends BaseEntityDTO {
    private Date startDate;
    private Date endDate;
    private Date performedDate;
    private SessionStatusDTO sessionStatus;
    private String reason;
    private FormDTO form;
    private RondaDTO ronda;
    private TutoredDTO mentee;
    private boolean demonstration;
    private String demonstrationDetails;
    private String strongPoints;
    private String pointsToImprove;
    private String workPlan;
    private String obsevations;

    public SessionDTO(Session session) {
        super(session);
        this.setStartDate(session.getStartDate());
        this.setEndDate(session.getEndDate());
        this.setPerformedDate(session.getPerformedDate());
        this.setPointsToImprove(session.getPointsToImprove());
        this.setStrongPoints(session.getStrongPoints());
        this.setObsevations(session.getObsevations());
        if(session.getStatus()!=null) {
            this.setSessionStatus(new SessionStatusDTO(session.getStatus()));
        }
        if(session.getForm()!=null) {
            this.setForm(new FormDTO(session.getForm()));
        }
        if(session.getRonda()!=null) {
            this.setRonda(new RondaDTO(session.getRonda()));
        }
        if(session.getTutored()!=null) {
            this.setMentee(new TutoredDTO(session.getTutored()));
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

    public SessionStatusDTO getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatusDTO sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public FormDTO getForm() {
        return form;
    }

    public void setForm(FormDTO form) {
        this.form = form;
    }

    public RondaDTO getRonda() {
        return ronda;
    }

    public void setRonda(RondaDTO ronda) {
        this.ronda = ronda;
    }

    public TutoredDTO getMentee() {
        return mentee;
    }

    public void setMentee(TutoredDTO mentee) {
        this.mentee = mentee;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Session getSession() {
        return new Session(this);
    }
}
