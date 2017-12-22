package com.example.rdas6313.sunshine.AdapterClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.tool.Binding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rdas6313.sunshine.DateUtils;
import com.example.rdas6313.sunshine.R;
import com.example.rdas6313.sunshine.data.WeatherContract;
import com.example.rdas6313.sunshine.databinding.ListItemBinding;
import com.example.rdas6313.sunshine.databinding.TodayLayoutBinding;
import com.example.rdas6313.sunshine.sync.IconAndDaynightUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatException;

/**
 * Created by rdas6313 on 17/7/17.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.myViewholder> {
    private Cursor mcursor;
    private Context context;
    private final ListItemListener listener;
    private static final int TODAY_LAYOUT = 0;
    private static final int FUTURE_LAYOUT = 1;
    private boolean shouldUseTodayLayout;
    public RecycleAdapter(Context con) {
        mcursor = null;
        context = con;
        shouldUseTodayLayout = context.getResources().getBoolean(R.bool.use_today_layout);
        listener = (ListItemListener) con;
    }


    @Override
    public int getItemCount() {
        if(mcursor == null)
            return 0;
        return mcursor.getCount();
    }

    @Override
    public myViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TODAY_LAYOUT:
                TodayLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.today_layout,parent,false);
                return new myViewholder(binding);
            case FUTURE_LAYOUT:
                ListItemBinding Fbinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.list_item,parent,false);
                return new myViewholder(Fbinding);
            default:
                throw new IllegalArgumentException("Wrong viewtype");
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(shouldUseTodayLayout && position == 0)
            return TODAY_LAYOUT;
        else
            return FUTURE_LAYOUT;
    }

    @Override
    public void onBindViewHolder(myViewholder holder, int position) {
        if(!mcursor.moveToPosition(position))
            return;
        String current_Temp = "";
        int weather_id = mcursor.getInt(mcursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID));
        if(weather_id == 0)
            current_Temp = mcursor.getString(mcursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_CURRENT_TEMP));
        long date_timeStamp = mcursor.getLong(mcursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE));
        double min_temp = mcursor.getDouble(mcursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));
        double max_temp = mcursor.getDouble(mcursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));
        //double humidity = mcursor.getDouble(mcursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_HUMIDITY));
        //double pressure = mcursor.getDouble(mcursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_PRESSURE));
        //double wind = mcursor.getDouble(mcursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED));
        String iconAnddaynight = mcursor.getString(mcursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_ICON));
        String icon = IconAndDaynightUtils.extractIcon(context,iconAnddaynight);
        String dayOrnight = IconAndDaynightUtils.extractDaynight(context,iconAnddaynight);
        String condition = mcursor.getString(mcursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_CONDITION));
        //String s = current_Temp+" "+DateUtils.convertTo(date_timeStamp)+" - "+min_temp+" - "+max_temp+" - "+humidity+" "+condition+" - "+wind;

        int resId = context.getResources().getIdentifier(icon,"drawable",context.getPackageName());
        switch (getItemViewType(position)){
            case TODAY_LAYOUT:
                holder.todayLayoutBinding.WeatherCondition.setText(condition);
                holder.todayLayoutBinding.weatherDate.setText(DateUtils.convertTo(date_timeStamp));
                holder.todayLayoutBinding.weatherIcon.setImageResource(resId);
                holder.todayLayoutBinding.weatherTempMax.setText(context.getResources().getString(R.string.temp,max_temp));
                holder.todayLayoutBinding.weatherTempMin.setText(context.getResources().getString(R.string.temp,min_temp));
                break;
            case FUTURE_LAYOUT:
                holder.mbinding.weatherDayAndCondition.weatherCondition.setText(condition);
                holder.mbinding.weatherDayAndCondition.weatherDate.setText(DateUtils.convertTo(date_timeStamp));
                holder.mbinding.weatherIcon.setImageResource(resId);
                holder.mbinding.weatherTempMax.setText(context.getResources().getString(R.string.temp,max_temp));
                holder.mbinding.weatherTempMin.setText(context.getResources().getString(R.string.temp,min_temp));
                break;
        }
    }
    public void clear(){
        if(mcursor != null)
            mcursor.close();
        mcursor = null;
    }
    public void swapCursor(Cursor cursor){
        clear();
        mcursor = cursor;
        notifyDataSetChanged();
    }
    public long getDateID(int pos){
        if(!mcursor.moveToPosition(pos))
            return -1;
        return mcursor.getLong(mcursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE));
    }
    public class myViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ListItemBinding mbinding;
        TodayLayoutBinding todayLayoutBinding;
        Binding binding;
        public myViewholder(ListItemBinding binding) {
            super(binding.getRoot());
            mbinding = binding;
            binding.getRoot().setOnClickListener(this);
        }
        public myViewholder(TodayLayoutBinding binding){
            super(binding.getRoot());
            todayLayoutBinding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        }
    }
}
