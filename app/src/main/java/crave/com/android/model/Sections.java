package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sandy on 12/11/15.
 */
public class Sections implements Parcelable {


    private Subsections[] subsections;
    private String section_name;





    protected Sections(Parcel in) {

        //Image info array
        int arrSize = in.readInt();
        if (arrSize ==0 ){
            this.subsections = null;
        }
        else {
            this.subsections = new Subsections[arrSize];
            for(int i=0; i<arrSize; ++i) {
                this.subsections[i] = in.readParcelable(Subsections.class.getClassLoader());
            }
        }
        this.section_name = in.readString();
    }

    public static final Creator<Sections> CREATOR = new Creator<Sections>() {
        @Override
        public Sections createFromParcel(Parcel in) {
            return new Sections(in);
        }

        @Override
        public Sections[] newArray(int size) {
            return new Sections[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // array
        if(subsections == null || subsections.length == 0) {
            dest.writeInt(0);
        }
        else {
            dest.writeInt(subsections.length);
            for(Subsections pin : subsections) {
                dest.writeParcelable(pin, 0);
            }
        }
        dest.writeString(this.section_name);
    }

    public String getSectionName() {
        return section_name;
    }

    public Subsections[] getSubsections(){
        return subsections;
    }

}
