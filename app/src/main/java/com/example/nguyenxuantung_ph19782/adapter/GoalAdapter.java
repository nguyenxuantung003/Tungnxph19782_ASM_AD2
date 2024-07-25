package com.example.nguyenxuantung_ph19782.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nguyenxuantung_ph19782.model.GoalGroup;
import com.example.nguyenxuantung_ph19782.R;

import java.util.List;


public class GoalAdapter extends ArrayAdapter<GoalGroup> {

    private Context context;
    private List<GoalGroup> goalList;

    public GoalAdapter(Context context, List<GoalGroup> goalList) {
        super(context, 0, goalList);
        this.context = context;
        this.goalList = goalList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_group_goal, parent, false);
        }

        // Get the data item for this position
        GoalGroup goal = getItem(position);

        // Lookup view for data population
        TextView titleTextView = convertView.findViewById(R.id.goalTitleTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.goalDescriptionTextView);

        // Populate the data into the template view using the data object
        if (goal != null) {
            titleTextView.setText(goal.getTitle());
            descriptionTextView.setText(goal.getDescription());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}