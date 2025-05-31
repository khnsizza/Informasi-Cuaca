package com.example.tisproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeeklyForecastAdapter extends RecyclerView.Adapter<WeeklyForecastAdapter.ViewHolder> {

    private List<DailyWeatherData> weeklyList;

    public WeeklyForecastAdapter(List<DailyWeatherData> weeklyList) {
        this.weeklyList = weeklyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyWeatherData data = weeklyList.get(position);

        holder.tvDay.setText(data.getDay());
        holder.tvCondition.setText(data.getCondition());
        holder.tvHighTemp.setText(data.getHighTemp() + "°");
        holder.tvLowTemp.setText(data.getLowTemp() + "°");
        holder.tvRainChance.setText(data.getRainChance() + "%");

        // Set weather icon based on condition
        setWeatherIcon(holder.ivWeatherIcon, data.getCondition());

        // Add click listener for each item
        holder.itemView.setOnClickListener(v -> {
            // You can add detailed view for each day here
            // For now, just show a toast
            android.widget.Toast.makeText(v.getContext(),
                    "Detailed forecast for " + data.getDay() + " coming soon!",
                    android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    private void setWeatherIcon(ImageView imageView, String condition) {
        switch (condition.toLowerCase()) {
            case "sunny":
            case "clear":
                imageView.setImageResource(R.drawable.cuaca);
                break;
            case "rainy":
            case "rain":
                imageView.setImageResource(R.drawable.hujan);
                break;
            case "partly cloudy":
            case "cloudy":
            case "clouds":
                imageView.setImageResource(R.drawable.cuaca);
                break;
            case "thunderstorm":
                imageView.setImageResource(R.drawable.hujan);
                break;
            default:
                imageView.setImageResource(R.drawable.cuaca);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return weeklyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvCondition, tvHighTemp, tvLowTemp, tvRainChance;
        ImageView ivWeatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvCondition = itemView.findViewById(R.id.tv_condition);
            tvHighTemp = itemView.findViewById(R.id.tv_high_temp);
            tvLowTemp = itemView.findViewById(R.id.tv_low_temp);
            tvRainChance = itemView.findViewById(R.id.tv_rain_chance);
            ivWeatherIcon = itemView.findViewById(R.id.iv_weather_icon);
        }
    }
}