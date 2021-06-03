package com.auction.leasonfragment.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.auction.leasonfragment.R;
import com.auction.leasonfragment.model.ModelNoteList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class NoteOpenFragment extends Fragment {

    private String id;
    private TextView noteName;
    private TextView noteData;
    private EditText textNote;
    private DatabaseReference reference;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    public NoteOpenFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_open, container, false);
        setToolBar(view);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        textNote = view.findViewById(R.id.textNote);
        noteData = view.findViewById(R.id.noteData);
        noteName = view.findViewById(R.id.noteName);

        textNote.setFocusable(false);
        textNote.setFocusableInTouchMode(false);
        textNote.setOnClickListener(view1 -> {
            textNote.setFocusable(true);
            textNote.setFocusableInTouchMode(true);
        });

        reference = FirebaseDatabase.getInstance().getReference("Note")
                .child(currentUser.getUid())
                .child("Note");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ModelNoteList modelNote = snapshot.getValue(ModelNoteList.class);
                    assert modelNote != null;
                    if (modelNote.getId().equals(id)){
                        noteName.setText(modelNote.getName());
                        noteData.setText(modelNote.getDateNote());
                        textNote.setText(modelNote.getText());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setToolBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_frag);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    private void saveNoteFareBase() {
        reference = FirebaseDatabase.getInstance().getReference("Note")
                .child(currentUser.getUid())
                .child("Note");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ModelNoteList modelNote = snapshot.getValue(ModelNoteList.class);
                    if (modelNote.getId().equals(id)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("text", textNote.getText().toString());
                        hashMap.put("dateNote", noteData.getText().toString());
                        hashMap.put("name", noteName.getText().toString());
                        hashMap.put("nameToLowerCase", noteName.getText().toString().toLowerCase());
                        reference.child(snapshot.getKey()).updateChildren(hashMap);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        saveNoteFareBase();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.chaneName) {
            AlertDialog.Builder adminDialog = new AlertDialog.Builder(getContext())
                    .setCancelable(false);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View adminWindow = inflater.inflate(R.layout.item_create_note_name, null);
            adminDialog.setView(adminWindow);

            adminDialog.setNegativeButton("Отмена", (dialogCansel, which) -> dialogCansel.dismiss());
            adminDialog.setPositiveButton("Изменить", (dialogFinish, which) -> {
                TextView nameNote = adminWindow.findViewById(R.id.nameNote);
                String name = nameNote.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(getContext(), "Заполните поле", Toast.LENGTH_SHORT).show();
                } else {
                    noteName.setText(name);
                    Toast.makeText(getContext(), "Имя изменено", Toast.LENGTH_SHORT).show();
                }
            });
            adminDialog.show();
            return true;
        }
        if (item.getItemId() == R.id.changeDate) {
            AlertDialog.Builder adminDialog = new AlertDialog.Builder(getContext())
                    .setCancelable(false);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View adminWindow = inflater.inflate(R.layout.dialog_date_picer, null);
            adminDialog.setView(adminWindow);

            adminDialog.setNegativeButton("Отмена", (dialogCansel, which) -> dialogCansel.dismiss());
            adminDialog.setPositiveButton("Изменить", (dialogFinish, which) -> {
                DatePicker datePicker = adminWindow.findViewById(R.id.datePicer);
                String forDateIndex = (datePicker.getDayOfMonth()
                        + "/" + (datePicker.getMonth() + 1)
                        + "/" + datePicker.getYear());
                noteData.setText(forDateIndex);
                Toast.makeText(getContext(), "Дата изменена", Toast.LENGTH_SHORT).show();
            });
            adminDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}