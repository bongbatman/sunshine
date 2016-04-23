package com.example.android.sunshine;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.sync.SunshineSyncAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    //    public ArrayAdapter<String> mForecastAdapter;

    private RecyclerView mRecyclerView;
    public List<String> weekForecast;
    public String[] forecastArray;
    public ForecastAdapter mForecastAdapter;
    private static final int FORECAST_LOADER = 0;
    Cursor c;
    public int mPosition = mRecyclerView.NO_POSITION;
    private static final String SELECTED_KEY = "selectedPosition";
    private ListView mListView;
    private final String LOG_TAG = ForecastFragment.class.getSimpleName();
    private String CITY_NAME_SNACKBAR;
    private boolean mUseTodayLayout, mAutoSelectView;
    private TextView emptyView;
    private int mChoiceMode;
    private boolean mHoldForTransition;
    private long mInitialSelectedDate = -1;


    public ForecastFragment() {

    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (mHoldForTransition) {
            getActivity().supportPostponeEnterTransition();
        }
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);







    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);




//        View emptyView = getView().findViewById(R.id.listView_forecast_empty);


////        getLoaderManager().initLoader(LIST_FRAGMENT_LOADER_ID, null, this);
//
        forecastArray = new String[]{"Test"};
////        if (mPosition != ListView.INVALID_POSITION) {
////            mListView = (ListView) getListView().findViewById(android.R.id.list);
////            mListView.setSelection(0);
////        }
//
//
        weekForecast = new ArrayList<>(Arrays.asList(forecastArray));
//
//
//        mForecastAdapter =
////                new ArrayAdapter<String>(
////                        getActivity(),
////                        R.layout.list_item_layout,
////                        R.id.list_item_forecast_textView,
////                        weekForecast
////                );
//
//        mForecastAdapter =
//                new ForecastAdapter(
//                        getActivity(),
//
//                );

//        c = null;
//
//        mForecastAdapter = new SimpleCursorAdapter(
//                getActivity(),
//                R.layout.list_item_layout,
//                c,
//                null,
//                null,
//                0
//        );



//        setListAdapter(mForecastAdapter);
//        TextView emptyView = (TextView) getListView().findViewById(R.id.listView_forecast_empty);
//        if (mForecastAdapter == null){
//            emptyView.setText(getString(R.string.empty_forecast_list));
//        }




    }

//    public interface Callback {
//        /**
//         * DetailFragmentCallback for when an item has been selected.
//         */
//        public void onItemSelected(Uri dateUri);
//    }




    @TargetApi(Build.VERSION_CODES.LOLLIPOP)



//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//
//
//        //This returns only visible child elements. So we can't get data when a list is scrolled.
////        TextView column = (TextView)l.getChildAt(position);
////        String itemText = column.getText().toString();
//
//
////        TextView itemText_raw = (TextView) l.getChildAt(position);
////        String itemText = itemText_raw.getText().toString();
//
//
////        String itemText = weatherForLocationUri.toString();
//        int itemPos = position;
////        Snackbar.make(v, (CharSequence) itemText, Snackbar.LENGTH_SHORT)
////                .setAction("Action", null).show();
////        Intent intent = new Intent(getActivity(), DetailActivity.class)
////                .putExtra("ItemLocation", itemPos);
////        startActivity(intent);
//
//        ((Callbacks) getActivity()).onItemSelected(position);
//
//
//
////        Cursor cursor = (Cursor) l.getItemAtPosition(position);
////        if (cursor != null) {
////            String locationSetting = Utility.getPreferredLocation(getActivity());
////            ((Callback) getActivity())
////                    .onItemSelected(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
////                            locationSetting, cursor.getLong(4)
////                    ));
////        }
//        mPosition = position;
//    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);





        emptyView = (TextView) rootView.findViewById(R.id.forecast_recycler_view_empty);



//        mListView = (ListView) rootView.findViewById(R.id.forecast_list_view);








        // The ForecastAdapter will take data from a source and
        // use it to populate the RecyclerView it's attached to.
        mForecastAdapter = new ForecastAdapter(getActivity(), new ForecastAdapter.ForecastAdapterOnClickHandler() {
            @Override
            public void onClick(int recyclerItemPos, ForecastAdapter.ForecastAdapterViewHolder vh) {
                mPosition = recyclerItemPos;
                ((Callbacks) getActivity()).onItemSelected(recyclerItemPos, vh);
            }
        }, emptyView, mChoiceMode);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.forecast_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setAdapter(mForecastAdapter);
        mRecyclerView.setHasFixedSize(true);


        final View parallaxView = rootView.findViewById(R.id.parallax_bar);
        if (null != parallaxView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int max = parallaxView.getHeight();
                        if (dy > 0) {
                            parallaxView.setTranslationY(Math.max(-max, parallaxView.getTranslationY() - dy / 2));
                        } else {
                            parallaxView.setTranslationY(Math.min(0, parallaxView.getTranslationY() - dy / 2));
                        }
                    }
                });
            }
        }
//

//        This won't work as the appbar doesn't belong to this layout

//        final AppBarLayout appbarView = (AppBarLayout)rootView.findViewById(R.id.appbar);
//        if (null != appbarView) {
//            ViewCompat.setElevation(appbarView, 0);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//                    @Override
//                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                        if (0 == mRecyclerView.computeVerticalScrollOffset()) {
//                            appbarView.setElevation(0);
//                        } else {
//                            appbarView.setElevation(appbarView.getTargetElevation());
//                        }
//                    }
//                });
//            }
//        }


//        mListView.setEmptyView(emptyView);
//
//        mListView.setAdapter(mForecastAdapter);


//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int itemPos = position;
//                ((Callbacks) getActivity()).onItemSelected(position);
//                mPosition = position;
//
//            }
//        });




        if (savedInstanceState != null) {
            mForecastAdapter.onRestoreInstanceState(savedInstanceState);
        }



//        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
//            mPosition = savedInstanceState.getInt(SELECTED_KEY);
//        }
        mForecastAdapter.setUseTodayLayout(mUseTodayLayout);

        return rootView;
    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String locationSetting = Utility.getPreferredLocation(getActivity());

        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());


        return new android.support.v4.content.CursorLoader(
                getActivity(),
                weatherForLocationUri,
                new String[]{WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
                        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
                        WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                        WeatherContract.WeatherEntry.COLUMN_DATE,
                        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
                        WeatherContract.LocationEntry.COLUMN_COORD_LAT,
                        WeatherContract.LocationEntry.COLUMN_COORD_LONG,
                        WeatherContract.LocationEntry.COLUMN_CITY_NAME},
                null,
                null,
                sortOrder);

    }
    private void openPreferredLocationInMap() {
        // Using the URI scheme for showing a location found on a map.  This super-handy
        // intent can is detailed in the "Common Intents" page of Android's developer site:
        // http://developer.android.com/guide/components/intents-common.html#Maps
        if ( null != mForecastAdapter ) {
            Cursor c = mForecastAdapter.getCursor();
            if ( null != c ) {
                c.moveToPosition(0);
                String posLat = c.getString(6);
                String posLong = c.getString(7);
                Uri geoLocation = Uri.parse("geo:" + posLat + "," + posLong);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(geoLocation);

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "Couldn't call " + geoLocation.toString() + ", no receiving apps installed!");
                }
            }

        }
    }




    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);
        if (mPosition != RecyclerView.NO_POSITION) {

            mRecyclerView.smoothScrollToPosition(mPosition);
        }


        if(data.getCount() != 0) {
        data.moveToPosition(0);
        CITY_NAME_SNACKBAR = data.getString(8);}

        if (data.getPosition() == 0) {
            getActivity().supportStartPostponedEnterTransition();
        } else {
            mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (mRecyclerView.getChildCount() > 0) {
                        mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                        if ( RecyclerView.NO_POSITION == mPosition ) mPosition = 0;
                        RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForAdapterPosition(mPosition);
                        if ( null != vh && mAutoSelectView){
                            mForecastAdapter.selectView(vh);
                        }
                        if (mHoldForTransition){
                            getActivity().supportStartPostponedEnterTransition();
                        }
                    }

                    return true;
                }
            });
        }


        updateEmptyView();
//        makeSnackBar();




    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != RecyclerView.NO_POSITION ) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        mForecastAdapter.onSaveInstanceState(outState);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);

        super.onResume();
    }

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_location_status_key))){
            updateEmptyView();
        }


    }

    public interface Callbacks {
        public void onItemSelected(int listItemPos, ForecastAdapter.ForecastAdapterViewHolder vh);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_forecastfragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement



        if (id == R.id.action_map) {
            openPreferredLocationInMap();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateEmptyView(){
        if (mForecastAdapter.getItemCount() == 0) {

            if (null != emptyView) {
                int message = R.string.empty_forecast_list;
                @SunshineSyncAdapter.LocationStatus int location = Utility.getLocationStatus(getActivity());
                switch (location) {
                    case SunshineSyncAdapter.LOCATION_STATUS_SERVER_DOWN:
                        message = R.string.empty_forecast_list_server_down;
                        break;
                    case SunshineSyncAdapter.LOCATION_STATUS_SERVER_INVALID:
                        message = R.string.empty_forecast_list_server_error;
                        break;
                    case SunshineSyncAdapter.LOCATION_STATUS_INVALID:
                        message = R.string.empty_forecast_list_invalid_location;
                        break;
                    default:
                        if (!Utility.isNetworkAvailable(getActivity())) {
                            message = R.string.empty_forecast_list_no_network;
                        }
                }


                emptyView.setText(message);
            }
        }
    }



    public void makeSnackBar() {

        //By adding extra check of mForecastAdapter.getCount() == 14 we can ensure that snackbar pops up only once when the cursor is fully loaded.
        //otherwise it will pop up twice or thrice untill the cursor is fully loaded which might be bit distracting.
        if (mForecastAdapter.getItemCount() != 0 && mForecastAdapter.getItemCount() == 14) {
//            String location = Utility.getPreferredLocation( getActivity() );
            if (Utility.isNetworkAvailable(getActivity())){
                Snackbar.make(getView(), "Weather in " + " " + CITY_NAME_SNACKBAR, Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecyclerView != null) {
            mRecyclerView.clearOnScrollListeners();
        }
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ForecastFragment,
                0, 0);
        mChoiceMode = a.getInt(R.styleable.ForecastFragment_android_choiceMode, AbsListView.CHOICE_MODE_NONE);
        mAutoSelectView = a.getBoolean(R.styleable.ForecastFragment_autoSelectView, false);
        mHoldForTransition = a.getBoolean(R.styleable.ForecastFragment_sharedElementTransitions, false);
        a.recycle();
    }
}





