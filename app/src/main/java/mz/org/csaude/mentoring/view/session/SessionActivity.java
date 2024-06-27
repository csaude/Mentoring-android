package mz.org.csaude.mentoring.view.session;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.adapter.recyclerview.form.FormAdapter;
import mz.org.csaude.mentoring.base.activity.BaseActivity;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.databinding.ActivitySessionBinding;
import mz.org.csaude.mentoring.listner.recyclerView.ClickListener;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.util.DateUtilities;
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
        getRelatedViewModel().setCurrRonda((Ronda) intent.getExtras().get("ronda"));
        getRelatedViewModel().setMentee((Tutored) intent.getExtras().get("mentee"));


        setSupportActionBar(sessionBinding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.sess_es_de_mentoria);

        sessionBinding.startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay;

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SessionActivity.this, (view1, year, monthOfYear, dayOfMonth) ->
                        getRelatedViewModel().setStartDate(DateUtilities.createDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, DateUtilities.DATE_FORMAT)), mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        populateFormList();

    }

    private void populateFormList() {
        this.formAdapter = new FormAdapter(sessionBinding.rcvForms, getRelatedViewModel().getTutorForms(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        sessionBinding.rcvForms.setLayoutManager(mLayoutManager);
        sessionBinding.rcvForms.setItemAnimator(new DefaultItemAnimator());
        sessionBinding.rcvForms.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));
        sessionBinding.rcvForms.setAdapter(formAdapter);
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
}
