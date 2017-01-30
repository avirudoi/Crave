package crave.com.android.fragment;

import android.app.Activity;
import android.content.Context;
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

import crave.com.android.R;
import crave.com.android.bll.AuthManagerHelper;
import crave.com.android.face.CallResponse;
import crave.com.android.face.LoginSignUpInterface;
import crave.com.android.face.OnOperationComplete;

/**
 * Created by avirudoi on 2/22/16.
 */
public class ForgotPasswordFragment extends Fragment {

    private TextView tvForgotPassword;

    LoginSignUpInterface loginSignUpInterface;

    EditText etEmail;

    private ProgressBar progressBar;

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
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof LoginSignUpInterface){
            loginSignUpInterface=(LoginSignUpInterface) context;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.forgat_password_fragment, container,
                false);

        bindWidgetView(rootView);
        return rootView;
    }

    public  void bindWidgetView(View view){
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        tvForgotPassword = (TextView) view.findViewById(R.id.tvForgotPassword);

        etEmail = (EditText) view.findViewById(R.id.etEmail);

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

    }

    private void submit() {
        final String emailVal = etEmail.getText().toString();

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
            AuthManagerHelper.Instance.forgotPassword(getActivity(), emailVal, new OnOperationComplete() {
                @Override
                public void onOperationComplete(int errorCode, String errorMsg) {
                    if (errorCode == CallResponse.Success) {
                        progressBar.setVisibility(View.GONE);
                        loginSignUpInterface.getEmailSendDialogFragment();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
}
