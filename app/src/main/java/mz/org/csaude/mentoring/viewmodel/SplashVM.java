package mz.org.csaude.mentoring.viewmodel;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.base.activity.BaseActivity;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.listner.rest.ServerStatusListener;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataService;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.view.login.LoginActivity;
import mz.org.csaude.mentoring.view.splash.SplashActivity;
import mz.org.csaude.mentoring.workSchedule.executor.ExecutorThreadProvider;
import mz.org.csaude.mentoring.workSchedule.executor.WorkerScheduleExecutor;

public class SplashVM extends BaseViewModel implements RestResponseListener, ServerStatusListener {


    public SplashVM(@NonNull Application application) {
        super(application);
    }

    @Override
    public void preInit() {

    }


    @Override
    public SplashActivity getRelatedActivity() {
        return (SplashActivity) super.getRelatedActivity();
    }

    public void initAppConfiguration() {
        //OneTimeWorkRequest request = WorkerScheduleExecutor.getInstance(getApplication()).runInitialSync();
        if (getApplication().isInitialSetupComplete()) {
            //try {Thread.sleep(3000);} catch (InterruptedException e) {throw new RuntimeException(e);}
            goToLogin();
        } else {
            getApplication().isServerOnline(this);
        }
    }

    public void goToLogin() {
        getRelatedActivity().nextActivityFinishingCurrent(LoginActivity.class);
    }

    @Override
    public void onServerStatusChecked(boolean isOnline) {
        if (isOnline) {
            OneTimeWorkRequest request = WorkerScheduleExecutor.getInstance(getApplication()).runInitialSync();

            WorkerScheduleExecutor.getInstance(getApplication()).getWorkManager().getWorkInfoByIdLiveData(request.getId()).observe(getRelatedActivity(), workInfo -> {
                if (workInfo != null) {
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        WorkInfo.State state = workInfo.getState();
                        goToLogin();
                    }
                }
            });
        } else {
            Utilities.displayAlertDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.server_unavailable)).show();
        }
    }
}
