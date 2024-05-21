    package com.example.ProjectWeather.Adapters;

    import android.content.Context;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.example.ProjectWeather.Activitis.Summary_Activity;
    import com.example.ProjectWeather.Domains.DayForecast;
    import com.example.ProjectWeather.R;

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

            String status = items.get(position).getChanceOfRain()+"%  raining";
            String maxTemp = "Max: "+ items.get(position).getMaxTemperature().substring(0,2) + "°";
            String minTemp = "Min: " + items.get(position).getMinTemperature().substring(0,2) + "°";

            holder.dayTxt.setText(items.get(position).getDayOfWeek().substring(0,3)); // chỉ lấy 3 chữ cái đầu của ngày (cho gọn)
            holder.statusTxt.setText(status);
            holder.highTxt.setText(maxTemp);
            holder.lowTxt.setText(minTemp);
            Glide.with(context).load(items.get(position).getIcon()).into(holder.pic);
    // Bắt sự kiện nhấn vào item
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, Summary_Activity.class);
                intent.putExtra("maxTemperature", items.get(position).getMaxTemperature());
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class viewholder extends RecyclerView.ViewHolder {
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
