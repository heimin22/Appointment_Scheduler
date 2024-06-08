package com.example.appointmentscheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder> {

    private Context context;
    private ArrayList meeting_name, meeting_date, meeting_time, meeting_desc, meeting_link;

    MeetingAdapter(Context context, ArrayList meeting_name, ArrayList meeting_date,
                   ArrayList meeting_time, ArrayList meeting_desc, ArrayList meeting_link) {
        this.context = context;
        this.meeting_name = meeting_name;
        this.meeting_time = meeting_time;
        this.meeting_date = meeting_date;
        this.meeting_desc = meeting_desc;
        this.meeting_link = meeting_link;
    }

    @NonNull
    @Override
    public MeetingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingAdapter.MyViewHolder holder, int position) {
        holder.meeting_name_txt.setText(String.valueOf(meeting_name.get(position)));
        holder.meeting_date_txt.setText(String.valueOf(meeting_date.get(position)));
        holder.meeting_time_txt.setText(String.valueOf(meeting_time.get(position)));
        holder.meeting_desc_txt.setText(String.valueOf(meeting_desc.get(position)));
        holder.meeting_link_txt.setText(String.valueOf(meeting_link.get(position)));
    }

    @Override
    public int getItemCount() {
        return meeting_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView meeting_name_txt, meeting_date_txt, meeting_time_txt, meeting_desc_txt, meeting_link_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            meeting_name_txt = itemView.findViewById(R.id.meetname_tv);
            meeting_date_txt = itemView.findViewById(R.id.date_tv);
            meeting_time_txt = itemView.findViewById(R.id.time_tv);
            meeting_desc_txt = itemView.findViewById(R.id.meetdescription_tv);
            meeting_link_txt = itemView.findViewById(R.id.meetlink_tv);
        }
    }
}
