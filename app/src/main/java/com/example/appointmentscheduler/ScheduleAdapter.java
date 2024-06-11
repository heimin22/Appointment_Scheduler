package com.example.appointmentscheduler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {

    private final Context context;
    Activity activity;
    private final ArrayList<String> scheduleId;
    private final ArrayList<String> schedName;
    private final ArrayList<String> schedDate;
    private final ArrayList<String> schedTime;
    private final ArrayList<String> schedDesc;
    private final ArrayList<String> schedLink;
    private final ArrayList<Boolean> schedIsFinish;

    public ScheduleAdapter(Activity activity, Context context, ArrayList<String> scheduleId, ArrayList<String> schedName, ArrayList<String> schedDate,
                           ArrayList<String> schedTime, ArrayList<String> schedDesc, ArrayList<String> schedLink, ArrayList<Boolean> schedIsFinish) {
        this.activity = activity;
        this.context = context;
        this.scheduleId = scheduleId;
        this.schedName = schedName;
        this.schedTime = schedTime;
        this.schedDate = schedDate;
        this.schedDesc = schedDesc;
        this.schedLink = schedLink;
        this.schedIsFinish = schedIsFinish;
    }

    @NonNull
    @Override
    public ScheduleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.MyViewHolder holder, final int position) {
        String id = scheduleId.get(position);
        String name = schedName.get(position);
        String date = schedDate.get(position);
        String time = schedTime.get(position);
        String desc = schedDesc.get(position);
        String link = schedLink.get(position);
        Boolean finished = schedIsFinish.get(position);
        holder.schedNameTxt.setText(name);
        holder.schedDateTxt.setText(date);
        holder.schedTimeTxt.setText(time);
        holder.schedDescTxt.setText(desc);
        holder.schedLinkTxt.setText(link);

//        Debugging purposes
//        Toast.makeText(context, "SchedID: "+id + "SchedStatus: " + finished, Toast.LENGTH_SHORT ).show();

        Log.d("ScheduleAdapter", "Position: " + position + ", Finished: " + finished);

        if (finished) {
            holder.checkImg.setVisibility(View.VISIBLE);
            holder.checkImg.setImageResource(R.drawable.check_on);
        } else {
            holder.checkImg.setVisibility(View.VISIBLE);
            holder.checkImg.setImageResource(R.drawable.check_off);
        }

        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                intent.putExtra("desc", desc);
                intent.putExtra("link", link);
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleId.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView checkImg;
        TextView schedNameTxt, schedDateTxt, schedTimeTxt, schedDescTxt, schedLinkTxt;
        LinearLayout rowLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            schedNameTxt = itemView.findViewById(R.id.meetname_tv);
            schedDateTxt = itemView.findViewById(R.id.date_tv);
            schedTimeTxt = itemView.findViewById(R.id.time_tv);
            schedDescTxt = itemView.findViewById(R.id.meetdescription_tv);
            schedLinkTxt = itemView.findViewById(R.id.meetlink_tv);
            checkImg = itemView.findViewById(R.id.check_status);
            rowLayout = itemView.findViewById(R.id.meetingRows);

        }
    }
}
