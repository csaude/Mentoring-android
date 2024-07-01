package mz.org.csaude.mentoring.viewmodel.mentorship;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mz.org.csaude.mentoring.BR;
import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.listner.dialog.IDialogListener;
import mz.org.csaude.mentoring.model.answer.Answer;
import mz.org.csaude.mentoring.model.answer.AnswerType;
import mz.org.csaude.mentoring.model.evaluationType.EvaluationType;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.formQuestion.FormQuestion;
import mz.org.csaude.mentoring.model.location.Cabinet;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.mentorship.Door;
import mz.org.csaude.mentoring.model.mentorship.IterationType;
import mz.org.csaude.mentoring.model.mentorship.Mentorship;
import mz.org.csaude.mentoring.model.mentorship.TimeOfDay;
import mz.org.csaude.mentoring.model.question.QuestionsCategory;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.ronda.RondaMentee;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.model.session.SessionStatus;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.util.DateUtilities;
import mz.org.csaude.mentoring.util.SimpleValue;
import mz.org.csaude.mentoring.util.SyncSatus;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.view.mentorship.CreateMentorshipActivity;
import mz.org.csaude.mentoring.view.session.SessionClosureActivity;

public class MentorshipVM extends BaseViewModel implements IDialogListener {

    private String CURR_MENTORSHIP_STEP = "";
    public static final String CURR_MENTORSHIP_STEP_TABLE_SELECTION = "TABLE_SELECTION";
    public static final String CURR_MENTORSHIP_STEP_MENTEE_SELECTION = "MENTEE_SELECTION";
    public static final String CURR_MENTORSHIP_STEP_PERIOD_SELECTION = "PERIOD_SELECTION";
    public static final String CURR_MENTORSHIP_STEP_QUESTION_SELECTION = "QUESTION_SELECTION";


    private Mentorship mentorship;

    private Ronda ronda;

    private Session session;

    private List<Form> forms;

    private List<Tutored> tutoreds;

    private List<FormQuestion> formQuestions;

    private TreeMap<SimpleValue, List<FormQuestion>> questionMap;

    private Listble currQuestionCategory;

    private List<Listble> categories;

    private boolean mentorshipCompleted;

    public void setCurrMentorshipStep(String step) {
        this.CURR_MENTORSHIP_STEP = step;
        notifyPropertyChanged(BR.currMentorshipStep);
    }

    @Bindable
    public String getCurrMentorshipStep() {
        return CURR_MENTORSHIP_STEP;
    }

    public MentorshipVM(@NonNull Application application) {
        super(application);
    }

    @Override
    public void preInit() {
    }

    public boolean isThereNextKey() {
        if (this.questionMap == null) return false;
        return this.questionMap.higherKey((SimpleValue) this.currQuestionCategory) != null;
    }

    public boolean isTherePreviousKey() {
        if (this.questionMap == null) return false;
        return this.questionMap.lowerKey((SimpleValue) this.currQuestionCategory) != null;
    }
    public void nextCategory() {
        setCurrQuestionCategory(this.questionMap.higherKey((SimpleValue) this.currQuestionCategory));
        getRelatedActivity().populateQuestionList();
    }

    public boolean isMentorshipCompleted() {
        return mentorshipCompleted;
    }

    public void setMentorshipCompleted(boolean mentorshipCompleted) {
        this.mentorshipCompleted = mentorshipCompleted;
    }

    private boolean allCurrentQuestionsResponded() {
        for (FormQuestion formQuestion : questionMap.get(this.currQuestionCategory)) {
            if (!Utilities.stringHasValue(formQuestion.getAnswer().getValue())) return false;
        }
        return true;
    }

    public void previousCategory() {
        if (this.questionMap.lowerKey((SimpleValue) this.currQuestionCategory) == null) {
            Utilities.displayAlertDialog(getRelatedActivity(), "Não existe uma categoria anterior para visualizar.").show();
            return;
        }
        setCurrQuestionCategory(this.questionMap.lowerKey((SimpleValue) this.currQuestionCategory));
        getRelatedActivity().populateQuestionList();
    }

    public void finnalizeMentorship() {
        if (!allQuestionsResponded()) {
            Utilities.displayAlertDialog(getRelatedActivity(), "Tem uma ou mais Competências sem a resposta indicada.").show();
            return;
        }

        Utilities.displayConfirmationDialog(getRelatedActivity(), "Terminou todas as Competências desta sessão, Confirma terminar a mesma?", "SIM", "NÃO",this).show();
    }

    private boolean allQuestionsResponded() {
        for (Map.Entry<SimpleValue, List<FormQuestion>> entry : questionMap.entrySet()) {
            for (FormQuestion question : entry.getValue()) {
                if (!Utilities.stringHasValue(question.getAnswer().getValue())) return false;
            }
        }
        return true;
    }

    @Bindable
    public Date getStartDate() {
        return this.mentorship.getStartDate();
    }

    public void setStartDate(Date startDate) {
        this.mentorship.setStartDate(startDate);
        notifyPropertyChanged(BR.startDate);
    }



    @Bindable
    public Date getEndDate() {
        return this.mentorship.getEndDate();
    }

    public void setEndDate(Date endDate) {
        this.mentorship.setEndDate(endDate);
         notifyPropertyChanged(BR.endDate);
    }

    @Bindable
    public Date getPerformedDate() {
        return this.mentorship.getPerformedDate();
    }

    public void setPerformedDate(Date performedDate) {
        this.mentorship.setPerformedDate(performedDate);
        notifyPropertyChanged(BR.performedDate);
    }


    @Bindable
    public Cabinet getCabinet() {
        return this.mentorship.getCabinet();
    }

    public void setCabinet(Cabinet cabinet) {
        this.mentorship.setCabinet(cabinet);
         notifyPropertyChanged(BR.cabinet);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Bindable
    public Door getDoor() {
        return this.mentorship.getDoor();
    }

    public void setDoor(Door door) {
        this.mentorship.setDoor(door);
         notifyPropertyChanged(BR.door);
    }

    public List<Form> getForms() {
        return forms;
    }

    public List<Form> getTutorForms() {
        try {
            this.forms = getApplication().getFormService().getAllOfTutor(getCurrentTutor());
            return forms;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void nextStep() {
        if (isTableSelectionStep()) {
            getRelatedActivity().populateMenteesList();
            setCurrMentorshipStep(CURR_MENTORSHIP_STEP_MENTEE_SELECTION);
        } else if (isMenteeSelectionStep()) {
            setCurrMentorshipStep(CURR_MENTORSHIP_STEP_PERIOD_SELECTION);
        } else if (isPeriodSelectionStep()) {
            loadQuestion();
            for (Listble listble : categories) {
                ((SimpleValue) listble).setExtraInfo("0/"+questionMap.get(listble).size());
            }
            getRelatedActivity().loadCategoryAdapter();
            getRelatedActivity().populateQuestionList();
            setCurrMentorshipStep(CURR_MENTORSHIP_STEP_QUESTION_SELECTION);
        } else if (isQuestionSelectionStep()) {
            finnalizeMentorship();
        }
        notifyPropertyChanged(BR.currMentorshipStep);
    }



    @Override
    public CreateMentorshipActivity getRelatedActivity() {
        return (CreateMentorshipActivity) super.getRelatedActivity();
    }

    public void previousStep() {
        if (isTableSelectionStep()) {
            setCurrMentorshipStep(CURR_MENTORSHIP_STEP_MENTEE_SELECTION);
        } else if (isMenteeSelectionStep()) {
            setCurrMentorshipStep(CURR_MENTORSHIP_STEP_PERIOD_SELECTION);
        } else if (isPeriodSelectionStep()) {
            setCurrMentorshipStep(CURR_MENTORSHIP_STEP_QUESTION_SELECTION);
        } else if (isQuestionSelectionStep()) {
            finnalizeMentorship();
        }
        notifyPropertyChanged(BR.currMentorshipStep);
    }

    public void setRonda(Ronda ronda) {
        this.ronda = ronda;
    }

    public Ronda getRonda() {
        return ronda;
    }

    public void setEvaluationType(String evaluationType) {
        try {
            this.mentorship.setEvaluationType(getApplication().getEvaluationTypeService().getByCode(evaluationType));
            if (!determineIterationNumber()) this.mentorship.setEvaluationType(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void determineMentorshipType() {
        try {
            if (this.mentorship == null) this.mentorship = new Mentorship();
            this.mentorship.setStartDate(DateUtilities.getCurrentDate());
            this.mentorship.setTutor(getApplication().getCurrMentor());
            this.mentorship.setUuid(Utilities.getNewUUID().toString());
            this.mentorship.setSyncStatus(SyncSatus.PENDING);
            this.mentorship.setCreatedAt(DateUtilities.getCurrentDate());
            this.mentorship.setPerformedDate(DateUtilities.getCurrentDate());
            if (this.ronda != null && this.ronda.isRondaZero()) {
                this.mentorship.setSession(generateZeroSession());
                this.mentorship.setIterationNumber(1);
                this.mentorship.setEvaluationType(getApplication().getEvaluationTypeService().getByCode(EvaluationType.CONSULTA));
            } else {
                this.mentorship.setSession(this.session);
                this.session.getRonda().addSession(this.mentorship.getSession());
                this.session.getRonda().addSession(getApplication().getSessionService().getAllOfRonda(this.session.getRonda()));

                this.mentorship.setForm(this.session.getForm());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Session generateZeroSession() {
        try {
            Session session = new Session();
            session.setRonda(this.ronda);
            session.setUuid(Utilities.getNewUUID().toString());
            session.setCreatedAt(DateUtilities.getCurrentDate());
            session.setSyncStatus(SyncSatus.PENDING);
            session.setStatus(getApplication().getSessionStatusService().getByuuid("953a6a3c-a583-4b96-86ee-91bcab7d3106"));
            session.setEndDate(this.mentorship.getEndDate());
            session.setStartDate(this.mentorship.getStartDate());
            session.setPerformedDate(DateUtilities.getCurrentDate());
            session.addMentorship(this.mentorship);
            session.setTutored(this.mentorship.getTutored());
            session.setForm(this.mentorship.getForm());
            return session;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isTableSelectionStep() {
        return this.CURR_MENTORSHIP_STEP.equals(CURR_MENTORSHIP_STEP_TABLE_SELECTION);
    }

    public boolean isMenteeSelectionStep() {
        return this.CURR_MENTORSHIP_STEP.equals(CURR_MENTORSHIP_STEP_MENTEE_SELECTION);
    }

    public boolean isPeriodSelectionStep() {
        return this.CURR_MENTORSHIP_STEP.equals(CURR_MENTORSHIP_STEP_PERIOD_SELECTION);
    }

    public boolean isQuestionSelectionStep() {
        return this.CURR_MENTORSHIP_STEP.equals(CURR_MENTORSHIP_STEP_QUESTION_SELECTION);
    }

    public void unselectAll() {
        for (Form form: forms){
            form.setItemSelected(false);
        }
    }

    public void selectForm(int position) {
        for (Form form : getForms()) {
            if (form.isSelected()) form.setItemSelected(false);
        }
        Form form = getForms().get(position);
        form.setItemSelected(true);
        mentorship.setForm(form);
    }

    public List<Tutored> getTutoreds() {
        return tutoreds;
    }

    public List<Tutored> getMentees() {
        try {
            this.tutoreds= getApplication().getTutoredService().getAllOfRondaForZeroEvaluation(this.mentorship.getSession().getRonda());
            for (Tutored tutored :this.tutoreds) {
                tutored.setListType(Listble.ListTypes.UNDEFINED);
            }
            return this.tutoreds;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectMentee(int position) {
        for (Tutored tutored : getTutoreds()) {
            if (tutored.isSelected()) tutored.setItemSelected(false);
        }
        Tutored tutored = getTutoreds().get(position);
        tutored.setItemSelected(true);
        mentorship.setTutored(tutored);
    }

    @Bindable
    public Listble getSelectedDoor() {
        return this.mentorship.getDoor();
    }

    public void setSelectedDoor(Listble selectedDoor) {
        if (selectedDoor == null) return;

        this.mentorship.setDoor((Door) selectedDoor);
        notifyPropertyChanged(BR.selectedDoor);
    }

    @Bindable
    public Listble getSelectedSector() {
        return this.mentorship.getCabinet();
    }

    public boolean isMentoringMentorship() {
        return !this.mentorship.getSession().getRonda().isRondaZero();
    }
    public void setSelectedSector(Listble selectedSector) {
        if (selectedSector == null) return;

        this.mentorship.setCabinet((Cabinet) selectedSector);
        notifyPropertyChanged(BR.selectedSector);
    }

    public List<Door> getDoors() {
        try {
            List<Door> doors = new ArrayList<>();
            doors.add(new Door());
            doors.addAll(getApplication().getDoorService().getAll());
            return doors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Cabinet> getSectors() {
        try {
            List<Cabinet> cabinets = new ArrayList<>();
            cabinets.add(new Cabinet());
            cabinets.addAll(getApplication().getCabinetService().getAll());
            return cabinets;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadQuestion() {
        try {
            this.formQuestions = getApplication().getFormQuestionService().getAllOfForm(this.mentorship.getForm(), this.mentorship.getEvaluationType().getCode());
            if (Utilities.listHasElements(this.formQuestions)) {
                for (FormQuestion formQuestion : formQuestions) {
                    formQuestion.setAnswer(new Answer());
                    formQuestion.getAnswer().setQuestion(formQuestion.getQuestion());
                    formQuestion.getAnswer().setSyncStatus(SyncSatus.PENDING);
                    formQuestion.getAnswer().setUuid(Utilities.getNewUUID().toString());
                    formQuestion.getAnswer().setCreatedAt(DateUtilities.getCurrentDate());
                    formQuestion.getAnswer().setMentorship(this.mentorship);
                    formQuestion.getAnswer().setForm(this.mentorship.getForm());
                    formQuestion.getAnswer().setValue("");
                    loadQuestionMap(formQuestion,formQuestion.getQuestion().getQuestionsCategory());
                }
                setCurrQuestionCategory(this.questionMap.firstKey());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void loadQuestionMap(FormQuestion formQuestion, QuestionsCategory category) {
        if (questionMap == null) {
            questionMap = new TreeMap<>((o1, o2) -> {
                if (o1.getId() != null && o2.getId() != null) {
                    return o1.getId().compareTo(o2.getId());
                } else if (o1.getDescription() != null && o2.getDescription() != null) {
                    return o1.getDescription().compareTo(o2.getDescription());
                }
                return 0;
            });
        }
        SimpleValue cat = SimpleValue.fastCreate(category.getId(), category.getDescription(), "0/0");
        if (!questionMap.containsKey(cat)) {
            questionMap.put(cat, new ArrayList<>());
            addToCategoryList(cat);
        }
        questionMap.get(cat).add(formQuestion);
    }

    private void addToCategoryList(SimpleValue cat) {
        if (categories == null) categories = new ArrayList<>();
        categories.add(cat);
    }

    public TreeMap<SimpleValue, List<FormQuestion>> getQuestionMap() {
        return questionMap;
    }

    @Bindable
    public Listble getCurrQuestionCategory() {
        return currQuestionCategory;
    }

    public void setCurrQuestionCategory(Listble currQuestionCategory) {
        if (currQuestionCategory == null) return;

        this.currQuestionCategory = currQuestionCategory;
        getRelatedActivity().populateQuestionList();
        notifyPropertyChanged(BR.currQuestionCategory);
    }

    @Override
    public void doOnConfirmed() {
        doSaveMentorship();
        showMentorshipSummary();
    }

    private void showMentorshipSummary() {
    }

    private void doSaveMentorship() {
        try {
            for (Map.Entry<SimpleValue, List<FormQuestion>> entry : questionMap.entrySet()) {
                for (FormQuestion question : entry.getValue()) {
                    this.mentorship.addAnswer(question.getAnswer());
                }
            }
            this.mentorship.setEndDate(DateUtilities.getCurrentDate());
            if (ronda.isRondaZero()) {
                this.mentorship.getTutored().setZeroEvaluationDone(true);
                this.mentorship.getSession().setTutored(this.mentorship.getTutored());
                this.mentorship.getSession().setEndDate(this.mentorship.getEndDate());
                this.mentorship.getSession().setStartDate(this.mentorship.getStartDate());
            } else {
                this.mentorship.setTutored(this.session.getTutored());
            }

            if (this.mentorship.getStartDate().before(this.mentorship.getSession().getStartDate())) {
                Utilities.displayAlertDialog(getRelatedActivity(), "A data de início não pode ser anterior a data de início da sessão.").show();
                return;
            }

            this.mentorship.getSession().setForm(this.mentorship.getForm());
            if (isMentoringMentorship()) {
                this.mentorship.getSession().addMentorship(this.mentorship);
                List<Mentorship> completedMentorships = getApplication().getMentorshipService().getAllOfSession(this.mentorship.getSession());
                this.mentorship.getSession().addMentorships(completedMentorships);
            }
            if (this.mentorship.getSession().canBeClosed()) {
                this.mentorship.getSession().setStatus(getApplication().getSessionStatusService().getByCode(SessionStatus.COMPLETE));
                this.mentorship.getSession().setEndDate(DateUtilities.getCurrentDate());
            }

            this.ronda.addSession(this.mentorship.getSession());
            ronda.setRondaMentees(getApplication().getRondaMenteeService().getAllOfRonda(this.ronda));
            this.ronda.tryToCloseRonda();
            getApplication().getMentorshipService().save(this.mentorship);
            Log.i("Saved Mentorship", this.mentorship.toString());
            if (isMentoringMentorship()) {
                if (this.mentorship.getSession().isCompleted()) {
                    initSessionClosure(this.mentorship.getSession());
                } else getRelatedActivity().finish();
            } else {
                getRelatedActivity().finish();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initSessionClosure(Session session) {
        Map<String, Object> params = new HashMap<>();
        params.put("session", session);
        getRelatedActivity().nextActivity(SessionClosureActivity.class, params);
    }

    private boolean determineIterationNumber() throws SQLException {
            List<Mentorship> completedMentorships = getApplication().getMentorshipService().getAllOfSession(this.mentorship.getSession());
            List<Mentorship> similarMentorships = new ArrayList<>();
            int maxIteration = 0;
            for (Mentorship mentorship : completedMentorships) {
                if (mentorship.getEvaluationType().getCode().equals(this.mentorship.getEvaluationType().getCode())) {
                    similarMentorships.add(mentorship);
                }
            }
            if (Utilities.listHasElements(similarMentorships)) {
                for (Mentorship mentorship : similarMentorships) {
                    if (mentorship.getIterationNumber() > maxIteration) {
                        maxIteration = mentorship.getIterationNumber();
                    }
                }
            }
            this.mentorship.setIterationNumber(maxIteration + 1);
            if (this.mentorship.getEvaluationType().getCode().equals(EvaluationType.CONSULTA)){
                if (this.mentorship.getForm().getTargetPatient() < this.mentorship.getIterationNumber()) {
                    Utilities.displayAlertDialog(getRelatedActivity(), "Não pode criar mais avaliações de observação de consulta nesta sessão, o maximo permitido é " + this.mentorship.getForm().getTargetPatient()).show();
                    return false;
                }
            } else if (this.mentorship.getEvaluationType().getCode().equals(EvaluationType.FICHA)) {
                if (this.mentorship.getForm().getTargetFile() < this.mentorship.getIterationNumber()) {
                    Utilities.displayAlertDialog(getRelatedActivity(), "Não pode criar mais avaliações de ficha nesta sessão, o maximo permitido é " + this.mentorship.getForm().getTargetFile()).show();
                    return false;
                }
            }
            return true;
    }

    public List<Listble> getCategories() {
        return categories;
    }

    public void setCategories(List<Listble> categories) {
        this.categories = categories;
    }

    @Override
    public void doOnDeny() {

    }

    public void setQuestionAnswer(FormQuestion formQuestion, String answerValue) {
        formQuestion.getAnswer().setValue(answerValue);
        int i=0;
        List<FormQuestion> formQuestionList= questionMap.get(this.currQuestionCategory);

        for (FormQuestion fq : formQuestionList) {
            if (Utilities.stringHasValue(fq.getAnswer().getValue()) && fq.getAnswer().getValue().length() > 1) i++;
        }
        for (Listble listble : categories) {
            if (listble.equals(this.currQuestionCategory)) {
                ((SimpleValue) listble).setExtraInfo(i + "/" + formQuestionList.size());
            }
        }
        ((SimpleValue) currQuestionCategory).setExtraInfo(i + "/" + formQuestionList.size());
        getRelatedActivity().reloadCategoryAdapter();
    }

    public String getStartTime() {
        return DateUtilities.formatToHHMI(DateUtilities.getCurrentDate());
    }
}
