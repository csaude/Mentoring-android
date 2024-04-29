package mz.org.csaude.mentoring.workSchedule.work;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.worker.BaseWorker;
import mz.org.csaude.mentoring.model.mentorship.IterationType;
import mz.org.csaude.mentoring.workSchedule.rest.IterationTypeRestService;

public class IterationTypeWorker extends BaseWorker<IterationType> {
    private IterationTypeRestService iterationTypeRestService;
    public IterationTypeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.iterationTypeRestService = new IterationTypeRestService((Application) getApplicationContext());
    }
    @Override
    public void doOnlineSearch(long offset, long limit) throws SQLException {
        this.iterationTypeRestService.restGetIterationTypes(this);
    }

    @Override
    protected void doAfterSearch(String flag, List<IterationType> recs) throws SQLException {
        changeStatusToFinished();
        doOnFinish();
    }

    @Override
    protected void doOnStart() {

    }

    @Override
    protected void doOnFinish() {

    }

    @Override
    protected void doSave(List<IterationType> recs) {

    }
}
