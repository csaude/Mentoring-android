package mz.org.csaude.mentoring.workSchedule.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.worker.BaseWorker;
import mz.org.csaude.mentoring.model.programmaticArea.ProgrammaticArea;
import mz.org.csaude.mentoring.workSchedule.rest.ProgrammaticAreaRestService;

public class ProgrammaticAreaWorker extends BaseWorker<ProgrammaticArea> {

    protected ProgrammaticAreaRestService programmaticAreaRestService;

    @Override
    public void doOnlineSearch(long offset, long limit) throws SQLException {
        this.programmaticAreaRestService.restGetProgrammaticArea(offset, limit, this);
    }

    public ProgrammaticAreaWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
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
