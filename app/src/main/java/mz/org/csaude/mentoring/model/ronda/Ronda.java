package mz.org.csaude.mentoring.model.ronda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.ronda.RondaDAOImpl;
import mz.org.csaude.mentoring.dto.ronda.RondaDTO;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.rondatype.RondaType;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.util.DateUtilities;
import mz.org.csaude.mentoring.util.RondaStatus;
import mz.org.csaude.mentoring.util.SyncSatus;
import mz.org.csaude.mentoring.util.Utilities;


@DatabaseTable(tableName = Ronda.TABLE_NAME, daoClass = RondaDAOImpl.class)
public class Ronda extends BaseModel implements Listble {

    public static final String TABLE_NAME = "ronda";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";

    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_HEALTH_FACILITY = "health_facility_id";
    public static final String COLUMN_RONDA_TYPE = "ronda_type_id";
    @DatabaseField(columnName = COLUMN_DESCRIPTION,  canBeNull = false)
    private String description;
    @DatabaseField(columnName = COLUMN_START_DATE, canBeNull = false)
    private Date startDate;

    @DatabaseField(columnName = COLUMN_END_DATE, canBeNull = true)
    private Date endDate;
    @DatabaseField(columnName = COLUMN_HEALTH_FACILITY, canBeNull = false, foreign = true, foreignAutoRefresh = true )
    private HealthFacility healthFacility;
    @DatabaseField(columnName = COLUMN_RONDA_TYPE, canBeNull = false, foreign = true, foreignAutoRefresh = true )
    private RondaType rondaType;

    private List<Session> sessions;
    private List<RondaMentee> rondaMentees;
    private List<RondaMentor> rondaMentors;
    public Ronda () {
    }

    public Ronda(RondaDTO rondaDTO) {
        super(rondaDTO);
        this.setDescription(rondaDTO.getDescription());
        this.setStartDate(rondaDTO.getStartDate());
        this.setEndDate(rondaDTO.getEndDate());
        this.setRondaType(new RondaType(rondaDTO.getRondaType()));
        if(rondaDTO.getHealthFacility()!=null) {
            this.setHealthFacility(new HealthFacility(rondaDTO.getHealthFacility()));
        }
        if (Utilities.listHasElements(rondaDTO.getRondaMentors())) {
            List<RondaMentor> rondaMentors = rondaDTO.getRondaMentors().stream()
                    .map(RondaMentor::new)
                    .collect(Collectors.toList());
            this.setRondaMentors(rondaMentors);
        }

        if (Utilities.listHasElements(rondaDTO.getRondaMentees())) {
            List<RondaMentee> rondaMentees = rondaDTO.getRondaMentees().stream()
                    .map(RondaMentee::new)
                    .collect(Collectors.toList());
            this.setRondaMentees(rondaMentees);
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
    public HealthFacility getHealthFacility() {
        return healthFacility;
    }
    public void setHealthFacility(HealthFacility healthFacility) {
        this.healthFacility = healthFacility;
    }
    public RondaType getRondaType() {
        return rondaType;
    }
    public void setRondaType(RondaType rondaType) {
        this.rondaType = rondaType;
    }
    public List<RondaMentee> getRondaMentees() {
        return rondaMentees;
    }

    public void setRondaMentees(List<RondaMentee> rondaMentees) {
        this.rondaMentees = rondaMentees;
    }

    public List<RondaMentor> getRondaMentors() {
        return rondaMentors;
    }

    public void setRondaMentors(List<RondaMentor> rondaMentors) {
        this.rondaMentors = rondaMentors;
    }
    @Override
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getDrawable() {
        return 0;
    }

    @Override
    public String getCode() {
        return null;
    }

    public String getRondaPeriod() {
        String period = this.getEndDate() == null ? DateUtilities.parseDateToDDMMYYYYString(this.getStartDate())
                : DateUtilities.parseDateToDDMMYYYYString(this.getStartDate()).concat(" - ").concat(DateUtilities.parseDateToDDMMYYYYString(this.getEndDate()));
        return period;
    }

    public String getRondaExuctionStatus() {
        return !this.isRondaCompleted() ? RondaStatus.ON_GOING.toString() : RondaStatus.FINISHED.toString();
    }

    @JsonIgnore
    public boolean isClosed() {
        return this.getEndDate() != null;
    }
    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    @JsonIgnore
    public boolean isRondaZero() {
        return this.rondaType.getCode().equals("SESSAO_ZERO");
    }

    public void addSession(Session session) {
        if(this.sessions == null) this.sessions = new ArrayList<>();
        if (!this.sessions.contains(session)){
            this.sessions.add(session);
        }
    }

    public void addSession(List<Session> sessions) {
        for (Session session : sessions) {
            this.addSession(session);
        }
    }
    public void removeSession(Session session) {
        if(this.sessions == null) return;
        this.sessions.remove(session);
    }
    public void tryToCloseRonda() {
        boolean allSessionsClosed = true;

        for (RondaMentee rondaMentee : rondaMentees) {
            if (!allMenteeSessionsClosed(rondaMentee.getTutored())) {
                allSessionsClosed = false;
                break;
            }
        }

        if (allSessionsClosed) {
            this.setEndDate(DateUtilities.getCurrentDate());
        }
    }

    private boolean allMenteeSessionsClosed(Tutored tutored) {
        if (!menteeHasFourSessions(tutored)) return false;

        for (Session session : sessions) {
            if (session.getTutored().equals(tutored) && !session.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    private boolean menteeHasFourSessions(Tutored tutored) {
        return this.sessions.stream().filter(session -> session.getTutored().equals(tutored)).count() == 4;
    }

    public boolean isRondaCompleted() {
        return this.getEndDate() != null;
    }

    private boolean hasSessionClosed(Tutored tutored) {
        for (Session session : sessions) {
            if (session.getTutored().equals(tutored) && session.isCompleted()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Ronda ronda = (Ronda) o;
        return Objects.equals(startDate, ronda.startDate) && Objects.equals(healthFacility, ronda.healthFacility) && Objects.equals(rondaType, ronda.rondaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startDate, healthFacility, rondaType);
    }

    public Tutor getActiveMentor() {
        if(this.getRondaMentors() == null) return null;
        for (RondaMentor rondaMentor : rondaMentors) {
            if (rondaMentor.isActive()) {
                return rondaMentor.getTutor();
            }
        }
        return null;
    }
}
