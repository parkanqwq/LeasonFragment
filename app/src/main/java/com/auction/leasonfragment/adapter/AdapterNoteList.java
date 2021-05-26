package com.auction.leasonfragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.auction.leasonfragment.NoteImageActivity;
import com.auction.leasonfragment.R;
import com.auction.leasonfragment.fragment.NoteFragment;
import com.auction.leasonfragment.fragment.NoteOpenFragment;
import com.auction.leasonfragment.model.ModelNoteList;

import java.util.List;

public class AdapterNoteList extends RecyclerView.Adapter<AdapterNoteList.UserViewHolder>{

    private final List<ModelNoteList> modelNoteLists;
    private final Context context;
    private final int position;


    public AdapterNoteList(List<ModelNoteList> modelNoteLists, Context context, int position) {
        this.modelNoteLists = modelNoteLists;
        this.context = context;
        this.position = position;
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
            showImage(position, view);
            NoteFragment.position = position;
        });
    }

    private void showImage(int position, View view){
        boolean isLandscape = view.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape){
            showLandImage(position);
        } else {
            showPortImage(position, view);
        }
    }

    private void showLandImage(int position) {
        NoteOpenFragment fragment = NoteOpenFragment.newInstance(position);
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.imageFragmentConteiner, fragment)
                .commit();
    }

    void showPortImage(int position, View view){
        Intent intent = new Intent();
        intent.setClass(view.getContext(), NoteImageActivity.class);
        intent.putExtra(NoteOpenFragment.ARG_INDEX, position);
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

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            minyNote = itemView.findViewById(R.id.minyNote);
            dateNote = itemView.findViewById(R.id.dateNote);
            nameNote = itemView.findViewById(R.id.nameNote);
        }
    }
}

