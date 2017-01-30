package crave.com.android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;

import crave.com.android.bll.AuthManagerHelper;

/**
 * Created by avirudoi on 6/22/16.
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseApp.getApps(this).isEmpty() == false) {
            AuthManagerHelper.Instance.initAutoManager(this);
        }
    }

}
