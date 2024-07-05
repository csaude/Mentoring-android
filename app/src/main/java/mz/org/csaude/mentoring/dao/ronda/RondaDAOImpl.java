package mz.org.csaude.mentoring.dao.ronda;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.application.MentoringApplication;
import mz.org.csaude.mentoring.base.dao.MentoringBaseDaoImpl;
import mz.org.csaude.mentoring.model.employee.Employee;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.ronda.RondaMentee;
import mz.org.csaude.mentoring.model.ronda.RondaMentor;
import mz.org.csaude.mentoring.model.rondatype.RondaType;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.util.DateUtilities;
import mz.org.csaude.mentoring.util.LifeCycleStatus;
import mz.org.csaude.mentoring.util.SyncSatus;
import mz.org.csaude.mentoring.workSchedule.work.MentoringDataBaseHelper;

public class RondaDAOImpl extends MentoringBaseDaoImpl<Ronda, Integer> implements RondaDAO {

    public RondaDAOImpl(Class<Ronda> dataClass) throws SQLException {
        super(dataClass);
    }

    public RondaDAOImpl(ConnectionSource connectionSource, Class<Ronda> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public RondaDAOImpl(ConnectionSource connectionSource, DatabaseTableConfig<Ronda> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public List<Ronda> getAllByHealthFacilityAndMentor(HealthFacility healthFacility, Tutor tutor, MentoringApplication mentoringApplication) throws SQLException {
        QueryBuilder<RondaMentor, Integer> rondaMentorQueryBuilder =  MentoringDataBaseHelper.getInstance(mentoringApplication.getApplicationContext()).getRondaMentorDAO().queryBuilder();
        rondaMentorQueryBuilder
                .where().eq(RondaMentor.COLUMN_TUTOR, tutor.getId());

        QueryBuilder<RondaMentee, Integer> rondaMenteeQueryBuilder =  MentoringDataBaseHelper.getInstance(mentoringApplication.getApplicationContext()).getRondaMenteeDAO().queryBuilder();

        QueryBuilder<Ronda, Integer> rondaQueryBuilder =  MentoringDataBaseHelper.getInstance(mentoringApplication.getApplicationContext()).getRondaDAO().queryBuilder();
        rondaQueryBuilder.join(rondaMentorQueryBuilder).join(rondaMenteeQueryBuilder)
                .where().eq(Ronda.COLUMN_HEALTH_FACILITY,  healthFacility.getId())
                .and().eq(Ronda.COLUMN_LIFE_CYCLE_STATUS, LifeCycleStatus.ACTIVE);

        return rondaQueryBuilder.orderBy(Employee.COLUMN_ID, true).query();
    }

    @Override
    public List<Ronda> getAllNotSynced() throws SQLException {
        return queryForEq(Ronda.COLUMN_SYNC_STATUS, SyncSatus.PENDING);
    }

    @Override
    public List<Ronda> getAllByRondaType(RondaType rondaType, MentoringApplication mentoringApplication) throws SQLException {
        QueryBuilder<mz.org.csaude.mentoring.model.rondatype.RondaType, Integer> rondaTypeQueryBuilder =  MentoringDataBaseHelper.getInstance(mentoringApplication.getApplicationContext()).getRondaTypeDAO().queryBuilder();
        rondaTypeQueryBuilder.where().eq(mz.org.csaude.mentoring.model.rondatype.RondaType.COLUMN_CODE, rondaType.getCode());

        QueryBuilder<Ronda, Integer> rondaQueryBuilder =  MentoringDataBaseHelper.getInstance(mentoringApplication.getApplicationContext()).getRondaDAO().queryBuilder();
        rondaQueryBuilder.join(rondaTypeQueryBuilder)
                .where().eq(Ronda.COLUMN_LIFE_CYCLE_STATUS, LifeCycleStatus.ACTIVE);

        return rondaQueryBuilder.orderBy(Employee.COLUMN_ID, true).query();
    }

    @Override
    public List<Ronda> getAllByMentor(Tutor tutor, MentoringApplication mentoringApplication) throws SQLException {

        QueryBuilder<RondaMentor, Integer> rondaMentorQueryBuilder = MentoringDataBaseHelper.getInstance(mentoringApplication.getApplicationContext()).getRondaMentorDAO().queryBuilder();
        rondaMentorQueryBuilder.where().eq(RondaMentor.COLUMN_TUTOR, tutor.getId());

        QueryBuilder<RondaMentee, Integer> rondaMenteeQueryBuilder = MentoringDataBaseHelper.getInstance(mentoringApplication.getApplicationContext()).getRondaMenteeDAO().queryBuilder();

        QueryBuilder<Ronda, Integer> rondaQueryBuilder = MentoringDataBaseHelper.getInstance(mentoringApplication.getApplicationContext()).getRondaDAO().queryBuilder();
        rondaQueryBuilder.join(rondaMentorQueryBuilder).join(rondaMenteeQueryBuilder)
                .where().eq(Ronda.COLUMN_LIFE_CYCLE_STATUS, LifeCycleStatus.ACTIVE);


        return rondaQueryBuilder.orderBy(Ronda.COLUMN_START_DATE, true).query();
    }


}
