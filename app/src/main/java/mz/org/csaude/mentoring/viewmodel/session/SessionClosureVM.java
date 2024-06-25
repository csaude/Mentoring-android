package mz.org.csaude.mentoring.viewmodel.session;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import java.util.Date;

import mz.org.csaude.mentoring.BR;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.service.session.SessionClosureService;
import mz.org.csaude.mentoring.view.session.SessionClosureActivity;

public class SessionClosureVM extends BaseViewModel {
    private Session session;

    private boolean initialDataVisible;

    private Date endDate;
    private SessionClosureService sessionClosureService;

    public SessionClosureVM(@NonNull Application application) {
        super(application);
    }

    @Override
    public void preInit() {

    }

    @Bindable
    public String getSessionStrongPoints() {
        return session.getStrongPoints();
    }

    public void setSessionStrongPoints(String strongPoints) {
        session.setStrongPoints(strongPoints);
        notifyPropertyChanged(BR.sessionStrongPoints);
    }

    @Bindable
    public String getPointsToImprove() {
        return session.getPointsToImprove();
    }

    public void setPointsToImprove(String strongPoints) {
        session.setPointsToImprove(strongPoints);
        notifyPropertyChanged(BR.pointsToImprove);
    }

    public void setObsevations(String strongPoints) {
        session.setObsevations(strongPoints);
        notifyPropertyChanged(BR.obsevations);
    }

    @Bindable
    public String getObsevations() {
        return session.getObsevations();
    }
    @Override
    public SessionClosureActivity getRelatedActivity() {
        return (SessionClosureActivity) super.getRelatedActivity();
    }

    @Bindable
    public String getWorkPlan() {
        return session.getWorkPlan();
    }

    public void setWorkPlan(String strongPoints) {
        session.setWorkPlan(strongPoints);
        notifyPropertyChanged(BR.workPlan);
    }

    @Bindable
    public boolean isInitialDataVisible() {
        return initialDataVisible;
    }

    public void setInitialDataVisible(boolean initialDataVisible) {
        this.initialDataVisible = initialDataVisible;
        this.notifyPropertyChanged(BR.initialDataVisible);
    }

    @Bindable
    public Date getEndDate() {
        return this.session.getEndDate();
    }

    public void setEndtDate(Date startDate) {
        this.session.setEndDate(startDate);
        notifyPropertyChanged(BR.endDate);
    }


    public void nextStep() {
        go to summary

    }

    public void changeInitialDataViewStatus(View view){
        getRelatedActivity().changeFormSectionVisibility(view);
    }


    public void saveAndContinue(){
        //
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
