package com.example.nguyenxuantung_ph19782.Expert.Adapter;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nguyenxuantung_ph19782.R;
import com.example.nguyenxuantung_ph19782.model.CounselingRequest;
import com.example.nguyenxuantung_ph19782.Expert.Fragment.ConsultationRequestDetailFragment;
import java.util.List;

public class ConsultationRequestsAdapter extends RecyclerView.Adapter<ConsultationRequestsAdapter.ViewHolder> {

    private Context context;
    private List<CounselingRequest> requests;
    private FragmentManager fragmentManager;

    public ConsultationRequestsAdapter(Context context, List<CounselingRequest> requests, FragmentManager fragmentManager) {
        this.context = context;
        this.requests = requests;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consultation_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CounselingRequest request = requests.get(position);
        holder.textViewTitle.setText(request.getUsername());
        holder.textViewDescription.setText(request.getContent());

        holder.itemView.setOnClickListener(v -> {
            // Hiển thị chi tiết yêu cầu
            ConsultationRequestDetailFragment fragment = new ConsultationRequestDetailFragment();
            Bundle args = new Bundle();
            args.putString("username", request.getUsername());
            args.putString("content", request.getContent());
            args.putString("requestId", request.getRequestId()); // Sử dụng requestId
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.frg1_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}