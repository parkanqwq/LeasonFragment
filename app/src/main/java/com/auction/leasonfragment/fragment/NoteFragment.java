package com.auction.leasonfragment.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auction.leasonfragment.MainActivity;
import com.auction.leasonfragment.NoteImageActivity;
import com.auction.leasonfragment.R;
import com.auction.leasonfragment.adapter.AdapterNoteList;
import com.auction.leasonfragment.model.ModelNoteList;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {

    private boolean isLandscape;
    public static int position = 0;
    private List<ModelNoteList> modelNoteLists;
    private ModelNoteList modelNote;
    public static final String CURRENT_NOTE= "CURRENT_NOTE";

    public NoteFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelNoteLists = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            modelNote = new ModelNoteList();
            modelNote.setName(MainActivity.noteDB.noteName[i]);
            modelNote.setDateNote(MainActivity.noteDB.noteData[i]);
            modelNote.setText(MainActivity.noteDB.noteTextArr[i]);
            modelNoteLists.add(modelNote);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager lnManagerAll = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(lnManagerAll);

        recyclerView.setAdapter(new AdapterNoteList(modelNoteLists, getContext(), position));
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null){
            position = savedInstanceState.getInt(CURRENT_NOTE, NoteOpenFragment.DEFAULT_INDEX);
        }
        if (isLandscape){
            showImage(position);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_NOTE, position);
    }

    void showImage(int index){
        if (isLandscape){
            showLandImage(index);
        } else {
            showPortImage(index);
        }
    }

    private void showLandImage(int index) {
        NoteOpenFragment fragment = NoteOpenFragment.newInstance(index);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.imageFragmentConteiner, fragment)
                .commit();
    }

    void showPortImage(int index){
        Intent intent = new Intent();
        intent.setClass(getActivity(), NoteImageActivity.class);
        intent.putExtra(NoteOpenFragment.ARG_INDEX, index);
        startActivity(intent);
    }
}