package mz.org.csaude.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.model.tutored.EnumFlowHistory;
import mz.org.csaude.mentoring.model.tutored.EnumFlowHistoryProgressStatus;
import mz.org.csaude.mentoring.model.tutored.FlowHistory;

/**
 * DTO for FlowHistory - normalized table version
 */
public class FlowHistoryDTO extends BaseEntityDTO {

    private static final long serialVersionUID = 1L;

    @SerializedName("estagio")
    private String estagio;

    @SerializedName("estado")
    private String estado;

    @SerializedName("classificacao")
    private Double classificacao;

    @SerializedName("seq")
    private Integer seq;

    @SerializedName("tutoredId")
    private Integer tutoredId;

    private String rondaUUID;


    // ----------------------------------------------------
    // CONSTRUCTORS
    // ----------------------------------------------------

    public FlowHistoryDTO() {}

    public FlowHistoryDTO(FlowHistory entity) {
        super(entity);
        this.tutoredId = entity.getTutoredId();
        this.seq = entity.getSeq();
        this.classificacao = entity.getClassificacao();

        this.estagio = entity.getEstagio() != null ? entity.getEstagio().name() : null;
        this.estado  = entity.getEstado()  != null ? entity.getEstado().name()  : null;
    }


    // ----------------------------------------------------
    // DTO → ENTITY
    // ----------------------------------------------------

    @JsonIgnore
    public FlowHistory toEntity() {
        FlowHistory fh = new FlowHistory();

        fh.setTutoredId(this.tutoredId);
        fh.setSeq(this.seq);
        fh.setClassificacao(this.classificacao);

        fh.setEstagio(
                this.estagio != null ? EnumFlowHistory.valueOf(this.estagio) : null
        );

        fh.setEstado(
                this.estado != null ? EnumFlowHistoryProgressStatus.valueOf(this.estado) : null
        );

        fh.setUuid(this.getUuid());
        fh.setCreatedAt(this.getCreatedAt());
        fh.setUpdatedAt(this.getUpdatedAt());
        fh.setLifeCycleStatus(this.getLifeCycleStatus());
        fh.setCreatedByUuid(this.getCreatedByuuid());
        fh.setUpdatedByUuid(this.getUpdatedByuuid());

        return fh;
    }

    // ----------------------------------------------------
    // GETTERS / SETTERS
    // ----------------------------------------------------

    public String getEstagio() { return estagio; }
    public void setEstagio(String estagio) { this.estagio = estagio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Double getClassificacao() { return classificacao; }
    public void setClassificacao(Double classificacao) { this.classificacao = classificacao; }

    public Integer getSeq() { return seq; }
    public void setSeq(Integer seq) { this.seq = seq; }

    public Integer getTutoredId() { return tutoredId; }
    public void setTutoredId(Integer tutoredId) { this.tutoredId = tutoredId; }
}
