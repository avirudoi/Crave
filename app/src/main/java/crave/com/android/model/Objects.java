package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Objects implements Parcelable {


	private Menu[] menus;

	public Objects() {
	}



    /**
     * Implementation for Parcelable.
     */
	public Objects(Parcel in) {

		int arrSize = in.readInt();
		if (arrSize ==0 ){
			this.menus = null;
		}
		else {
			this.menus = new Menu[arrSize];
			for(int i=0; i<arrSize; ++i) {
				this.menus[i] = in.readParcelable(Menu.class.getClassLoader());
			}
		}
	}
	
	/* Implementation for Parcelable */
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {

		// array
		if(menus == null || menus.length == 0) {
			parcel.writeInt(0);
		}
		else {
			parcel.writeInt(menus.length);
			for(Menu pin : menus) {
				parcel.writeParcelable(pin, 0);
			}
		}
	}


    public static final Creator<Objects> CREATOR
    							= new Creator<Objects>() {
											public Objects createFromParcel(Parcel in) {
											    return new Objects(in);
											}
										
											public Objects[] newArray(int size) {
											    return new Objects[size];
											}
		};
    


	public Menu[] getMenus(){
		return menus;
	}

}