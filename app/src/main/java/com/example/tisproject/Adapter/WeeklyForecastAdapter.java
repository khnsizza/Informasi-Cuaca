package com.example.tisproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tisproject.Model.DailyWeatherData;
import com.example.tisproject.R;

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
        holder.tvHighTemp.setText(data.getHighTemp() + "°");
        holder.tvLowTemp.setText(data.getLowTemp() + "°");
        holder.tvRainChance.setText(data.getRainChance() + "%");
        holder.tvCondition.setText(data.getCondition());
        holder.tvRainChance.setText(data.getRainChance() + "%");

        // Set icon drawable by icon name
        int iconResId = holder.itemView.getContext().getResources()
                .getIdentifier(data.getWeatherIcon(), "drawable", holder.itemView.getContext().getPackageName());
        if (iconResId != 0) {
            holder.ivWeatherIcon.setImageResource(iconResId);
        } else {
            holder.ivWeatherIcon.setImageResource(R.drawable.cuaca); // default icon
        }
    }

    @Override
    public int getItemCount() {
        return weeklyList != null ? weeklyList.size() : 0;
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