package com.auction.leasonfragment.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.auction.leasonfragment.R;
import com.auction.leasonfragment.model.NoteModel;

public class NoteOpenFragment extends Fragment {

    public static final String ARG_INDEX = "index";
    public static final int DEFAULT_INDEX = 0;
    private int index = DEFAULT_INDEX;
    private boolean isLandscape;

    public NoteOpenFragment() {}

    public static NoteOpenFragment newInstance(int index) {
        NoteOpenFragment fragment = new NoteOpenFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX, DEFAULT_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_open, container, false);
        setToolBar(view);

        DatePicker datePicker = view.findViewById(R.id.datePicker);
        TextView textNote = view.findViewById(R.id.textNote);
        TextView noteData = view.findViewById(R.id.noteData);
        TextView noteName = view.findViewById(R.id.noteName);
        LinearLayout linerDate = view.findViewById(R.id.linerDate);
        Button buttonOK = view.findViewById(R.id.buttonOK);

        NoteModel noteModel = new NoteModel();
        noteName.setText(noteModel.noteName[index]);
        noteData.setText(noteModel.noteData[index]);
        textNote.setText(noteModel.noteTextArr[index]);

        noteData.setOnClickListener(view1 -> {
            linerDate.setVisibility(View.VISIBLE);
        });
        buttonOK.setOnClickListener(view1 -> {
            String forDateIndex = (datePicker.getDayOfMonth()
                    + "/" + (datePicker.getMonth()+1)
                    + "/" + datePicker.getYear());
            noteData.setText(forDateIndex);
            linerDate.setVisibility(View.GONE);
            noteModel.noteData[index] = forDateIndex;
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setToolBar(View view){
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        Toolbar toolbar = view.findViewById(R.id.toolbar_frag);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        if (isLandscape) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add){
            Toast.makeText(getContext(), "настройка текста", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}