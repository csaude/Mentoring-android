package mz.org.csaude.mentoring.view.session;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.List;

import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.adapter.recyclerview.form.FormAdapter;
import mz.org.csaude.mentoring.base.activity.BaseActivity;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.databinding.ActivitySessionBinding;
import mz.org.csaude.mentoring.listner.recyclerView.ClickListener;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.util.DateUtilities;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.view.ronda.CreateRondaActivity;
import mz.org.csaude.mentoring.viewmodel.session.SessionVM;

public class SessionActivity extends BaseActivity implements ClickListener.OnItemClickListener {

    private ActivitySessionBinding sessionBinding;

    private FormAdapter formAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionBinding = DataBindingUtil.setContentView(this, R.layout.activity_session);
        sessionBinding.setViewModel(getRelatedViewModel());

        Intent intent = this.getIntent();

        getRelatedViewModel().getExecutorService().execute(()-> {
            populateFormList();

            if (getApplicationStep().isApplicationStepEdit()) {
                getRelatedViewModel().setSession((Session) intent.getExtras().get("session"));
                getRelatedViewModel().setCurrRonda(getRelatedViewModel().getSession().getRonda());
                getRelatedViewModel().setMentee(getRelatedViewModel().getSession().getTutored());
                getRelatedViewModel().setSelectedForm();
            } else {
                getRelatedViewModel().setCurrRonda((Ronda) intent.getExtras().get("ronda"));
                getRelatedViewModel().setMentee((Tutored) intent.getExtras().get("mentee"));
            }
        });


        setSupportActionBar(sessionBinding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.sess_es_de_mentoria);

        sessionBinding.startDate.setOnClickListener(v -> {

            // (Opcional) Restringir datas futuras (até hoje)
            CalendarConstraints constraints = new CalendarConstraints.Builder()
                    .setEnd(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            MaterialDatePicker<Long> picker = MaterialDatePicker.Builder
                    .datePicker()
                    .setTitleText(R.string.select_date) // ex: "Selecionar data"
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(constraints)
                    .setTheme(R.style.ThemeOverlay_App_DatePicker) // opcional
                    .build();

            picker.addOnPositiveButtonClickListener(utcMillis -> {
                // MaterialDatePicker devolve a seleção em UTC millis (meia-noite UTC).
                // Convertemos e "normalizamos" para meia-noite no timezone do device.
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(utcMillis);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                getRelatedViewModel().setStartDate(cal.getTime());
            });

            picker.show(getSupportFragmentManager(), "session_start_date");
        });

    }

    private void populateFormList() {
        List<Form> tutorForms = getRelatedViewModel().getTutorForms();

        // Update the UI on the main thread
        runOnUiThread(() -> {
            if (tutorForms != null && !tutorForms.isEmpty()) {
                // Initialize the adapter with the fetched forms
                formAdapter = new FormAdapter(sessionBinding.rcvForms, tutorForms, this);

                // Set up the RecyclerView layout manager and other properties
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                sessionBinding.rcvForms.setLayoutManager(mLayoutManager);
                sessionBinding.rcvForms.setItemAnimator(new DefaultItemAnimator());
                sessionBinding.rcvForms.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));
                sessionBinding.rcvForms.setAdapter(formAdapter);
            } else {
                // Handle the case where there are no forms to display (optional)
                Utilities.displayAlertDialog(this, getString(R.string.no_forms_available)).show();
            }
        });
    }


    @Override
    public BaseViewModel initViewModel() {
        return new ViewModelProvider(this).get(SessionVM.class);
    }

    @Override
    public SessionVM getRelatedViewModel() {
        return (SessionVM) super.getRelatedViewModel();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
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
    public void onItemClick(View view, int position) {

    }

    public void onLongItemClick(View v, int position) {
        getRelatedViewModel().selectForm(position);
        formAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onBackPressed() {
        getApplicationStep().changeToList();
        super.onBackPressed();
    }
}
