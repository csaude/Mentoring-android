package mz.org.csaude.mentoring.workSchedule.work;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import java.sql.SQLException;
import java.util.List;
//8087
import mz.org.csaude.mentoring.base.worker.BaseWorker;
import mz.org.csaude.mentoring.model.programmaticArea.ProgrammaticArea;
import mz.org.csaude.mentoring.workSchedule.rest.ProgrammaticAreaRestService;

public class ProgrammaticAreaWorker extends BaseWorker<ProgrammaticArea> {

    private ProgrammaticAreaRestService programmaticAreaRestService;

    public ProgrammaticAreaWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        programmaticAreaRestService = new ProgrammaticAreaRestService((Application) this.context.getApplicationContext());
    }

    @Override
    public void doOnlineSearch(long offset, long limit) throws SQLException {
        programmaticAreaRestService.restGetProgrammings(limit, offset, this);
    }

    @Override
    protected void doOnStart() {

    }

    @Override
    protected void doOnFinish() {

    }

    @Override
    protected void doSave(List<ProgrammaticArea> recs) {

    }
}
