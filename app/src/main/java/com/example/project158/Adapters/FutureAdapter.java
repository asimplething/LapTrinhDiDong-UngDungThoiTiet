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
import com.example.project158.Domains.DayForecast;
import com.example.project158.Domains.FutureDomain;
import com.example.project158.R;

import java.util.ArrayList;

public class FutureAdapter extends RecyclerView.Adapter<FutureAdapter.viewholder> {
    ArrayList<DayForecast> items;
    Context context;

    public FutureAdapter(ArrayList<DayForecast> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FutureAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_future, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureAdapter.viewholder holder, int position) {

        String status = items.get(position).getChanceOfRain()+"%";
        String maxTemp = "Max: "+ items.get(position).getMaxTemperature().substring(0,5) + "°";
        String minTemp = "Min: " + items.get(position).getMinTemperature().substring(0,5) + "°";

        holder.dayTxt.setText(items.get(position).getDayOfWeek().substring(0,3)); // chỉ lấy 3 chữ cái đầu của ngày (cho gọn)
        holder.statusTxt.setText(status);
        holder.highTxt.setText(maxTemp);
        holder.lowTxt.setText(minTemp);
        //holder.pic.setVisibility(View.GONE);
        //int drawableResourceId = holder.itemView.getResources().getIdentifier(items.get(position).getIcon(), "drawable", holder.itemView.getContext().getPackageName());

        //Glide.with(context).load(drawableResourceId).into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView dayTxt, statusTxt, lowTxt, highTxt;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            dayTxt = itemView.findViewById(R.id.dayTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            lowTxt = itemView.findViewById(R.id.lowTxt);
            highTxt = itemView.findViewById(R.id.highTxt);
            pic = itemView.findViewById(R.id.picFuture);
        }


    }
}
