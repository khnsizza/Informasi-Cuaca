package com.example.tisproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tisproject.Model.HourlyWeatherData;
import com.example.tisproject.R;

import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {

    private List<HourlyWeatherData> hourlyList;

    public HourlyForecastAdapter(List<HourlyWeatherData> hourlyList) {
        this.hourlyList = hourlyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyWeatherData data = hourlyList.get(position);

        holder.tvTime.setText(data.getTime());
        holder.tvTemperature.setText(data.getTemperature() + "°");
        holder.tvRainChance.setText(data.getRainChance() + "%");

        // You can set different icons based on weather conditions
        // For now, using the default weather icon
        holder.ivWeatherIcon.setImageResource(R.drawable.cuaca);
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTemperature, tvRainChance;
        ImageView ivWeatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTemperature = itemView.findViewById(R.id.tv_temperature);
            tvRainChance = itemView.findViewById(R.id.tv_rain_chance);
            ivWeatherIcon = itemView.findViewById(R.id.iv_weather_icon);
        }
    }
}