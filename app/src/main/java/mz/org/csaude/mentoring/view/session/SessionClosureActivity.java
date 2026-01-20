package mz.org.csaude.mentoring.view.session;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;
import com.google.android.material.datepicker.MaterialDatePicker;

import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.base.activity.BaseActivity;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.databinding.ActivitySessionClosureBinding;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.util.DateUtilities;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.viewmodel.session.SessionClosureVM;

public class SessionClosureActivity extends BaseActivity {


    private ActivitySessionClosureBinding binding;
    private BroadcastReceiver finishReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_closure);
        binding.setViewModel(getRelatedViewModel());

        Intent intent = this.getIntent();
        getRelatedViewModel().setSession((Session) intent.getExtras().get("session"));

        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.fecho_da_sess_o);

        binding.sessionEndDate.setOnClickListener(v -> showEndDatePicker());

        binding.nextSessionDate.setOnClickListener(v -> showNextSessionDatePicker());


        finishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("FINISH_ACTIVITY".equals(intent.getAction())) {
                    finish();
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(finishReceiver, new IntentFilter("FINISH_ACTIVITY"));

    }

    private void showEndDatePicker() {
        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.select_session_end_date) // cria esse string se ainda não existir
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            if (selection == null) return;

            // Converter millis para dd-MM-yyyy
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // 0-based
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            String dateStr = day + "-" + month + "-" + year;

            getRelatedViewModel().setEndtDate(
                    DateUtilities.createDate(dateStr, DateUtilities.DATE_FORMAT)
            );
        });

        datePicker.show(getSupportFragmentManager(), "END_DATE_PICKER");
    }

    private void showNextSessionDatePicker() {
        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.select_next_session_date) // cria esse string se ainda não existir
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            if (selection == null) return;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // 0-based
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            String dateStr = day + "-" + month + "-" + year;

            getRelatedViewModel().setNextSessionDate(
                    DateUtilities.createDate(dateStr, DateUtilities.DATE_FORMAT)
            );
        });

        datePicker.show(getSupportFragmentManager(), "NEXT_SESSION_DATE_PICKER");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button click
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public BaseViewModel initViewModel() {
        return new ViewModelProvider(this).get(SessionClosureVM.class);
    }

    @Override
    public SessionClosureVM getRelatedViewModel() {
        return (SessionClosureVM) super.getRelatedViewModel();
    }

    private void switchLayout(){
        getRelatedViewModel().setInitialDataVisible(!getRelatedViewModel().isInitialDataVisible());
    }
    public void changeFormSectionVisibility(View view){
        if(view.equals(binding.pontosFortes)){
            if(binding.pontosFortesLyt.getVisibility() == View.VISIBLE){
                binding.btnCollapsePontosFortes.setImageResource(R.drawable.sharp_arrow_drop_up_24);
                switchLayout();
                Utilities.collapse(binding.pontosFortesLyt);
            } else {
                switchLayout();
                Utilities.expand(binding.pontosFortesLyt);
                binding.btnCollapsePontosFortes.setImageResource(R.drawable.baseline_arrow_drop_down_24);
            }

        } else if(view.equals(binding.aspectosMelhorar)){
            if(binding.aspectosPorMelhorarLyt.getVisibility() == View.VISIBLE){
                binding.btnCollapseAspectosMelhorar.setImageResource(R.drawable.sharp_arrow_drop_up_24);
                switchLayout();
                Utilities.collapse(binding.aspectosPorMelhorarLyt);
            } else {
                switchLayout();
                Utilities.expand(binding.aspectosPorMelhorarLyt);
                binding.btnCollapseAspectosMelhorar.setImageResource(R.drawable.baseline_arrow_drop_down_24);
            }
        }  else if(view.equals(binding.planoMelhoria)){
            if(binding.planoMelhorariaLyt.getVisibility() == View.VISIBLE){
                switchLayout();
                Utilities.collapse(binding.planoMelhorariaLyt);
                binding.btnCollapsePlanoTrabalho.setImageResource(R.drawable.sharp_arrow_drop_up_24);
            } else {
                switchLayout();
                Utilities.expand(binding.planoMelhorariaLyt);
                binding.btnCollapsePlanoTrabalho.setImageResource(R.drawable.baseline_arrow_drop_down_24);
            }
        } else if(view.equals(binding.observacoes)){
            if(binding.observacoesLyt.getVisibility() == View.VISIBLE){
                switchLayout();
                Utilities.collapse(binding.observacoesLyt);
                binding.btnObservacoes.setImageResource(R.drawable.sharp_arrow_drop_up_24);
            } else {
                switchLayout();
                Utilities.expand(binding.observacoesLyt);
                binding.btnObservacoes.setImageResource(R.drawable.baseline_arrow_drop_down_24);
            }
        }

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(finishReceiver);
        super.onDestroy();
    }
}
