package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Avi
 */
public class ResultMenuModel implements Parcelable {


    private Objects[] objects;



    public Objects[] getObjects(){
        return objects;
    }

    protected ResultMenuModel(Parcel in) {

        //Image info array
        int arrSize = in.readInt();
        if (arrSize ==0 ){
            this.objects = null;
        }
        else {
            this.objects = new Objects[arrSize];
            for(int i=0; i<arrSize; ++i) {
                this.objects[i] = in.readParcelable(Objects.class.getClassLoader());
            }
        }
    }

    public static final Creator<ResultMenuModel> CREATOR = new Creator<ResultMenuModel>() {
        @Override
        public ResultMenuModel createFromParcel(Parcel in) {
            return new ResultMenuModel(in);
        }

        @Override
        public ResultMenuModel[] newArray(int size) {
            return new ResultMenuModel[size];
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
            for(Objects pin : objects) {
                dest.writeParcelable(pin, 0);
            }
        }
    }


}
