package com.auction.leasonfragment.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.auction.leasonfragment.NoteImageActivity;
import com.auction.leasonfragment.R;
import com.auction.leasonfragment.model.NoteModel;

public class NoteFragment extends Fragment {

    private boolean isLandscape;
    private int position = 0;

    public static final String CURRENT_NOTE= "CURRENT_NOTE";

    public NoteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
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



    private void initList(View view) {
        LinearLayout linearLayout = (LinearLayout) view;
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        NoteModel noteModel = new NoteModel();
        for (int i = 0; i <  noteModel.noteName.length; i++) {
            String note =  noteModel.noteDescription[i];
            String noteDescriptions = noteModel.noteData[i];
            TextView noteName = new TextView(getContext());
            TextView noteDescription = new TextView(getContext());
            noteName.setText(note);
            noteName.setTextSize(30);
            noteName.setPadding(8, 8, 8 ,0);
            noteName.setTextColor(Color.parseColor("#7CB342"));
            noteDescription.setText(noteDescriptions);
            noteDescription.setTextSize(14);
            noteDescription.setPadding(16, 0, 8, 8);
            linearLayout.addView(noteName);
            linearLayout.addView(noteDescription);

            final int cerrentIndex = i;
            noteName.setOnClickListener(view1 -> {
                showImage(cerrentIndex);
                position = cerrentIndex;
            });
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