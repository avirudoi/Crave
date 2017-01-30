package crave.com.android.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import crave.com.android.R;
import crave.com.android.face.MainPlaceInterface;
import crave.com.android.fragment.MenuPageFragment;
import crave.com.android.fragment.PlacePageFragment;
import crave.com.android.model.Businesses;

/**
 * Created by avirudoi on 2/22/16.
 */
public class PlacePageActivity extends AppCompatActivity implements MainPlaceInterface {

    private FragmentManager fm;

    PlacePageFragment placePageFragment;
    MenuPageFragment menuPageFragment;

    Businesses businesses;

    double mMyLat, mMyLong;



    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);

        setContentView(R.layout.place_activity);

        Intent i = getIntent();
        if (i != null) {
            businesses = (Businesses) i.getParcelableExtra("businesses");
            mMyLat = i.getDoubleExtra("lat",0);
            mMyLong = i.getDoubleExtra("long",0);

            // Using db id's for better design purposes

        }else{
            finish();
            Toast.makeText(this,"some error happned", Toast.LENGTH_LONG);
        }

        displayPlacePagesMainFragment();
    }

    @Override
    public void displayPlacePagesMainFragment() {

        Bundle args = new Bundle();
        args.putString("imageUrl", businesses.getHighResImage());
        args.putString("name",businesses.getTitle());
        args.putString("snippet",businesses.getSnippetText());
        if(businesses.getLocation() != null){
            args.putString("city",businesses.getLocation().getCity());
            args.putDouble("lat", businesses.getLocation().getCoordinate().getLatitude());
            args.putDouble("long", businesses.getLocation().getCoordinate().getLongitude());
            args.putString("address",businesses.getLocation().getAddress());
        }else {
            args.putString("city",businesses.getCity());
            args.putDouble("lat", businesses.getLatitude());
            args.putDouble("long", businesses.getLongitude());
            args.putString("address",businesses.getAddress());
        }

        args.putString("imageRating", businesses.getRatingImgUrl());
        args.putString("snippet",businesses.getSnippetText());
        args.putInt("countNumber", businesses.getCountNumber());
        args.putString("phone", businesses.getPhone());

        args.putDouble("mylat", mMyLat);
        args.putDouble("mylong", mMyLong);

        args.putParcelable("bussnies", businesses);

        placePageFragment = new PlacePageFragment();
        placePageFragment.setArguments(args);
        fm = getFragmentManager();
        fm.beginTransaction()
                .add(R.id.top_fragment_container, placePageFragment,
                        "place_page_fragment")//.addToBackStack(null)
                .commit();
    }

    public void callActivityTest(){
        finish();
    }

    @Override
    public void replacePlacePagesMainFragment() {

        Bundle args = new Bundle();
        args.putString("imageUrl", businesses.getHighResImage());
        args.putString("name",businesses.getTitle());
        args.putString("snippet",businesses.getSnippetText());
        if(businesses.getLocation() != null){
            args.putString("city",businesses.getLocation().getCity());
            args.putDouble("lat", businesses.getLocation().getCoordinate().getLatitude());
            args.putDouble("long", businesses.getLocation().getCoordinate().getLongitude());
            args.putString("address",businesses.getLocation().getAddress());
        }else {
            args.putString("city",businesses.getCity());
            args.putDouble("lat", businesses.getLatitude());
            args.putDouble("long", businesses.getLongitude());
            args.putString("address",businesses.getAddress());
        }

        args.putString("imageRating", businesses.getRatingImgUrl());
        args.putString("snippet",businesses.getSnippetText());
        args.putInt("countNumber", businesses.getCountNumber());
        args.putString("phone", businesses.getPhone());

        args.putDouble("mylat", mMyLat);
        args.putDouble("mylong", mMyLong);

        placePageFragment = new PlacePageFragment();
        placePageFragment.setArguments(args);
        fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.top_fragment_container, placePageFragment,
                        "place_page_fragment")//.addToBackStack(null)
                .commit();
    }

    public void displayMenuPageFragment(String placeId){
        Bundle args = new Bundle();
        args.putString("placeId",placeId);
        menuPageFragment = new MenuPageFragment();
        menuPageFragment.setArguments(args);

        if(fm == null){
            fm = getFragmentManager();
        }
        fm.beginTransaction()
                .replace(R.id.top_fragment_container, menuPageFragment,
                        "menu_Page_Fragment").addToBackStack("place_page_fragment")
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
