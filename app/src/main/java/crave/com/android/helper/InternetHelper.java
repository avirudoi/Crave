package crave.com.android.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetHelper {

	static ConnectivityManager manager;

	public static boolean isNetworkAvailable(Context context) {
		if (context!=null) {
			manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			boolean isAvailable = false;

			if (networkInfo != null && networkInfo.isConnected()) {
				isAvailable = true;
			}
			return isAvailable;
		} else
			return false;
	}

	public static boolean isWiFiAvailable(Context context) {

		boolean isAvailable = false;

		manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

		if (activeNetwork != null
				&& activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

			isAvailable = true;

		}
		return isAvailable;
	}



}
