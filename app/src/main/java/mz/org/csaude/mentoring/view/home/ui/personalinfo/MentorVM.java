package mz.org.csaude.mentoring.view.home.ui.personalinfo;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.BR;
import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.model.employee.Employee;
import mz.org.csaude.mentoring.model.location.District;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.location.Location;
import mz.org.csaude.mentoring.model.location.Province;
import mz.org.csaude.mentoring.model.partner.Partner;
import mz.org.csaude.mentoring.model.professionalCategory.ProfessionalCategory;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.service.professionalCategory.ProfessionalCategoryService;
import mz.org.csaude.mentoring.service.session.SessionService;
import mz.org.csaude.mentoring.service.tutor.TutorService;
import mz.org.csaude.mentoring.util.SimpleValue;
import mz.org.csaude.mentoring.util.Utilities;

public class MentorVM extends BaseViewModel {

    private Tutor tutor;
    private TutorService tutorService;
    private ProfessionalCategoryService professionalCategoryService;
    private SessionService sessionService;
    private Location location;
    private Province province;
    private ProfessionalCategory professionalCategory;
    private District district;
    private HealthFacility healthFacility;
    private boolean initialDataVisible;
    private List<District> districts;
    private List<HealthFacility> healthFacilities;
    private List<SimpleValue> menteeLabors;
    private boolean ONGEmployee;
    private Partner selectedNgo;
    public MentorVM(@NonNull Application application) {

        super(application);

        tutorService = getApplication().getTutorService();
        districts = new ArrayList<>();
        healthFacilities = new ArrayList<>();
        menteeLabors = new ArrayList<>();
        location = new Location();

        loadMeteeLabors();
        preInit();
    }

    public void changeInitialDataViewStatus(View view){
        getPersonalInfoFragment().changeFormSectionVisibility(view);
    }

    @Override
    public void preInit() {
        this.tutor = getApplication().getCurrMentor();
        List<Location> locations = new ArrayList<>();
        try {
            locations = getApplication().getLocationService().getAllOfEmploee(this.tutor.getEmployee());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.location = locations.get(0);
    }

    @Bindable
    public String getName() {
        return this.tutor.getEmployee().getName();
    }

    public void setName(String name) {
        this.tutor.getEmployee().setName(name);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getSurname(){
        return this.tutor.getEmployee().getSurname();
    }

    public void setSurname(String surname){
        this.tutor.getEmployee().setSurname(surname);
    }
    @Bindable
    public int getNuit() {
        return this.tutor.getEmployee().getNuit();
    }
    public void setNuit(int nuit) {
        this.tutor.getEmployee().setNuit(nuit);
    }

    public List<ProfessionalCategory> getAllProfessionalCategys() throws SQLException {
        return getApplication().getProfessionalCategoryService().getAll();
    }
    @Bindable
    public Listble getProfessionalCategory() {
        return this.tutor.getEmployee().getProfessionalCategory();
    }
    public void setProfessionalCategory(Listble professionalCategory) {
        this.tutor.getEmployee().setProfessionalCategory((ProfessionalCategory) professionalCategory);
        notifyPropertyChanged(BR.professionalCategory);
    }
    @Bindable
    public int getTrainingYear() {
        return this.tutor.getEmployee().getTrainingYear();
    }
    public void setTrainingYear(int trainingYear) {
        this.tutor.getEmployee().setTrainingYear(trainingYear);
    }
    @Bindable
    public String getPhoneNumber() {
        return this.tutor.getEmployee().getPhoneNumber();
    }
    public void setPhoneNumber(String phoneNumber) {
        this.tutor.getEmployee().setPhoneNumber(phoneNumber);
    }
    @Bindable
    public String getEmail() {
        return this.tutor.getEmployee().getEmail();
    }
    public void setEmail(String email) {
        this.tutor.getEmployee().setEmail(email);
    }
    @Bindable
    public Listble getPartner() {
        return this.tutor.getEmployee().getPartner();
    }
    public void setPartner(Partner partner) {
        this.tutor.getEmployee().setPartner(partner);
    }

    public List<Tutor> getAllTutors() throws SQLException {
        return tutorService.getAll();
    }
    public List<Province> getAllProvince() throws SQLException {
        List<Province> provinceList = new ArrayList<>();
        //provinceList.add(new Province());
        provinceList.addAll(getApplication().getProvinceService().getAllOfTutor(getApplication().getCurrMentor()));
        return provinceList;
    }
    @Bindable
    public Listble getSelectedNgo() {
        return  this.tutor.getEmployee().getPartner();
    }
    public void setSelectedNgo(Listble selectedNgo) {
        this.tutor.getEmployee().setPartner((Partner) selectedNgo);
        notifyPropertyChanged(BR.selectedNgo);
    }

    private void doUpdate(){

        this.tutor.getEmployee().setProfessionalCategory((ProfessionalCategory) getProfessionalCategory());
        this.tutor.setEmployee(getEmployee());

        this.location.setEmployee(tutor.getEmployee());
        this.location.setProvince((Province) getProvince());
        this.location.setDistrict((District) getDistrict());
        this.location.setHealthFacility((HealthFacility) getHealthFacility());

        String error = this.tutor.validade();
        if (Utilities.stringHasValue(error)) {
            Utilities.displayAlertDialog(getRelatedActivity(), error).show();
            return;
        }

        try {
            getApplication().getEmployeeService().saveOrUpdateEmployee(tutor.getEmployee());
            this.tutorService.saveOrUpdate(tutor);
            this.getApplication().getLocationService().saveOrUpdate(location);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(){
        this.doUpdate();
    }

    @Bindable
    public Tutor getTutor() {
        return this.tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Employee getEmployee(){
        return this.tutor.getEmployee();
    }
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Bindable
    public Listble getProvince() {
        return this.location.getProvince();
    }
    public void setProvince(Listble province) {
        this.location.setProvince((Province) province);
        try {
            this.districts.clear();
            this.healthFacilities.clear();
            if (province.getId() <= 0) return;
            //this.districts.add(new District());
            if (province.getId() <= 0) return;
            this.districts.addAll(getApplication().getDistrictService().getByProvinceAndMentor(this.location.getProvince(), getApplication().getCurrMentor()));
            getPersonalInfoFragment().reloadDistrcitAdapter();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Bindable
    public Listble getDistrict(){
        return this.location.getDistrict();
    }
    public void setDistrict(Listble district){
        try {
            this.location.setDistrict((District) district);
            this.healthFacilities.clear();
            if (district.getId() <= 0) return;
            //this.healthFacilities.add(new HealthFacility());
            this.healthFacilities.addAll(getApplication().getHealthFacilityService().getHealthFacilityByDistrictAndMentor((District) district, getApplication().getCurrMentor()));
            getPersonalInfoFragment().reloadHealthFacility();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<District> getDistricts() {
        return districts;
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
    @Bindable
    public Listble getHealthFacility(){
        return this.location.getHealthFacility();
    }

    public void setHealthFacility(Listble healthFacility){
        this.location.setHealthFacility((HealthFacility) healthFacility);
    }
    private void loadMeteeLabors(){
        this.menteeLabors.add(SimpleValue.fastCreate("SNS"));
        this.menteeLabors.add(SimpleValue.fastCreate("ONG"));
    }
    @Bindable
    public boolean isInitialDataVisible() {
        return initialDataVisible;
    }
    public void setInitialDataVisible(boolean initialDataVisible) {
        this.initialDataVisible = initialDataVisible;
        this.notifyPropertyChanged(BR.initialDataVisible);
    }
    public List<SimpleValue> getMenteeLabors() {
        return menteeLabors;
    }

    @Bindable
    public Listble getMenteeLabor(){
        return Utilities.findOnArray(this.menteeLabors, SimpleValue.fastCreate("SNS"));
    }

    public void setMenteeLabor(Listble menteeLabor){
        if (this.tutor.getEmployee() == null) return;
        SimpleValue selectSimpleValue = (SimpleValue) menteeLabor;
        if (selectSimpleValue.getDescription().equals("ONG")){
            setONGEmployee(true);
        } else {
            setONGEmployee(false);
            try {
                this.tutor.getEmployee().setPartner(getApplication().getPartnerService().getMISAU());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Bindable
    public boolean isONGEmployee() {
        return ONGEmployee;
    }

    public void setONGEmployee(boolean ONGEmployee) {
        this.ONGEmployee = ONGEmployee;
        notifyPropertyChanged(BR.oNGEmployee);
    }

    public PersonalInfoFragment getPersonalInfoFragment(){
        return (PersonalInfoFragment) super.getGenericFragment();
    }

    public List getAllPartners() {
        try {
            return getApplication().getPartnerService().getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}