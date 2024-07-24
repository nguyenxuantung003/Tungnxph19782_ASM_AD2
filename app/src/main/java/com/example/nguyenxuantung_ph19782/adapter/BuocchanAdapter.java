package com.example.nguyenxuantung_ph19782.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Buocchan;

import java.util.List;

public class BuocchanAdapter extends RecyclerView.Adapter<BuocchanAdapter.StepHistoryViewHolder> {

    private List<Buocchan> stepHistoryList;

    public BuocchanAdapter(List<Buocchan> stepHistoryList) {
        this.stepHistoryList = stepHistoryList;
    }




    @NonNull
    @Override
    public BuocchanAdapter.StepHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_buocchan, parent, false);
        return new StepHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuocchanAdapter.StepHistoryViewHolder holder, int position) {
        Buocchan stepHistory = stepHistoryList.get(position);
        holder.dateTextView.setText(stepHistory.getDate());
        holder.stepsTextView.setText(String.valueOf(stepHistory.getStepsCount()));
    }

    @Override
    public int getItemCount() {
        return stepHistoryList.size();
    }
    public static class StepHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, stepsTextView;

        public StepHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            stepsTextView = itemView.findViewById(R.id.steps_text_view);
        }
    }
}
