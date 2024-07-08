package mz.org.csaude.mentoring.viewmodel.ronda;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mz.org.csaude.mentoring.BR;
import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.dto.ronda.RondaDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.listner.rest.ServerStatusListener;
import mz.org.csaude.mentoring.model.location.District;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.location.Province;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.ronda.RondaMentee;
import mz.org.csaude.mentoring.model.ronda.RondaMentor;
import mz.org.csaude.mentoring.model.rondatype.RondaType;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.service.ronda.RondaService;
import mz.org.csaude.mentoring.service.ronda.RondaTypeService;
import mz.org.csaude.mentoring.util.DateUtilities;
import mz.org.csaude.mentoring.util.LifeCycleStatus;
import mz.org.csaude.mentoring.util.RondaTypeEnum;
import mz.org.csaude.mentoring.util.SimpleValue;
import mz.org.csaude.mentoring.util.SyncSatus;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.view.ronda.CreateRondaActivity;
import mz.org.csaude.mentoring.view.ronda.RondaActivity;

public class RondaVM extends BaseViewModel implements RestResponseListener<Ronda>, ServerStatusListener {
    private RondaService rondaService;
    private RondaTypeService rondaTypeService;
    private Ronda ronda;
    private Province selectedProvince;
    private SimpleValue mentorType;
    private District selectedDistrict;
    private HealthFacility selectedHealthFacility;
    private Province province;
    private List<Province> provinces;
    private List<District> districts;
    private List<HealthFacility> healthFacilities;
    private Tutored selectedMentee;
    private List<Tutored> menteeList;

    private List<Tutored> selectedMentees;

    private ObservableField<String> searchText = new ObservableField<>("");

    public RondaVM(@NonNull Application application) {
        super(application);
        this.ronda = new Ronda();
        rondaService = getApplication().getRondaService();
        rondaTypeService = getApplication().getRondaTypeService();
    }

    @Override
    public CreateRondaActivity getRelatedActivity() {
        return (CreateRondaActivity) super.getRelatedActivity();
    }

    @Override
    public void preInit() {
        try {
            this.provinces = getApplication().getProvinceService().getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Ronda getRonda() {
        return ronda;
    }

    @Bindable
    public Date getStartDate() {
        return this.ronda.getStartDate();
    }

    public void setStartDate(Date startDate) {
        this.ronda.setStartDate(startDate);
        notifyPropertyChanged(BR.startDate);
    }

    public void setRonda(Ronda ronda) {
        this.ronda = ronda;
    }

    @Bindable
    public Listble getHealthFacility() {
        return this.ronda.getHealthFacility();
    }

    public void setHealthFacility(Listble selectedHealthFacility) {
        if (selectedHealthFacility == null) return;

        if (StringUtils.isEmpty(((HealthFacility)selectedHealthFacility).getUuid())) return;
        this.selectedHealthFacility = (HealthFacility) selectedHealthFacility;
        try {
            if(this.menteeList == null) this.menteeList = new ArrayList<>();
            this.menteeList.clear();

            if(!StringUtils.isEmpty(this.selectedHealthFacility.getUuid())) {
                this.ronda.setHealthFacility(this.selectedHealthFacility);
                setMenteeList(getApplication().getTutoredService().getAllForMentoringRound(this.selectedHealthFacility, !this.ronda.isRondaZero()));
            }
            getRelatedActivity().reloadMenteesAdapter();
            notifyPropertyChanged(BR.healthFacility);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMenteeList(List<Tutored> menteeList) {
        this.menteeList = menteeList;
        for (Tutored tutored : this.menteeList) {
            tutored.setListType(Listble.ListTypes.SELECTION_LIST);
        }
    }

    public List<Tutored> getrondaMenteeList() {
        return menteeList;
    }

    public void addMentee(Listble mentee) {
        if (this.ronda.getRondaMentees() == null) this.ronda.setRondaMentees(new ArrayList<>());
        if (!this.ronda.isRondaZero() && this.ronda.getRondaMentees().size() < 8) {
            this.ronda.getRondaMentees().add(RondaMentee.fastCreate(this.ronda, (Tutored) mentee));
        } else if (this.ronda.isRondaZero()) {
            this.ronda.getRondaMentees().add(RondaMentee.fastCreate(this.ronda, (Tutored) mentee));
        } else {
            Utilities.displayAlertDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.max_mentees_reached)).show();
        }
    }


    @Bindable
    public Listble getSelectedProvince() {
        return selectedProvince;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setSelectedProvince(Listble selectedProvince) {
        this.selectedProvince = (Province) selectedProvince;
        if (selectedProvince.getId() == null) return;

        if (this.districts == null) this.districts = new ArrayList<>();
        if (this.healthFacilities == null) this.healthFacilities = new ArrayList<>();
        try {
            this.districts.clear();
            this.healthFacilities.clear();
            if (selectedProvince.getId() == null) return;
            this.districts.add(new District());
            if (selectedProvince.getId() == null) return;
            this.districts.addAll(getApplication().getDistrictService().getByProvinceAndMentor(this.selectedProvince, getApplication().getCurrMentor()));
            getRelatedActivity().reloadDistrictAdapter();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Bindable
    public Listble getMentorType() {
        return new SimpleValue(ronda.getMentorType());
    }

    public void setMentorType(Listble mentorType) {
        this.mentorType = (SimpleValue) mentorType;
        this.ronda.setMentorType(this.mentorType.getDescription());
        notifyPropertyChanged(BR.mentorType);
    }

    @Bindable
    public Listble getSelectedDistrict() {
        return selectedDistrict;
    }

    public void setSelectedDistrict(Listble selectedDistrict) {
        if (selectedDistrict == null || selectedDistrict.getId() == null) return;
        try {
            this.selectedDistrict = (District) selectedDistrict;
            this.healthFacilities.clear();
            if (selectedDistrict.getId() == null) return;
            this.healthFacilities.add(new HealthFacility());
            this.healthFacilities.addAll(getApplication().getHealthFacilityService().getHealthFacilityByDistrictAndMentor(this.selectedDistrict, getApplication().getCurrMentor()));
            getRelatedActivity().reloadHealthFacility();
            notifyPropertyChanged(BR.selectedDistrict);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Bindable
    public Tutored getSelectedMentee() {
        return selectedMentee;
    }

    public void setSelectedMentee(Tutored selectedMentee) {
        this.selectedMentee = selectedMentee;
        notifyPropertyChanged(BR.selectedMentee);
    }

    public void addSelectedMentee() {
        // validate mentee here...
        if (selectedMentees == null) selectedMentees = new ArrayList<>();
        if(selectedMentee != null){
            if (!selectedMentees.contains(selectedMentee)) {
                selectedMentee.setListPosition(selectedMentees.size()+1);
                selectedMentee.setListType(Listble.ListTypes.SELECTION_LIST);
                selectedMentees.add(selectedMentee);
                getRelatedActivity().displaySelectedMentees();
                setSelectedMentee(null);
                notifyPropertyChanged(BR.selectedMentee);
                notifyPropertyChanged(BR.selectedMentees);


            }else {
                Utilities.displayAlertDialog(getRelatedActivity(), "O Mentorando seleccionado já existe na lista!").show();
            }
        }else{
            Utilities.displayAlertDialog(getRelatedActivity(),"Campo Mentorando está vazio. Por favor, seleccione um Mentorando para adicionar à lista.").show();
        }
    }

    public void save() {
        this.doSave();
    }
    private void doSave(){
        try {
            if (!isValid()) return;


            ronda.setSyncStatus(SyncSatus.SENT);
            if (!getApplication().getApplicationStep().isApplicationStepEdit()) {
                ronda.setUuid(Utilities.getNewUUID().toString());
                this.ronda.setCreatedAt(DateUtilities.getCurrentDate());
                this.ronda.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
            }
            ronda.setStartDate(this.getStartDate());
            ronda.setHealthFacility(this.selectedHealthFacility);
            int count = getApplication().getRondaService().countRondas();
            count++;
            ronda.setDescription(ronda.getRondaType().getDescription()+" "+count);
            List<RondaMentee> rondaMentees = new ArrayList<>();
            for (Tutored tutored : this.getSelectedMentees()) {
                RondaMentee rondaMentee = new RondaMentee();
                rondaMentee.setUuid(Utilities.getNewUUID().toString());
                rondaMentee.setSyncStatus(SyncSatus.SENT);
                rondaMentee.setCreatedAt(DateUtilities.getCurrentDate());
                rondaMentee.setTutored(tutored);
                rondaMentee.setStartDate(this.getStartDate());
                rondaMentees.add(rondaMentee);
            }

            List<RondaMentor> rondaMentors = new ArrayList<>();
            Tutor tutor = this.getApplication().getCurrMentor();
            RondaMentor rondaMentor = new RondaMentor();
            if (!getApplication().getApplicationStep().isApplicationStepEdit()) {
                rondaMentor.setUuid(Utilities.getNewUUID().toString());
                rondaMentor.setCreatedAt(DateUtilities.getCurrentDate());
                rondaMentor.setStartDate(this.getStartDate());
            }
            rondaMentor.setSyncStatus(SyncSatus.SENT);
            rondaMentor.setTutor(tutor);
            rondaMentors.add(rondaMentor);
            this.ronda.setRondaMentees(rondaMentees);
            this.ronda.setRondaMentors(rondaMentors);
            String error = this.ronda.validade();
            if (Utilities.stringHasValue(error)) {
                Utilities.displayAlertDialog(getRelatedActivity(), error).show();
                return;
            }
            getApplication().isServerOnline(this);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValid() {
        if (this.ronda.getStartDate() == null) {
            Utilities.displayAlertDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.start_date_required)).show();
            return false;
        }
        if (this.ronda.getHealthFacility() == null) {
            Utilities.displayAlertDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.health_facility_required)).show();
            return false;
        }
        if (!Utilities.listHasElements(this.selectedMentees)) {
            Utilities.displayAlertDialog(getRelatedActivity(), getRelatedActivity().getString(R.string.mentees_required)).show();
            return false;
        }

        return true;
    }

    @Bindable
    public List<Tutored> getMenteeList() {
        if (menteeList == null) menteeList = new ArrayList<>();
        return menteeList;
    }



    public List<Tutored> getMentees() {
        try {
            return getApplication().getTutoredService().getAllOfRondaForNewRonda(selectedHealthFacility);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Province> getAllProvince() throws SQLException {
        List<Province> provinceList = new ArrayList<>();
        provinceList.add(new Province());
        provinceList.addAll(getApplication().getProvinceService().getAllOfTutor(getApplication().getCurrMentor()));
        return provinceList;
    }
    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }
    public List<HealthFacility> getHealthFacilities() {
        return healthFacilities;
    }
    public void setHealthFacilities(List<HealthFacility> healthFacilities) {
        this.healthFacilities = healthFacilities;
    }
    public void edit(Ronda ronda) {
        Map<String, Object> params = new HashMap<>();
        params.put("relatedRecord", ronda);
        getCurrentStep().changeToEdit();
        getRelatedActivity().nextActivity(CreateRondaActivity.class, params);
    }

    public void setSelectedMentees(List<Tutored> mentees) {
        this.selectedMentees = mentees;
    }

    @Bindable
    public List<Tutored> getSelectedMentees() {
        return this.selectedMentees;
    }
    private boolean validateRondaMentee(Tutored selectedRondaMentee) {
        // TODO
        return true;
    }

    public void removeFromSelected(Tutored tutored) {
        this.selectedMentees.remove(tutored);
        getRelatedActivity().displaySelectedMentees();

    }

    public void changeInitialDataViewStatus(View view){
        getRelatedActivity().changeFormSectionVisibility(view);
    }

    @Override
    public void onServerStatusChecked(boolean isOnline) {
        if (isOnline) {
            if (getApplication().getApplicationStep().isApplicationStepEdit()) {
                getApplication().getRondaRestService().restPatchRonda(this.ronda, this);
            } else {
                getApplication().getRondaRestService().restPostRonda(this.ronda, this);
            }
        } else {
            Utilities.displayAlertDialog(getRelatedActivity(), getRelatedActivity().getString(mz.org.csaude.mentoring.R.string.server_unavailable)).show();
        }
    }
    @Override
    public void doOnRestErrorResponse(String errorMsg) {
        Utilities.displayAlertDialog(getRelatedActivity(), errorMsg).show();
    }

    @Override
    public void doOnResponse(String flag, List<Ronda> objects) {
        Map<String, Object> params = new HashMap<>();
        params.put("rondaType", objects.get(0).getRondaType());
        params.put("title", objects.get(0).isRondaZero() ? "Ronda Zero" : "Ronda de Mentoria");
        getApplication().getApplicationStep().changeToList();
        getRelatedActivity().nextActivityFinishingCurrent(RondaActivity.class, params);
    }

    public void initRondaEdition() {
        try {
            ronda.setRondaMentees(getApplication().getRondaMenteeService().getAllOfRonda(ronda));
            if (this.selectedMentees == null) this.selectedMentees = new ArrayList<>();
            for (RondaMentee rondaMentee : ronda.getRondaMentees()) {
                rondaMentee.getTutored().setListType(Listble.ListTypes.SELECTION_LIST);
                this.selectedMentees.add(rondaMentee.getTutored());
            }

            getRelatedActivity().displaySelectedMentees();

            ronda.getHealthFacility().setDistrict(getApplication().getDistrictService().getById(ronda.getHealthFacility().getDistrict().getId()));
            this.setSelectedProvince(getRonda().getHealthFacility().getDistrict().getProvince());
            this.setSelectedDistrict(getRonda().getHealthFacility().getDistrict());
            this.setHealthFacility(getRonda().getHealthFacility());
        } catch (SQLException e) {
            Log.e("RondaVM", "initRondaEdition:" + e.getMessage());
        }

    }
}
