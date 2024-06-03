package com.example.appointmentscheduler;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private final String currentDate; // new
    private final int currentYear;
    private final int currentMonth;
    private final int displayedYear;
    private final int displayedMonth;


    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, int displayedYear, int displayedMonth)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.displayedYear = displayedYear;
        this.displayedMonth = displayedMonth;

        /*
            ito bago sa baba
         */

        Calendar calendar =Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.getDefault());
        currentDate = dateFormat.format(calendar.getTime());
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        String day = daysOfMonth.get(position);
        holder.dayOfMonth.setText(day);

        if (day.equals(currentDate) && displayedYear == currentYear && displayedMonth == currentMonth) {
            holder.squareView.setVisibility(View.VISIBLE);
            holder.dayOfMonth.setTextColor(Color.BLACK);
        }
        else {
            holder.squareView.setVisibility(View.GONE);
            holder.dayOfMonth.setTextColor(Color.BLACK);
        }

        holder.dayOfMonth.setText(daysOfMonth.get(position));
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView dayOfMonth;
        public final View squareView;
        private final OnItemListener onItemListener;

        public CalendarViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            squareView = itemView.findViewById(R.id.squareView);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
        }
    }

}