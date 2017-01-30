package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sandy on 12/11/15.
 */
public class ResultArrayModel implements Parcelable {


    private Businesses[] businesses;



    public Businesses[] getFeedData(){
        return businesses;
    }

    protected ResultArrayModel(Parcel in) {

        //Image info array
        int arrSize = in.readInt();
        if (arrSize ==0 ){
            this.businesses = null;
        }
        else {
            this.businesses = new Businesses[arrSize];
            for(int i=0; i<arrSize; ++i) {
                this.businesses[i] = in.readParcelable(Businesses.class.getClassLoader());
            }
        }
    }

    public static final Creator<ResultArrayModel> CREATOR = new Creator<ResultArrayModel>() {
        @Override
        public ResultArrayModel createFromParcel(Parcel in) {
            return new ResultArrayModel(in);
        }

        @Override
        public ResultArrayModel[] newArray(int size) {
            return new ResultArrayModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // array
        if(businesses == null || businesses.length == 0) {
            dest.writeInt(0);
        }
        else {
            dest.writeInt(businesses.length);
            for(Businesses pin : businesses) {
                dest.writeParcelable(pin, 0);
            }
        }
    }


}
