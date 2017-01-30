package crave.com.android.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import crave.com.android.R;
import crave.com.android.adapter.PhotoGridAdapter;
import crave.com.android.db.BusinessDataSource;
import crave.com.android.model.Businesses;

/**
 * Created by avirudoi on 2/22/16.
 */
public class FavoritePageActivity extends AppCompatActivity {

    private FragmentManager fm;
    public android.support.v7.app.ActionBar actionBar;
    private GridView gridView;
    private LinearLayout llEmpty;
    private Button btBrowse;

    private PhotoGridAdapter photoGridAdapter;

    private List<Businesses> favoriteList;

    private double stringLatitude, stringLongitude;

    Parcelable state;

    ImageView exit_icon;

    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);

        setContentView(R.layout.favorite_page_main);
        gridView = (GridView)findViewById(R.id.gvPhotoGallery);
        llEmpty = (LinearLayout)findViewById(R.id.llEmpty);
        btBrowse = (Button)findViewById(R.id.btBrowse);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        customeActionBar();

        if(favoriteList == null){
            favoriteList = new ArrayList<>();
        }


        Intent i = getIntent();
        if (i != null) {
            stringLatitude = i.getDoubleExtra("lat",0);
            stringLongitude = i.getDoubleExtra("long",0);

            // Using db id's for better design purposes

        }else{
            finish();
            Toast.makeText(this,"some error happned", Toast.LENGTH_LONG);
        }

        setView();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(FavoritePageActivity.this,PlacePageActivity.class);
                myIntent.putExtra("businesses", (Parcelable) checkIfPassTime().get(position));
                myIntent.putExtra("lat",stringLatitude);
                myIntent.putExtra("long",stringLongitude);
                startActivity(myIntent);
            }
        });

        exit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public List<Businesses> checkIfPassTime(){
        favoriteList = BusinessDataSource.Instance.returnListOfFavorite();
        long timeNow = System.currentTimeMillis();
        //long hours = TimeUnit.MILLISECONDS.toHours(timeNow);
        long hours   = (((timeNow / (1000*60*60)) % 24));
        for (int i = 0; i<favoriteList.size(); i ++){
            //Log.i("aviTag", "time now " + hours + "time added " + TimeUnit.MILLISECONDS.toHours(timeNow -favoriteList.get(i).getAddTime()));
            if(24<( TimeUnit.MILLISECONDS.toHours(timeNow -favoriteList.get(i).getAddTime()))){
                BusinessDataSource.Instance.removeFromFavorite(favoriteList.get(i).getPlaceId());
            }
        }

        favoriteList = BusinessDataSource.Instance.returnListOfFavorite();
        return favoriteList;
    }

    private void setView(){
        if(checkIfPassTime().size()>0){
            gridView.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
                photoGridAdapter = new PhotoGridAdapter(this,checkIfPassTime(),stringLatitude,stringLongitude);

            gridView.setAdapter(photoGridAdapter);
            if(state != null) {
                gridView.onRestoreInstanceState(state);
            }
        }else {
            gridView.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
            btBrowse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(FavoritePageActivity.this,MainActivity.class);
                    startActivity(myIntent);
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("aviTag", "saving listview state @ onPause");
        state = gridView.onSaveInstanceState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /*
 * Adds Avatar to top action bar
 * */
    private void customeActionBar() {
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflator.inflate(R.layout.favorite_actionbar, null);
        exit_icon = (ImageView) action_bar_view.findViewById(R.id.exit_icon);

        actionBar.setCustomView(action_bar_view);
    }
}
