package mz.org.csaude.mentoring.view.login;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.base.activity.BaseActivity;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.databinding.ActivityLoginBinding;
import mz.org.csaude.mentoring.listner.dialog.IDialogListener;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.view.splash.SplashActivity;
import mz.org.csaude.mentoring.viewmodel.login.LoginVM;

public class LoginActivity extends BaseActivity implements IDialogListener {

    private ActivityLoginBinding loginBinding;

    @Override
    protected boolean isAutoLogoutEnabled() {
        return false; // Disable auto logout in LoginActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginBinding.setViewModel(getRelatedViewModel());
       hasDataNotSyncro();

    }

    @Override
    public BaseViewModel initViewModel() {
        return new ViewModelProvider(this).get(LoginVM.class);
    }

    @Override
    public LoginVM getRelatedViewModel() {
        return (LoginVM) super.getRelatedViewModel();
    }

    public void hasDataNotSyncro() {
        boolean isDataNotSyncro = getRelatedViewModel().isDataNotSyncro();
        if (isDataNotSyncro) {
            Utilities.displayCustomConfirmationDialog(this , this.getString(R.string.data_not_synchronized), "SIM",  this.getString(R.string.no), this).show();
        } else {
            return;
        }
    }

    @Override
    public void doOnConfirmed() {
        getRelatedViewModel().ConfirmedSyncData();
    }

    @Override
    public void doOnDeny() {
        this.nextActivity(SplashActivity.class);
    }
}
