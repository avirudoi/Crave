package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sandy on 12/11/15.
 */
public class Contents implements Parcelable {


    private String text;
    private String type;
    private String price;
    private String name;
    private String description;


    protected Contents(Parcel in) {
        this.text = in.readString();
        this.type = in.readString();
        this.price = in.readString();
        this.name = in.readString();
        this.description = in.readString();
    }

    public static final Creator<Contents> CREATOR = new Creator<Contents>() {
        @Override
        public Contents createFromParcel(Parcel in) {
            return new Contents(in);
        }

        @Override
        public Contents[] newArray(int size) {
            return new Contents[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.type);
        dest.writeString(this.price);
        dest.writeString(this.name);
        dest.writeString(this.description);
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
