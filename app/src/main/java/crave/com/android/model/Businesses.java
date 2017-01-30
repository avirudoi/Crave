package crave.com.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

public class Businesses implements Parcelable {


	private String id;
	private String image_url;
	private String name;
	private String address;
	private double distance;
	private String display_phone;
	private String snippet_text;
	private String rating_img_url_large;
	private Boolean is_closed;
	private int is_closed_int;
	private Location location;
	private int review_count;
	private String city;
	private long addTime;

	private double latitude;
	private double longitude;


	public Businesses() {
	}

    /**
     * Implementation for Parcelable.
     */
	public Businesses(Parcel in) {
		this.id = in.readString();
		this.image_url = in.readString();
		this.name = in.readString();
		this.address = in.readString();
		this.distance = in.readDouble();
		this.display_phone = in.readString();
		this.snippet_text = in.readString();
		this.rating_img_url_large = in.readString();
		//this.is_closed =  in.readByte() != 0;
		this.is_closed_int = in.readInt();
		this.location = in.readParcelable(Location.class.getClassLoader());
		this.review_count = in.readInt();
		this.city = in.readString();

		this.addTime = in.readLong();

		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
	}
	
	/* Implementation for Parcelable */
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(this.id);
		parcel.writeString(this.image_url);
		parcel.writeString(this.name);
		parcel.writeString(this.address);
		parcel.writeDouble(this.distance);
		parcel.writeString(this.display_phone);
		parcel.writeString(this.snippet_text);
		parcel.writeString(this.rating_img_url_large);
//		parcel.writeByte((byte) (this.is_closed ? 1 : 0));
		parcel.writeInt(this.is_closed_int);
		parcel.writeParcelable(this.location, 0);
		parcel.writeInt(this.review_count);
		parcel.writeString(this.city);

		parcel.writeLong(this.addTime);

		parcel.writeDouble(this.latitude);
		parcel.writeDouble(this.longitude);
	}


    public static final Creator<Businesses> CREATOR
    							= new Creator<Businesses>() {
											public Businesses createFromParcel(Parcel in) {
											    return new Businesses(in);
											}
										
											public Businesses[] newArray(int size) {
											    return new Businesses[size];
											}
		};
    

    public String getImageUrl() {
    	return image_url;
    }

	public String getHighResImage(){
		String image = image_url;
		if(image != null){
			StringBuilder b = new StringBuilder(image);
			b.replace(image.lastIndexOf("m"), image.lastIndexOf("g") + 1, "o.jpg");
			image = b.toString();
			return image;
		}else {
			return null;
		}
	}

	public String getTitle() {
		return name;
	}

	public String getDistance() {

		DecimalFormat df = new DecimalFormat("#.##");

		return df.format(distance/1609.344);
	}

	public String getPhone() {
		return display_phone;
	}

	public String getSnippetText() {
		return snippet_text;
	}

	public Boolean is_closed() {
		return is_closed;
	}

	/*public int is_closedint() {

		return (is_closed == true) ? 0:1 ;
	}*/

	public void setIs_closeInt(int close) {

		is_closed_int = close;
	}

	public Location getLocation(){
		return location;
	}

	public String getPlaceId(){
		return id;
	}

	public long getAddTime(){
		addTime = System.currentTimeMillis();
		return addTime;
	}

	public String getRatingImgUrl(){
		return rating_img_url_large;
	}

	public int getCountNumber(){
		return review_count;
	}

	public void setReviewCount(int number){
		review_count = number;
	}


	// create all the sets methed
	public void setBusinessId(String finalId){
		id = finalId;
	}

	public void setImageUrl(String imageUrl){
		image_url = imageUrl;
	}

	public void setBusinessName(String title){
		name = title;
	}

	public void setDistance(double dis){
		distance = dis;
	}

	public void setPhone(String phone){
		display_phone = phone;
	}

	public void setSnippet_text(String text){
		snippet_text = text;
	}

	public void setRating_img(String rating){
		rating_img_url_large = rating;
	}

	public void setIs_closed(String rating){
		rating_img_url_large = rating;
	}


	public void setAddTime(long time){
		addTime = time;
	}

	public void setAddress(String location){
		address = location;
	}

	public String getAddress(){
		return address;
	}

	public String getCity(){
		return city;
	}

	public void setLat(double lat){
		latitude = lat;
	}

	public void setLong(double longt){
		longitude = longt;
	}

	public void setCity(String location){
		city = location;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

}