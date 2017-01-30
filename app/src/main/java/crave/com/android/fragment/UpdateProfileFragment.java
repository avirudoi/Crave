package crave.com.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import crave.com.android.R;
import crave.com.android.bll.AuthManagerHelper;
import crave.com.android.face.CallResponse;
import crave.com.android.face.EditProfileInterface;
import crave.com.android.face.OnOperationComplete;
import crave.com.android.helper.DineHookConstants;
import crave.com.android.helper.InternetHelper;

/**
 * Created by avirudoi on 2/22/16.
 */
public class UpdateProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";


    EditProfileInterface editProfileInterface;

    private EditText etEmail, etLastName, etFirstName;

    private ProgressBar progressBar;

    private Toolbar mToolBar;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditProfileInterface) {
            editProfileInterface = (EditProfileInterface) context;
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
        if (activity instanceof EditProfileInterface) {
            editProfileInterface = (EditProfileInterface) activity;
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

        View rootView = inflater.inflate(R.layout.update_profile_fragment, container,
                false);
        mToolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mToolBar.setContentInsetsAbsolute(0,0);
        mToolBar.setPadding(0, 0, 0, 0);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolBar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        //upArrow.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);


        bindWidgetView(rootView);
        return rootView;
    }


    public void bindWidgetView(View view) {
        TextView tvChangePassword = (TextView) view.findViewById(R.id.tvChangePassword);
        TextView tv_save = (TextView)view.findViewById(R.id.tv_save);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);

        if(AuthManagerHelper.Instance.getOneUser() != null){
            etFirstName.setText(AuthManagerHelper.Instance.getOneUser().getFirstName());
            etLastName.setText(AuthManagerHelper.Instance.getOneUser().getLastName());
            etEmail.setText(AuthManagerHelper.Instance.getOneUser().getEmail());
        }else {
            getActivity().finish();
            Toast.makeText(getActivity(),"sorry some error happned", Toast.LENGTH_SHORT).show();
        }

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetHelper.isNetworkAvailable(getActivity())) {
                updateProfile();
            }else{
                Toast.makeText(getActivity(), DineHookConstants.STRING_NO_INTERNET, Toast.LENGTH_LONG).show();
            }
            }
        });


        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileInterface.changePasswordFragment();
            }
        });

    }

    private void updateProfile() {
        final String emailVal = etEmail.getText().toString();
        String lname = etLastName.getText().toString();
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

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    if (focusView != null) {
                        focusView.requestFocus();
                    }


                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    AuthManagerHelper.Instance.updateProfile(getActivity(), emailVal, fname, lname, new OnOperationComplete() {
                        @Override
                        public void onOperationComplete(int errorCode, String errorMsg) {
                            if (errorCode == CallResponse.Success) {
                                Toast.makeText(getActivity(),"your user information got update successfully", Toast.LENGTH_SHORT).show();
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


