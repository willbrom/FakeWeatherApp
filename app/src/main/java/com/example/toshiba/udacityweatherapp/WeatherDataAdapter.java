package com.example.toshiba.udacityweatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class WeatherDataAdapter extends RecyclerView.Adapter<WeatherDataAdapter.WeatherViewHolder> {


    private String[] weatherData;
    private final ListItemClickListener clickListener;

    interface ListItemClickListener {
        void itemClickListener(String weatherAtPosition);
    }

    WeatherDataAdapter(ListItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.weatherItemTextView.setText(weatherData[position]);
    }

    @Override
    public int getItemCount() {
        if (weatherData == null) return 0;
        return weatherData.length;
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView weatherItemTextView;

        WeatherViewHolder(View itemView) {
            super(itemView);

            weatherItemTextView = itemView.findViewById(R.id.tv_list_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String weatherAtPosition = weatherData[adapterPosition];
            clickListener.itemClickListener(weatherAtPosition);
        }
    }

    void setWeatherData(String[] weatherData) {
        this.weatherData = weatherData;
        notifyDataSetChanged();
    }

}
