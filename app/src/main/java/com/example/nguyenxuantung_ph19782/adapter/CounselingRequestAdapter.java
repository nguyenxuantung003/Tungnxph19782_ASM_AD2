package com.example.nguyenxuantung_ph19782.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.CounselingRequest;
import java.util.List;

public class CounselingRequestAdapter extends RecyclerView.Adapter<CounselingRequestAdapter.ViewHolder> {

    private List<CounselingRequest> requests;
    private OnItemClickListener onItemClickListener;

    public CounselingRequestAdapter(List<CounselingRequest> requests, OnItemClickListener onItemClickListener) {
        this.requests = requests;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_counseling_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CounselingRequest request = requests.get(position);
        holder.usernameTextView.setText(request.getUsername());
        holder.contentTextView.setText(request.getContent());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView contentTextView;

        ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.tvUsername);
            contentTextView = itemView.findViewById(R.id.tvContent);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(CounselingRequest request);
    }
}