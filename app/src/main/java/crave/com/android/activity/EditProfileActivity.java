package crave.com.android.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import crave.com.android.R;
import crave.com.android.face.EditProfileInterface;
import crave.com.android.fragment.ChangePasswordFragment;
import crave.com.android.fragment.UpdateProfileFragment;

/**
 * Created by avirudoi on 2/22/16.
 */
public class EditProfileActivity extends AppCompatActivity implements EditProfileInterface {

    private FragmentManager fragmentManager;

    UpdateProfileFragment updateProfileFragment;

    ChangePasswordFragment changePasswordFragment;


    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);

        setContentView(R.layout.login_signup_activity);

        updateProfileFragment();
    }

    @Override
    public void updateProfileFragment() {

        Bundle args = new Bundle();
        updateProfileFragment = new UpdateProfileFragment();
        updateProfileFragment.setArguments(args);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.top_fragment_container, updateProfileFragment,
                        "updateProfileFragment")
                .commit();
    }

    public void changePasswordFragment(){
        Bundle args = new Bundle();
        changePasswordFragment = new ChangePasswordFragment();
        changePasswordFragment.setArguments(args);
        if(fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.top_fragment_container, changePasswordFragment,
                        "changePasswordFragment").addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if(updateProfileFragment != null && updateProfileFragment.isVisible()) {
            finish();
        }
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

}

