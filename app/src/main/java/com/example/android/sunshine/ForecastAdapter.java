package com.example.android.sunshine;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.GlideModule;

import java.io.File;
import java.util.List;

/**
 * Created by Nishant on 15/02/2016.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {
    public final int VIEW_TYPE_TODAY = 0;
    public final int VIEW_TYPE_TOMORROW = 1;
    private boolean mUseTodayLayout = false;
    private static final String TODAY = "Today";
    final private Context mContext;
    final private ItemChoiceManager mICM;
    private Cursor mCursor;
    final private ForecastAdapterOnClickHandler mClickHandler;
    final private View mEmptyView;
    public ForecastAdapter(Context context, ForecastAdapterOnClickHandler dh, View emptyView, int choiceMode) {
        mContext = context;
        mClickHandler = dh;
        mEmptyView = emptyView;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);

    }




    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView iconView;
        public final TextView forecastTextView;
        public final TextView dateTextView;
        public final TextView highTempTextView;
        public final TextView lowTempTextView;

        public ForecastAdapterViewHolder (View view) {
            super(view);
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            forecastTextView = (TextView) view.findViewById(R.id.list_item_forecast_textView);
            dateTextView = (TextView) view.findViewById(R.id.list_item_date_textView);
            highTempTextView = (TextView) view.findViewById(R.id.list_item_high_textView);
            lowTempTextView = (TextView) view.findViewById(R.id.list_item_low_textView);


            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition, this);

        }
    }


    public static interface ForecastAdapterOnClickHandler {
        void onClick(int recyclerItemPos, ForecastAdapterViewHolder vh);
    }

















//
//    public ForecastAdapter(Context context, Cursor c, int flags) {
//        super(context, c, flags);
//        mContext = context;
//    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
    }

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if ( parent instanceof RecyclerView ) {
            int layoutId = -1;
            switch (viewType) {
                case VIEW_TYPE_TODAY: {
                    layoutId = R.layout.list_item_forecast_today_new;
                    break;
                }
                case VIEW_TYPE_TOMORROW: {
                    layoutId = R.layout.list_item_layout_new;
                    break;
                }
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            view.setFocusable(true);
            return new ForecastAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        String day;

        mCursor.moveToPosition(position);







        String maxTemp = mCursor.getString(1);
        String minTemp = mCursor.getString(2);
        String description = mCursor.getString(3);
        long date = mCursor.getLong(4);
        int weatherId = mCursor.getInt(5);

        double maxDoubleTemp = Double.parseDouble(maxTemp);
        double minDoubleTemp = Double.parseDouble(minTemp);
        day = Utility.getFriendlyDayString(mContext, date, true);
        String resultData = day + " " + "-" + " " + description + " " + "-" + " " + maxTemp + "\\" + minTemp;




        holder.forecastTextView.setText(description);
        holder.dateTextView.setText(day);
        holder.highTempTextView.setText(Utility.formatTemperature(mContext, maxDoubleTemp));
        holder.lowTempTextView.setText(Utility.formatTemperature(mContext, minDoubleTemp));


        int viewType = getItemViewType(mCursor.getPosition());




        if (viewType == VIEW_TYPE_TODAY){
            if (day.contains(TODAY)){
                holder.dateTextView.setText(R.string.weatherToday);
                ViewCompat.setTransitionName(holder.iconView, "iconView" + position);

            }
            Glide.with(mContext)
                    .load(Utility.getArtUrlForWeatherCondition(mContext, weatherId))
                    .into(holder.iconView);
//            viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
        }else if (viewType == VIEW_TYPE_TOMORROW){
            if (day.contains(TODAY)){
                holder.dateTextView.setText(R.string.weatherToday);
                ViewCompat.setTransitionName(holder.iconView, "iconView" + position);
            }


            File cacheDirName = Glide.getPhotoCacheDir(mContext);


//            Log.d("GLIDE CACHE DIR", cacheDirName.toString());
//            viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(weatherId));
            Glide.with(mContext)
                    .load(Utility.getArtUrlForWeatherCondition(mContext, weatherId))
                    .error(Utility.getArtResourceForWeatherCondition(weatherId))
                    .into(holder.iconView);


        }





//        cursor.moveToNext();

//        if (cursor.isAfterLast()) {
//            cursor.close();
//        }

        mICM.onBindViewHolder(holder, position);




    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_TOMORROW;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mICM.onRestoreInstanceState(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mICM.onSaveInstanceState(outState);
    }


    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);

    }

    public Cursor getCursor() {
        return mCursor;
    }

//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//
//        int viewType = getItemViewType(cursor.getPosition());
//
//        int layoutId = -1;
//
//        if (viewType == VIEW_TYPE_TODAY) {
//            layoutId = R.layout.list_item_forecast_today_new;
//        }else if (viewType == VIEW_TYPE_TOMORROW) {
//            layoutId = R.layout.list_item_layout_new;
//        }
//
//        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        view.setTag(viewHolder);
//
//        return view;
//    }

//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        String day;
//
//
//
//
//
//
//
//        String maxTemp = cursor.getString(1);
//        String minTemp = cursor.getString(2);
//        String description = cursor.getString(3);
//        long date = cursor.getLong(4);
//        int weatherId = cursor.getInt(5);
//
//        double maxDoubleTemp = Double.parseDouble(maxTemp);
//        double minDoubleTemp = Double.parseDouble(minTemp);
//        day = Utility.getFriendlyDayString(context, date);
//        String resultData = day + " " + "-" + " " + description + " " + "-" + " " + maxTemp + "\\" + minTemp;
//
//
//
//
//        viewHolder.forecastTextView.setText(description);
//        viewHolder.dateTextView.setText(day);
//        viewHolder.highTempTextView.setText(Utility.formatTemperature(context, maxDoubleTemp));
//        viewHolder.lowTempTextView.setText(Utility.formatTemperature(context, minDoubleTemp));
//
//
//        int viewType = getItemViewType(cursor.getPosition());
//
//
//
//
//        if (viewType == VIEW_TYPE_TODAY){
//            if (day.contains(TODAY)){
//                viewHolder.dateTextView.setText(R.string.weatherToday);
//
//            }
//            Glide.with(context)
//                    .load(Utility.getArtUrlForWeatherCondition(mContext, weatherId))
//                    .into(viewHolder.iconView);
////            viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
//        }else if (viewType == VIEW_TYPE_TOMORROW){
//            if (day.contains(TODAY)){
//                viewHolder.dateTextView.setText(R.string.weatherToday);
//            }
//
//
//            File cacheDirName = Glide.getPhotoCacheDir(mContext);
//
//
////            Log.d("GLIDE CACHE DIR", cacheDirName.toString());
////            viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(weatherId));
//            Glide.with(context)
//                    .load(Utility.getArtUrlForWeatherCondition(mContext, weatherId))
//                    .error(Utility.getArtResourceForWeatherCondition(weatherId))
//                    .into(viewHolder.iconView);
//
//        }
//
////        cursor.moveToNext();
//
////        if (cursor.isAfterLast()) {
////            cursor.close();
////        }
//
//
//
//    }
public void selectView(RecyclerView.ViewHolder viewHolder) {
    if ( viewHolder instanceof ForecastAdapterViewHolder ) {
        ForecastAdapterViewHolder vfh = (ForecastAdapterViewHolder)viewHolder;
        vfh.onClick(vfh.itemView);
    }
}

    public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }




}
