package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sandy on 12/11/15.
 */
public class ResultArrayLuca implements Parcelable {


    private LucaMain[] objects;



    public LucaMain[] getLucaData(){
        return objects;
    }

    protected ResultArrayLuca(Parcel in) {

        //Image info array
        int arrSize = in.readInt();
        if (arrSize ==0 ){
            this.objects = null;
        }
        else {
            this.objects = new LucaMain[arrSize];
            for(int i=0; i<arrSize; ++i) {
                this.objects[i] = in.readParcelable(LucaMain.class.getClassLoader());
            }
        }
    }

    public static final Creator<ResultArrayLuca> CREATOR = new Creator<ResultArrayLuca>() {
        @Override
        public ResultArrayLuca createFromParcel(Parcel in) {
            return new ResultArrayLuca(in);
        }

        @Override
        public ResultArrayLuca[] newArray(int size) {
            return new ResultArrayLuca[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // array
        if(objects == null || objects.length == 0) {
            dest.writeInt(0);
        }
        else {
            dest.writeInt(objects.length);
            for(LucaMain pin : objects) {
                dest.writeParcelable(pin, 0);
            }
        }
    }
}
