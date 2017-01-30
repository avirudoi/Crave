package crave.com.android.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import crave.com.android.R;
import crave.com.android.face.LoginSignUpInterface;
import crave.com.android.fragment.EmailSendDialogFragment;
import crave.com.android.fragment.ForgotPasswordFragment;
import crave.com.android.fragment.LandingPageFragment;
import crave.com.android.fragment.LogInPageFragment;
import crave.com.android.fragment.SignUpPageFragment;

/**
 * Created by avirudoi on 2/22/16.
 */
public class LogInSignUpActivity extends BaseActivity implements LoginSignUpInterface {

    private FragmentManager fragmentManager;

    LandingPageFragment landingPageFragment;
    LogInPageFragment logInPageFragment;
    SignUpPageFragment signUpPageFragment;
    ForgotPasswordFragment forgotPasswordFragment;

    EmailSendDialogFragment emailSendDialogFragment;

    public android.support.v7.app.ActionBar actionBar;


    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);

        setContentView(R.layout.login_signup_activity);
        actionBar = getSupportActionBar();
        actionBar.hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayLandingFragment();
    }

    @Override
    public void displayLandingFragment() {

        Bundle args = new Bundle();
        landingPageFragment = new LandingPageFragment();
        landingPageFragment.setArguments(args);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.top_fragment_container, landingPageFragment,
                        "landingPageFragment")
                .commit();
    }

    public void loginFragment(){
        Bundle args = new Bundle();
        logInPageFragment = new LogInPageFragment();
        logInPageFragment.setArguments(args);
        if(fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.top_fragment_container, logInPageFragment,
                        "logInPageFragment").addToBackStack(null)
                .commit();
    }

    public void signUpFragment(){
        Bundle args = new Bundle();
        signUpPageFragment = new SignUpPageFragment();
        signUpPageFragment.setArguments(args);

        if(fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.top_fragment_container, signUpPageFragment,
                        "signUpPageFragment").addToBackStack(null)
                .commit();
    }

    public void forgotPasswordFragment(){
        Bundle args = new Bundle();
        forgotPasswordFragment = new ForgotPasswordFragment();
        forgotPasswordFragment.setArguments(args);

        if(fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.top_fragment_container, forgotPasswordFragment,
                        "forgotPasswordFragment").addToBackStack(null)
                .commit();
    }

    public void getEmailSendDialogFragment() {
        if (fragmentManager==null) {
            fragmentManager = getSupportFragmentManager();
        }
        emailSendDialogFragment = new EmailSendDialogFragment();
        //updateUserProfileDialogFragment.setArguments(null);
        if (emailSendDialogFragment != null) {
            emailSendDialogFragment.show(fragmentManager, "updateUserProfileDialogFragment");
        }
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
        if(landingPageFragment != null && landingPageFragment.isVisible()) {
            finish();
        }
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

}

