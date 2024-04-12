package mz.org.csaude.mentoring.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import mz.org.csaude.mentoring.base.activity.BaseActivity;
import mz.org.csaude.mentoring.base.activity.GenericActivity;
import mz.org.csaude.mentoring.base.application.MentoringApplication;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.base.viewModel.BaseViewModel;
import mz.org.csaude.mentoring.listner.dialog.IListbleDialogListener;
import mz.org.csaude.mentoring.model.user.User;

import java.sql.SQLException;

/**
 * @author Jose Julai Ritsure
 */
public abstract class GenericFragment extends Fragment implements GenericActivity, IListbleDialogListener {

    protected BaseViewModel relatedViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.relatedViewModel = initViewModel();
        if (this.relatedViewModel != null) {
            this.relatedViewModel.setRelatedActivity(getMyActivity());
            this.relatedViewModel.setGenericFragment(this);
         //   this.relatedViewModel.setRelatedFragment(this);

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.relatedViewModel != null) {
            this.relatedViewModel.preInit();
        }
    }

    protected BaseActivity getMyActivity(){
        return (BaseActivity) getActivity();
    }

    @Override
    public void remove(int position) throws SQLException {

    }

    @Override
    public void remove(BaseModel baseModel) {

    }

    protected void displayDataOnRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, 0));
        recyclerView.setAdapter(adapter);

    }

    public BaseViewModel getRelatedViewModel() {
        return relatedViewModel;
    }

}
