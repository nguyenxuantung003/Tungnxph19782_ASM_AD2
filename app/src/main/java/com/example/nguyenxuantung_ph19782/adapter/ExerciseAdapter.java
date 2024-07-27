package com.example.nguyenxuantung_ph19782.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.nguyenxuantung_ph19782.R;

public class ExerciseAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] exercises;

    public ExerciseAdapter(Context context, String[] exercises) {
        super(context, R.layout.item_exercise, exercises);
        this.context = context;
        this.exercises = exercises;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_exercise, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.exercise_name_item);
        nameTextView.setText(exercises[position]);

        return convertView;
    }
}