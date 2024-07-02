package mz.org.csaude.mentoring.model.tutored;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.tutored.TutoredDaoImpl;
import mz.org.csaude.mentoring.dto.tutored.TutoredDTO;
import mz.org.csaude.mentoring.model.employee.Employee;


@DatabaseTable(tableName = Tutored.COLUMN_TABLE_NAME, daoClass = TutoredDaoImpl.class)
public class Tutored extends BaseModel {

    public static final String COLUMN_TABLE_NAME = "tutored";
    public static final String COLUMN_EMPLOYEE = "emplyee_id";
    public static final String COLUMN_ZERO_EVALUATION_STATUS = "zero_evatuation_status";

    @DatabaseField(columnName = COLUMN_EMPLOYEE, foreign = true, foreignAutoRefresh = true )
    private Employee employee;

    @DatabaseField(columnName = COLUMN_ZERO_EVALUATION_STATUS)
    private boolean zeroEvaluationDone;
    public Tutored() {
    }

    public Tutored(Employee employee) {
        this.setEmployee(employee);
    }

    public Tutored(TutoredDTO tutoredDTO) {
        super(tutoredDTO);
        this.setEmployee(new Employee(tutoredDTO.getEmployeeDTO()));
        this.setZeroEvaluationDone(tutoredDTO.isZeroEvaluationDone());
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


    @Override
    public String getDescription() {
        return this.employee.getFullName();
    }

    public boolean isZeroEvaluationDone() {
        return zeroEvaluationDone;
    }

    public void setZeroEvaluationDone(boolean zeroEvaluationDone) {
        this.zeroEvaluationDone = zeroEvaluationDone;
    }

    @Override
    public int getDrawable() {
        return 0;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String validade() {
        return this.employee.validade();
    }

    @Override
    public String getListType() {
        return listTyp;
    }

    @Override
    public String toString() {
        return "Tutored{" +
                "employee=" + employee +
                ", zeroEvaluationDone=" + zeroEvaluationDone +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tutored)) return false;
        if (!super.equals(o)) return false;
        Tutored tutored = (Tutored) o;
        return Objects.equals(employee, tutored.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employee);
    }
}
