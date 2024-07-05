package mz.org.csaude.mentoring.view.home.ui.notifications;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.service.ronda.RondaService;
import mz.org.csaude.mentoring.service.session.SessionService;
import mz.org.csaude.mentoring.view.home.ui.personalinfo.PersonalInfoFragment;

public class NotificationsVM extends BaseViewModel {

    private List<Session> sessions ;

    private SessionService sessionService;

    private RondaService rondaService;

    public NotificationsVM(@NonNull Application application) {
        super(application);

        this.sessions = new ArrayList<>();
        this.sessionService = getApplication().getSessionService();
        this.rondaService = getApplication().getRondaService();
    }

    @Override
    public void preInit() {

    }

    public void changeInitialDataViewStatus(View view){
        getNotificationsActivity().changeFormSectionVisibility(view);
    }

  private NotificationsActivity getNotificationsActivity(){
        return (NotificationsActivity) super.getRelatedActivity();
  }

    public List<Session> getSessions() {
        try {
            return this.sessions = this.sessionService.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSessions(List<Session> sessions) {

    }

    public List<Session> getSessionsByMentor() {
        try {

            List<Ronda> rondas = this.rondaService.getAllByMentor(getCurrentTutor(), getApplication());
            if(rondas != null){
                for (Ronda ronda : rondas){
                    sessions.addAll(this.sessionService.getAllOfRondaPending(ronda));
                }
            }
             return this.sessions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
