package mz.org.csaude.mentoring.workSchedule.work.post;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.worker.BaseWorker;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.workSchedule.rest.UserRestService;

public class PATCHUserWorker extends BaseWorker<User> {

    private final UserRestService userRestService;

    public PATCHUserWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.userRestService = new UserRestService((Application) getApplicationContext(), new User(getInputData().getString("username"), getInputData().getString("password")));
    }

    @Override
    protected void doOnStart() {

    }

    @Override
    public void doOnlineSearch(long offset, long limit) throws SQLException {
        userRestService.pacthUser(this);
    }

    @Override
    protected void doAfterSearch(String flag, List<User> recs) throws SQLException {
        changeStatusToFinished();
        doOnFinish();
    }

    @Override
    protected void doOnFinish() {

    }

    @Override
    protected void doSave(List<User> recs) {

    }
}
