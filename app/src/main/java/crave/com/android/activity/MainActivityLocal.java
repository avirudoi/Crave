package crave.com.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import crave.com.android.R;
import crave.com.android.adapter.CustomListViewAdapter;
import crave.com.android.adapter.SwipeFlingAdapterView;
import crave.com.android.bll.PermissionHelper;
import crave.com.android.db.BusinessDataSource;
import crave.com.android.db.PreferencesManager;
import crave.com.android.helper.DineHookConstants;
import crave.com.android.helper.InternetHelper;
import crave.com.android.model.Businesses;
import crave.com.android.model.ResultArrayModel;
import crave.com.android.network.FetchData;
import crave.com.android.views.NiceSpinner;
import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class MainActivityLocal extends DrawerActivity implements OnLocationUpdatedListener,
        OnActivityUpdatedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private  DataCall task = new DataCall();

    CustomListViewAdapter adapter;

    private Context mContext;

    public TextView tvLocation;

    private double stringLatitude, stringLongitude, nStringLatitude, nStringLongitude;

    private LocationGooglePlayServicesProvider provider;

    ArrayList<String> imageUrl = new ArrayList<>();

    ArrayList<Businesses> objectList = new ArrayList<>();

    ImageView placeImage;

    //data type for first location 0 or 1
    int dataType = 0;

    ProgressBar progressBar;

    TextView tvNoPlacesText;

    SwipeFlingAdapterView flingContainer;

    private boolean locationPermission;

    private boolean helperLayout = true;

    private String type = "dinner";

    private String radios = "1000";

    HashMap<String,Businesses> hAllPlaces;

    private EditText filterText = null;
    ArrayAdapter<String> adapterSearch;
    ListView search_list;

    private ImageView ivFilterIconOpen, ivFilterIconClose;

    private LinearLayout llSearch_area;

    private LinearLayout distance_bar_area;

    private DiscreteSeekBar radius_seekbar;

    private TextView tvDistanceNumber, tvDistanceNumberClose, tvMiles_1,tvMiles_2;

    private LinearLayout llDistanceAreaOpen, llDistanceAreaClose, llseekBar_background;

    private LinearLayout llHelperLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();


        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        distance_bar_area = (LinearLayout) findViewById(R.id.distance_bar_area);
        distance_bar_area.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        placeImage = new ImageView(this);


        tvNoPlacesText = (TextView) findViewById(R.id.tvNoPlacesText);
        tvNoPlacesText.setVisibility(View.GONE);


        tvLocation = (TextView) findViewById(R.id.tvLocation);


        if (InternetHelper.isNetworkAvailable(MainActivityLocal.this)) {
            if(Build.VERSION.SDK_INT >=23){

                 if(PermissionHelper.Instance.isLocationPermissionEnabled(MainActivityLocal.this)){
                         startLocation();
                    }else{
                        PermissionHelper.Instance.onRequestLocationPermission(MainActivityLocal.this, locationPermission);
                    }
            }else{
                startLocation();
            }
        }else{
            Toast.makeText(MainActivityLocal.this, DineHookConstants.STRING_NO_INTERNET, Toast.LENGTH_LONG).show();
        }

            filterSpinner();


        nav_view.findViewById(R.id.layout_activate_favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent favorite = new Intent(MainActivityLocal.this, FavoritePageActivity.class);
                setRegularHamburger();
                favorite.putExtra("lat",stringLatitude);
                favorite.putExtra("long",stringLongitude);
                startActivity(favorite);
            }
        });


        // add listview with filer edit text
        final String[] messages = {"Breakfast", "Lunch", "Dinner","Brunch", "Japanese","Asian", "Chinese","Thai","Bar and Grill","Mexican","Vegetarian","Mediterranean", "Italian", "American", "French",
                "Sandwiches","Soups","Ice Cream", "Acai", "Asian Buffet", "Bakery", "BBQ", "Brazilian","Buffet","Brewery",
        "Burgers","Boba","Cuban","Coffee Shop","Chinese","Chicken n Waffles","Donuts","Fast Food","Fish","Frozen Yogurt","Greek",
        "Hawaiian","Juice Bar","Indian", "Jamaican", "Lebanese", "Macaroons", "Poke'","Panini", "Pizza", "Pub", "Ramen", "Seafood", "Smoothies", "Spanish",
        "Sushi", "Vietnamese", "Vegan", "Wine Bar", "Wings"};

        search_list = (ListView) findViewById(R.id.search_list);
        filterText = (EditText) findViewById(R.id.search_bar);
        llSearch_area = (LinearLayout) findViewById(R.id.llSearch_area);

        ivFilterIconOpen = (ImageView) findViewById(R.id.ivFilterIconOpen);
        ivFilterIconClose = (ImageView) findViewById(R.id.ivFilterIconClose);
        ivFilterIconClose.setVisibility(View.GONE);
        radius_seekbar = (DiscreteSeekBar) findViewById(R.id.radius_seekbar);
        llseekBar_background = (LinearLayout) findViewById(R.id.llseekBar_background);
        llseekBar_background.setVisibility(View.GONE);

        llDistanceAreaOpen = (LinearLayout) findViewById(R.id.llDistanceAreaOpen);
        llDistanceAreaClose = (LinearLayout) findViewById(R.id.llDistanceAreaClose);
        llDistanceAreaClose.setVisibility(View.GONE);

        llDistanceAreaClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llseekBar_background.setVisibility(View.GONE);
                llDistanceAreaClose.setVisibility(View.GONE);
                llDistanceAreaOpen.setVisibility(View.VISIBLE);
            }
        });

        llDistanceAreaOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llseekBar_background.setVisibility(View.VISIBLE);
                llDistanceAreaClose.setVisibility(View.VISIBLE);
                llDistanceAreaOpen.setVisibility(View.GONE);
            }
        });

        tvDistanceNumber = (TextView) findViewById(R.id.tvDistanceNumber);
        tvDistanceNumberClose = (TextView) findViewById(R.id.tvDistanceNumberClose);
        tvMiles_1 = (TextView) findViewById(R.id.tvMiles_1);
        tvMiles_2 = (TextView) findViewById(R.id.tvMiles_2);
        tvMiles_1.setText("Mile");
        tvMiles_2.setText("Mile");


        tvDistanceNumber.setText( String.valueOf((Integer.parseInt(radios)/(1000))));
        tvDistanceNumberClose.setText( String.valueOf((Integer.parseInt(radios)/(1000))));


        llSearch_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterText.setFocusable(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(filterText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        ivFilterIconOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radius_seekbar.setVisibility(View.VISIBLE);
                ivFilterIconClose.setVisibility(View.VISIBLE);
                ivFilterIconOpen.setVisibility(View.GONE);
            }
        });

        ivFilterIconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radius_seekbar.setVisibility(View.GONE);
                ivFilterIconClose.setVisibility(View.GONE);
                ivFilterIconOpen.setVisibility(View.VISIBLE);
            }
        });

        if(filterText.isFocused()){
            search_list.setVisibility(View.VISIBLE);
        }else {
            search_list.setVisibility(View.GONE);
        }

        radius_seekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                radius_seekbar.setOnProgressChangeListener(seekBarListener);
                return false;
            }
        });

        adapterSearch = new ArrayAdapter<String>(MainActivityLocal.this, android.R.layout.simple_list_item_1, messages);
        search_list.setTextFilterEnabled(true);
        search_list.setAdapter(adapterSearch);

        filterText.addTextChangedListener(filterTextWatcher);

        filterText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchMethed(filterText.getText().toString());
                    search_list.setVisibility(View.GONE);
                    hideSoftKeyboard();
                    return true;
                }
                return false;
            }
        });

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchMethed(adapterSearch.getItem(position));
                search_list.setVisibility(View.GONE);
                hideSoftKeyboard();
            }
        });

        llHelperLayout = (LinearLayout) findViewById(R.id.llHelperLayout);
        llHelperLayout.setVisibility(View.GONE);

        nav_view.findViewById(R.id.layout_helper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                llHelperLayout.setVisibility(View.VISIBLE);
            }
        });

        llHelperLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llHelperLayout.setVisibility(View.GONE);
            }
        });

    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(count == 0){
                search_list.setVisibility(View.GONE);
            }else {
                search_list.setVisibility(View.VISIBLE);
            }

            adapterSearch.getFilter().filter(s);
        }

    };

    private void searchMethed(String word){
        type = word;
        filterText.setText(type);
        filterText.setSelection(filterText.getText().length());
        if (InternetHelper.isNetworkAvailable(MainActivityLocal.this)) {
            task = new DataCall();

            if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                task.cancel(true);
            }
            task.execute(word,radios);
        } else {
            Toast.makeText(MainActivityLocal.this, DineHookConstants.STRING_NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    private void searchByDistanceMethed(String radios){
        if (InternetHelper.isNetworkAvailable(MainActivityLocal.this)) {
            task = new DataCall();

            if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                task.cancel(true);
            }
            task.execute(type,radios);
        } else {
            Toast.makeText(MainActivityLocal.this, DineHookConstants.STRING_NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }



    private final DiscreteSeekBar.OnProgressChangeListener seekBarListener = new DiscreteSeekBar.OnProgressChangeListener() {
        @Override
        public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
            String progress;

            radios = String.valueOf(value * 1000);
        }

        @Override
        public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            tvDistanceNumber.setText(String.valueOf((Integer.parseInt(radios)/(1000))));
            tvDistanceNumberClose.setText( String.valueOf((Integer.parseInt(radios)/(1000))));
            searchByDistanceMethed(radios);

            if(radios.equals("1000")){
                tvMiles_1.setText("Mile");
                tvMiles_2.setText("Mile");
            }else {
                tvMiles_1.setText("Miles");
                tvMiles_2.setText("Miles");
            }

        }
    };




    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }


    public void setImageAdapter(final ArrayList<Businesses> object){


        adapter = null;
        adapter = new CustomListViewAdapter(mContext,R.layout.item,object);

        ImageButton right = (ImageButton) findViewById(R.id.right);

        ImageButton left = (ImageButton) findViewById(R.id.left);

        //set the listener and the adapter
        flingContainer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        flingContainer.refreshAdapter();
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                //Log.d("LIST", "removed object!");
                object.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
               /* Toast.makeText(mContext, "Left!", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                setNotifiHamburger();
                if(BusinessDataSource.Instance.getOneBussnies(((Businesses) dataObject).getPlaceId()) == null){
                    BusinessDataSource.Instance.saveBusiness((Businesses) dataObject);
                }
               /* Toast.makeText(mContext, "Right!", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                //imageList.add("XML ".concat(String.valueOf(i)));
                adapter.notifyDataSetChanged();
                if (itemsInAdapter == 0) {
                    if (InternetHelper.isNetworkAvailable(MainActivityLocal.this)) {
                        getLocation(stringLatitude, stringLongitude, 5000);
                       // Log.d("newLocation", "old location lat " + stringLatitude + "long " + stringLongitude + "new lat " + nStringLatitude + " new long " + nStringLongitude);
                        tvNoPlacesText.setVisibility(View.GONE);
                        task = new DataCall();

                        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                            task.cancel(true);
                        }
                        task.execute(type,radios);
                    } else {
                        Toast.makeText(MainActivityLocal.this, DineHookConstants.STRING_NO_INTERNET, Toast.LENGTH_LONG).show();

                    }
                }
               // Log.d("LIST", "notified");
        }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });

        if (InternetHelper.isNetworkAvailable(MainActivityLocal.this)) {
        // Optionally add an OnItemClickListener
            flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
                @Override
                public void onItemClicked(int itemPosition, Object dataObject) {
                    Intent placeIntent = new Intent(MainActivityLocal.this, PlacePageActivity.class);
                    placeIntent.putExtra("lat",stringLatitude);
                    placeIntent.putExtra("long",stringLongitude);
                    placeIntent.putExtra("businesses", (Parcelable) dataObject);
                    MainActivityLocal.this.startActivity(placeIntent);
                }
            });
        }else {
            Toast.makeText(MainActivityLocal.this, DineHookConstants.STRING_NO_INTERNET, Toast.LENGTH_LONG).show();
        }

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                * Trigger the right event manually.
                        */
                flingContainer.getTopCardListener().selectRight();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                * Trigger the right event manually.
                        */
                flingContainer.getTopCardListener().selectLeft();
            }
        });



    }

    public void filterSpinner(){
        NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        final List<String> messages = Arrays.asList("Breakfast", "Lunch", "Dinner","Brunch", "japanese","Asian", "Chinese","Thai","bar and grill","Mexican","vegetarian","Mediterranean", "italian", "American", "French",
        "sandwiches","Soups","Ice Cream");
        niceSpinner.attachDataSource(messages);
        niceSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (InternetHelper.isNetworkAvailable(MainActivityLocal.this)) {
                    type = messages.get(position);
                    task = new DataCall();

                    if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                        task.cancel(true);
                    }
                    task.execute(type,radios);
                } else {
                    Toast.makeText(MainActivityLocal.this, DineHookConstants.STRING_NO_INTERNET, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Log.d("aviTag", "onActivityResult, requestCode: " + requestCode + ", resultCode: " + resultCode);

       *//* if (provider != null) {
            provider.onActivityResult(requestCode, resultCode, data);
        }*//*

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Log.i("aviTag", "activity pass ");
            }
        }
    }*/


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i< permissions.length ; i++){

            String permission = permissions[i];
            int grandResult = grantResults[i];

            if(permission.equals(android.Manifest.permission.ACCESS_FINE_LOCATION)){
                if (grandResult == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startLocation();
                    return;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d("aviTag", "onActivityResult, requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (requestCode == RESULT_OK) {
            String stredittext=data.getStringExtra("edittextvalue");
            Log.d("aviTag", "onActivityResult, requestCode: " + requestCode + ", resultCode: " + resultCode);
        }
    }




    private ResultArrayModel fetchData(String finalType){
        dataType = 0;
        FetchData fetch = new FetchData(FetchData.OAUTH_CONSUMER_KEY,FetchData.OAUTH_CONSUMER_SECRET,FetchData.OAUTH_CONSUMER_TOKEN,FetchData.OAUTH_CONSUMER_TOKEN_SECRET);
        String response = fetch.serverCall(MainActivityLocal.this, stringLatitude, stringLongitude, finalType,radios);
        Gson gson = new Gson();
        ResultArrayModel object = gson.fromJson(response, ResultArrayModel.class);
        dataType = 1;
        return object;
    }

    private ResultArrayModel fetchDataNew(String finalType){
        dataType = 1;
        FetchData fetch = new FetchData(FetchData.OAUTH_CONSUMER_KEY,FetchData.OAUTH_CONSUMER_SECRET,FetchData.OAUTH_CONSUMER_TOKEN,FetchData.OAUTH_CONSUMER_TOKEN_SECRET);
        String response = fetch.serverCall(MainActivityLocal.this, nStringLatitude, nStringLongitude, finalType,radios);
        Gson gson = new Gson();
        ResultArrayModel object = gson.fromJson(response, ResultArrayModel.class);
        return object;
    }

    public void getLocation(double x0, double y0, int radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(y0);

        nStringLatitude = new_x + x0;
        nStringLongitude = y + y0;
        //System.out.println("Longitude: " + foundLongitude + "  Latitude: " + foundLatitude );
    }

    private void startLocation() {
        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);
        smartLocation.activity().start(this);

    }

    private void stopLocation() {
        SmartLocation.with(this).location().stop();

        SmartLocation.with(this).activity().stop();
    }

    private void showLocation(Location location) {
        if (location != null) {

            stringLatitude =  location.getLatitude();

            stringLongitude = location.getLongitude();


          /*  final String text = String.format("Latitude %.6f, Longitude %.6f",
                    location.getLatitude(),
                    location.getLongitude());
            tvLocation.setText(text);*/
            // We are going to get the address for the current position
            SmartLocation.with(MainActivityLocal.this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                       /* StringBuilder builder = new StringBuilder(text);
                        builder.append("\n[Reverse Geocoding] ");
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));*/
                       /* tvLocation.setText(builder.toString());*/
                        task.execute(type,radios);
                        //stopLocation();
                    }
                }
            });
        } else {
            tvLocation.setText("Null location");
        }
//        SmartLocation.with(this).location().stop();

    }

/*    private String getNameFromType(DetectedActivity activityType) {
        switch (activityType.getType()) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.TILTING:
                return "tilting";
            default:
                return "unknown";
        }
    }*/

    private void showActivity(DetectedActivity detectedActivity) {
       /* if (detectedActivity != null) {
            tvLocation.setText(
                    String.format("Activity %s with %d%% confidence",
                            getNameFromType(detectedActivity),
                            detectedActivity.getConfidence())
            );
        } else {
            tvLocation.setText("Null activity");
        }*/
    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);

    }

    @Override
    public void onStop() {
        super.onStop();
        SmartLocation.with(MainActivityLocal.this).location().stop();
        SmartLocation.with(MainActivityLocal.this).geocoding().stop();
        Log.i("aviTag", "on stop ");
    }

    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {
        showActivity(detectedActivity);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    /*
* AsyncTask class to retrieve the data from the URL
* */
    private class DataCall extends AsyncTask<String,Void,ResultArrayModel> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            flingContainer.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected ResultArrayModel doInBackground(String... params) {

            if(dataType == 0){
                String mType = params[0];
                String radios = params[1];
                return fetchData(mType);
            }
                String mType = params[0];
                String radios = params[1];
                return fetchDataNew(mType);
        }
        /*
        * Set up view widgets when data is accessible
        * */
        @Override
        protected void onPostExecute(ResultArrayModel feed) {

            if(objectList == null || objectList.size()>0){
                objectList = null;
                objectList = new ArrayList<>();
            }

           /* if(hAllPlaces == null){
                hAllPlaces = new HashMap<>();
            }*/
            HashMap<String,Businesses> temp = new HashMap<>();
            if(feed.getFeedData() != null && feed.getFeedData().length>0){
                for(int k = 0; k <feed.getFeedData().length; k++){
                    objectList.add(feed.getFeedData()[k]);
                    //temp.put(feed.getFeedData()[k].getPlaceId(),feed.getFeedData()[k]);

                }
                //temp.keySet().removeAll(hAllPlaces.keySet());
                //hAllPlaces.putAll(temp);
               // hAllPlaces.put(feed.getFeedData()[k].getPlaceId(),feed.getFeedData()[k]);
              /*  for(int k = 0; k<temp.size(); k++){
                    if(!hAllPlaces.containsKey(temp.get(k))) {
                        Businesses bs= temp.get(k);
                        objectList.add(bs);
                    }
                }*/
                Collections.shuffle(objectList);
            }

            if(imageUrl != null){
                setImageAdapter(objectList);
                flingContainer.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);

            if(PreferencesManager.Instance.retrieveValue(Boolean.class, "helper", "homePageHelper", helperLayout) == true){
                llHelperLayout.setVisibility(View.VISIBLE);
                helperLayout = false;
                PreferencesManager.Instance.storeKeyValue(Boolean.class, "helper", "homePageHelper", helperLayout);
            }





            stopLocation();
        }
    }

}
