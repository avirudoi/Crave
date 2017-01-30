package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Menu implements Parcelable {


	private String menu_name;
	private Sections[] sections;



	public Menu() {
	}



    /**
     * Implementation for Parcelable.
     */
	public Menu(Parcel in) {
		this.menu_name = in.readString();

		int arrSize = in.readInt();
		if (arrSize ==0 ){
			this.sections = null;
		}
		else {
			this.sections = new Sections[arrSize];
			for(int i=0; i<arrSize; ++i) {
				this.sections[i] = in.readParcelable(Sections.class.getClassLoader());
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
		parcel.writeString(this.menu_name);

		// array
		if(sections == null || sections.length == 0) {
			parcel.writeInt(0);
		}
		else {
			parcel.writeInt(sections.length);
			for(Sections pin : sections) {
				parcel.writeParcelable(pin, 0);
			}
		}
	}


    public static final Creator<Menu> CREATOR
    							= new Creator<Menu>() {
											public Menu createFromParcel(Parcel in) {
											    return new Menu(in);
											}
										
											public Menu[] newArray(int size) {
											    return new Menu[size];
											}
		};
    

    public String getMenuName() {
    	return menu_name;
    }

	public Sections[] getSections(){
		return sections;
	}

}