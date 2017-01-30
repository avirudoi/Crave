package crave.com.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MainDbOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = MainDbOpenHelper.class.getSimpleName();

	private static final String DATABASE_NAME = "dine.db";

	//before incrementing the database version make sure to update the onupgrade method
	private static final int DATABASE_VERSION = 1;

	private static MainDbOpenHelper sInstance;


	//Info to create a settings
	public static final String TABLE_SETTING = "setting";
	public static final String SETTING_COLUMN_USERID = "userId";
	public static final String SETTING_COLUMN_LOGINCOUNT = "loginCount";
	public static final String SETTING_COLUMN_DEFAULTPRIVACY = "defaultPrivacy";
	public static final String SETTING_COLUMN_LASTPAGEVIEWED = "lastPageViewed";

	//Create story SQL statement
	private static final String SETTINGS_CREATE =
			"CREATE TABLE " + TABLE_SETTING + " (" +
					SETTING_COLUMN_USERID + " TEXT, " +
					SETTING_COLUMN_LOGINCOUNT + " INTEGER, " +
					SETTING_COLUMN_DEFAULTPRIVACY + " TEXT, " +
					SETTING_COLUMN_LASTPAGEVIEWED + " TEXT " +
					")";

	//Info to create a business
	public static final String TABLE_BUSINESS = "Business";
	public static final String BUSINESS_COLUMN_ID = "BusinessId";
	public static final String BUSINESS_COLUMN_IMAGE_URL = "image";
	public static final String BUSINESS_COLUMN_NAME= "name";
	public static final String BUSINESS_COLUMN_CITY= "city";
	public static final String BUSINESS_COLUMN_LAT= "lat";
	public static final String BUSINESS_COLUMN_LONG= "long";
	public static final String BUSINESS_COLUMN_PHONE = "phone";
	public static final String BUSINESS_COLUMN_SNIPPET = "snippet";
	public static final String BUSINESS_COLUMN_RATING = "rating";
	//public static final String BUSINESS_COLUMN_OPEN_STATUS = "openStatus";
	public static final String BUSINESS_COLUMN_LOCATION = "location";
	public static final String BUSINESS_COLUMN_REVIEW_COUNT = "reviewCount";
	public static final String BUSINESS_COLUMN_ADD_TIME = "addTime";


	//Create story SQL statement
	private static final String BUSINESS_CREATE =
			"CREATE TABLE " + TABLE_BUSINESS + " (" +
					BUSINESS_COLUMN_ID + " INTEGER, " +
					BUSINESS_COLUMN_IMAGE_URL + " TEXT, " +
					BUSINESS_COLUMN_NAME + " TEXT, " +
					BUSINESS_COLUMN_CITY + " TEXT, " +
					BUSINESS_COLUMN_LAT + " TEXT, " +
					BUSINESS_COLUMN_LONG + " TEXT, " +
					BUSINESS_COLUMN_PHONE + " TEXT, " +
					BUSINESS_COLUMN_SNIPPET + " TEXT, " +
					BUSINESS_COLUMN_RATING + " TEXT, " +
					//BUSINESS_COLUMN_OPEN_STATUS + " INTEGER, " +
					BUSINESS_COLUMN_LOCATION + " TEXT, " +
					BUSINESS_COLUMN_REVIEW_COUNT + " TEXT, " +
					BUSINESS_COLUMN_ADD_TIME + " INTEGER " +
					")";


	//Info to create a user
		public static final String TABLE_USER = "_User";
		public static final String USER_COLUMN_USERID = "userId";
		public static final String USER_COLUMN_EMAIL= "email";
		public static final String USER_COLUMN_FIRSTNAME = "firstname";
		public static final String USER_COLUMN_LASTNAME = "lastname";
		public static final String USER_COLUMN_CITY = "city";
		public static final String USER_COLUMN_DISPLAY_NAME = "displayName";
		public static final String USER_COLUMN_PICTUREURL = "pictureUrl";


		//Create story SQL statement
		private static final String USER_CREATE =
				"CREATE TABLE " + TABLE_USER + " (" +
						USER_COLUMN_USERID + " TEXT, " +
						USER_COLUMN_EMAIL + " TEXT, " +
						USER_COLUMN_FIRSTNAME + " TEXT, " +
						USER_COLUMN_LASTNAME + " TEXT, " +
						USER_COLUMN_CITY + " TEXT, " +
						USER_COLUMN_DISPLAY_NAME + " TEXT, " +
						USER_COLUMN_PICTUREURL + " TEXT " +
						")";



	 public static MainDbOpenHelper getInstance(Context context) {

		    // Use the application context, which will ensure that you
		    // don't accidentally leak an Activity's context.
		    // See this article for more information: http://bit.ly/6LRzfx
		    if (sInstance == null) {
		      sInstance = new MainDbOpenHelper(context.getApplicationContext());
		    }
		    return sInstance;
		  }

	private MainDbOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(USER_CREATE);
		db.execSQL(BUSINESS_CREATE);
		Log.i(LOGTAG, "Tables have been created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


		int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion)
        {
            switch (upgradeTo)
            {
                case 1:
                    break;

            }
            upgradeTo++;
        }
		
		
		Log.i(LOGTAG, "Database has been upgraded from " +
				oldVersion + " to " + newVersion);
	}
}
