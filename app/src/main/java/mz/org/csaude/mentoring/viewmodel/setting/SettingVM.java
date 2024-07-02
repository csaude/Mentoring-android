package mz.org.csaude.mentoring.viewmodel.setting;

import static mz.org.csaude.mentoring.util.Constants.METADATA_SYNC_TIME;
import static mz.org.csaude.mentoring.util.Constants.SESSION_SYNC_TIME;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import java.sql.SQLException;

import mz.org.csaude.mentoring.BR;
import mz.org.csaude.mentoring.base.application.MentoringApplication;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.model.setting.Setting;
import mz.org.csaude.mentoring.service.setting.SettingService;
import mz.org.csaude.mentoring.service.setting.SettingServiceImpl;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.workSchedule.executor.WorkerScheduleExecutor;

public class SettingVM extends BaseViewModel {

    private Setting setting;

    private SettingService settingService;
    private WorkerScheduleExecutor workerScheduleExecutor;
    public int sessionSyncTime;
    public int metadataSyncTime;

    public SettingVM(@NonNull Application application) {
        super(application);
        this.settingService = new SettingServiceImpl(application);
        workerScheduleExecutor = WorkerScheduleExecutor.getInstance(getApplication());
        this.sessionSyncTime = this.getApplication().getMentoringSharedPreferences().getInt(SESSION_SYNC_TIME, 2);
        this.metadataSyncTime = this.getApplication().getMentoringSharedPreferences().getInt(METADATA_SYNC_TIME, 2);
    }

    @Override
    public void preInit() {
    }
    @Bindable
    public String getDescription() {
        return this.setting.getDescription();
    }

    public void setDescription(String description) {
        this.setting.setDescription(description);
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getValue() {
        return this.setting.getValue();
    }

    public void setValue(String value) {
        this.setting.setValue(value);
        notifyPropertyChanged(BR.value);
    }

    private String designation;

    @Bindable
    public String getDesignation() {
        return this.setting.getDesignation();
    }

    public void setDesignation(String designation) {
        this.setting.setDesignation(designation);
        notifyPropertyChanged(BR.designation);
    }

    public void save() {
        try {
            this.settingService.save(this.setting)
                    ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void syncAllNow() {
        workerScheduleExecutor.syncNowData();
    }

    public void saveSessionSyncTime() {
        SharedPreferences.Editor editor = this.getApplication().getMentoringSharedPreferences().edit();
        editor.putInt(SESSION_SYNC_TIME, this.sessionSyncTime);
        editor.commit();
    }
    public void saveMetadataSyncTime() {
        SharedPreferences.Editor editor = this.getApplication().getMentoringSharedPreferences().edit();
        editor.putInt(METADATA_SYNC_TIME, this.metadataSyncTime);
        editor.commit();
    }

    @Bindable
    public String getSessionSyncTime() {
        return Utilities.parseIntToString(this.getApplication().getMentoringSharedPreferences().getInt(SESSION_SYNC_TIME, 2));
    }

    public void setSessionSyncTime(String sessionSyncTime) {
        this.sessionSyncTime = Integer.parseInt(sessionSyncTime);
        this.notifyPropertyChanged(BR.sessionSyncTime);
    }

    @Bindable
    public String getMetadataSyncTime() {
        return Utilities.parseIntToString(this.getApplication().getMentoringSharedPreferences().getInt(METADATA_SYNC_TIME, 2));
    }

    public void setMetadataSyncTime(String metadataSyncTime) {
        this.metadataSyncTime = Integer.parseInt(metadataSyncTime);
        this.notifyPropertyChanged(BR.metadataSyncTime);
    }
}
