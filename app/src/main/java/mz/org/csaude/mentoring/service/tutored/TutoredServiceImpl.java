package mz.org.csaude.mentoring.service.tutored;

import android.app.Application;

import androidx.room.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mz.org.csaude.mentoring.base.service.BaseServiceImpl;
import mz.org.csaude.mentoring.dao.flowhistory.FlowHistoryDao;
import mz.org.csaude.mentoring.dao.tutored.TutoredDao;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.location.Location;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.ronda.RondaMentee;
import mz.org.csaude.mentoring.model.tutored.EnumFlowHistory;
import mz.org.csaude.mentoring.model.tutored.EnumFlowHistoryProgressStatus;
import mz.org.csaude.mentoring.model.tutored.FlowHistory;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.service.employee.EmployeeService;
import mz.org.csaude.mentoring.service.employee.EmployeeServiceImpl;
import mz.org.csaude.mentoring.util.LifeCycleStatus;
import mz.org.csaude.mentoring.util.SyncSatus;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.viewmodel.tutored.StageFilter;

public class TutoredServiceImpl extends BaseServiceImpl<Tutored> implements TutoredService{

    TutoredDao tutoredDao;


    EmployeeService employeeService;
    FlowHistoryDao flowHistoryDao;

    public TutoredServiceImpl(Application application) {
        super(application);
    }

    @Override
    public void init(Application application) throws SQLException {
        super.init(application);
        this.tutoredDao = getDataBaseHelper().getTutoredDao();
        this.employeeService = new EmployeeServiceImpl(application);
        this.flowHistoryDao = getDataBaseHelper().getFlowHistoryDao();
    }

    public Tutored save(Tutored tutored) throws SQLException {
        tutored.setId((int) this.tutoredDao.insert(tutored));
        if (Utilities.listHasElements(tutored.getFlowHistory())) {
            tutored.getFlowHistory().forEach(mfh -> mfh.setTutoredId(tutored.getId()));
            flowHistoryDao.insertAll(tutored.getFlowHistory());
        }

        return tutored;

    }

    @Override
    public Tutored update(Tutored record) throws SQLException {
        this.tutoredDao.update(record);
        return record;
    }

    @Override
    public int delete(Tutored record) throws SQLException {
        return this.tutoredDao.delete(record);
    }

    @Override
    public List<Tutored> getAll() throws SQLException {
        List<Tutored> tutoreds = this.tutoredDao.queryForAll();
        for (Tutored tutored : tutoreds) {
            tutored.setEmployee(getApplication().getEmployeeService().getById(tutored.getEmployeeId()));
            tutored.setFlowHistory(flowHistoryDao.getByTutored(tutored.getId()));
        }
        return tutoreds;
    }

    @Override
    public Tutored getById(int id) throws SQLException {
        Tutored tutored = this.tutoredDao.queryForId(id);
        tutored.setEmployee(getApplication().getEmployeeService().getById(tutored.getEmployeeId()));
        tutored.setFlowHistory(flowHistoryDao.getByTutored(tutored.getId()));
        return tutored;
    }

    @Override
    public Tutored getByuuid(String uuid) throws SQLException {
        return this.tutoredDao.getByUuid(uuid);
    }

    @Override
    @Transaction
    public void savedOrUpdateTutoreds(List<Tutored> tutoreds) throws SQLException {
        for (Tutored tutored: tutoreds) {
            savedOrUpdateTutored(tutored);
        }
    }

    @Override
    public Tutored savedOrUpdateTutored(Tutored tutored) throws SQLException {

        Tutored existing = this.tutoredDao.getByUuid(tutored.getUuid());

        // Garante que o Employee está persistido
        tutored.setEmployee(
                getApplication()
                        .getEmployeeService()
                        .saveOrUpdateEmployee(tutored.getEmployee())
        );

        if (existing != null) {
            // manter o mesmo ID
            tutored.setId(existing.getId());
            this.update(tutored);
        } else {
            // novo registo
            this.save(tutored);
            // se o teu save NÃO setar o ID de volta no objeto,
            // podes reconsultar por UUID:
            if (tutored.getId() == null) {
                Tutored persisted = tutoredDao.getByUuid(tutored.getUuid());
                if (persisted != null) {
                    tutored.setId(persisted.getId());
                }
            }
        }

        // --- Persistir FlowHistory normalizado ---
        if (tutored.getId() != null && Utilities.listHasElements(tutored.getFlowHistory())) {

            // 1) apaga o histórico antigo deste mentorando
            flowHistoryDao.deleteByTutoredId(tutored.getId());

            // 2) insere o histórico atual vindo do objeto
            int fallbackSeq = 1;
            for (FlowHistory fh : tutored.getFlowHistory()) {
                if (fh == null) continue;

                fh.setTutoredId(tutored.getId());

                // se vier sem seq definido, gera uma sequência incremental
                if (fh.getSeq() == null) {
                    fh.setSeq(fallbackSeq++);
                }

                flowHistoryDao.insert(fh);
            }
        }

        return tutored;
    }


    @Override
    public List<Tutored> getAllOfRonda(Ronda currRonda) throws SQLException {
        List<Tutored> tutoreds =  this.tutoredDao.getAllOfRonda(currRonda.getId());
        for (Tutored tutored : tutoreds) {
            tutored.setEmployee(getApplication().getEmployeeService().getById(tutored.getEmployeeId()));
            tutored.setFlowHistory(flowHistoryDao.getByTutored(tutored.getId()));
        }
        return tutoreds;
    }

    @Override
    public List<Tutored> getAllOfRondaForZeroEvaluation(Ronda currRonda) throws SQLException {
        List<Tutored> tutoreds =  this.tutoredDao.getAllOfRondaForZeroEvaluation(currRonda.getId());
        for (Tutored tutored : tutoreds) {
            tutored.setEmployee(getApplication().getEmployeeService().getById(tutored.getEmployeeId()));
            tutored.setFlowHistory(flowHistoryDao.getByTutored(tutored.getId()));
        }
        return tutoreds;
    }

    @Override
    public List<Tutored> getAllOfHealthFacility(HealthFacility healthFacility) throws SQLException {
        List<Tutored> tutoreds = this.tutoredDao.getAllOfHealthFacility(healthFacility.getId(), String.valueOf(LifeCycleStatus.ACTIVE));
        for (Tutored tutored : tutoreds) {
            tutored.setEmployee(getApplication().getEmployeeService().getById(tutored.getEmployeeId()));
            tutored.setFlowHistory(flowHistoryDao.getByTutored(tutored.getId()));
        }
        return tutoreds;
    }


    @Override
    public List<Tutored> getAllNotSynced() throws SQLException {
        List<Tutored> tutoreds = this.tutoredDao.getAllNotSynced(String.valueOf(SyncSatus.PENDING));
        for (Tutored tutored : tutoreds) {
            tutored.setEmployee(getApplication().getEmployeeService().getById(tutored.getEmployeeId()));
            tutored.getEmployee().setLocations(getApplication().getLocationService().getAllOfEmploee(tutored.getEmployee()));
            tutored.setFlowHistory(flowHistoryDao.getByTutored(tutored.getId()));
        }
        return tutoreds;
    }

    @Override
    public List<Tutored> getAllForMentoringRound(HealthFacility healthFacility, boolean zeroEvaluation) throws SQLException {
        List<Tutored> tutoreds = this.tutoredDao.getAllForMentoringRound(healthFacility.getId(), String.valueOf(LifeCycleStatus.ACTIVE), zeroEvaluation);
        for (Tutored tutored : tutoreds) {
            tutored.setEmployee(getApplication().getEmployeeService().getById(tutored.getEmployeeId()));
            tutored.setFlowHistory(flowHistoryDao.getByTutored(tutored.getId()));
        }
        return tutoreds;
    }

    @Override
    public List<Tutored> getAllOfRondaForNewRonda(HealthFacility healthFacility) throws SQLException {
        return this.tutoredDao.getAllOfHealthFacilityForNewRonda(healthFacility.getId(), String.valueOf(LifeCycleStatus.ACTIVE));
    }

    @Override
    public List<Tutored> getAllPagenated(List<Location> locations, long offset, long limit) {
        List<Integer> locationIds = new ArrayList<>();
        for (Location location : locations) {
            locationIds.add(location.getHealthFacilityId());
        }
        List<Tutored> tutoreds = this.tutoredDao.getTutoredsPaginated(locationIds, (int) limit, (int) offset);
        for (Tutored tutored : tutoreds) {
            try {
                tutored.setEmployee(getApplication().getEmployeeService().getById(tutored.getEmployeeId()));
                tutored.setFlowHistory(flowHistoryDao.getByTutored(tutored.getId()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return tutoreds;
    }

    @Override
    public List<Tutored> getByFlowHistory(EnumFlowHistory flow,
                                          EnumFlowHistoryProgressStatus status,
                                          HealthFacility hf) {

        // Se algum parâmetro essencial vier nulo, não há como filtrar corretamente
        if (flow == null || status == null || hf == null) {
            return java.util.Collections.emptyList();
        }

        List<Tutored> tutoreds =
                tutoredDao.findByFlowHistory(flow, status, hf.getId());

        for (Tutored tutored : tutoreds) {
            try {
                tutored.setEmployee(
                        getApplication()
                                .getEmployeeService()
                                .getById(tutored.getEmployeeId())
                );
                tutored.setFlowHistory(flowHistoryDao.getByTutored(tutored.getId()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return tutoreds;
    }


    @Override
    public void updateFlowHistory(List<RondaMentee> rondaMentees, EnumFlowHistoryProgressStatus enumFlowHistoryProgressStatus) {
        for (RondaMentee rondaMentee : rondaMentees) {
            Tutored tutored = tutoredDao.queryForId(rondaMentee.getMenteeId());
            for (FlowHistory flowHistory : tutored.getFlowHistory()) {
                if (flowHistory.getEstagio().code().equals(EnumFlowHistory.RONDA_CICLO.code())) {
                    flowHistory.setEstado(enumFlowHistoryProgressStatus);
                }
                tutoredDao.update(tutored);
            }
        }

    }

    @Override
    public List<Tutored> getAllByStageFilter(StageFilter filter,
                                             List<Location> mentorLocations) throws SQLException {

        // Se for ALL, mantém comportamento original (sem filtrar por fluxo)
        if (filter == null || filter == StageFilter.ALL) {
            return getAllPagenated(mentorLocations, 0, 9999); // ou o método que já usas
        }

        // Extrai IDs de unidades sanitárias
        List<Integer> hfIds = new ArrayList<>();
        if (mentorLocations != null) {
            for (Location loc : mentorLocations) {
                if (loc != null && loc.getHealthFacilityId() != null) {
                    hfIds.add(loc.getHealthFacilityId());
                }
            }
        }
        if (hfIds.isEmpty()) return new ArrayList<>();

        // Mapeia StageFilter -> (flow, status)
        EnumFlowHistory flow;
        EnumFlowHistoryProgressStatus status = EnumFlowHistoryProgressStatus.AGUARDA_INICIO;

        switch (filter) {
            case AWAIT_ZERO:
                flow = EnumFlowHistory.SESSAO_ZERO;
                break;
            case START_ROUND:
                flow = EnumFlowHistory.RONDA_CICLO;
                break;
            case SEMESTRAL:
                flow = EnumFlowHistory.SESSAO_SEMESTRAL;
                break;
            case ALL:
            default:
                // já tratado acima, mas por segurança:
                return getAllPagenated(mentorLocations, 0, 9999);
        }

        List<Tutored> tutoreds = tutoredDao.findByLatestFlowAndStatus(hfIds, flow, status);
        for (Tutored tutored : tutoreds) {
            tutored.setEmployee(getApplication().getEmployeeService().getById(tutored.getEmployeeId()));
            tutored.setFlowHistory(flowHistoryDao.getByTutored(tutored.getId()));
        }
        return tutoreds;
    }

}
