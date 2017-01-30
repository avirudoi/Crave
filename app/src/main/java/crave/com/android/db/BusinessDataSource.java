package crave.com.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import crave.com.android.model.Businesses;

public enum BusinessDataSource {

	Instance;

	private static final String LOGTAG = BusinessDataSource.class.getSimpleName();

	private SQLiteDatabase database;
	private SQLiteOpenHelper dbOpenHelper;

	/*
	 * public UserDataSource (Context context) { dbOpenHelper = new
	 * StoryDbOpenHelper(context); }
	 */
	public void initBusinessObject(Context context) {
		dbOpenHelper = MainDbOpenHelper.getInstance(context);
		database = dbOpenHelper.getWritableDatabase();
	}


	private static final String[] businessColumns = {
			MainDbOpenHelper.BUSINESS_COLUMN_ID,
			MainDbOpenHelper.BUSINESS_COLUMN_IMAGE_URL,
			MainDbOpenHelper.BUSINESS_COLUMN_NAME,
			MainDbOpenHelper.BUSINESS_COLUMN_CITY,
			MainDbOpenHelper.BUSINESS_COLUMN_LAT,
			MainDbOpenHelper.BUSINESS_COLUMN_LONG,
			MainDbOpenHelper.BUSINESS_COLUMN_PHONE,
			MainDbOpenHelper.BUSINESS_COLUMN_SNIPPET,
			MainDbOpenHelper.BUSINESS_COLUMN_RATING,
			//MainDbOpenHelper.BUSINESS_COLUMN_OPEN_STATUS,
			MainDbOpenHelper.BUSINESS_COLUMN_LOCATION,
			MainDbOpenHelper.BUSINESS_COLUMN_REVIEW_COUNT,
			MainDbOpenHelper.BUSINESS_COLUMN_ADD_TIME,};




	public void open1() {
		database = dbOpenHelper.getWritableDatabase();
	}

	public void close1() {
		dbOpenHelper.close();
	}

	public boolean isDatabaseOpen() {
		return database.isOpen();
	}


	public void saveBusiness(Businesses businesses) {
		ContentValues values = new ContentValues();
		values.put(MainDbOpenHelper.BUSINESS_COLUMN_ID, businesses.getPlaceId());
		values.put(MainDbOpenHelper.BUSINESS_COLUMN_IMAGE_URL, businesses.getImageUrl());
		values.put(MainDbOpenHelper.BUSINESS_COLUMN_NAME, businesses.getTitle());
		values.put(MainDbOpenHelper.BUSINESS_COLUMN_CITY, businesses.getLocation().getCity());
		if(businesses.getLocation().getCoordinate() != null){
			values.put(MainDbOpenHelper.BUSINESS_COLUMN_LAT, businesses.getLocation().getCoordinate().getLatitude());
			values.put(MainDbOpenHelper.BUSINESS_COLUMN_LONG, businesses.getLocation().getCoordinate().getLongitude());
		}else {
			Log.i("aviTag","get coordinate null");
		}
		values.put(MainDbOpenHelper.BUSINESS_COLUMN_PHONE, businesses.getPhone());
		values.put(MainDbOpenHelper.BUSINESS_COLUMN_SNIPPET, businesses.getSnippetText());
		values.put(MainDbOpenHelper.BUSINESS_COLUMN_RATING, businesses.getRatingImgUrl());
		//values.put(MainDbOpenHelper.BUSINESS_COLUMN_OPEN_STATUS, businesses.is_closedint());
		values.put(MainDbOpenHelper.BUSINESS_COLUMN_LOCATION, businesses.getLocation().getAddress());
		values.put(MainDbOpenHelper.BUSINESS_COLUMN_REVIEW_COUNT,businesses.getCountNumber());

		values.put(MainDbOpenHelper.BUSINESS_COLUMN_ADD_TIME, businesses.getAddTime());

		database.insert(MainDbOpenHelper.TABLE_BUSINESS, null, values);

		Log.i(LOGTAG, "businesses Addded: " +businesses.getCountNumber());
	}

	public void removeFromFavorite(String itemId) {

		database.delete(MainDbOpenHelper.TABLE_BUSINESS,
				MainDbOpenHelper.BUSINESS_COLUMN_ID + "='" + itemId + "'",
				null);
	}

	// get only 1 favorite based on ID
	public Businesses getOneBussnies(String bussinesId) {
		Cursor cursor = database.query(MainDbOpenHelper.TABLE_BUSINESS,
				businessColumns, MainDbOpenHelper.BUSINESS_COLUMN_ID + "='"
						+ bussinesId + "'", null, null, null, null);

		Log.i(LOGTAG, "cursor user count:" + cursor.getCount());

		return cursorOneUser(cursor);
	}

	public Businesses cursorOneUser(Cursor cursor) {
		if (cursor.getCount() > 0) {

			cursor.moveToNext();
			// cursor.moveToNext();
			Businesses businesses = new Businesses();

			businesses.setBusinessId(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_ID)));
			businesses.setImageUrl(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_IMAGE_URL)));
			businesses.setBusinessName(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_NAME)));
			businesses.setCity(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_CITY)));
			businesses.setLat(cursor.getDouble(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_LAT)));
			businesses.setLong(cursor.getDouble(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_LONG)));
			businesses.setPhone(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_PHONE)));
			businesses.setSnippet_text(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_SNIPPET)));
			businesses.setRating_img(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_RATING)));
			/*businesses.set(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_LOCATION)));*/
			businesses.setReviewCount(cursor.getInt(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_REVIEW_COUNT)));
			businesses.setAddTime(cursor.getLong(cursor
					.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_ADD_TIME)));

			cursor.close();
			return businesses;
		}

		else {
			cursor.close();
			return null;
		}
	}


	// get 1 business
	public List<Businesses> returnListOfFavorite() {

		Cursor cursor = database.query(MainDbOpenHelper.TABLE_BUSINESS,
				businessColumns, null, null, null, null, null);
		Log.i(LOGTAG, "get All list of favorite" + cursor.getCount());
		return cursorListPfBusiness(cursor);
	}


	public List<Businesses> cursorListPfBusiness(Cursor cursor) {
		List<Businesses> businesses = new ArrayList<Businesses>();
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {
				Businesses business = new Businesses();
				business.setBusinessId(cursor.getString(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_ID)));
				business.setImageUrl(cursor.getString(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_IMAGE_URL)));
				business.setBusinessName(cursor.getString(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_NAME)));
				business.setCity(cursor.getString(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_CITY)));
				business.setLat(cursor.getDouble(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_LAT)));
				business.setLong(cursor.getDouble(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_LONG)));
				business.setPhone(cursor.getString(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_PHONE)));
				business.setSnippet_text(cursor.getString(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_SNIPPET)));
				business.setRating_img(cursor.getString(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_RATING)));
			/*	business.setIs_closeInt(cursor.getInt(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_OPEN_STATUS)));*/
				business.setAddress(cursor.getString(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_LOCATION)));
				business.setReviewCount(cursor.getInt(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_REVIEW_COUNT)));
				business.setAddTime(cursor.getLong(cursor
						.getColumnIndex(MainDbOpenHelper.BUSINESS_COLUMN_ADD_TIME)));
				businesses.add(business);
			}
		}
		return businesses;
	}
}
