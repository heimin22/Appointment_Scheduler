package com.example.appointmentscheduler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FinishedSchedAdapter extends RecyclerView.Adapter<FinishedSchedAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> names, dates, times, descr, links;

    public FinishedSchedAdapter(Context context, ArrayList<String> names, ArrayList<String> dates, ArrayList<String> times, ArrayList<String> descr, ArrayList<String> links) {
        this.context = context;
        this.names = names;
        this.dates = dates;
        this.times = times;
        this.descr = descr;
        this.links = links;
    }

    @NonNull
    @Override
    @SuppressLint("ResourceType")
    public FinishedSchedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinishedSchedAdapter.MyViewHolder holder, int position) {
        holder.sched_name.setText(names.get(position));
        holder.sched_date.setText(dates.get(position));
        holder.sched_time.setText(times.get(position));
        holder.sched_desc.setText(descr.get(position));
        holder.sched_link.setText(links.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sched_name, sched_date, sched_time, sched_desc, sched_link;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sched_name = itemView.findViewById(R.id.meetname_tv);
            sched_date = itemView.findViewById(R.id.date_tv);
            sched_time = itemView.findViewById(R.id.time_tv);
            sched_desc = itemView.findViewById(R.id.meetdescription_tv);
            sched_link = itemView.findViewById(R.id.meetlink_tv);
        }
    }
}
