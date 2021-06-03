package com.auction.leasonfragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.auction.leasonfragment.adapter.AdapterNoteList;
import com.auction.leasonfragment.model.ModelNoteList;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private SignInButton googleSignBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private List<ModelNoteList> modelNoteLists;
    private AdapterNoteList adapterNoteList;
    private RecyclerView recyclerView;
    private TextView userNameGoogle;
    private NavigationView navigationView;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        initView();
        actMethod();
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void actMethod() {
        googleSignBtn = findViewById(R.id.googleSignBtn);
        recyclerView = findViewById(R.id.recyclerView);
        googleSignBtn.setOnClickListener(view -> {
            signIn();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    recreate();
                }).addOnFailureListener(e -> {
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null){
            userNameGoogle.setText(currentUser.getDisplayName());
            recyclerView.setVisibility(View.VISIBLE);
            navigationView.setVisibility(View.VISIBLE);
        } else {
            userNameGoogle.setText("Гость");
            recyclerView.setVisibility(View.GONE);
            navigationView.setVisibility(View.GONE);
        }
    }

    private void initView() {
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
    }


    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        userNameGoogle = headerLayout.findViewById(R.id.userNameGoogle);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (navigateFragment(id)){
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            if (navigateFragment(id)) {
                return true;
            }
            return super.onOptionsItemSelected(item);
    }

    private boolean navigateFragment(int id) {
        switch (id) {
            case R.id.action_exit_google:
                AlertDialog.Builder adminDialogExit = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false);
                adminDialogExit.setTitle("Выйти");
                adminDialogExit.setMessage("Вы уверены");
                adminDialogExit.setNegativeButton("Нет", (dialogCansel, which) -> dialogCansel.dismiss());
                adminDialogExit.setPositiveButton("Выйти", (dialogFinish, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    recreate();
                });
                adminDialogExit.show();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_main:
                AlertDialog.Builder adminDialog = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View adminWindow = inflater.inflate(R.layout.item_create_note_name, null);
                adminDialog.setView(adminWindow);

                adminDialog.setNegativeButton("Отмена", (dialogCansel, which) -> dialogCansel.dismiss());
                adminDialog.setPositiveButton("Создать", (dialogFinish, which) -> {
                    TextView nameNote = adminWindow.findViewById(R.id.nameNote);
                    String name = nameNote.getText().toString();

                    if (name.equals("")) {
                        Toast.makeText(MainActivity.this, "Заполните поле", Toast.LENGTH_SHORT).show();
                    } else {
                        Date date = new Date();
                        String dateplas = String.valueOf(new Random().nextInt(1000));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        String stringDate = sdf.format(date);

                        try {
                            reference = FirebaseDatabase.getInstance().getReference("Note")
                                    .child(currentUser.getUid())
                                    .child("Note")
                                    .push();
                            HashMap<String, Object> hashMapRef = new HashMap<>();
                            hashMapRef.put("id", date.toString()+dateplas);
                            hashMapRef.put("text", "");
                            hashMapRef.put("name", name);
                            hashMapRef.put("dateNote", stringDate);
                            hashMapRef.put("nameToLowerCase", name.toLowerCase());
                            reference.setValue(hashMapRef);
                        } catch (Exception e){}

                        Toast.makeText(getApplicationContext(), "Заметка создана", Toast.LENGTH_SHORT).show();
                    }
                });
                adminDialog.show();
                return true;
            case R.id.action_delete_all:
                AlertDialog.Builder adminDialogDelete = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false);
                LayoutInflater inflaterelete = LayoutInflater.from(MainActivity.this);
                View adminWindowDelete = inflaterelete.inflate(R.layout.card_delete, null);
                adminDialogDelete.setView(adminWindowDelete);

                adminDialogDelete.setNegativeButton("Нет", (dialogCansel, which) -> dialogCansel.dismiss());
                adminDialogDelete.setPositiveButton("Удалить все!", (dialogFinish, which) -> {
                    try {
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("Note")
                                .child(currentUser.getUid())
                                .child("Note");
                        reference.removeValue();
                    } catch (Exception e){}

                    Toast.makeText(this, "Все заметки удалены", Toast.LENGTH_SHORT).show();
                });
                adminDialogDelete.show();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchUsers(newText.toLowerCase());
                return true;
            }
        });
        return true;
    }

    private void searchUsers(String s) {
        modelNoteLists = new ArrayList<>();
        try {
            Query query = FirebaseDatabase.getInstance().getReference("Note")
                    .child(currentUser.getUid())
                    .child("Note")
                    .orderByChild("nameToLowerCase")
                    .startAt(s)
                    .endAt(s+"\uf8ff");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    modelNoteLists.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ModelNoteList modelNoteList = snapshot.getValue(ModelNoteList.class);
                        modelNoteLists.add(modelNoteList);
                    }
                    adapterNoteList = new AdapterNoteList(getApplicationContext(), modelNoteLists, dataSnapshot);
                    recyclerView.setAdapter(adapterNoteList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e){}
    }
}