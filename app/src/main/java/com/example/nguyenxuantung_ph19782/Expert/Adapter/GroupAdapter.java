package com.example.nguyenxuantung_ph19782.Expert.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.Group;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private final List<Group> groupList;
    private final OnItemClickListener onItemClickListener;

    public GroupAdapter(List<Group> groupList, OnItemClickListener onItemClickListener) {
        this.groupList = groupList;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grops_forchuyegia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group group = groupList.get(position);
        holder.textViewGroupName.setText(group.getGroupName());
        holder.textViewGroupDescription.setText(group.getDescription());

        // Set click listener
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(group));
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Group group);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewGroupName;
        public TextView textViewGroupDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGroupName = itemView.findViewById(R.id.textViewGroupName);
            textViewGroupDescription = itemView.findViewById(R.id.textViewGroupDescription);
        }
    }
}