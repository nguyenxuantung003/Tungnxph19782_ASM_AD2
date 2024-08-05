package com.example.nguyenxuantung_ph19782.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Exercise;

import java.util.List;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {

    public ExerciseAdapter(Context context, List<Exercise> exercises) {
        super(context, 0, exercises);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_exercise, parent, false);
        }

        Exercise exercise = getItem(position);
        TextView nameTextView = convertView.findViewById(R.id.exercise_name);
        nameTextView.setText(exercise.getName());

        return convertView;
    }
}