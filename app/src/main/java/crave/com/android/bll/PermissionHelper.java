package crave.com.android.bll;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import crave.com.android.db.PreferencesManager;

/**
 * A base class that offers inheriting Activities the functionality to display banner and full screen ads.
 * @author Avi Rudoi
 *
 */
public enum PermissionHelper {

	Instance;

	public static final int REQUEST_CODE_LOCATION_PERMISSION = 5;



	//Location
	public int onRequestLocationPermission(Context context, boolean locationPermission){

		locationPermission = false;

		// Here, thisActivity is the current activity
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
					Manifest.permission.ACCESS_FINE_LOCATION)) {

				// Show an expanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.
				locationPermission = true;
				PreferencesManager.Instance.storeKeyValue(Boolean.class, "permission", "locationPermissionBoolean", locationPermission);


				ActivityCompat.requestPermissions((Activity) context,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						REQUEST_CODE_LOCATION_PERMISSION);

			} else {
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions((Activity) context,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						REQUEST_CODE_LOCATION_PERMISSION);
				locationPermission = PreferencesManager.Instance.retrieveValue(Boolean.class, "permission", "locationPermissionBoolean", locationPermission);


				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		}
		return 0;
	}

	public boolean isLocationPermissionEnabled(Context context) {
		return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
	}

	public static void startInstalledAppDetailsActivity(final Context context) {
		if (context == null) {
			return;
		}
		final Intent i = new Intent();
		i.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.setData(Uri.parse("package:" + context.getPackageName()));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		context.startActivity(i);
	}

}