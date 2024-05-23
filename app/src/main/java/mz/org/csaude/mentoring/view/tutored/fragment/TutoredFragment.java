package mz.org.csaude.mentoring.view.tutored.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.adapter.tutored.TutoredAdapter;
import mz.org.csaude.mentoring.base.fragment.GenericFragment;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.databinding.FragmentTutoredsBinding;
import mz.org.csaude.mentoring.listner.dialog.IListbleDialogListener;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.viewmodel.tutored.TutoredVM;

/**
 * @author Jose Julai Ritsure
 */
public class TutoredFragment extends GenericFragment implements IListbleDialogListener {
    private FragmentTutoredsBinding fragmentTutoredBinding;

    private RecyclerView rcvTutoreds;

    private List<Tutored> tutoreds;

    private TutoredAdapter tutoredItemAdapter;

    public TutoredFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentTutoredBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tutoreds, container, false);
        return fragmentTutoredBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTutoredBinding.setViewModel(getRelatedViewModel());
        this.rcvTutoreds = fragmentTutoredBinding.rcvTutoreds;
        initAdapter();
    }

    public void initAdapter() {
        this.tutoreds = getRelatedViewModel().getAllTutoreds();
        if (Utilities.listHasElements(this.tutoreds)) {
            this.tutoredItemAdapter = new TutoredAdapter(rcvTutoreds, this.tutoreds, getMyActivity());
            displayDataOnRecyclerView(rcvTutoreds, tutoredItemAdapter, getContext());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        this.rcvTutoreds = fragmentTutoredBinding.rcvTutoreds;

        this.tutoreds = getRelatedViewModel().getAllTutoreds();

        if (Utilities.listHasElements(tutoreds)) {
            initAdapter();
        } else if (getMyActivity().getPositionRemoved() != null) {
            tutoredItemAdapter = new TutoredAdapter(rcvTutoreds, this.tutoreds, getMyActivity());
            tutoredItemAdapter.notifyItemRangeRemoved(0, tutoreds.size());

        }
    }

    @Override
    public TutoredVM getRelatedViewModel() {
        return (TutoredVM) getMyActivity().getRelatedViewModel();
    }


    @Override
    public void remove(int position) {

        String errorMsg = getRelatedViewModel().tutoredHasSessions();

        if (!Utilities.stringHasValue(errorMsg)) {
            try {
                tutoreds.remove(getRelatedViewModel().getTutored());
                rcvTutoreds.getAdapter().notifyItemRemoved(position);
                rcvTutoreds.removeViewAt(position);
                rcvTutoreds.getAdapter().notifyItemRangeChanged(position, rcvTutoreds.getAdapter().getItemCount());
                getRelatedViewModel().deleteTutored(getRelatedViewModel().getTutored());
                Utilities.displayAlertDialog(TutoredFragment.this.getContext(), getString(R.string.record_sucessfully_removed)).show();
            } catch (SQLException e) {
                Utilities.displayAlertDialog(TutoredFragment.this.getContext(), errorMsg).show();
            }

        } else {
            Utilities.displayAlertDialog(TutoredFragment.this.getContext(), errorMsg).show();
        }
    }


    @Override
    public BaseViewModel initViewModel() {
        return new ViewModelProvider(this).get(TutoredVM.class);
    }


}
