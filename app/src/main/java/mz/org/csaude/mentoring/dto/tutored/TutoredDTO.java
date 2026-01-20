package mz.org.csaude.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.dto.employee.EmployeeDTO;
import mz.org.csaude.mentoring.model.tutored.FlowHistory;
import mz.org.csaude.mentoring.model.tutored.Tutored;

/**
 * @author Jose Julai Ritsure
 */
public class TutoredDTO extends BaseEntityDTO {

    private EmployeeDTO employeeDTO;

    private boolean zeroEvaluationDone;

    private double zeroEvaluationScore;

    /**
     * NEW: preferred list field for flow history (array in JSON)
     */
    @SerializedName("flowHistoryMenteeAuxDTO")
    private List<FlowHistoryDTO> flowHistoryMenteeAuxDTO;


    public TutoredDTO() {
    }

    public TutoredDTO(Tutored tutored) {
        super(tutored);
        setZeroEvaluationScore(tutored.getZeroEvaluationScore());
        setZeroEvaluationDone(tutored.isZeroEvaluationDone());
        this.setEmployeeDTO(tutored.getEmployee() != null ? new EmployeeDTO(tutored.getEmployee()) : null);

        // Map entity -> DTO list (preferred)
        if (tutored.getFlowHistory() != null) {
            this.flowHistoryMenteeAuxDTO = new ArrayList<>();
            for (FlowHistory fh : tutored.getFlowHistory()) {
                this.flowHistoryMenteeAuxDTO.add(new FlowHistoryDTO(fh));
            }
        }

        // No need to populate the legacy single field on output, but you may set it if required:
        // this.flowHistoryMenteeAuxDTO = (flowHistoryMenteeAuxDTOList != null && !flowHistoryMenteeAuxDTOList.isEmpty())
        //         ? flowHistoryMenteeAuxDTOList.get(0) : null;
    }

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    public boolean isZeroEvaluationDone() {
        return zeroEvaluationDone;
    }

    public void setZeroEvaluationDone(boolean zeroEvaluationDone) {
        this.zeroEvaluationDone = zeroEvaluationDone;
    }

    public double getZeroEvaluationScore() {
        return zeroEvaluationScore;
    }

    public void setZeroEvaluationScore(double zeroEvaluationScore) {
        this.zeroEvaluationScore = zeroEvaluationScore;
    }

    // -------- NEW / PREFERRED LIST ACCESSORS --------
    public List<FlowHistoryDTO> getFlowHistoryMenteeAuxDTO() {
        return flowHistoryMenteeAuxDTO;
    }

    public void setFlowHistoryMenteeAuxDTO(List<FlowHistoryDTO> flowHistoryMenteeAuxDTO) {
        this.flowHistoryMenteeAuxDTO = flowHistoryMenteeAuxDTO;
    }

    /**
     * Map DTO -> Entity (wrap legacy single value into a list if list is null/empty)
     */
    @JsonIgnore
    public Tutored getMentee() {
        Tutored tutored = new Tutored();
        tutored.setUuid(this.getUuid());
        tutored.setZeroEvaluationDone(this.isZeroEvaluationDone());
        tutored.setZeroEvaluationScore(this.getZeroEvaluationScore());
        tutored.setCreatedAt(this.getCreatedAt());
        tutored.setUpdatedAt(this.getUpdatedAt());
        tutored.setLifeCycleStatus(this.getLifeCycleStatus());
        tutored.setCreatedByUuid(this.getCreatedByuuid());
        tutored.setUpdatedByUuid(this.getUpdatedByuuid());

        if (this.getEmployeeDTO() != null) {
            tutored.setEmployee(this.getEmployeeDTO().getEmployee());
        }

        if (this.flowHistoryMenteeAuxDTO != null) {
            List<FlowHistory> list = new ArrayList<>();
            for (FlowHistoryDTO dto : this.flowHistoryMenteeAuxDTO) {
                list.add(dto.toEntity());
            }
            tutored.setFlowHistory(list);
        }

        return tutored;
    }
}
