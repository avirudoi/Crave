package crave.com.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import crave.com.android.model.NewUser;

public enum  UserDataSource {

	Instance;

	private static final String LOGTAG = UserDataSource.class.getSimpleName();

	private SQLiteDatabase database;
	private SQLiteOpenHelper dbOpenHelper;


	public void initUser(Context context) {
		dbOpenHelper = MainDbOpenHelper.getInstance(context);
		database = dbOpenHelper.getWritableDatabase();
	}


	private static final String[] userProfileColumns = {
			MainDbOpenHelper.USER_COLUMN_USERID,
			MainDbOpenHelper.USER_COLUMN_EMAIL,
			MainDbOpenHelper.USER_COLUMN_FIRSTNAME,
			MainDbOpenHelper.USER_COLUMN_LASTNAME,
			MainDbOpenHelper.USER_COLUMN_CITY,
			MainDbOpenHelper.USER_COLUMN_DISPLAY_NAME,
			MainDbOpenHelper.USER_COLUMN_PICTUREURL,};



	public void open1() {
		database = dbOpenHelper.getWritableDatabase();
	}

	public void close1() {
		dbOpenHelper.close();
	}

	public boolean isDatabaseOpen() {
		return database.isOpen();
	}



	public void CreateUser(NewUser user) {
		ContentValues values = new ContentValues();
		values.put(MainDbOpenHelper.USER_COLUMN_USERID, user.getUserId());
		values.put(MainDbOpenHelper.USER_COLUMN_EMAIL, user.getEmail());
		values.put(MainDbOpenHelper.USER_COLUMN_FIRSTNAME, user.getFirstName());
		values.put(MainDbOpenHelper.USER_COLUMN_LASTNAME, user.getLastName());
		values.put(MainDbOpenHelper.USER_COLUMN_CITY, user.getCity());
		values.put(MainDbOpenHelper.USER_COLUMN_DISPLAY_NAME, user.getDisplayName());
		values.put(MainDbOpenHelper.USER_COLUMN_PICTUREURL,user.getPictureUrl());

		database.insert(MainDbOpenHelper.TABLE_USER, null, values);

		Log.i(LOGTAG, "User Created: " + user.getFirstName());
	}


	// get only 1 story back
	public NewUser getOneUser(String userId) {
		Cursor cursor = database.query(MainDbOpenHelper.TABLE_USER,
				userProfileColumns, MainDbOpenHelper.USER_COLUMN_USERID + "='"
						+ userId + "'", null, null, null, null);

		Log.i(LOGTAG, "cursor user count:" + cursor.getCount());
		
		return cursorOneUser(cursor);
	}


	public NewUser cursorOneUser(Cursor cursor) {
		if (cursor.getCount() > 0) {

			cursor.moveToNext();
			// cursor.moveToNext();
			NewUser user = new NewUser();

			user.setUserId(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.USER_COLUMN_USERID)));
			user.setEmail(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.USER_COLUMN_EMAIL)));
			user.setFirstName(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.USER_COLUMN_FIRSTNAME)));
			user.setLastName(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.USER_COLUMN_LASTNAME)));
			user.setCity(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.USER_COLUMN_CITY)));
			user.setDisplayName(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.USER_COLUMN_DISPLAY_NAME)));
			user.setPictureUrl(cursor.getString(cursor
					.getColumnIndex(MainDbOpenHelper.USER_COLUMN_PICTUREURL)));

			cursor.close();
			return user;
		}

		else {
			cursor.close();
			return null;
		}
	}

	public void updateOneUser(NewUser user) {
		ContentValues values = new ContentValues();
		values.put(MainDbOpenHelper.USER_COLUMN_USERID, user.getUserId());
		values.put(MainDbOpenHelper.USER_COLUMN_EMAIL, user.getEmail());
		values.put(MainDbOpenHelper.USER_COLUMN_FIRSTNAME, user.getFirstName());
		values.put(MainDbOpenHelper.USER_COLUMN_LASTNAME, user.getLastName());
		values.put(MainDbOpenHelper.USER_COLUMN_DISPLAY_NAME, user.getDisplayName());
		values.put(MainDbOpenHelper.USER_COLUMN_PICTUREURL, user.getPictureUrl());

		database.update(MainDbOpenHelper.TABLE_USER, values,
				MainDbOpenHelper.USER_COLUMN_USERID + "='" + user.getUserId()
						+ "'", null);
		// story.setId(insertid);

		Log.i(LOGTAG, "User has been updated:" + user.getUserId());
		// return story;
	}
}
