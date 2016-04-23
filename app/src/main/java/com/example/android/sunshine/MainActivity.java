package com.example.android.sunshine;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.sunshine.gcm.RegistrationIntentService;
import com.example.android.sunshine.sync.SunshineSyncAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements ForecastFragment.Callbacks {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();




    private boolean isTwoPane = false;
    private String mLocation;
    private boolean defaultUnit;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    SQLiteDatabase db;
    Cursor c;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    private static long back_pressed;
    private View mView;













    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private AppBarLayout mAppBar;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        getSupportActionBar().setElevation(0);
        SunshineSyncAdapter.initializeSyncAdapter(getApplicationContext());


//        Work around to set elevation of appbar on vertical offset or scroll.


        mAppBar = (AppBarLayout) findViewById(R.id.appbar);







        if (checkPlayServices()) {
            // Because this is the initial creation of the app, we'll want to be certain we have
            // a token. If we do not, then we will start the IntentService that will register this
            // application with GCM.
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this);
            boolean sentToken = sharedPreferences.getBoolean(SENT_TOKEN_TO_SERVER, false);
            if (!sentToken) {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }


        }





//        toolbar.setLogo(R.drawable.ic_logo);



        mLocation = Utility.getPreferredLocation(this);
        defaultUnit = Utility.isMetric(this);

        if (findViewById(R.id.forecast_fragment_container) != null) {
            isTwoPane = true;

            if (savedInstanceState == null) {
                Bundle b = new Bundle();
                b.putInt("listItemPos", 0);
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(b);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.forecast_fragment_container, detailFragment, DETAILFRAGMENT_TAG)
                        .commit();
            }

        }else{
            isTwoPane = false;



            ForecastFragment ff = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMain);
            ff.mForecastAdapter.setUseTodayLayout(true);

//            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    updateWeather();
//
//
//                    Snackbar.make(view, "Refreshing Weather Data", Snackbar.LENGTH_SHORT)
//                            .setAction("Action", null).show();

//
//                }
//            });
        }










//        getFragmentManager().beginTransaction()
//                .add(R.id.fragContainer, mainFrag)
//                .commit();





        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void updateWeather() {

//        weather = new FetchWeatherTask(getApplicationContext(), this);
        SunshineSyncAdapter.syncImmediately(getApplicationContext());


//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_defaultValue));
//
//        Intent alarmIntent = new Intent(this, SunshineService.AlarmReceiver.class);
//        alarmIntent.putExtra(SunshineService.LOCATION_QUERY_EXTRA, location);
//
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
//        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pi);
//
//        Intent intent = new Intent(this, SunshineService.class);
//        intent.putExtra(SunshineService.LOCATION_QUERY_EXTRA, location);
////        weather.execute(location);
//        this.startService(intent);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));

            return true;
        }





        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onStart() {
        super.onStart();
//        updateWeather();






        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.android.sunshine/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.android.sunshine/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();

    }

    @Override
    protected void onResume() {
        super.onResume();


        if(mAppBar != null && !isTwoPane) {
            mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                        if (verticalOffset == 0) {
                            mAppBar.setElevation(0);
                        } else {
                            mAppBar.setElevation(mAppBar.getTargetElevation());
                        }
                    }
                }
            });
        }

        String location = Utility.getPreferredLocation(this);
//
        ForecastFragment ff = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMain);
////        if(defaultUnit != Utility.isMetric(this)){
////            ff.getLoaderManager().restartLoader(0, null, ff);
////        }
//        // update the location in our second pane using the fragment manager
//
//
//
//
        if (location != null && !location.equals(mLocation)) {
////            updateWeather();
//
////            String[] selectionArgs = {"Kanpur"};
////
////            WeatherDbHelper dbHelper = new WeatherDbHelper(this);
////
////            db = dbHelper.getReadableDatabase();
////
////            db.delete(WeatherContract.LocationEntry.TABLE_NAME, WeatherContract.LocationEntry.COLUMN_CITY_NAME + "=?", selectionArgs);
////
////            Log.d("DATABASE DELETED", "Table Deleted");
//
//
//            Bundle b = new Bundle();
//            b.putInt("listItemPos", ff.mPosition);
            if (null != ff) {
                ff.getLoaderManager().restartLoader(0, null, ff);

//                ListView ffListView = (ListView) findViewById(R.id.forecast_list_view);
//
//                if (isTwoPane) {
//                    ffListView.setSelection(ff.mPosition);
//
//
//                    DetailFragment detailFragment = new DetailFragment();
//                    detailFragment.setArguments(b);
////                    ffListView = (ListView) ff.getListView().findViewById(android.R.id.list);
////                    ffListView.setSelection(ff.mPosition);
//
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.forecast_fragment_container, detailFragment, DETAILFRAGMENT_TAG)
//                            .commit();
//                }
//
//
//            }
////            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
////            if ( null != df ) {
////                df.onLocationChanged(location);
////            }
//            mLocation = location;
//            Log.d("New Location", mLocation);
//        }


            }
        }
    }

//    @Override
//    public void processFinish(Cursor output) {
////        ForecastFragment mainFrag = (ForecastFragment) getFragmentManager().findFragmentById(R.id.fragmentMain);
////        mainFrag.mForecastAdapter.clear();
////        mainFrag.mForecastAdapter.addAll(output);
//
//
////        mainFrag.mForecastAdapter.swapCursor(output);
//
////        output.close();
//
////       new ForecastAdapter(getApplicationContext(), output, 0);
//
////        Bundle b = new Bundle();
////        b.putInt("listItemPos", 0);
////        DetailFragment detailFragment = new DetailFragment();
////        detailFragment.setArguments(b);
//
//
////        if (isTwoPane == true) {
////            getSupportFragmentManager().beginTransaction()
////                    .replace(R.id.forecast_fragment_container, detailFragment, DETAILFRAGMENT_TAG)
////                    .commit();
////        }
//
//
//
//        String location = Utility.getPreferredLocation( this );
//        // update the location in our second pane using the fragment manager
//        if (location != null && !location.equals(mLocation)) {
//
////            String[] selectionArgs = {"Kanpur"};
////
////            WeatherDbHelper dbHelper = new WeatherDbHelper(this);
////
////            db = dbHelper.getReadableDatabase();
////
////            db.delete(WeatherContract.LocationEntry.TABLE_NAME, WeatherContract.LocationEntry.COLUMN_CITY_NAME + "=?", selectionArgs);
////
////            Log.d("DATABASE DELETED", "Table Deleted");
//
//            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentMain);
//            Bundle b = new Bundle();
//            b.putInt("listItemPos", ff.mPosition);
//            if ( null != ff ) {
//                ff.getLoaderManager().restartLoader(0, null, ff);
//                ListView ffListView = (ListView) ff.getListView().findViewById(android.R.id.list);
//                ffListView.setSelection(ff.mPosition);
//
//                if (isTwoPane) {
//
//                    DetailFragment detailFragment = new DetailFragment();
//                    detailFragment.setArguments(b);
////                    ffListView = (ListView) ff.getListView().findViewById(android.R.id.list);
////                    ffListView.setSelection(ff.mPosition);
//
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.forecast_fragment_container, detailFragment, DETAILFRAGMENT_TAG)
//                            .commit();
//                }
//
//
//            }
////            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
////            if ( null != df ) {
////                df.onLocationChanged(location);
////            }
//            mLocation = location;
//            Log.d("New Location", mLocation);
//        }
//
//
//    }

    @Override
    public void onItemSelected(int listItemPos, ForecastAdapter.ForecastAdapterViewHolder vh) {
        Bundle b = new Bundle();
        b.putInt("listItemPos", listItemPos);
        if (isTwoPane) {


            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(b);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.forecast_fragment_container, detailFragment, DETAILFRAGMENT_TAG)
                    .commit();
        }else {


            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra("ItemLocation", b);
//            startActivity(intent);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // collect the shared elements
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this,
                        new Pair<View, String>(vh.iconView, getString(R.string.detail_icon_transition_name)));
                // has to be called on the activity, not the fragment
                this.startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }


        }
    }


//    public class fetchWeatherTask extends AsyncTask<String, Void, String[]> {
//
//        private final String LOG_TAG = fetchWeatherTask.class.getSimpleName();
//
//        private String getReadableDateString(long time) {
//            // Because the API returns a unix timestamp (measured in seconds),
//            // it must be converted to milliseconds in order to be converted to valid date.
//            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
//            return shortenedDateFormat.format(time);
//        }
//
//        /**
//         * Prepare the weather high/lows for presentation.
//         */
//        private String formatHighLows(double high, double low) {
//            // For presentation, assume the user doesn't care about tenths of a degree.
//            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            String unitType = sharedPrefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));
//
//
//            if (unitType.equals(getString(R.string.pref_units_imperial))) {
//                high = (high * 1.8) + 32;
//                low = (low * 1.8) + 32;
//            }else if (!unitType.equals(getString(R.string.pref_units_metric)))
//            {
//                Log.d(LOG_TAG, "Unit Type Not Found" +" "+ unitType);
//            }
//
//
//
//
//
//            long roundedHigh = Math.round(high);
//            long roundedLow = Math.round(low);
//
//
//
//            String highLowStr = roundedHigh + "/" + roundedLow;
//            return highLowStr;
//        }
//
//        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
//                throws JSONException {
//
//            // These are the names of the JSON objects that need to be extracted.
//            final String OWM_LIST = "list";
//            final String OWM_WEATHER = "weather";
//            final String OWM_TEMPERATURE = "temp";
//            final String OWM_MAX = "max";
//            final String OWM_MIN = "min";
//            final String OWM_DESCRIPTION = "main";
//
//            JSONObject forecastJson = new JSONObject(forecastJsonStr);
//            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
//
//            // OWM returns daily forecasts based upon the local time of the city that is being
//            // asked for, which means that we need to know the GMT offset to translate this data
//            // properly.
//
//            // Since this data is also sent in-order and the first day is always the
//            // current day, we're going to take advantage of that to get a nice
//            // normalized UTC date for all of our weather.
//
//            Time dayTime = new Time();
//            dayTime.setToNow();
//
//            // we start at the day returned by local time. Otherwise this is a mess.
//            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
//
//            // now we work exclusively in UTC
//            dayTime = new Time();
//
//            String[] resultStrs = new String[numDays];
//            for (int i = 0; i < weatherArray.length(); i++) {
//                // For now, using the format "Day, description, hi/low"
//                String day;
//                String description;
//                String highAndLow;
//
//                // Get the JSON object representing the day
//                JSONObject dayForecast = weatherArray.getJSONObject(i);
//
//                // The date/time is returned as a long.  We need to convert that
//                // into something human-readable, since most people won't read "1400356800" as
//                // "this saturday".
//                long dateTime;
//                // Cheating to convert this to UTC time, which is what we want anyhow
//                dateTime = dayTime.setJulianDay(julianStartDay + i);
//                day = getReadableDateString(dateTime);
//
//                // description is in a child array called "weather", which is 1 element long.
//                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
//                description = weatherObject.getString(OWM_DESCRIPTION);
//
//                // Temperatures are in a child object called "temp".  Try not to name variables
//                // "temp" when working with temperature.  It confuses everybody.
//                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
//                double high = temperatureObject.getDouble(OWM_MAX);
//                double low = temperatureObject.getDouble(OWM_MIN);
//
//                highAndLow = formatHighLows(high, low);
//                resultStrs[i] = day + " - " + description + " - " + highAndLow;
//            }
//
//            for (String s : resultStrs) {
//                Log.v(LOG_TAG, "Forecast entry: " + s);
//            }
//            return resultStrs;
//
//        }
//
//
//        @Override
//        protected String[] doInBackground(String... params) {
//            // These two need to be declared outside the try/catch
//            // so that they can be closed in the finally block.
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//
//            // Will contain the raw JSON response as a string.
//            String forecastJsonStr = null;
//
//            String format = "json";
//            String units = "metric";
//            int numDays = 7;
//            String appId = "291db479e4df7b65a571670ca8a2415a";
//
//            final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
//            final String QUERY_PARAM = "q";
//            final String FORMAT_PARAM = "mode";
//            final String UNITS_PARAM = "units";
//            final String DAYS_PARAM = "cnt";
//            final String ID_PARAM = "appid";
//
//
//            try {
//                // Construct the URL for the OpenWeatherMap query
//                // Possible parameters are avaiable at OWM's forecast API page, at
//                // http://openweathermap.org/API#forecast
//                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
//                        .appendQueryParameter(QUERY_PARAM, params[0])
//                        .appendQueryParameter(FORMAT_PARAM, format)
//                        .appendQueryParameter(UNITS_PARAM, units)
//                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
//                        .appendQueryParameter(ID_PARAM, appId)
//                        .build();
//
//
//                URL url = new URL(builtUri.toString());
//
//                Log.v(LOG_TAG, "BUILT URI" + " " + builtUri.toString());
//
//                // Create the request to OpenWeatherMap, and open the connection
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                // Read the input stream into a String
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//                    // Nothing to do.
//                    return null;
//                }
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                    // But it does make debugging a *lot* easier if you print out the completed
//                    // buffer for debugging.
//                    buffer.append(line + "\n");
//                }
//
//                if (buffer.length() == 0) {
//                    // Stream was empty.  No point in parsing.
//                    return null;
//
//                }
//                forecastJsonStr = buffer.toString();
//                Log.v(LOG_TAG, "Forecast Json String" + forecastJsonStr);
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Error ", e);
//                // If the code didn't successfully get the weather data, there's no point in attemping
//                // to parse it.
//                return null;
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("PlaceholderFragment", "Error closing stream", e);
//                    }
//                }
//            }
//
//            try {
//                return getWeatherDataFromJson(forecastJsonStr, numDays);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                String[] catchStr = {"error"};
//                return catchStr;
//            }
//
//        }
//
//
//        @Override
//        protected void onPostExecute(String[] result) {
//
//
//            //once the activity is created(onCreate) we can't find the fragment just like that. So we need to call it with fragment id
//            ForecastFragment mainFrag = (ForecastFragment) getFragmentManager().findFragmentById(R.id.fragment);
//            mainFrag.mForecastAdapter.clear();
//            mainFrag.mForecastAdapter.addAll(result);
//
//
//        }
//    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
//        String text = "SUCCESS CONNECTING";
//
//        if (resultCode == ConnectionResult.SUCCESS) {
//
//            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
//        }
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        mView = parent;
        return super.onCreateView(parent, name, context, attrs);

    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis() && mView != null) {
            super.onBackPressed();
        }else if(mView != null) {
            Snackbar.make(mView, "Press once again to exit!", Snackbar.LENGTH_LONG).show();
            back_pressed = System.currentTimeMillis();
        }


    }




}
