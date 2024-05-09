package mz.org.csaude.mentoring.workSchedule.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.worker.BaseWorker;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;
import mz.org.csaude.mentoring.util.Http;
import mz.org.csaude.mentoring.util.Utilities;

public class MentorshipWorker extends BaseWorker<Mentorship> {
    private String requestType;

    public MentorshipWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        requestType = getInputData().getString("requestType");
    }

    @Override
    public void doOnlineSearch(long offset, long limit) throws SQLException {
        if (Utilities.stringHasValue(requestType) && requestType.equalsIgnoreCase(String.valueOf(Http.POST))) {
            getApplication().getMentorshipRestService().restPostMentorships(this);
        } else {
            getApplication().getMentorshipRestService().restGetMentorships(this);
        }
    }

    @Override
    protected void doOnStart() {

    }

    @Override
    protected void doAfterSearch(String flag, List<Mentorship> recs) throws SQLException {
        changeStatusToFinished();
        doOnFinish();
    }

    @Override
    protected void doOnFinish() {

    }

    @Override
    protected void doSave(List<Mentorship> recs) {

    }
}
