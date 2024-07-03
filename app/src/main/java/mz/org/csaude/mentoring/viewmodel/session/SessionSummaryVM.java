package mz.org.csaude.mentoring.viewmodel.session;

import android.app.Application;

import androidx.annotation.NonNull;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.model.session.SessionSummary;
import mz.org.csaude.mentoring.util.PDFGenerator;
import mz.org.csaude.mentoring.util.Utilities;

public class SessionSummaryVM extends BaseViewModel {

    private Session session;

    private List<SessionSummary> sessionSummaryList;

    public SessionSummaryVM(@NonNull Application application) {
        super(application);
    }

    @Override
    public void preInit() {

    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void print() {
        boolean print = PDFGenerator.createPDF(getRelatedActivity(), this.sessionSummaryList, this.session.getTutored());
        if (print) {
            Utilities.displayAlertDialog(getRelatedActivity(), "Resumo impresso com sucesso, encontre o documento no diretório de downloads.").show();
        } else {
            Utilities.displayAlertDialog(getRelatedActivity(), "Não foi possível imprimir o documento.").show();
        }
    }

    public List<SessionSummary> getSessionSummaryList() {
        return sessionSummaryList;
    }

    public void generateSessionSummary() {
        sessionSummaryList = getApplication().getSessionService().generateSessionSummary(session);
        if (Utilities.listHasElements(sessionSummaryList)) {
            getRelatedActivity().displaySearchResults();
        }
    }
}