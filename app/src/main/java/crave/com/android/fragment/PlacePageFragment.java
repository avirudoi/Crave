package crave.com.android.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import crave.com.android.R;
import crave.com.android.db.BusinessDataSource;
import crave.com.android.face.MainPlaceInterface;
import crave.com.android.helper.GlideHelper;
import crave.com.android.helper.HelperMethod;
import crave.com.android.helper.PicassoHelper;
import crave.com.android.helper.WidgetHelper;
import crave.com.android.model.Businesses;
import crave.com.android.model.ResultArrayLuca;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by avirudoi on 2/22/16.
 */
public class PlacePageFragment extends Fragment implements OnMapReadyCallback {

    MainPlaceInterface mainPlaceInterface;
    ImageView ivplaceImage;
    ImageButton ibCall, ibDirections, ibFavorites;
    String mImageUrl, mName, mSnippet, mCity, mRatingImage , mPhone;

    int mReviewCount;

    public double lat, longtitude, myLat, myLong;

    public static final String SECTION_TEXT  = "SECTION_TEXT";

    public static final String TYPE_DESCRIPTION = "typeDescription";

    Button btMenuButton;

    ImageView ivRatingImage;

    private ProgressBar progressBar;

    MapView mMapView;

    MapFragment mapFragment;

    GoogleMap map;

    TextView tvAddress;

    String address;

    private Businesses businesses;

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof MainPlaceInterface){
            mainPlaceInterface=(MainPlaceInterface) context;
        }
    }

    /*
 * Deprecated on API 23
 * Use onAttachToContext instead
 */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        if (activity instanceof MainPlaceInterface){
            mainPlaceInterface=(MainPlaceInterface) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Bundle extras = getArguments();
        if (extras != null) {

            businesses = extras.getParcelable("bussnies");

            mImageUrl = (String) extras.get("imageUrl");
            mName = (String) extras.get("name");
            mSnippet = (String) extras.get("snippet");
            mCity = (String) extras.get("city");
            mPhone = (String) extras.get("phone");
            mRatingImage = (String) extras.get("imageRating");
            mReviewCount = (int) extras.get("countNumber");
            lat = (double) extras.get("lat");
            longtitude  = (double) extras.get("long");
            myLat = (double) extras.get("mylat");
            myLong  = (double) extras.get("mylong");
            address = (String) extras.get("address");

        }

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.place_page_main, container,
                    false);

            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");

            bindWidgetView(rootView, savedInstanceState);
            //progressBar.setVisibility(View.VISIBLE);
            displayView();
            fetchData(mName, mCity);

            return rootView;
        }

        public  void bindWidgetView(View view, Bundle save){

            ivRatingImage = (ImageView)view.findViewById(R.id.ivRatingImage);
            mMapView = (MapView) view.findViewById(R.id.map);
            // Need to call MapsInitializer before doing any CameraUpdateFactory calls
            MapsInitializer.initialize(this.getActivity());
            mMapView.onCreate(save);
            mMapView.getMapAsync(this);



           // updateMarkerPlace();

            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            btMenuButton = (Button) view.findViewById(R.id.btMenuButton);
            btMenuButton.setVisibility(View.GONE);
            ivplaceImage = (ImageView)view.findViewById(R.id.ivplaceImage);

            tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            WidgetHelper.setSafeText((TextView) view.findViewById(R.id.tvTitle), mName);
            WidgetHelper.setSafeText((TextView) view.findViewById(R.id.tvNumberOfReviews), mReviewCount + " Reviews");
            WidgetHelper.setSafeText((TextView) view.findViewById(R.id.tvDistance), HelperMethod.Instance.returnDistance(myLat,myLong,lat,longtitude) + " mi.");
            WidgetHelper.setSafeText((TextView) view.findViewById(R.id.tvSnippetText), mSnippet);
            ibCall = (ImageButton)view.findViewById(R.id.ibCall);
            ibDirections = (ImageButton)view.findViewById(R.id.ibDirections);
            ibFavorites = (ImageButton)view.findViewById(R.id.ibFavorites);

            if(businesses != null){
                if(BusinessDataSource.Instance.getOneBussnies(businesses.getPlaceId()) == null){
                    ibFavorites.setImageResource(R.mipmap.ic_un_favorite);
                }else {
                    ibFavorites.setImageResource(R.mipmap.ic_favorite);
                }
            }
            setView();
        }

    public void setView(){

        tvAddress.setText(address);

        ibCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mPhone));
                startActivity(intent);
            }
        });

        ibDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + myLat+ "," + myLong + "&daddr= " + lat + "," + longtitude));
                startActivity(intent);
            }
        });

        ibFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BusinessDataSource.Instance.getOneBussnies(businesses.getPlaceId()) == null){
                    ibFavorites.setImageResource(R.mipmap.ic_favorite);
                    BusinessDataSource.Instance.saveBusiness(businesses);
                    Toast.makeText(getActivity(),"place added to your favorite", Toast.LENGTH_SHORT).show();
                }else {
                    ibFavorites.setImageResource(R.mipmap.ic_un_favorite);
                    BusinessDataSource.Instance.removeFromFavorite(businesses.getPlaceId());
                    getActivity().finish();
                    Toast.makeText(getActivity(),"place removed from your favorite", Toast.LENGTH_SHORT).show();
                }
                //mainPlaceInterface.callActivityTest();
            }
        });
    }

    public void updateMarkerPlace(){

        LatLng markerPlace = new LatLng(lat, longtitude);

        map.addMarker(new MarkerOptions()
                .position(markerPlace)
                .draggable(true));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPlace, 15));
    }


    //data call object
    private void fetchData(String name, String locality){

        RestAdapter.Builder adapterBuilder = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://api.locu.com").setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("api_key", "c04f4ff7ccc91b8634a22d90b468379165a4dc0a");
                    }
                });
        GetLucaPage service = adapterBuilder.build().create(GetLucaPage.class);
        service.getLucaMain(name, locality, new Callback<ResultArrayLuca>() {
            @Override
            public void success(ResultArrayLuca resultArrayLuca, Response response) {
                progressBar.setVisibility(View.GONE);
                if(resultArrayLuca.getLucaData().length>0){
                    onSuccessPlaceObject(resultArrayLuca);
                }else{

                }
            }

            @Override
            public void failure(RetrofitError error) {
                //Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public void onSuccessPlaceObject(ResultArrayLuca object){

        if( object.getLucaData()[0].isHasMenu() == true){
            btMenuButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            if(object.getLucaData()[0].getPlaceId() != null){
                openMenuButton (object.getLucaData()[0].getPlaceId());
            }else {
                btMenuButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        }else {
            btMenuButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(false);

        LatLng markerPlace = new LatLng(lat, longtitude);

        googleMap.addMarker(new MarkerOptions()
                .position(markerPlace)
                .draggable(true));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPlace, 15));

    }


    public interface GetLucaPage{
        @GET("/v1_0/venue/search/")
        void getLucaMain(@Query("name") String name, @Query("locality") String city, Callback<ResultArrayLuca> Response);

        void getLucaPlaceId(@Query("name") String name, @Query("locality") String city, Callback<ResultArrayLuca> Response);
    }

        public void displayView(){
            PicassoHelper.Instance.displayRatingImage(mRatingImage,getActivity(),ivRatingImage);

            if(mImageUrl != null){
                GlideHelper.Instance.displayMainImage(mImageUrl, getActivity(), ivplaceImage);
            }
        }

    public void openMenuButton (final String placeId){

        btMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainPlaceInterface != null) {
                    mainPlaceInterface.displayMenuPageFragment(placeId);
                }else {
                    Toast.makeText(getActivity(),"some error happened try again later", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
