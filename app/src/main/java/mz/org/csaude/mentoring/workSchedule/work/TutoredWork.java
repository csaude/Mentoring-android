package mz.org.csaude.mentoring.workSchedule.work;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.worker.BaseWorker;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.user.UserService;
import mz.org.csaude.mentoring.service.user.UserServiceImpl;
import mz.org.csaude.mentoring.workSchedule.rest.CareerRestService;
import mz.org.csaude.mentoring.workSchedule.rest.TutoredRestService;

public class TutoredWork extends BaseWorker<Tutored> {

    private UserService userService;
    private TutoredRestService tutoredRestService;
    private User user;

    public TutoredWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.userService = new UserServiceImpl( (Application) getApplicationContext());
    }

    @Override
    public void doOnlineSearch(long offset, long limit) throws SQLException {
       this.tutoredRestService.restGetTutoredByUserUuid(this);
    }

    @Override
    protected void doOnStart() {

        try {
             user = this.userService.getAll().get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
      this.tutoredRestService = new TutoredRestService((Application) getApplicationContext(), user);
    }

    @Override
    protected void doAfterSearch(String flag, List<Tutored> recs) throws SQLException {
        changeStatusToFinished();
        doOnFinish();
    }
    @Override
    protected void doOnFinish() {

    }

    @Override
    protected void doSave(List<Tutored> recs) {

    }
}
