package mz.org.csaude.mentoring.dto.ronda;

import java.util.Date;

import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.dto.tutored.TutoredDTO;
import mz.org.csaude.mentoring.dto.tutored.FlowHistoryDTO;
import mz.org.csaude.mentoring.model.ronda.RondaMentee;

public class RondaMenteeDTO extends BaseEntityDTO {

    private Date startDate;
    private Date endDate;
    private TutoredDTO mentee;
    private RondaDTO ronda;

    // Agora usando o DTO, não a entidade
    private FlowHistoryDTO flowHistoryMenteeAuxDTO;

    public RondaMenteeDTO() { }

    public RondaMenteeDTO(RondaMentee rondaMentee) {
        super(rondaMentee);

        this.setStartDate(rondaMentee.getStartDate());
        if (rondaMentee.getEndDate() != null) {
            this.setEndDate(rondaMentee.getEndDate());
        }

        if (rondaMentee.getTutored() != null) {
            // Constrói o DTO do mentorando (já carrega a lista de FlowHistoryDTO)
            TutoredDTO menteeDTO = new TutoredDTO(rondaMentee.getTutored());
            this.setMentee(menteeDTO);

            // Se tiver histórico no DTO, escolhe o que NÃO é SESSAO_ZERO (ex.: RONDA_CICLO, SEMESTRAL, etc.)
            if (menteeDTO.getFlowHistoryMenteeAuxDTO() != null &&
                    !menteeDTO.getFlowHistoryMenteeAuxDTO().isEmpty()) {

                FlowHistoryDTO selected = null;

                for (FlowHistoryDTO fhDto : menteeDTO.getFlowHistoryMenteeAuxDTO()) {

                    if (selected == null ||
                            (fhDto.getSeq() != null && selected.getSeq() != null && fhDto.getSeq() > selected.getSeq())) {

                        selected = fhDto;
                    }
                }

                this.flowHistoryMenteeAuxDTO = selected;

            }
        }

        if (rondaMentee.getRonda() != null) {
            this.setRonda(new RondaDTO(rondaMentee.getRonda()));
        }
    }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public TutoredDTO getMentee() { return mentee; }
    public void setMentee(TutoredDTO mentee) { this.mentee = mentee; }

    public RondaDTO getRonda() { return ronda; }
    public void setRonda(RondaDTO ronda) { this.ronda = ronda; }

    public FlowHistoryDTO getFlowHistoryMenteeAuxDTO() { return flowHistoryMenteeAuxDTO; }
    public void setFlowHistoryMenteeAuxDTO(FlowHistoryDTO flowHistoryMenteeAuxDTO) { this.flowHistoryMenteeAuxDTO = flowHistoryMenteeAuxDTO; }

    public RondaMentee getRondaMentee() {
        RondaMentee rondaMentee = new RondaMentee();
        rondaMentee.setUuid(this.getUuid());
        rondaMentee.setStartDate(this.getStartDate());
        rondaMentee.setEndDate(this.getEndDate());
        rondaMentee.setCreatedAt(this.getCreatedAt());
        rondaMentee.setUpdatedAt(this.getUpdatedAt());
        rondaMentee.setLifeCycleStatus(this.getLifeCycleStatus());
        rondaMentee.setCreatedByUuid(this.getCreatedByuuid());
        rondaMentee.setUpdatedByUuid(this.getUpdatedByuuid());

        if (this.getMentee() != null) {
            rondaMentee.setTutored(this.getMentee().getMentee());
        }
        if (this.getRonda() != null) {
            rondaMentee.setRonda(this.getRonda().getRonda());
        }

        // O FlowHistory real é gerido pela camada de serviço
        // (FlowHistory table + vínculo ao Tutored/Ronda),
        // por isso não mapeamos aqui para a entidade.

        return rondaMentee;
    }
}
