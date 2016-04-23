package com.example.android.sunshine;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.sunshine.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private String mForecastStr;
    private ShareActionProvider mShareActionProvider;
    static final String DETAIL_URI = "URI";

    private static final int DETAIL_LOADER = 0;
    private static TextView mForecastTextView;
    private static TextView mMaxTempTextView;
    private static TextView mMinTempTextView;
    private static TextView mHumidityTextView;
    private static TextView mPressureTextView;
    private static TextView mWindSpeedTextView;
    private static TextView mDayTextView;
    private static TextView mDateTextView;
    private static ImageView mIconView;
    private int mListPos;
    static final String DETAIL_TRANSITION_ANIMATION = "DTA";
    private boolean mTransitionAnimation;


    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();



        mListPos = b.getInt("listItemPos", 0);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }else{
            Log.d(LOG_TAG, "Share Action Provider is Null ?");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mForecastTextView = (TextView) rootView.findViewById(R.id.detail_forecast_TextView);
        mMaxTempTextView = (TextView) rootView.findViewById(R.id.detail_high_TextView);
        mMinTempTextView = (TextView) rootView.findViewById(R.id.detail_low_TextView);
        mHumidityTextView = (TextView) rootView.findViewById(R.id.detail_humidity_TextView);
        mPressureTextView = (TextView) rootView.findViewById(R.id.detail_pressure_TextView);
        mWindSpeedTextView = (TextView) rootView.findViewById(R.id.detail_wind_TextView);
        mDayTextView = (TextView) rootView.findViewById(R.id.detail_day_TextView);
//        mDateTextView = (TextView) rootView.findViewById(R.id.detail_date_TextView);
        mIconView = (ImageView) rootView.findViewById(R.id.detail_forecast_icon);

        Bundle arguments = getArguments();
        if (arguments != null){
            mTransitionAnimation = arguments.getBoolean(DetailFragment.DETAIL_TRANSITION_ANIMATION);
        }



//        Intent intent = getActivity().getIntent();
//        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//            mForecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
//            TextView detail_Textview = (TextView) rootView.findViewById(R.id.detailActivityTextView);
//            detail_Textview.setText(mForecastStr);
//        }


        return rootView;
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

//        Intent intent = getActivity().getIntent();
        String locationSetting = Utility.getPreferredLocation(getActivity());

        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());


//        if (intent == null) {
//            return null;
//        }




        return new android.support.v4.content.CursorLoader(
                getActivity(),
                weatherForLocationUri,
                new String[]{WeatherContract.WeatherEntry.TABLE_NAME+ "." + WeatherContract.WeatherEntry._ID,
                        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
                        WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                        WeatherContract.WeatherEntry.COLUMN_DATE,
                        WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
                        WeatherContract.WeatherEntry.COLUMN_PRESSURE,
                        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
                        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
                        WeatherContract.WeatherEntry.COLUMN_DEGREES,
                        },
                null,
                null,
                sortOrder);

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {



//
//        Intent intent = getActivity().getIntent();
//        int itemLoc = getArguments().getInt("listItemPos", 0);

        data.moveToFirst();
        data.move(mListPos);


        String maxTemp = data.getString(1);
        String minTemp = data.getString(2);
        String description = data.getString(3);
        String dateRaw = data.getString(4).trim();
        float humidity = data.getFloat(5);
        float pressure = data.getFloat(6);
        String windSpeed = data.getString(7);
        float widDir = data.getFloat(9);

        String formattedHumidity = String.format(getActivity().getString(R.string.format_humidity), humidity);
        String formattedPressure = String.format(getActivity().getString(R.string.format_pressure), pressure);
        Float windSpeedFormatter = Float.parseFloat(windSpeed);
        int weatherId = data.getInt(8);
        long dateQuery = Long.parseLong(dateRaw);
        Double maxTempFormatter = Double.parseDouble(maxTemp);
        Double minTempFormatter = Double.parseDouble(minTemp);
        String day = Utility.formatDate(dateQuery);
        mForecastStr = day + " " + "-" + " " + description + " " + "-" + " " + maxTemp + "\\" + minTemp;

        mForecastTextView.setText(description);
        mMaxTempTextView.setText(Utility.formatTemperature(getActivity(), maxTempFormatter));
        mMinTempTextView.setText(Utility.formatTemperature(getActivity(), minTempFormatter));
        mHumidityTextView.setText(formattedHumidity);
        mPressureTextView.setText(formattedPressure);
        mWindSpeedTextView.setText(Utility.getFormattedWind(getActivity(), windSpeedFormatter, widDir));
//        mDateTextView.setText(day);
        mDayTextView.setText(Utility.getDayName(getActivity(), dateQuery));
//        mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));


        Glide.with(this)
                .load(Utility.getArtUrlForWeatherCondition(getActivity(), weatherId))
                .error(Utility.getArtResourceForWeatherCondition(weatherId))
                .into(mIconView);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (mTransitionAnimation){
            activity.supportStartPostponedEnterTransition();
        }


        if (mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

}
