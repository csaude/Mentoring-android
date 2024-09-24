package mz.org.csaude.mentoring.viewmodel.login;

import android.app.Application;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mz.org.csaude.mentoring.BR;
import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.listner.rest.ServerStatusListener;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.model.session.SessionRecommendedResource;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.user.UserService;
import mz.org.csaude.mentoring.service.user.UserSyncService;
import mz.org.csaude.mentoring.util.DateUtilities;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.view.home.MainActivity;
import mz.org.csaude.mentoring.workSchedule.executor.WorkerScheduleExecutor;
import mz.org.csaude.mentoring.workSchedule.rest.UserRestService;

public class LoginVM extends BaseViewModel implements RestResponseListener<User>, ServerStatusListener {

    private final UserService userService;
    private final User user;

    private boolean remeberMe;

    private boolean authenticating;
    private final WorkerScheduleExecutor workerScheduleExecutor;


    private UserSyncService userSyncService;


    public LoginVM(@NonNull Application application) {
        super(application);
        userService = getApplication().getUserService();
        this.user= new User();
        if (Utilities.stringHasValue(getApplication().getLastUser())) {
            user.setUserName(getApplication().getLastUser());
            setRemeberMe(true);
        }
        workerScheduleExecutor = WorkerScheduleExecutor.getInstance(application);
        this.userSyncService = new UserRestService(application, this.user);
    }

    @Override
    public void preInit() {
        getApplication().initSessionManager();
    }

    @Bindable
    public String getUserName() {
        return this.user.getUserName();
    }
    public void setUserName(String userName) {
        this.user.setUserName(userName);
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getUserPassword() {
        return this.user.getPassword();
    }
    public void setUserPassword(String password) {
        this.user.setPassword(password);
        notifyPropertyChanged(BR.userPassword);
    }

    public void doLogin() {
        getExecutorService().execute(()-> {
            setAuthenticating(true);
            try {
                if (AppHasUser()) {
                    doLocalLogin();
                } else {
                    getApplication().isServerOnline(this);
                }
                getApplication().saveDefaultSyncSettings();
                getApplication().saveDefaultLastSyncDate(DateUtilities.getCurrentDate());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void doLocalLogin() throws SQLException {
        User logedUser = userService.login(this.user);

        if (logedUser != null) {
            if (!logedUser.isActivated()) {
                String inactiveMessage = getRelatedActivity().getString(R.string.user_inactive);
                Utilities.displayAlertDialog(getRelatedActivity(), inactiveMessage).show();
                return;
            }
            getApplication().setAuthenticatedUser(logedUser, remeberMe);
            goHome();
        } else {
            String invalidMessage = getRelatedActivity().getString(R.string.invalid_user_or_password);
            Utilities.displayAlertDialog(getRelatedActivity(), invalidMessage).show();
        }

        setAuthenticating(false);
    }

    private boolean AppHasUser() {
        try {
            return Utilities.listHasElements(userService.getAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doOnRestSucessResponse(User user) {
        getRelatedActivity().runOnUiThread(() -> {
            try {
                if (getApplication().isInitialSetupComplete()) {
                    getApplication().init();
                    goHome();
                } else {
                    OneTimeWorkRequest request = WorkerScheduleExecutor.getInstance(getApplication()).runPostLoginSync();

                    WorkerScheduleExecutor.getInstance(getApplication()).getWorkManager().getWorkInfoByIdLiveData(request.getId()).observe(getRelatedActivity(), workInfo -> {
                        if (workInfo != null) {
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                try {
                                    getApplication().init();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }

                                OneTimeWorkRequest downloadMentorData = WorkerScheduleExecutor.getInstance(getApplication()).downloadMentorData();
                                WorkerScheduleExecutor.getInstance(getApplication()).getWorkManager().getWorkInfoByIdLiveData(downloadMentorData.getId()).observe(getRelatedActivity(), info -> {
                                    if (info.getState() == WorkInfo.State.SUCCEEDED) {
                                        getApplication().setInitialSetUpComplete();
                                        getApplication().saveDefaultLastSyncDate(DateUtilities.getCurrentDate());
                                        goHome();
                                    }
                                });
                            }
                        }
                    });
                }
            } catch (SQLException e) {
                Log.e("LoginVM", "doOnRestSucessResponse: ", e);
            }
        });
    }


    private void goHome() {
        try {
            getApplication().init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> params = new HashMap<>();
        getRelatedActivity().nextActivityFinishingCurrent(MainActivity.class, params);
    }

    @Bindable
    public boolean isAuthenticating() {
        return this.authenticating;
    }

    public void setAuthenticating(boolean authenticating) {
        this.authenticating = authenticating;
        notifyPropertyChanged(BR.authenticating);
    }

    @Bindable
    public boolean isRemeberMe() {
        return remeberMe;
    }

    public void setRemeberMe(boolean remeberMe) {
        this.remeberMe = remeberMe;
        notifyPropertyChanged(BR.remeberMe);
    }

    public void changeRemeberMeStatus() {
        setRemeberMe(!isRemeberMe());
    }

    @Override
    public void onServerStatusChecked(boolean isOnline) {
        if (isOnline) {
            userSyncService.doOnlineLogin(this, remeberMe);
        }else {
            Utilities.displayAlertDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.server_unavailable)).show();
        }
    }

    @Override
    public void doOnRestErrorResponse(String errormsg) {
        runOnMainThread(() -> {
            if (Utilities.stringHasValue(errormsg)) {
                Utilities.displayAlertDialog(getRelatedActivity(), errormsg).show();
            } else {
                String invalidMessage = getRelatedActivity().getString(R.string.invalid_user_or_password);
                Utilities.displayAlertDialog(getRelatedActivity(), invalidMessage).show();
            }
            setAuthenticating(false);
        });
    }

    public void ConfirmedSyncData() {
        doSync();
    }

    private void doSync(){
        ProgressDialog progressDialog = new ProgressDialog(getRelatedActivity());
        progressDialog.setMessage(getApplication().getString(R.string.syncing_data_please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        OneTimeWorkRequest request = workerScheduleExecutor.syncNowData();

        workerScheduleExecutor.getWorkManager().getWorkInfoByIdLiveData(request.getId()).observe(getRelatedActivity(), new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo info) {

                if(info != null){
                    if(info.getState() == WorkInfo.State.SUCCEEDED) {
                        // Sync succeed
                        Utilities.displayAlertDialog(getRelatedActivity(), getApplication().getString(R.string.sync_completed_successfully)).show();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    } else if(info.getState() == WorkInfo.State.FAILED){
                        Utilities.displayAlertDialog(getRelatedActivity(), getApplication().getString(R.string.sync_failed)).show();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        //getRelatedActivity().nextActivity(SplashActivity.class);
                    }
                }
            }
        });

    }

    public boolean isDataNotSyncro(){

        final boolean result[] = {false};
// Cria um objeto de bloqueio para sincronização
        final Object lock = new Object();

        Date lastDateSync = getApplication().getDefaultLastSyncDate();
        if(lastDateSync == null) return false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtilities.getCurrentDate());
        calendar.add(Calendar.DAY_OF_MONTH, -7);

        Date lastOneWeek = calendar.getTime();

        if(lastDateSync.before(lastOneWeek) || DateUtilities.isSameDay(lastDateSync, lastOneWeek)){
            System.out.println("A data é menor ou igual à data de hoje.");

            getExecutorService().submit(() -> {
                synchronized (lock) {
                    try {
                        List<Mentorship> mentorshipList = getApplication().getMentorshipService().getAllNotSynced(getApplication());
                        List<Session> sessions = getApplication().getSessionService().getAllNotSynced();
                        List<Ronda> rondas = getApplication().getRondaService().getAllNotSynced();
                        if(Utilities.listHasElements(mentorshipList) || Utilities.listHasElements(sessions) || Utilities.listHasElements(rondas) ){
                            result[0] = true;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    // Notifica a thread principal que a operação foi concluída
                    lock.notify();
                }
            });
            synchronized (lock) {
                try {
                    // Espera a operação de banco de dados ser concluída
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else{
            result[0] = false;
        }

        return  result[0];
    }

}
