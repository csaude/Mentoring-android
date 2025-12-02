package mz.org.csaude.mentoring.view.tutored.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.adapter.tutored.TutoredAdapter;
import mz.org.csaude.mentoring.base.fragment.GenericFragment;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.databinding.FragmentTutoredBinding;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.view.common.VerticalSpaceItemDecoration;
import mz.org.csaude.mentoring.viewmodel.tutored.StageFilter;
import mz.org.csaude.mentoring.viewmodel.tutored.TutoredVM;

/**
 * Lista de mentorandos. Reutilizada 4x no NavGraph com argumento "stage".
 * Implementa Searchable para receber o texto da SearchBar.
 */
public class TutoredFragment extends GenericFragment
        implements Searchable, TutoredRefreshable, TutoredAdapter.OnTutoredActionListener {

    private FragmentTutoredBinding binding;
    private TutoredAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tutored, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setViewModel(getRelatedViewModel());
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // Recycler + Adapter
        binding.rcvTutoreds.setLayoutManager(new LinearLayoutManager(requireContext()));
        int space = getResources().getDimensionPixelSize(R.dimen.dimen_8dp);
        binding.rcvTutoreds.addItemDecoration(
                new VerticalSpaceItemDecoration(space, true)); // true = também top/bottom

        // Lista inicial (caso o VM já tenha resultados de antes)
        List<Tutored> initial =
                getRelatedViewModel().getSearchResults() != null
                        ? new ArrayList<>(getRelatedViewModel().getSearchResults())
                        : new ArrayList<>();

        adapter = new TutoredAdapter(binding.rcvTutoreds, initial, getMyActivity(), this);
        binding.rcvTutoreds.setAdapter(adapter);
        binding.rcvTutoreds.scheduleLayoutAnimation();

        // Lê o argumento "stage" e aplica no VM antes do primeiro carregamento
        StageFilter filter = StageFilter.ALL;
        Bundle args = getArguments();
        if (args != null) {
            String fromNav = args.getString("stage");
            if (fromNav != null) {
                try {
                    filter = StageFilter.valueOf(fromNav);
                } catch (IllegalArgumentException ex) {
                    // valor inesperado, mantém ALL
                    filter = StageFilter.ALL;
                }
            }
        }
        getRelatedViewModel().setStageFilter(filter);

        // Dispara o primeiro carregamento (filtrando por estágio no BD)
        getRelatedViewModel().reloadWithFilter();
    }

    @Override
    public TutoredVM getRelatedViewModel() {
        return (TutoredVM) super.getRelatedViewModel();
    }

    // ===== Searchable =====
    @Override
    public void onSearchQueryChanged(@NonNull String query) {
        if (getRelatedViewModel() == null) return;
        getRelatedViewModel().setCurrentQuery(query);
        // dispara o recálculo no VM (BD + filtro texto)
        getRelatedViewModel().reloadWithFilter();
    }

    // Chamado via TutoredRefreshable pelo VM
    @Override
    public void refreshList() {
        updateList();
    }

    // Chamado explicitamente se quiseres do VM
    public void displaySearchResults() {
        updateList();
    }

    private void updateList() {
        if (adapter == null) return;
        List<Tutored> data = getRelatedViewModel().getSearchResults();
        if (data == null) data = new ArrayList<>();
        adapter.submitList(data);

        // Exibir "sem registos" vs lista
        boolean hasData = data != null && !data.isEmpty();
        binding.rcvTutoreds.setVisibility(hasData ? View.VISIBLE : View.GONE);
        binding.noRecordsText.setVisibility(hasData ? View.GONE : View.VISIBLE);

        if (hasData) {
            binding.rcvTutoreds.scheduleLayoutAnimation();
        }
    }

    @Override
    public void onEdit(Tutored tutored) {
        getRelatedViewModel().edit(tutored);
    }

    @Override
    public BaseViewModel initViewModel() {
        return new ViewModelProvider(requireActivity()).get(TutoredVM.class);
    }
}
