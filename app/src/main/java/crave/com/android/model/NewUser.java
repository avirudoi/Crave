
package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NewUser implements Parcelable {

	private String userId;

	private String email;
	private String displayName;
	private String firstName;
	private String lastName;

	private String city;

	private String pictureUrl;

	private String FBUrl;

	public NewUser() {
		
	}

	public NewUser(Parcel in) {
		this.userId = in.readString();
		this.email = in.readString();
		this.displayName = in.readString();
		this.firstName = in.readString();
		this.lastName = in.readString();
		this.city = in.readString();
		this.pictureUrl = in.readString();


	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.userId);
		dest.writeString(this.email);
		dest.writeString(this.displayName);
		dest.writeString(this.firstName);
		dest.writeString(this.lastName);
		dest.writeString(this.city);
		dest.writeString(this.pictureUrl);

	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<NewUser> CREATOR
			= new Parcelable.Creator<NewUser>() {
		public NewUser createFromParcel(Parcel in) {
			return new NewUser(in);
		}

		public NewUser[] newArray(int size) {
			return new NewUser[size];
		}
	};

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getFBUrl() {
		return FBUrl;
	}

	public void setFBUrl(String FBUrl) {
		this.FBUrl = FBUrl;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
