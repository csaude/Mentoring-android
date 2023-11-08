package mz.org.csaude.mentoring.workSchedule.work;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.worker.BaseWorker;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.user.UserService;
import mz.org.csaude.mentoring.service.user.UserServiceImpl;
import mz.org.csaude.mentoring.workSchedule.rest.FormRestService;

public class FormWorker extends BaseWorker<Form> {

    protected FormRestService formRestService;

    private UserService userService;

    private User user;

    public FormWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.userService = new UserServiceImpl((Application) getApplicationContext());
    }

    @Override
    public void doOnlineSearch(long offset, long limit) throws SQLException {
        this.formRestService.restGetAllFormsByProgramaticArea(this);
    }
    @Override
    protected void doOnStart() {
        try {
            user = userService.getAll().get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.formRestService = new FormRestService((Application) getApplicationContext(), user);
    }

    @Override
    protected void doAfterSearch(String flag, List<Form> recs) throws SQLException {
        changeStatusToFinished();
        doOnFinish();
    }
    @Override
    protected void doOnFinish() {

    }

    @Override
    protected void doSave(List<Form> recs) {

    }
}
