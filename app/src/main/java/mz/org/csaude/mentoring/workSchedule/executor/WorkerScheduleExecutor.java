package mz.org.csaude.mentoring.workSchedule.executor;

import android.app.Application;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.List;

import mz.org.csaude.mentoring.model.setting.Setting;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.workSchedule.work.CabinetWorker;
import mz.org.csaude.mentoring.workSchedule.work.CareerWorker;
import mz.org.csaude.mentoring.workSchedule.work.FormWorker;
import mz.org.csaude.mentoring.workSchedule.work.ProgrammaticAreaWorker;
import mz.org.csaude.mentoring.workSchedule.work.TutorWorker;
import mz.org.csaude.mentoring.workSchedule.work.TutoredWork;
import mz.org.csaude.mentoring.workSchedule.work.UserWorker;

public class WorkerScheduleExecutor {

    private static final String TAG = "WorkerScheduler";
    public static final int JOB_ID = 1000;
    public static final int ONE_TIME_REQUEST_JOB_ID = 1001;

    private WorkManager workManager;

    private static WorkerScheduleExecutor instance;

    private Application application;

    private List<Setting> settings;

    private WorkerScheduleExecutor(Application application) {
        this.application = application;
        this.workManager = WorkManager.getInstance(application);
    }

    public WorkManager getWorkManager() {
        return workManager;
    }

    public static WorkerScheduleExecutor getInstance(Application application) {
        if (instance == null) {
            instance = new WorkerScheduleExecutor(application);
        }
        return instance;
    }
    public OneTimeWorkRequest runinitialSync() {
        OneTimeWorkRequest cabinetOneTimeWorkRequest = new OneTimeWorkRequest.Builder(CabinetWorker.class).addTag("ONE_TIME_CABINET_ID" + ONE_TIME_REQUEST_JOB_ID).build();
        OneTimeWorkRequest careerOneTimeWorkRequest = new OneTimeWorkRequest.Builder(CareerWorker.class).addTag("ONE_TIME_CAREER_ID" + ONE_TIME_REQUEST_JOB_ID).build();

        workManager.beginUniqueWork("INITIAL_APP_SETUP", ExistingWorkPolicy.KEEP, cabinetOneTimeWorkRequest).then(careerOneTimeWorkRequest).enqueue();
        return careerOneTimeWorkRequest;

        //return (ListenableFuture) workManager.beginUniqueWork("INITIAL_APP_SETUP", ExistingWorkPolicy.KEEP, Arrays.asList(cabinetOneTimeWorkRequest, careerOneTimeWorkRequest)).enqueue().getResult();
    }

    public void runOneTimeCabinetSync() {
        OneTimeWorkRequest cabinetOneTimeWorkRequest = new OneTimeWorkRequest.Builder(CabinetWorker.class).addTag("ONE_TIME_CABINET_ID" + ONE_TIME_REQUEST_JOB_ID).build();
        if (!Utilities.isWorkScheduled("ONE_TIME_CABINET_ID" + ONE_TIME_REQUEST_JOB_ID, workManager) && !Utilities.isWorkRunning("INIT_CABINET_ID " + JOB_ID, workManager)) {
            workManager.enqueue(cabinetOneTimeWorkRequest);
        }
    }

    public OneTimeWorkRequest runPostLoginSync(User user) {
        Data inputData = new Data.Builder()
                .putString("username", user.getUserName())
                .putString("password", user.getPassword())
                .build();
        OneTimeWorkRequest userOneTimeWorkRequest = new OneTimeWorkRequest.Builder(UserWorker.class).addTag("ONE_TIME_USER_ID" + ONE_TIME_REQUEST_JOB_ID).setInputData(inputData).build();
        OneTimeWorkRequest tutorOneTimeWorkRequest = new OneTimeWorkRequest.Builder(TutorWorker.class).addTag("ONE_TIME_TUTOR_ID" + ONE_TIME_REQUEST_JOB_ID).build();
        OneTimeWorkRequest programmaticAreaOneTimeworkResquest = new OneTimeWorkRequest.Builder(ProgrammaticAreaWorker.class).addTag("ONE_TIME_PROGRAMMATICAREA_ID" + ONE_TIME_REQUEST_JOB_ID).build();
        OneTimeWorkRequest formOneTimeworkResquest = new OneTimeWorkRequest.Builder(FormWorker.class).addTag("ONE_TIME_FORM_ID" + ONE_TIME_REQUEST_JOB_ID).build();
        OneTimeWorkRequest tutoredOneTimeworkResquest = new OneTimeWorkRequest.Builder(TutoredWork.class).addTag("ONE_TIME_TUTORED_ID" + ONE_TIME_REQUEST_JOB_ID).build();


        workManager.beginUniqueWork("INITIAL_APP_SETUP", ExistingWorkPolicy.KEEP, userOneTimeWorkRequest).then(tutorOneTimeWorkRequest)
                .then(programmaticAreaOneTimeworkResquest).then(tutoredOneTimeworkResquest).then(formOneTimeworkResquest).
                enqueue();
        return tutoredOneTimeworkResquest;
    }
}
