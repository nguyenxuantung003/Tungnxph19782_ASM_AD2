package com.example.nguyenxuantung_ph19782.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nguyenxuantung_ph19782.adapter.ExerciseAdapter;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Exercise;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ExerciseListActivity extends AppCompatActivity {

    private List<Exercise> exerciseList;
    private ExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        ListView listView = findViewById(R.id.exercise_list_view);

        exerciseList = new ArrayList<>();
        adapter = new ExerciseAdapter(this, exerciseList);
        listView.setAdapter(adapter);

        DatabaseReference exercisesRef = FirebaseDatabase.getInstance().getReference("Exercises");
        exercisesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exerciseList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = snapshot.getValue(Exercise.class);
                    exerciseList.add(exercise);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Exercise exercise = exerciseList.get(position);
            Intent intent = new Intent(ExerciseListActivity.this, ExerciseDetailActivity.class);
            intent.putExtra("exercise_name", exercise.getName());
            intent.putExtra("exercise_description", exercise.getDetails());
            intent.putExtra("exercise_benefits", exercise.getBenefits());
            intent.putExtra("exercise_instructions", exercise.getInstructions());
            startActivity(intent);
        });
    }
}