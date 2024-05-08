package mz.org.csaude.mentoring.workSchedule.work;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.worker.BaseWorker;
import mz.org.csaude.mentoring.model.session.SessionStatus;
import mz.org.csaude.mentoring.workSchedule.rest.SessionStatusRestService;

public class SessionStatusWorker extends BaseWorker<SessionStatus> {
    private SessionStatusRestService sessionStatusRestService;
    public SessionStatusWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.sessionStatusRestService = new SessionStatusRestService((Application) getApplicationContext());
    }
    @Override
    public void doOnlineSearch(long offset, long limit) throws SQLException {
        this.sessionStatusRestService.restGetSessionStatuses(this);
    }

    @Override
    protected void doAfterSearch(String flag, List<SessionStatus> recs) throws SQLException {
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
    protected void doSave(List<SessionStatus> recs) {

    }
}
