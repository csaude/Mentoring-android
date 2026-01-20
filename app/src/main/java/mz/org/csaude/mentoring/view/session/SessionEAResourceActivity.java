package mz.org.csaude.mentoring.view.session;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.adapter.resource.ResourceAdapter;
import mz.org.csaude.mentoring.base.activity.BaseActivity;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.databinding.ActivitySessionEaresourceBinding;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.viewmodel.session.SessionResourcesVM;

public class SessionEAResourceActivity extends BaseActivity {

    private ResourceAdapter resourceAdapter;
    private ActivitySessionEaresourceBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_earesource);
        binding.setViewModel(getRelatedViewModel());
        binding.setLifecycleOwner(this);

        // Safe session extraction
        Intent intent = getIntent();
        Session session = null;

        // Prefer getSerializableExtra (adapt if you use Parcelable)
        try {
            session = (Session) intent.getSerializableExtra("session");
        } catch (Exception ignored) {}

        if (session == null) {
            finish();
            return;
        }

        getRelatedViewModel().setSession(session);

        setSupportActionBar(binding.toolbar.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.session_closure_title)); // crie essa string se quiser
        }

        setupRecyclerView();

        // opcional: carregar tudo ao abrir (se preferir)
        // getRelatedViewModel().initSearch();
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvResources.setLayoutManager(layoutManager);
        binding.rcvResources.setItemAnimator(new DefaultItemAnimator());

        // Add divider only once
        if (binding.rcvResources.getItemDecorationCount() == 0) {
            binding.rcvResources.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            );
        }

        resourceAdapter = new ResourceAdapter(binding.rcvResources, getRelatedViewModel().getNodeList(), this);
        binding.rcvResources.setAdapter(resourceAdapter);
    }

    @Override
    public void displaySearchResults() {
        super.displaySearchResults();
        // Adapter already set. Just refresh list.
        if (resourceAdapter != null) {
            resourceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public BaseViewModel initViewModel() {
        return new ViewModelProvider(this).get(SessionResourcesVM.class);
    }

    @Override
    public SessionResourcesVM getRelatedViewModel() {
        return (SessionResourcesVM) super.getRelatedViewModel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // called by adapter
    public void onLongItemClick(View v, int position) {
        getRelatedViewModel().selectResource(position);
        if (resourceAdapter != null) {
            resourceAdapter.notifyItemChanged(position);
        }
    }
}
