package crave.com.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import crave.com.android.R;
import crave.com.android.activity.MainActivity;
import crave.com.android.bll.AuthManagerHelper;
import crave.com.android.face.CallResponse;
import crave.com.android.face.EditProfileInterface;
import crave.com.android.face.OnOperationComplete;
import crave.com.android.helper.DineHookConstants;
import crave.com.android.helper.InternetHelper;

/**
 * Created by avirudoi on 2/22/16.
 */
public class ChangePasswordFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";


    EditProfileInterface editProfileInterface;

    private EditText etNewPassword;

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

        View rootView = inflater.inflate(R.layout.change_password_fragment, container,
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

        TextView tv_save = (TextView)view.findViewById(R.id.tv_save);
        TextView tv_back = (TextView)view.findViewById(R.id.tv_back);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        etNewPassword = (EditText) view.findViewById(R.id.etNewPassword);

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetHelper.isNetworkAvailable(getActivity())) {
                changePassword();
                }else{
                    Toast.makeText(getActivity(), DineHookConstants.STRING_NO_INTERNET, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void changePassword() {
        final String passwordNewVal = etNewPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for Password.
        if (etNewPassword.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(passwordNewVal)) {
                etNewPassword.setError(getString(R.string.error_field_required));
                focusView = etNewPassword;
                cancel = true;
            } else if (passwordNewVal.length() < 6 || passwordNewVal.contains(" ")) {
                etNewPassword.setError(getString(R.string.error_invalid_password));
                focusView = etNewPassword;
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
                    AuthManagerHelper.Instance.changePassword(getActivity(), passwordNewVal, new OnOperationComplete() {
                        @Override
                        public void onOperationComplete(int errorCode, String errorMsg) {
                            if (errorCode == CallResponse.Success) {
                                Toast.makeText(getActivity(),"your password updated successfully", Toast.LENGTH_SHORT).show();
                                Intent myIntent = new Intent(getActivity(), MainActivity.class);
                                getActivity().finish();
                                startActivity(myIntent);
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




