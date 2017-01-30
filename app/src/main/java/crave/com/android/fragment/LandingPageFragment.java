package crave.com.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import crave.com.android.R;
import crave.com.android.activity.MainActivity;
import crave.com.android.bll.BusinessManagerHelper;
import crave.com.android.face.LoginSignUpInterface;

/**
 * Created by avirudoi on 2/22/16.
 */
public class LandingPageFragment extends Fragment {

    private TextView tvLogIn, tvSignUp, tvSkip;

    LoginSignUpInterface loginSignUpInterface;
    private DataCall task = new DataCall();

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


        Bundle extras = getArguments();
        if (extras != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.landing_fragment, container,
                false);

        bindWidgetView(rootView);
        return rootView;
    }


    public  void bindWidgetView(View view){
        tvLogIn = (TextView) view.findViewById(R.id.tvLogIn);
        tvSignUp = (TextView) view.findViewById(R.id.tvSignUp);
        tvSkip = (TextView) view.findViewById(R.id.tvSkip);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(myIntent);
                getActivity().finish();
                //task.execute("");
                //BusinessManagerHelper.Instance.getLocalDataBase(getActivity());
            }
        });

        tvLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSignUpInterface.loginFragment();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSignUpInterface.signUpFragment();
            }
        });

    }

    /*
* AsyncTask class to retrieve the data from the URL
* */
    private class DataCall extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... params) {

            BusinessManagerHelper.Instance.getLocalDataBase(getActivity());
            return "Executed";
        }
        /*
        * Set up view widgets when data is accessible
        * */
        @Override
        protected void onPostExecute(String result) {
            Log.i("aviTag","working");


        }
    }
}
