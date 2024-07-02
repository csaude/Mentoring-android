package mz.org.csaude.mentoring.model.ronda;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.ronda.RondaMentorDAOImpl;
import mz.org.csaude.mentoring.dto.ronda.RondaMentorDTO;
import mz.org.csaude.mentoring.model.tutor.Tutor;


@DatabaseTable(tableName = RondaMentor.TABLE_NAME, daoClass = RondaMentorDAOImpl.class)
public class RondaMentor extends BaseModel {

    public static final String TABLE_NAME = "ronda_mentor";
    public static final String COLUMN_RONDA = "ronda_id";
    public static final String COLUMN_TUTOR = "mentor_id";
    public static final String COLUMN_START_DATE = "start_date";

    public static final String COLUMN_END_DATE = "end_date";

    @DatabaseField(columnName = COLUMN_RONDA, canBeNull = false, foreign = true, foreignAutoRefresh = true )
    private Ronda ronda;

    @DatabaseField(columnName = COLUMN_TUTOR, canBeNull = false, foreign = true, foreignAutoRefresh = true )
    private Tutor tutor;

    @DatabaseField(columnName = COLUMN_START_DATE, canBeNull = false)
    private Date startDate;

    @DatabaseField(columnName = COLUMN_END_DATE, canBeNull = true)
    private Date endDate;

    public RondaMentor() {
    }

    public RondaMentor(RondaMentorDTO rondaMentorDTO) {
        super(rondaMentorDTO);
        this.setStartDate(rondaMentorDTO.getStartDate());
        this.setEndDate(rondaMentorDTO.getEndDate());
        if(rondaMentorDTO.getMentor()!=null) {
            this.setTutor(new Tutor(rondaMentorDTO.getMentor()));
        }
        if(rondaMentorDTO.getRonda()!=null) {
            this.setRonda(new Ronda(rondaMentorDTO.getRonda()));
        }
    }

    public Ronda getRonda() {
        return ronda;
    }

    public void setRonda(Ronda ronda) {
        this.ronda = ronda;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
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

    public boolean isActive() {
        return getEndDate() == null;
    }
}
