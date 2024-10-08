package mz.org.csaude.mentoring.model.ronda;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.Date;

import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dto.ronda.RondaMentorDTO;
import mz.org.csaude.mentoring.model.tutor.Tutor;

@Entity(tableName = RondaMentor.TABLE_NAME,
        foreignKeys = {
                @ForeignKey(entity = Ronda.class,
                        parentColumns = "id",
                        childColumns = RondaMentor.COLUMN_RONDA,
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Tutor.class,
                        parentColumns = "id",
                        childColumns = RondaMentor.COLUMN_TUTOR,
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = {RondaMentor.COLUMN_RONDA}),
                @Index(value = {RondaMentor.COLUMN_TUTOR})
        })
public class RondaMentor extends BaseModel {

    public static final String TABLE_NAME = "ronda_mentor";
    public static final String COLUMN_RONDA = "ronda_id";
    public static final String COLUMN_TUTOR = "mentor_id";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";

    @NonNull
    @ColumnInfo(name = COLUMN_RONDA)
    private Integer rondaId;

    @Ignore
    @Relation(parentColumn = COLUMN_RONDA, entityColumn = "id")
    private Ronda ronda;

    @NonNull
    @ColumnInfo(name = COLUMN_TUTOR)
    private Integer tutorId;

    @Ignore
    @Relation(parentColumn = COLUMN_TUTOR, entityColumn = "id")
    private Tutor tutor;

    @NonNull
    @ColumnInfo(name = COLUMN_START_DATE)
    private Date startDate;

    @ColumnInfo(name = COLUMN_END_DATE)
    private Date endDate;

    public RondaMentor() {
    }

    @Ignore
    public RondaMentor(RondaMentorDTO rondaMentorDTO) {
        super(rondaMentorDTO);
        this.setStartDate(rondaMentorDTO.getStartDate());
        this.setEndDate(rondaMentorDTO.getEndDate());
        if (rondaMentorDTO.getMentor() != null) {
            this.setTutor(new Tutor(rondaMentorDTO.getMentor()));
            this.tutorId = this.tutor.getId();
        }
        if (rondaMentorDTO.getRonda() != null) {
            this.setRonda(new Ronda(rondaMentorDTO.getRonda()));
            this.rondaId = this.ronda.getId();
        }
    }

    public Integer getRondaId() {
        return rondaId;
    }

    public void setRondaId(Integer rondaId) {
        this.rondaId = rondaId;
    }

    public Ronda getRonda() {
        return ronda;
    }

    public void setRonda(Ronda ronda) {
        this.ronda = ronda;
        this.rondaId = ronda.getId();
    }

    public Integer getTutorId() {
        return tutorId;
    }

    public void setTutorId(Integer tutorId) {
        this.tutorId = tutorId;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
        this.tutorId = tutor.getId();
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
