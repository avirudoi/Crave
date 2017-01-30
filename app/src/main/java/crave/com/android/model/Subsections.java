package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sandy on 12/11/15.
 */
public class Subsections implements Parcelable {


    private Contents[] contents;



    public Contents[] getContents(){
        return contents;
    }

    protected Subsections(Parcel in) {

        //Image info array
        int arrSize = in.readInt();
        if (arrSize ==0 ){
            this.contents = null;
        }
        else {
            this.contents = new Contents[arrSize];
            for(int i=0; i<arrSize; ++i) {
                this.contents[i] = in.readParcelable(Contents.class.getClassLoader());
            }
        }
    }

    public static final Creator<Subsections> CREATOR = new Creator<Subsections>() {
        @Override
        public Subsections createFromParcel(Parcel in) {
            return new Subsections(in);
        }

        @Override
        public Subsections[] newArray(int size) {
            return new Subsections[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // array
        if(contents == null || contents.length == 0) {
            dest.writeInt(0);
        }
        else {
            dest.writeInt(contents.length);
            for(Contents pin : contents) {
                dest.writeParcelable(pin, 0);
            }
        }
    }
}
