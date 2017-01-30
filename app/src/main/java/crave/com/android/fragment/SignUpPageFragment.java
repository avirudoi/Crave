package crave.com.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import crave.com.android.R;
import crave.com.android.activity.MainActivity;
import crave.com.android.bll.AuthManagerHelper;
import crave.com.android.face.CallResponse;
import crave.com.android.face.LoginSignUpInterface;
import crave.com.android.face.OnOperationComplete;
import crave.com.android.helper.DineHookConstants;
import crave.com.android.helper.InternetHelper;

/**
 * Created by avirudoi on 2/22/16.
 */
public class SignUpPageFragment extends Fragment {

    private static final String TAG = "SignUpPageFragment";


    LoginSignUpInterface loginSignUpInterface;

    EditText etEmail, etPassword, etFirstName;

    private ProgressBar progressBar;


    /* The callback manager for Facebook */
    private CallbackManager mFacebookCallbackManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginSignUpInterface) {
            loginSignUpInterface = (LoginSignUpInterface) context;
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
        if (activity instanceof LoginSignUpInterface){
            loginSignUpInterface=(LoginSignUpInterface) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getArguments();
        if (extras != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.sign_up_fragment, container,
                false);

        mFacebookCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);


        if (InternetHelper.isNetworkAvailable(getActivity())) {
        AuthManagerHelper.Instance.facebookLogin(getActivity(), loginButton, mFacebookCallbackManager, new OnOperationComplete() {
            @Override
            public void onOperationComplete(int errorCode, String errorMsg) {
                if (errorCode == CallResponse.Success) {
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(myIntent);
                    getActivity().finish();
                }
            }
        });
        }else{
            Toast.makeText(getActivity(), DineHookConstants.STRING_NO_INTERNET, Toast.LENGTH_LONG).show();
        }


        bindWidgetView(rootView);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }



    public void bindWidgetView(View view) {
        TextView tvSignUp = (TextView) view.findViewById(R.id.tvSignUp);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetHelper.isNetworkAvailable(getActivity())) {
                createAccount();
                    }else{
                Toast.makeText(getActivity(), DineHookConstants.STRING_NO_INTERNET, Toast.LENGTH_LONG).show();
                 }
            }
        });

    }

    private void createAccount() {
        final String emailVal = etEmail.getText().toString();
        final String passwordVal = etPassword.getText().toString();
        final String fname = etFirstName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for Email
        if (etEmail.getVisibility() == View.VISIBLE) {
            // Check for a valid email address.
            if (TextUtils.isEmpty(emailVal)) {
                etEmail.setError(getString(R.string.error_field_required));
                focusView = etEmail;
                cancel = true;
            } else if (Patterns.EMAIL_ADDRESS.matcher(emailVal).matches() == false) {
                etEmail.setError(getString(R.string.error_invalid_email));
                focusView = etEmail;
                cancel = true;
            }
        }

        // Check for Password.
        if (etPassword.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(passwordVal)) {
                etPassword.setError(getString(R.string.error_field_required));
                focusView = etPassword;
                cancel = true;
            } else if (passwordVal.length() < 6 || passwordVal.contains(" ")) {
                etPassword.setError(getString(R.string.error_invalid_password));
                focusView = etPassword;
                cancel = true;
            }
        }

        // Check for Password.
        if (etFirstName.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(fname)) {
                etFirstName.setError(getString(R.string.error_field_required));
                focusView = etFirstName;
                cancel = true;
            }
        }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                if (focusView != null) {
                    focusView.requestFocus();
                }


            } else {
                progressBar.setVisibility(View.VISIBLE);
                AuthManagerHelper.Instance.SignUpUser(getActivity(), emailVal, passwordVal, fname, new OnOperationComplete() {
                    @Override
                    public void onOperationComplete(int errorCode, String errorMsg) {
                        if (errorCode == CallResponse.Success) {
                            Intent myIntent = new Intent(getActivity(), MainActivity.class);
                            startActivity(myIntent);
                            getActivity().finish();
                            progressBar.setVisibility(View.GONE);
                        }else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }



