package com.example.ProjectWeather.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ProjectWeather.R;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private final List<String> cityList;
    static OnCityClickListener listener;
    public CityAdapter(List<String> cityList, OnCityClickListener listener) {
        this.cityList = cityList;
        CityAdapter.listener = listener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.bind(cityList.get(position));
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public interface OnCityClickListener {
        void onCityClick(String city);
    }
    public static class CityViewHolder extends RecyclerView.ViewHolder {

        private final TextView cityTextView;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.cityTxt);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String city = cityTextView.getText().toString();
                    listener.onCityClick(city);
                }
            });
        }

        public void bind(String cityName) {
            cityTextView.setText(cityName);
        }
    }
}