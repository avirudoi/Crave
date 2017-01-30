package crave.com.android.helper;

/**
 * Created by Avi Rudoi on 12/14/15.
 */
public enum  HelperMethod {

    Instance;

 /*   public String returnDistance(double latMy, double longtitude, double latFinal, double longFinal){
        Location locationA = new Location("point A");
        locationA.setLatitude(latMy);
        locationA.setLongitude(longtitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(latFinal);
        locationB.setLongitude(longFinal);
        return String.valueOf(locationA.distanceTo(locationB)).substring(0,3) ;
    }*/

/*    public double returnDistance2(double latMy, double longtitude, double latFinal, double longFinal){
        Location locationA = new Location("point A");
        locationA.setLatitude(latMy);
        locationA.setLongitude(longtitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(latFinal);
        locationB.setLongitude(longFinal);

        double distance=selected_location.distanceTo(near_locations);

        double MILES_PER_KILOMETER = 0.621;

        double km;       // Number of kilometers.
        double mi;       // Number of miles.

        km = Double.parseDouble(String.valueOf(locationA.distanceTo(locationB)));

        //... Computation
        mi = km * MILES_PER_KILOMETER;

        return distance;
    }*/

    public static String returnDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return String.valueOf(dist).substring(0,4);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



}