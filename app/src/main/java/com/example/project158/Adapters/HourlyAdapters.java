package com.example.project158.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project158.Domains.Hour;
import com.example.project158.R;

import java.util.ArrayList;

public class HourlyAdapters extends RecyclerView.Adapter<HourlyAdapters.viewholder> {
    ArrayList<Hour> items;
    Context context;

    public HourlyAdapters(ArrayList<Hour> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public HourlyAdapters.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_hourly, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyAdapters.viewholder holder, int position) {
        holder.hourTxt.setText(items.get(position).getTime().substring(11)); // chỉ lấy giờ trong ngày, không lấy cả thời gian ngày
        holder.tempTxt.setText(items.get(position).getTemp_c() + "°");

        Glide.with(context)
                .load("https:"+items.get(position).getCondition().getIconPath())
                .into(holder.picHour);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView hourTxt, tempTxt;
        ImageView picHour;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            hourTxt = itemView.findViewById(R.id.hourTxt);
            tempTxt = itemView.findViewById(R.id.tempTxt);
            picHour = itemView.findViewById(R.id.picHour);
        }
    }
}
