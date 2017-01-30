package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LocalBusinesses implements Parcelable {


	private String title;

	private String display_number;
	private String place_full_address;
	private String placeDescription;
	private String priceprice;
	private String displayTimeOpen;

	private double latitude;
	private double longitude;


	public LocalBusinesses() {
	}

    /**
     * Implementation for Parcelable.
     */
	public LocalBusinesses(Parcel in) {
		this.title = in.readString();
		this.display_number = in.readString();
		this.place_full_address = in.readString();
		this.placeDescription = in.readString();
		this.priceprice = in.readString();
		this.displayTimeOpen = in.readString();

		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
	}
	
	/* Implementation for Parcelable */
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(this.title);
		parcel.writeString(this.display_number);
		parcel.writeString(this.place_full_address);
		parcel.writeString(this.placeDescription);
		parcel.writeString(this.priceprice);
		parcel.writeString(this.displayTimeOpen);

		parcel.writeDouble(this.latitude);
		parcel.writeDouble(this.longitude);
	}


    public static final Creator<LocalBusinesses> CREATOR
    							= new Creator<LocalBusinesses>() {
											public LocalBusinesses createFromParcel(Parcel in) {
											    return new LocalBusinesses(in);
											}
										
											public LocalBusinesses[] newArray(int size) {
											    return new LocalBusinesses[size];
											}
		};


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDisplay_number() {
		return display_number;
	}

	public void setDisplay_number(String display_number) {
		this.display_number = display_number;
	}

	public String getPlace_full_address() {
		return place_full_address;
	}

	public void setPlace_full_address(String place_full_address) {
		this.place_full_address = place_full_address;
	}

	public String getPlaceDescription() {
		return placeDescription;
	}

	public void setPlaceDescription(String placeDescription) {
		this.placeDescription = placeDescription;
	}

	public String getPriceprice() {
		return priceprice;
	}

	public void setPriceprice(String priceprice) {
		this.priceprice = priceprice;
	}

	public String getDisplayTimeOpen() {
		return displayTimeOpen;
	}

	public void setDisplayTimeOpen(String displayTimeOpen) {
		this.displayTimeOpen = displayTimeOpen;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}