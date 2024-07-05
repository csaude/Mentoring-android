package mz.org.csaude.mentoring.adapter.recyclerview.session;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.adapter.recyclerview.generic.AbstractRecycleViewAdapter;
import mz.org.csaude.mentoring.base.activity.BaseActivity;
import mz.org.csaude.mentoring.databinding.SessionListItemBinding;
import mz.org.csaude.mentoring.databinding.SessionMentorListItemBinding;
import mz.org.csaude.mentoring.model.session.Session;
import mz.org.csaude.mentoring.view.home.ui.notifications.NotificationsVM;
import mz.org.csaude.mentoring.viewmodel.session.SessionListVM;

public class SessionMentorAdapter extends AbstractRecycleViewAdapter<Session> {
    public SessionMentorAdapter(RecyclerView recyclerView, List<Session> records, BaseActivity activity) {
        super(recyclerView, records, activity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SessionMentorListItemBinding sessionMentorListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.session_mentor_list_item, parent, false);
        return new SessionMentorViewHolder(sessionMentorListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Session session = records.get(position);
        SessionMentorViewHolder sessionMentorViewHolder = (SessionMentorViewHolder) holder;
        sessionMentorViewHolder.sessionMentorListItemBinding.setSession(session);
        sessionMentorViewHolder.sessionMentorListItemBinding.setViewModel((NotificationsVM) activity.getRelatedViewModel());
    }

     public class SessionMentorViewHolder extends RecyclerView.ViewHolder {

          private final SessionMentorListItemBinding sessionMentorListItemBinding;
        public SessionMentorViewHolder(@NonNull SessionMentorListItemBinding sessionMentorListItemBinding) {
            super(sessionMentorListItemBinding.getRoot());
            this.sessionMentorListItemBinding = sessionMentorListItemBinding;
        }
    }
}
