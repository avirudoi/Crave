package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Coordinate implements Parcelable {


	private double latitude;
	private double longitude;


	public Coordinate() {
	}

    /**
     * Implementation for Parcelable.
     */
	public Coordinate(Parcel in) {
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
		parcel.writeDouble(this.latitude);
		parcel.writeDouble(this.longitude);
	}

    public static final Creator<Coordinate> CREATOR
    							= new Creator<Coordinate>() {
											public Coordinate createFromParcel(Parcel in) {
											    return new Coordinate(in);
											}
										
											public Coordinate[] newArray(int size) {
											    return new Coordinate[size];
											}
		};

    public double getLatitude() {
    	return latitude;
    }

	public double getLongitude() {
		return longitude;
	}

}