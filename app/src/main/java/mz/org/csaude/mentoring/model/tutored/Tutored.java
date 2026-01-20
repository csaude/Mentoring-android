package mz.org.csaude.mentoring.model.tutored;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dto.tutored.FlowHistoryDTO;
import mz.org.csaude.mentoring.dto.tutored.TutoredDTO;
import mz.org.csaude.mentoring.model.employee.Employee;

@Entity(tableName = Tutored.COLUMN_TABLE_NAME,
        foreignKeys = @ForeignKey(
                entity = Employee.class,
                parentColumns = "id",
                childColumns = Tutored.COLUMN_EMPLOYEE,
                onDelete = ForeignKey.CASCADE
        ))
public class Tutored extends BaseModel {

    public static final String COLUMN_TABLE_NAME = "tutored";
    public static final String COLUMN_EMPLOYEE = "employee_id";
    public static final String COLUMN_ZERO_EVALUATION_STATUS = "zero_evaluation_status";
    public static final String COLUMN_ZERO_EVALUATION_SCORE = "zero_evaluation_score";

    @NonNull
    @ColumnInfo(name = COLUMN_EMPLOYEE)
    private Integer employeeId;

    @Ignore
    private Employee employee;

    @ColumnInfo(name = COLUMN_ZERO_EVALUATION_STATUS)
    private boolean zeroEvaluationDone;

    @ColumnInfo(name = COLUMN_ZERO_EVALUATION_SCORE)
    private double zeroEvaluationScore;

    @Ignore
    private List<FlowHistory> flowHistory;

    public Tutored() {}

    public Tutored(Integer employeeId) {
        this.employeeId = employeeId;
    }

    @Ignore
    public Tutored(Employee employee) {
        this.employeeId = employee.getId();
        this.employee = employee;
    }

    @Ignore
    public Tutored(TutoredDTO tutoredDTO) {
        super(tutoredDTO);
        this.zeroEvaluationDone = tutoredDTO.isZeroEvaluationDone();
        this.zeroEvaluationScore = tutoredDTO.getZeroEvaluationScore();

        if (tutoredDTO.getEmployeeDTO() != null) {
            this.employee = new Employee(tutoredDTO.getEmployeeDTO());
            this.employeeId = this.employee.getId();
        }

        if (tutoredDTO.getFlowHistoryMenteeAuxDTO() != null &&
                !tutoredDTO.getFlowHistoryMenteeAuxDTO().isEmpty()) {

            this.flowHistory = new ArrayList<>();

            for (FlowHistoryDTO fhDTO : tutoredDTO.getFlowHistoryMenteeAuxDTO()) {
                FlowHistory fh = fhDTO.toEntity(); // <--- conversion from DTO → Entity
                this.flowHistory.add(fh);
            }

        } else {
            this.flowHistory = null;
        }
    }

    @Override
    public String validade() {
        return employee.validade();
    }

    // Getters/Setters
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) {
        this.employee = employee;
        if (employee != null) this.employeeId = employee.getId();
    }

    public boolean isZeroEvaluationDone() { return zeroEvaluationDone; }
    public void setZeroEvaluationDone(boolean zeroEvaluationDone) { this.zeroEvaluationDone = zeroEvaluationDone; }

    public double getZeroEvaluationScore() { return zeroEvaluationScore; }
    public void setZeroEvaluationScore(double zeroEvaluationScore) { this.zeroEvaluationScore = zeroEvaluationScore; }

    public List<FlowHistory> getFlowHistory() { return flowHistory; }
    public void setFlowHistory(List<FlowHistory> flowHistory) { this.flowHistory = flowHistory; }

    @Override
    public String getDescription() {
        return this.employee != null ? this.employee.getFullName() : "No Employee";
    }

    @Override
    public String toString() {
        return this.getEmployee() != null ? this.getEmployee().getFullName() : super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tutored)) return false;
        if (!super.equals(o)) return false;
        Tutored tutored = (Tutored) o;
        return Objects.equals(employeeId, tutored.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employeeId);
    }

    public void addFlowHistory(FlowHistory fh) {
        if (this.flowHistory == null) this.flowHistory = new ArrayList<>();
        this.flowHistory.add(fh);
    }

    public boolean isOnStatus(EnumFlowHistory enumFlowHistory, EnumFlowHistoryProgressStatus enumFlowHistoryProgressStatus) {
        FlowHistory maxfh = getLastFlowHistory();
        if (maxfh != null && maxfh.getEstagio().equals(enumFlowHistory) && maxfh.getEstado().equals(enumFlowHistoryProgressStatus)) {
            return true;
        }
        return false;
    }

    public Integer determineNextSeq() {
        return getLastFlowHistory().getSeq() + 1;
    }

    public FlowHistory getLastFlowHistory() {
        int maxSeq = 0;
        FlowHistory maxfh = null;
        for (FlowHistory fh : this.flowHistory) {
            if (fh.getSeq() > maxSeq) {
                maxSeq = fh.getSeq();
                maxfh = fh;
            }
        }
        return maxfh;
    }
}
