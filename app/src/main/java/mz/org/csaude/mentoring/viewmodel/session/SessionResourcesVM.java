package mz.org.csaude.mentoring.viewmodel.session;

import android.app.Application;
import android.app.Dialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mz.org.csaude.mentoring.BR;
import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.base.searchparams.AbstractSearchParams;
import mz.org.csaude.mentoring.base.viewModel.SearchVM;
import mz.org.csaude.mentoring.listner.dialog.IDialogListener;
import mz.org.csaude.mentoring.model.resourceea.Node;
import mz.org.csaude.mentoring.model.resourceea.Resource;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.model.session.SessionRecommendedResource;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.view.session.SessionSummaryActivity;

public class SessionResourcesVM extends SearchVM<Resource> implements IDialogListener {

    private static final String TAG = SessionResourcesVM.class.getSimpleName();

    private boolean recommendResources;
    private String searchText;

    private final List<Node> nodeList = new ArrayList<>();
    private final List<SessionRecommendedResource> recommendedResources = new ArrayList<>();

    private Session session;

    private boolean hivChecked;
    private boolean tbChecked;

    public SessionResourcesVM(@NonNull Application application) {
        super(application);
    }

    @Override
    public void preInit() {
        // Se quiser defaults:
        // setHivChecked(true);
        // setTbChecked(true);
    }

    // -------------------------
    // Bindables
    // -------------------------

    @Bindable
    public boolean isRecommendResources() {
        return recommendResources;
    }

    public void setRecommendResources(boolean recommendResources) {
        this.recommendResources = recommendResources;
        notifyPropertyChanged(BR.recommendResources);
    }

    public void changeRecommendResourcesStatus() {
        setRecommendResources(!isRecommendResources());
    }

    @Bindable
    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
        notifyPropertyChanged(BR.searchText);
    }

    @Bindable
    public boolean isHivChecked() {
        return hivChecked;
    }

    public void setHivChecked(boolean hivChecked) {
        this.hivChecked = hivChecked;
        notifyPropertyChanged(BR.hivChecked);
    }

    @Bindable
    public boolean isTbChecked() {
        return tbChecked;
    }

    public void setTbChecked(boolean tbChecked) {
        this.tbChecked = tbChecked;
        notifyPropertyChanged(BR.tbChecked);
    }

    public void changeHivChecked() {
        setHivChecked(!isHivChecked());
        // opcional: aplicar filtro imediatamente
        initSearch();
    }

    public void changeTBChecked() {
        setTbChecked(!isTbChecked());
        // opcional: aplicar filtro imediatamente
        initSearch();
    }

    // -------------------------
    // Session
    // -------------------------

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    // -------------------------
    // SearchVM overrides
    // -------------------------

    @Override
    public List<Resource> doSearch(long offset, long limit) throws SQLException {
        return getApplication().getResourceService().getAll();
    }

    @Override
    public void displaySearchResults() {
        try {
            if (!Utilities.listHasElements(getSearchResults())) {
                doOnNoRecordFound();
                return;
            }

            Resource first = getSearchResults().get(0);
            if (first == null || !Utilities.stringHasValue(first.getResource())) {
                doOnNoRecordFound();
                return;
            }

            JSONArray jsonArray = new JSONArray(first.getResource());
            List<JSONObject> children = getApplication()
                    .getResourceService()
                    .getChildrenWithNameAndDescription(jsonArray, null, null, null);

            List<Node> nodes = getApplication().getResourceService().convertToNodeList(children);

            nodeList.clear();

            if (!Utilities.listHasElements(nodes)) {
                doOnNoRecordFound();
                return;
            }

            // 1) text filter (name/description, case-insensitive)
            String q = (getSearchText() == null) ? "" : getSearchText().trim().toLowerCase();

            if (Utilities.stringHasValue(q)) {
                for (Node node : nodes) {
                    String name = node.getName() == null ? "" : node.getName().toLowerCase();
                    String desc = node.getDescription() == null ? "" : node.getDescription().toLowerCase();

                    if (name.contains(q) || desc.contains(q)) {
                        nodeList.add(node);
                    }
                }
            } else {
                nodeList.addAll(nodes);
            }

            // 2) HIV/TB filter
            // - none checked => show all
            // - any checked => keep only those checked
            boolean filterHiv = isHivChecked();
            boolean filterTb = isTbChecked();

            if (filterHiv || filterTb) {
                Iterator<Node> it = nodeList.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    String programLower = (node.getProgram() == null) ? "" : node.getProgram().toLowerCase();

                    boolean isHiv = programLower.contains("hiv");
                    boolean isTb = programLower.contains("tb");

                    boolean keep = (isHiv && filterHiv) || (isTb && filterTb);

                    // se não é hiv nem tb, remove quando estiver filtrando
                    if (!isHiv && !isTb) keep = false;

                    if (!keep) it.remove();
                }
            }

            if (!Utilities.listHasElements(nodeList)) {
                doOnNoRecordFound();
                return;
            }

            // 3) keep selection consistent (marks selected items)
            syncSelectionFlags();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        getRelatedActivity().displaySearchResults();
    }

    @Override
    public AbstractSearchParams<Resource> initSearchParams() {
        return null;
    }

    @Override
    protected void doOnNoRecordFound() {
        Utilities.displayAlertDialog(
                getRelatedActivity(),
                getRelatedActivity().getString(R.string.no_results_found)
        ).show();
    }

    // -------------------------
    // Selection logic (no getNode())
    // -------------------------

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void selectResource(int position) {
        if (session == null) return;
        if (position < 0 || position >= nodeList.size()) return;

        Node node = nodeList.get(position);
        String key = nodeKey(node);
        if (!Utilities.stringHasValue(key)) return;

        node.setItemSelected(!node.isSelected());

        if (node.isSelected()) {
            if (!containsRecommendedKey(key)) {
                recommendedResources.add(new SessionRecommendedResource(session, node));
            }
        } else {
            removeRecommendedByKey(key);
        }
    }

    private String nodeKey(Node node) {
        if (node == null) return null;
        String name = node.getName();
        if (!Utilities.stringHasValue(name)) return null;

        // compatível com o teu ctor de SessionRecommendedResource
        return name.replace(" ", "_").trim();
    }

    private boolean containsRecommendedKey(String key) {
        if (!Utilities.stringHasValue(key)) return false;
        if (!Utilities.listHasElements(recommendedResources)) return false;

        for (SessionRecommendedResource rr : recommendedResources) {
            if (rr == null) continue;

            String rrKey = rr.getResourceName();
            if (!Utilities.stringHasValue(rrKey)) rrKey = rr.getResourceLink();

            if (key.equals(rrKey)) return true;
        }
        return false;
    }

    private void removeRecommendedByKey(String key) {
        if (!Utilities.stringHasValue(key)) return;
        if (!Utilities.listHasElements(recommendedResources)) return;

        Iterator<SessionRecommendedResource> it = recommendedResources.iterator();
        while (it.hasNext()) {
            SessionRecommendedResource rr = it.next();
            if (rr == null) continue;

            String rrKey = rr.getResourceName();
            if (!Utilities.stringHasValue(rrKey)) rrKey = rr.getResourceLink();

            if (key.equals(rrKey)) {
                it.remove();
                break;
            }
        }
    }

    private void syncSelectionFlags() {
        if (!Utilities.listHasElements(nodeList)) return;

        for (Node node : nodeList) {
            String key = nodeKey(node);
            node.setItemSelected(containsRecommendedKey(key));
        }
    }

    // -------------------------
    // Close session flow
    // -------------------------

    public void closeSession() {
        Utilities.displayConfirmationDialog(
                getRelatedActivity(),
                getRelatedActivity().getString(R.string.confirm_end_session),
                getRelatedActivity().getString(R.string.yes),
                getRelatedActivity().getString(R.string.no),
                this
        ).show();
    }

    @Override
    public void doOnConfirmed() {
        goToSessionSummary();
    }

    @Override
    public void doOnDeny() {
        // no-op
    }

    private void goToSessionSummary() {
        if (session == null) {
            Utilities.displayAlertDialog(
                    getRelatedActivity(),
                    getRelatedActivity().getString(R.string.session_update_error)
            ).show();
            return;
        }

        Dialog progress = Utilities.showLoadingDialog(
                getRelatedActivity(),
                getRelatedActivity().getString(R.string.processando)
        );

        getExecutorService().execute(() -> {
            try {
                // Update session
                getApplication().getSessionService().update(session);

                if (isRecommendResources()) {
                    if (!Utilities.listHasElements(recommendedResources)) {
                        runOnMainThread(() -> {
                            dismissProgress(progress);
                            Utilities.displayAlertDialog(
                                    getRelatedActivity(),
                                    getRelatedActivity().getString(R.string.no_resource_selected_error)
                            ).show();
                        });
                        return;
                    }

                    // Save recommended resources
                    getApplication().getSessionService().saveRecommendedResources(session, recommendedResources);
                }

                runOnMainThread(() -> {
                    dismissProgress(progress);

                    Map<String, Object> params = new HashMap<>();
                    session.setMentorships(Collections.emptyList()); // mantém teu comportamento
                    params.put("session", session);

                    getRelatedActivity().nextActivityFinishingCurrent(SessionSummaryActivity.class, params);
                });

            } catch (SQLException e) {
                Log.e(TAG, "goToSessionSummary: ", e);

                runOnMainThread(() -> {
                    dismissProgress(progress);
                    Utilities.displayAlertDialog(
                            getRelatedActivity(),
                            getRelatedActivity().getString(R.string.session_update_error)
                    ).show();
                });
            }
        });
    }
}
