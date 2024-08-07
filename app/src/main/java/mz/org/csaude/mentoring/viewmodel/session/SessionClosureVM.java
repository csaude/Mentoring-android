package mz.org.csaude.mentoring.viewmodel.session;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mz.org.csaude.mentoring.BR;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.service.session.SessionClosureService;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.view.session.SessionClosureActivity;
import mz.org.csaude.mentoring.view.session.SessionEAResourceActivity;

public class SessionClosureVM extends BaseViewModel {
    private Session session;

    private boolean initialDataVisible;

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
        session.setObservations(strongPoints);
        notifyPropertyChanged(BR.obsevations);
    }

    @Bindable
    public String getObsevations() {
        return session.getObservations();
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
        try {
            if (session.getEndDate().before(session.getStartDate())) {
                Utilities.displayAlertDialog(getRelatedActivity(), "A data de fim da sessão não pode ser menor que a data de início").show();
                return;
            }

            if (sessionCloseDateBeforeLastMentorship()) {
                Utilities.displayAlertDialog(getRelatedActivity(), "A data de fim da sessão não pode ser menor que a data final da última avaliação").show();
                return;
            }
            getApplication().getSessionService().update(session);
            session.getRonda().setRondaMentors(getApplication().getRondaMentorService().getRondaMentors(session.getRonda()));
            Map<String, Object> params = new HashMap<>();
            params.put("session", session);
            getRelatedActivity().nextActivity(SessionEAResourceActivity.class, params);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private boolean sessionCloseDateBeforeLastMentorship() {
        for (Mentorship mentorship : session.getMentorships()) {
            if (session.getEndDate().before(mentorship.getEndDate())) {
                return true;
            }
        }
        return false;
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
