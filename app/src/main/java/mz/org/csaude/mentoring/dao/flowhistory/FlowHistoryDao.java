package mz.org.csaude.mentoring.dao.flowhistory;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import mz.org.csaude.mentoring.model.tutored.FlowHistory;
import mz.org.csaude.mentoring.model.tutored.EnumFlowHistory;
import mz.org.csaude.mentoring.model.tutored.EnumFlowHistoryProgressStatus;
import mz.org.csaude.mentoring.model.tutored.Tutored;

@Dao
public interface FlowHistoryDao {

    @Insert
    long insert(FlowHistory flowHistory);

    @Insert
    void insertAll(List<FlowHistory> list);

    @Update
    int update(FlowHistory flowHistory);

    @Query("SELECT * FROM " + FlowHistory.TABLE_NAME +
            " WHERE tutored_id = :tutoredId ORDER BY seq")
    List<FlowHistory> getByTutored(int tutoredId);

    @Query("SELECT MAX(seq) FROM " + FlowHistory.TABLE_NAME +
            " WHERE tutored_id = :tutoredId")
    Integer getMaxSeqForTutored(int tutoredId);

    @Query("SELECT DISTINCT t.* " +
            "FROM tutored t " +
            "JOIN " + FlowHistory.TABLE_NAME + " fh ON fh.tutored_id = t.id " +
            "JOIN employee e ON e.id = t.employee_id " +
            "JOIN location l ON e.id = l.employee_id " +
            "WHERE t.life_cycle_status = 'ACTIVE' " +
            "AND fh.estagio = :flowCode " +
            "AND fh.estado = :statusCode " +
            "AND l.health_facility_id = :hfId " +
            "ORDER BY e.surname COLLATE NOCASE, e.name COLLATE NOCASE")
    List<Tutored> findTutoredByFlowHistory(
            EnumFlowHistory flowCode,
            EnumFlowHistoryProgressStatus statusCode,
            Integer hfId
    );

    @Query("DELETE FROM flow_history WHERE tutored_id = :tutoredId")
    void deleteByTutoredId(int tutoredId);

    @Query("DELETE FROM flow_history WHERE id = :lastFlowHistoryId")
    void delete(int lastFlowHistoryId);
}