package mz.org.csaude.mentoring.adapter.recyclerview.tutored;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mz.org.csaude.mentoring.R;
import mz.org.csaude.mentoring.adapter.recyclerview.generic.AbstractRecycleViewAdapter;
import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.activity.BaseActivity;
import mz.org.csaude.mentoring.databinding.TutoredListItemBinding;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.view.mentorship.CreateMentorshipActivity;
import mz.org.csaude.mentoring.viewmodel.mentorship.MentorshipVM;
import mz.org.csaude.mentoring.viewmodel.ronda.RondaVM;

public class TutoredAdapter extends AbstractRecycleViewAdapter<Tutored> {

    // Reuse existing listener interface from the other TutoredAdapter package
    private final mz.org.csaude.mentoring.adapter.tutored.TutoredAdapter.OnTutoredActionListener actionListener;

    public TutoredAdapter(RecyclerView recyclerView,
                          List<Tutored> records,
                          BaseActivity activity,
                          mz.org.csaude.mentoring.adapter.tutored.TutoredAdapter.OnTutoredActionListener actionListener) {
        super(recyclerView, records, activity);
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TutoredListItemBinding b = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.tutored_list_item,
                parent,
                false
        );
        return new TutoredViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Tutored t = super.records.get(position);
        TutoredViewHolder vh = (TutoredViewHolder) holder;

        vh.b.setTutored(t);

        // Verifica tipos de lista
        boolean isMentorshipSelection =
                t.getListType() != null &&
                        t.getListType().equals(Listble.ListTypes.MENTORSHIP_MENTEE_SELECTION.name());

        boolean isSelectionListClassic =
                t.getListType() != null &&
                        t.getListType().equals(Listble.ListTypes.SELECTION_LIST.name());

        // 1) Checkbox só para MENTORSHIP_MENTEE_SELECTION
        vh.b.chkSelect.setVisibility(isMentorshipSelection ? View.VISIBLE : View.GONE);

        vh.b.chkSelect.setOnCheckedChangeListener(null);
        vh.b.chkSelect.setChecked(t.isSelected());

        vh.b.chkSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isMentorshipSelection) return;

            if (activity instanceof CreateMentorshipActivity) {
                MentorshipVM vm = (MentorshipVM) activity.getRelatedViewModel();

                if (vm.isMenteeSelectionStep()) {
                    // Usa a lógica existente do VM (desseleciona outros, define mentorship.tutored, etc.)
                    vm.selectMentee(vh.getBindingAdapterPosition());
                    notifyDataSetChanged();
                } else {
                    t.setItemSelected(isChecked);
                }
            } else {
                t.setItemSelected(isChecked);
            }
        });

        // 2) 3 dots:
        //    - Esconder totalmente em MENTORSHIP_MENTEE_SELECTION
        //    - Mostrar normalmente nos restantes tipos
        if (isMentorshipSelection) {
            vh.b.btnMore.setVisibility(View.GONE);
            vh.b.btnMore.setOnClickListener(null);
        } else {
            vh.b.btnMore.setVisibility(View.VISIBLE);

            vh.b.btnMore.setOnClickListener(v -> {
                PopupMenu menu = new PopupMenu(v.getContext(), v);
                menu.getMenuInflater().inflate(R.menu.menu_tutored_item, menu.getMenu());

                // "Remove" só faz sentido nos contextos clássicos de SELECTION_LIST (ex.: Ronda)
                menu.getMenu().findItem(R.id.action_remove).setVisible(isSelectionListClassic);

                menu.setOnMenuItemClickListener(item ->
                        handleMenuClick(item, v, t, vh.getBindingAdapterPosition())
                );
                menu.show();
            });
        }

        // Nada de auto-toggle aqui – apenas reflectimos o estado do model.
    }

    private boolean handleMenuClick(MenuItem item, View anchor, Tutored t, int pos) {
        int id = item.getItemId();

        if (id == R.id.action_call) {
            String phone = (t.getEmployee() != null) ? t.getEmployee().getPhoneNumber() : null;
            if (phone == null || phone.isEmpty()) {
                Toast.makeText(anchor.getContext(), R.string.no_phone_available, Toast.LENGTH_SHORT).show();
                return true;
            }
            Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            anchor.getContext().startActivity(dial);
            return true;

        } else if (id == R.id.action_edit) {
            if (activity instanceof CreateMentorshipActivity && actionListener != null) {
                actionListener.onEdit(t);
                /*((CreateMentorshipActivity) activity).onLongItemClick(anchor, pos);*/
            }
            return true;

        } else if (id == R.id.action_remove) {
            if (activity.getRelatedViewModel() instanceof RondaVM) {
                ((RondaVM) activity.getRelatedViewModel()).removeFromSelected(t);
            }
            return true;
        }
        return false;
    }

    public class TutoredViewHolder extends RecyclerView.ViewHolder {
        private final TutoredListItemBinding b;

        public TutoredViewHolder(@NonNull TutoredListItemBinding binding) {
            super(binding.getRoot());
            this.b = binding;

            b.getRoot().setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                Tutored t = records.get(pos);

                boolean isMentorshipSelection =
                        t.getListType() != null &&
                                t.getListType().equals(Listble.ListTypes.MENTORSHIP_MENTEE_SELECTION.name());

                if (activity instanceof CreateMentorshipActivity) {
                    CreateMentorshipActivity act = (CreateMentorshipActivity) activity;
                    MentorshipVM vm = (MentorshipVM) act.getRelatedViewModel();

                    if (isMentorshipSelection && vm.isMenteeSelectionStep()) {
                        // Toque na linha também selecciona o mentorando
                        vm.selectMentee(pos);
                        notifyDataSetChanged();
                    } else {
                        // Comportamento antigo (table selection, outros fluxos, etc.)
                        act.onLongItemClick(v, pos);
                    }
                }

                selectedPosition = pos;
                // Em selecção de mentee, já fizemos notifyDataSetChanged() acima,
                // por isso este notifyItemChanged é mais relevante para outros contextos.
                notifyItemChanged(pos);
            });
        }
    }
}
