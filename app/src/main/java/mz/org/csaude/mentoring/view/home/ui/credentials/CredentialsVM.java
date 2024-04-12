package mz.org.csaude.mentoring.view.home.ui.credentials;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.ViewModel;

import mz.org.csaude.mentoring.BR;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.user.UserService;

public class CredentialsVM extends BaseViewModel {

    private UserService userService;
    private User user;

    private boolean initialDataVisible;

    private Tutor tutor;

    public CredentialsVM(@NonNull Application application) {
        super(application);
        this.userService = getApplication().getUserService();
        this.user = getApplication().getAuthenticatedUser();
    }

    @Override
    public void preInit() {

    }
    @Bindable
    public String getPassword(){
        return this.user.getPassword();
    }

    public void setPassword(String userPassWord){

        this.user.setPassword(userPassWord);
        notifyPropertyChanged(BR.userPassword);
    }
/*
    public CredentialsFragment getCredentialsFragment(){
        return (CredentialsFragment) super.getGenericFragment();
    }
*/
    private void doUpdate(){
        this.user.getUserName();
    }

    public void update(){
       doUpdate();
    }

    public boolean isInitialDataVisible() {
        return initialDataVisible;
    }

    public void setInitialDataVisible(boolean initialDataVisible) {
        this.initialDataVisible = initialDataVisible;
    }
}