package mz.org.csaude.mentoring.workSchedule.work;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.worker.BaseWorker;
import mz.org.csaude.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.csaude.mentoring.model.programmaticArea.ProgrammaticArea;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.user.UserServiceImpl;
import mz.org.csaude.mentoring.workSchedule.rest.CabinetRestService;
import mz.org.csaude.mentoring.workSchedule.rest.ProgrammaticAreaRestService;

public class ProgrammaticAreaWorker extends BaseWorker<ProgrammaticArea> {

    private UserServiceImpl userService;

    protected ProgrammaticAreaRestService programmaticAreaRestService;

    private List<ProgrammaticAreaDTO> programmaticAreaDTOS = new ArrayList<>();
    private User user;
    @Override
    public void doOnlineSearch(long offset, long limit) throws SQLException {
          this.programmaticAreaRestService.restGetProgrammaticAreaByTutorUuid(this);
    }

    public ProgrammaticAreaWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.userService = new UserServiceImpl( (Application) getApplicationContext());
    }

    @Override
    protected void doOnStart() {

        try {
            user = this.userService.getAll().get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
      this.programmaticAreaRestService = new ProgrammaticAreaRestService((Application) getApplicationContext(), user);
    }

    @Override
    protected void doAfterSearch(String flag, List<ProgrammaticArea> recs) throws SQLException {
        changeStatusToFinished();
        doOnFinish();
    }
    @Override
    protected void doOnFinish() {

    }

    @Override
    protected void doSave(List<ProgrammaticArea> recs) {

    }
}
