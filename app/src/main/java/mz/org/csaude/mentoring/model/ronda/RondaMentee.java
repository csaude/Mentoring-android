package mz.org.csaude.mentoring.model.ronda;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.ronda.RondaMenteeDAOImpl;
import mz.org.csaude.mentoring.dto.ronda.RondaMenteeDTO;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.util.DateUtilities;


@DatabaseTable(tableName = RondaMentee.TABLE_NAME, daoClass = RondaMenteeDAOImpl.class)
public class RondaMentee extends BaseModel {
    public static final String TABLE_NAME = "ronda_mentee";
    public static final String COLUMN_RONDA = "ronda_id";
    public static final String COLUMN_MENTEE = "mentee_id";
    public static final String COLUMN_START_DATE = "start_date";

    public static final String COLUMN_END_DATE = "end_date";

    public RondaMentee() {
    }

    public RondaMentee(Ronda ronda, Tutored tutored, Date startDate) {
        this.ronda = ronda;
        this.tutored = tutored;
        this.startDate = startDate;
    }

    @DatabaseField(columnName = COLUMN_RONDA, canBeNull = false, foreign = true, foreignAutoRefresh = true )
    private Ronda ronda;

    @DatabaseField(columnName = COLUMN_MENTEE, canBeNull = false, foreign = true, foreignAutoRefresh = true )
    private Tutored tutored;

    @DatabaseField(columnName = COLUMN_START_DATE, canBeNull = false)
    private Date startDate;

    @DatabaseField(columnName = COLUMN_END_DATE)
    private Date endDate;

    public RondaMentee(RondaMenteeDTO rondaMenteeDTO) {
        super(rondaMenteeDTO);
        this.setStartDate(rondaMenteeDTO.getStartDate());
        this.setEndDate(rondaMenteeDTO.getEndDate());
        if(rondaMenteeDTO.getMentee()!=null) {
            this.setTutored(new Tutored(rondaMenteeDTO.getMentee()));
        }
        if(rondaMenteeDTO.getRonda()!=null) {
            this.setRonda(new Ronda(rondaMenteeDTO.getRonda()));
        }
    }


    public static RondaMentee fastCreate(Ronda ronda, Tutored mentee) {
        return new RondaMentee(ronda, mentee, DateUtilities.getCurrentDate());
    }

    public Ronda getRonda() {
        return ronda;
    }

    public void setRonda(Ronda ronda) {
        this.ronda = ronda;
    }

    public Tutored getTutored() {
        return tutored;
    }

    public void setTutored(Tutored tutored) {
        this.tutored = tutored;
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
}
