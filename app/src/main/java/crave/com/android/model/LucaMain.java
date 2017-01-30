package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LucaMain implements Parcelable {



	private String name;
	private String locality;
	private Boolean has_menu;
	private String id;
	private String street_address;



	public LucaMain() {
	}

    /**
     * Implementation for Parcelable.
     */
	public LucaMain(Parcel in) {
		this.name = in.readString();
		this.locality = in.readString();
		this.has_menu =  in.readByte() != 0;
		this.id = in.readString();
		this.street_address = in.readString();
	}
	
	/* Implementation for Parcelable */
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(this.name);
		parcel.writeString(this.locality);
		parcel.writeByte((byte) (this.has_menu ? 1 : 0));
		parcel.writeString(this.id);
		parcel.writeString(this.street_address);
	}


	public static final Creator<LucaMain> CREATOR
    							= new Creator<LucaMain>() {
											public LucaMain createFromParcel(Parcel in) {
											    return new LucaMain(in);
											}
										
											public LucaMain[] newArray(int size) {
											    return new LucaMain[size];
											}
		};


	public String getTitle() {
		return name;
	}


	public String getLocality() {
		return locality;
	}

	public String getPlaceId() {
		return id;
	}

	public String getAddress() {
		return street_address;
	}

	public Boolean isHasMenu() {
		return has_menu;
	}
}