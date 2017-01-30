package crave.com.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import crave.com.android.R;
import crave.com.android.bll.AuthManagerHelper;
import crave.com.android.db.UserDataSource;
import crave.com.android.helper.CircularImageView;
import crave.com.android.helper.PicassoHelper;


/**
 * An abstract activity class that implements the Android navigation drawer functionality.
 * <p>Classes that derive from this class will need to implement the setLayout method.</p>
 */
public abstract class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected NavigationView nav_view;
    private ActionBar actionBar;

    private static final String TAG = DrawerActivity.class.getSimpleName();

    /* The callback manager for Facebook */
    private CallbackManager mFacebookCallbackManager;

    /* Used to track user logging in/out off Facebook */
    private AccessTokenTracker mFacebookAccessTokenTracker;

    private ActionBarDrawerToggle toggle;


    private Button tvLogout;

    UserDataSource uds;

    TextView headerNameView;
    CircularImageView headerAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setPadding(0, 0, 0, 0);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        ViewStub stub = (ViewStub) findViewById(R.id.activityStub);
        stub.setLayoutResource(setLayout());
        stub.inflate();


        nav_view = (NavigationView) findViewById(R.id.nav_view);
        populateNavHeaderViews(nav_view);


        if(AuthManagerHelper.Instance.getOneUser() != null){
            updateUserUi();
        }else {
            headerAvatar.setVisibility(View.VISIBLE);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        toggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
        setRegularHamburger();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if user logged in with Facebook, stop tracking their token
        if (mFacebookAccessTokenTracker != null) {
            mFacebookAccessTokenTracker.stopTracking();
        }
    }

    protected void setRegularHamburger(){
        toggle.setHomeAsUpIndicator(R.mipmap.hamburger_icon); //set your own
    }

    protected void setNotifiHamburger(){
        toggle.setHomeAsUpIndicator(R.mipmap.hamburger_icon_notifi); //set your own
    }

    protected abstract int setLayout();

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        closeDrawer();
        return true;
    }

    /**
     * Binds the UI elements in the Navigation view and populates its entries.
     *
     * @param nav_header_view
     */
    private void populateNavHeaderViews(NavigationView nav_header_view) {


        headerNameView = (TextView) nav_header_view.findViewById(R.id.header_name);
        headerAvatar = (CircularImageView) nav_header_view.findViewById(R.id.header_avatar);

        mFacebookCallbackManager = CallbackManager.Factory.create();

        tvLogout = (Button) nav_header_view.findViewById(R.id.tvLogout);

        headerAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        nav_header_view.findViewById(R.id.layout_my_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(DrawerActivity.this, EditProfileActivity.class);
                    closeDrawer();
                    startActivity(i);
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                logout();
                Intent myIntent = new Intent(DrawerActivity.this, LogInSignUpActivity.class);
                startActivity(myIntent);
                finish();
                Toast.makeText(DrawerActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateUserUi(){
        if(AuthManagerHelper.Instance.getOneUser() != null){

            if(AuthManagerHelper.Instance.getOneUser().getDisplayName() != null){
                headerNameView.setText(AuthManagerHelper.Instance.getOneUser().getDisplayName());
            }else {
                headerNameView.setText(AuthManagerHelper.Instance.getOneUser().getFirstName());
            }

            if(AuthManagerHelper.Instance.getOneUser().getPictureUrl() != null){
                headerAvatar.setVisibility(View.VISIBLE);
                PicassoHelper.Instance.displayProfilePageImage(AuthManagerHelper.Instance.getOneUser().getPictureUrl().replaceFirst("s",""), DrawerActivity.this, headerAvatar);
            }
        }
    }

    protected void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    protected void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

        /**
         * Unauthenticate from Firebase and from providers where necessary.
         */
           private void logout() {
               AuthManagerHelper.Instance.unUothorizeFireBase(this);
                /* Logout from Facebook */
                LoginManager.getInstance().logOut();
            }
}

