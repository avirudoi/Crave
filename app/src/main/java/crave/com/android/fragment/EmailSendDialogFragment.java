package crave.com.android.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import crave.com.android.R;
import crave.com.android.face.LoginSignUpInterface;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class EmailSendDialogFragment extends DialogFragment {

    LoginSignUpInterface loginSignUpInterface;


    public EmailSendDialogFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.send_email_dialog_fragment, container, false);

        TextView tvOk = (TextView) view.findViewById(R.id.tvOk);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSignUpInterface.signUpFragment();
                dismiss();
            }
        });

        return view;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
       dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

}
