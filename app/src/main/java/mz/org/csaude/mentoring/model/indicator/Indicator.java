package mz.org.csaude.mentoring.model.indicator;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.answer.AnswerDAOImpl;
import mz.org.csaude.mentoring.dao.indicator.IndicatorDAOImpl;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.setting.Setting;
import mz.org.csaude.mentoring.model.tutor.Tutor;

@Data
@DatabaseTable(tableName = Indicator.TABLE_NAME, daoClass = IndicatorDAOImpl.class)
public class Indicator extends BaseModel {

    public static final String TABLE_NAME = "indicator";

    public static final String COLUMN_CODE = "code";

    public static final String COLUMN_PERFORMED_DATE = "performed_date";

    public static final String COLUMN_REFERREDMONTH = "referred_month";

    public static final String COLUMN_TUTOR = "tutor";

    public static final String COLUMN_FORM = "form";

    public static final String COLUMN_HEALTHFACILITY = "healthFacility";

    @DatabaseField(columnName = COLUMN_CODE)
    private String code;

    @DatabaseField(columnName = COLUMN_PERFORMED_DATE)
    private LocalDateTime performedDate;

    @DatabaseField(columnName = COLUMN_REFERREDMONTH)
    private LocalDate referredMonth;

    @DatabaseField(columnName = COLUMN_TUTOR)
    private Tutor tutor;

    @DatabaseField(columnName = COLUMN_FORM)
    private Form form;

    @DatabaseField(columnName = COLUMN_HEALTHFACILITY)
    private HealthFacility healthFacility;

    public Indicator() {
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getPerformedDate() {
        return performedDate;
    }

    public LocalDate getReferredMonth() {
        return referredMonth;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public Form getForm() {
        return form;
    }

    public HealthFacility getHealthFacility() {
        return healthFacility;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPerformedDate(LocalDateTime performedDate) {
        this.performedDate = performedDate;
    }

    public void setReferredMonth(LocalDate referredMonth) {
        this.referredMonth = referredMonth;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setHealthFacility(HealthFacility healthFacility) {
        this.healthFacility = healthFacility;
    }
}
