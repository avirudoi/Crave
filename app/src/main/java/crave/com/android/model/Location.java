package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

	private String city;
	private Coordinate coordinate;
	private String[] display_address;


	public Location() {
	}

    /**
     * Implementation for Parcelable.
     */
	public Location(Parcel in) {
		this.city = in.readString();
		this.coordinate = in.readParcelable(Coordinate.class.getClassLoader());
		this.display_address = in.createStringArray();

	}
	
	/* Implementation for Parcelable */
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(this.city);
		parcel.writeParcelable(this.coordinate, 0);
		parcel.writeStringArray(this.display_address);

	}


    public static final Creator<Location> CREATOR
    							= new Creator<Location>() {
											public Location createFromParcel(Parcel in) {
											    return new Location(in);
											}
										
											public Location[] newArray(int size) {
											    return new Location[size];
											}
		};

	public void setCity (String place) {
		city = place;
	}


  public String getCity() {
    	return city;
    }

	public Coordinate getCoordinate(){
		return coordinate;
	}

	public String[] getFullAddress(){
		return display_address;
	}

	public String getAddress(){

		StringBuilder address = new StringBuilder();

		for(int i = 0; i< display_address.length; i ++){
			address.append(display_address[i] + " ");
			if(i >2){
				if(display_address[i] == display_address[2]){
					address.append(display_address[i].split(",")[1]);
				}
			}
		}
		return address.toString();
	}

}