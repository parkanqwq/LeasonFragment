package com.auction.leasonfragment.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.auction.leasonfragment.NoteImageActivity;
import com.auction.leasonfragment.R;
import com.auction.leasonfragment.model.ModelNoteList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterNoteList extends RecyclerView.Adapter<AdapterNoteList.UserViewHolder>{

    private final List<ModelNoteList> modelNoteLists;
    private final DataSnapshot snapshot;
    private final Context context;

    public AdapterNoteList(Context context, List<ModelNoteList> modelNoteLists, DataSnapshot snapshot) {
        this.modelNoteLists = modelNoteLists;
        this.snapshot = snapshot;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterNoteList.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNoteList.UserViewHolder holder, int position) {
        ModelNoteList modelNote = modelNoteLists.get(position);
        holder.nameNote.setText(modelNote.getName());
        holder.dateNote.setText(modelNote.getDateNote());
        holder.minyNote.setText(modelNote.getText());
        holder.itemView.setOnClickListener(view -> {
            showImage(position, view, holder);
        });
        holder.imageChange.setOnClickListener(view -> {
            final AlertDialog.Builder adminDialog = new AlertDialog.Builder(view.getContext())
                    .setCancelable(false);
            LayoutInflater inflater = LayoutInflater.from(view.getContext());
            final View adminWindow = inflater.inflate(R.layout.card_delete, null);
            adminDialog.setView(adminWindow);

            adminDialog.setNegativeButton("нет", (dialogCansel, which) -> dialogCansel.dismiss());
            adminDialog.setPositiveButton("да", (dialogFinish, which) -> {
                for (DataSnapshot snapshot : snapshot.getChildren()){
                    ModelNoteList modelNote1 = snapshot.getValue(ModelNoteList.class);
                    if (modelNote1.getId().equals(modelNoteLists.get(position).getId())){
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Note")
                                .child(snapshot.getKey());
                        reference.removeValue();
                        Toast.makeText(view.getContext(), "Заметка удалена", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            adminDialog.show();
        });
    }

    private void showImage(int position, View view, AdapterNoteList.UserViewHolder holder){
        Intent intent = new Intent();
        intent.setClass(view.getContext(), NoteImageActivity.class);
        intent.putExtra("id", modelNoteLists.get(position).getId());
        view.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return modelNoteLists.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder{
        private final TextView nameNote;
        private final TextView dateNote;
        private final TextView minyNote;
        private final ImageView imageChange;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            minyNote = itemView.findViewById(R.id.minyNote);
            dateNote = itemView.findViewById(R.id.dateNote);
            nameNote = itemView.findViewById(R.id.nameNote);
            imageChange = itemView.findViewById(R.id.imageChange);
        }
    }
}

