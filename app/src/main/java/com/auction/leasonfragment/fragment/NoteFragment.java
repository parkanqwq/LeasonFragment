package com.auction.leasonfragment.fragment;

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.auction.leasonfragment.R;
import com.auction.leasonfragment.adapter.AdapterNoteList;
import com.auction.leasonfragment.model.ModelNoteList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {

    public static final int ADD_FRAGMENT_ZERO_POSITION = 0;
    private List<ModelNoteList> modelNoteLists;
    private AdapterNoteList adapterNoteList;
    private RecyclerView recyclerView;
    private  FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    public NoteFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recyclerView);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            StaggeredGridLayoutManager lnManagerAll = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(lnManagerAll);
        } else {
            LinearLayoutManager lnManagerAll = new LinearLayoutManager(view.getContext());
            recyclerView.setLayoutManager(lnManagerAll);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        modelNoteLists = new ArrayList<>();
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Note")
                    .child(currentUser.getUid())
                    .child("Note");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    modelNoteLists.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ModelNoteList modelNote = snapshot.getValue(ModelNoteList.class);
                        modelNoteLists.add(ADD_FRAGMENT_ZERO_POSITION, modelNote);
                    }
                    adapterNoteList = new AdapterNoteList(getContext(), modelNoteLists, dataSnapshot);
                    recyclerView.setAdapter(adapterNoteList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (Exception e){}


    }
}