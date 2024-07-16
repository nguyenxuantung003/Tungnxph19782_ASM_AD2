package com.example.nguyenxuantung_ph19782.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.adapter.LoiBietOnAdapter;
import com.example.nguyenxuantung_ph19782.model.Loibieton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LoiBietOnActivity extends AppCompatActivity {
    private EditText etGratitudeNotes;
    private FloatingActionButton fab;
    private ListView lvGratitudeNotes;
    private Button btnAddGratitudeNote;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private ArrayList<Loibieton> gratitudeNotesList;
    private LoiBietOnAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loi_biet_on);

        etGratitudeNotes = findViewById(R.id.etGratitudeNotes);
        fab = findViewById(R.id.fab);
        lvGratitudeNotes = findViewById(R.id.lvGratitudeNotes);

        databaseReference = FirebaseDatabase.getInstance().getReference("MentalActivities");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        gratitudeNotesList = new ArrayList<>();
        adapter = new LoiBietOnAdapter(this, gratitudeNotesList);
        lvGratitudeNotes.setAdapter(adapter);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(LoiBietOnActivity.this, ThemLoiBietOnActivity.class);
            startActivity(intent);
        });
        loadGratitudeNotes();
    }
    private void loadGratitudeNotes() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Query query = databaseReference.orderByChild("userId").equalTo(userId);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    gratitudeNotesList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Loibieton mentalActivity = snapshot.getValue(Loibieton.class);
                        if (mentalActivity != null) {
                            gratitudeNotesList.add(mentalActivity);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });
        }
    }
}