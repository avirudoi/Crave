package crave.com.android.bll;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import crave.com.android.db.BusinessDataSource;
import crave.com.android.db.MainDbOpenHelper;
import crave.com.android.db.UserDataSource;

/**
 * Created by avirudoi on 2/21/16.
 */
public class EasyFoodApplication extends Application {

    private static Context mApplication; // future use

    private static EasyFoodApplication instance;


    public EasyFoodApplication() {
        super();
        instance = this;
    }

    public static EasyFoodApplication getInstance() {
        return EasyFoodApplication.instance;
    }

    /**
     * Initialization of application components.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
        MainDbOpenHelper.getInstance(this);
        UserDataSource.Instance.initUser(this);
        BusinessDataSource.Instance.initBusinessObject(this);
        //generateFacebookKeyHash();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public void generateFacebookKeyHash(){
        try {
            String packageName = this.getApplicationContext().getPackageName();
            PackageInfo info = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Log.d("Package Name=", this.getApplicationContext().getPackageName());

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

        }
    }


}

