package com.example.f_vacc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class HistoryAdapter extends  RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<History> list = Collections.emptyList();
    Context context;

    public HistoryAdapter(List<History> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View historyItem = inflater.inflate(R.layout.history_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(historyItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.etEstablishmentName.setText("Location: " + list.get(position).getEstablishmentName());
        holder.etTimeinDate.setText("From: " + list.get(position).getTimeIn());
        holder.etTimeoutDate.setText("To: " + list.get(position).getTimeOut());
        holder.etId.setText("ID: " + list.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView etEstablishmentName;
        TextView etTimeinDate;
        TextView etTimeoutDate;
        TextView etId;

        ViewHolder(View itemView) {
            super(itemView);
            etEstablishmentName = itemView.findViewById(R.id.etEstablishmentName);
            etTimeinDate = itemView.findViewById(R.id.etTimein);
            etTimeoutDate = itemView.findViewById(R.id.etTimeout);
            etId = itemView.findViewById(R.id.etId);
        }
    }
}
