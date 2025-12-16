// mz.org.csaude.mentoring.model.tutored.FlowHistory
package mz.org.csaude.mentoring.model.tutored;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.util.LifeCycleStatus;

@Entity(
        tableName = FlowHistory.TABLE_NAME,
        foreignKeys = @ForeignKey(
                entity = Tutored.class,
                parentColumns = "id",
                childColumns = FlowHistory.COLUMN_TUTORED_ID,
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(FlowHistory.COLUMN_TUTORED_ID)
        }
)
public class FlowHistory extends BaseModel {

    public static final String TABLE_NAME = "flow_history";
    public static final String COLUMN_TUTORED_ID = "tutored_id";

    @NonNull
    @ColumnInfo(name = COLUMN_TUTORED_ID)
    private Integer tutoredId;

    @SerializedName("estagio")
    @ColumnInfo(name = "estagio")
    private EnumFlowHistory estagio;

    @SerializedName("estado")
    @ColumnInfo(name = "estado")
    private EnumFlowHistoryProgressStatus estado;

    @SerializedName("classificação")
    @Nullable
    @ColumnInfo(name = "classificacao")
    private Double classificacao;

    @ColumnInfo(name = "seq")
    private Integer seq;

    public FlowHistory() {}

    public FlowHistory(@NonNull Integer tutoredId,
                       EnumFlowHistory estagio,
                       EnumFlowHistoryProgressStatus estado,
                       @Nullable Double classificacao,
                       LifeCycleStatus lifeCycleStatus,
                       Integer seq) {
        this.tutoredId = tutoredId;
        this.estagio = estagio;
        this.estado = estado;
        this.classificacao = classificacao;
        this.seq = seq;
        this.lifeCycleStatus = lifeCycleStatus;
    }

    @NonNull
    public Integer getTutoredId() { return tutoredId; }
    public void setTutoredId(@NonNull Integer tutoredId) { this.tutoredId = tutoredId; }

    public EnumFlowHistory getEstagio() { return estagio; }
    public void setEstagio(EnumFlowHistory estagio) { this.estagio = estagio; }

    public EnumFlowHistoryProgressStatus getEstado() { return estado; }
    public void setEstado(EnumFlowHistoryProgressStatus estado) { this.estado = estado; }

    @Nullable public Double getClassificacao() { return classificacao; }
    public void setClassificacao(@Nullable Double classificacao) { this.classificacao = classificacao; }

    public Integer getSeq() { return seq; }
    public void setSeq(Integer seq) { this.seq = seq; }
}
