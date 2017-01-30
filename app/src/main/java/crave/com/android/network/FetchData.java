package crave.com.android.network;

/**
 * Created by Avi Rudoi on 12/11/15.
 */

import android.content.Context;
import android.content.SharedPreferences;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.net.HttpURLConnection;

public class FetchData {

    String charset = "UTF-8";
    HttpURLConnection urlConnection;


    private static final String API_HOST = "api.yelp.com";

    private static final String SEARCH_PATH = "/v2/search";


    final static String URL = "https://maps.googleapis.com/maps/api/place/radarsearch/json?location=51.503186,-0.126446&radius=5000&types=food&key=AIzaSyDx5XScE2TEeZG6Ih1LaJxydtH-mGrFdUQ";

    public static final String OAUTH_CONSUMER_KEY  = "iKU5Wt5tKx8G9e6N6LrU9Q";
    public static final String OAUTH_CONSUMER_SECRET  = "951PMej95BceP77sV5zyI2GieX4";
    public static final String OAUTH_CONSUMER_TOKEN  = "yjARc8e6Ki3r8RIT114SgwdqMmQ127Xt";
    public static final String OAUTH_CONSUMER_TOKEN_SECRET  = "i-t4WawpjZ_DcWfQQmvez7is_k4";


    private Context context;
    private String authTime;
    SharedPreferences prefs;

    OAuthService service;
    Token accessToken;


    /**
     * Setup the Yelp API OAuth credentials.
     *
     * @param consumerKey Consumer key
     * @param consumerSecret Consumer secret
     * @param token Token
     * @param tokenSecret Token secret
     */
    public FetchData(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service =
                new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey)
                        .apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    /**
     * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
     *
     * @param path API endpoint to be queried
     * @return <tt>OAuthRequest</tt>
     */
    private OAuthRequest createOAuthRequest(String path) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + API_HOST + path);
        return request;
    }


    /**
     * Constructor to make a server call and get the main object back that return place id
     * @param context
     */
    public String serverCall(Context context, double lat, double lotitude, String type, String radios){
            OAuthRequest request = createOAuthRequest(SEARCH_PATH);
            request.addQuerystringParameter("term", type);
            request.addQuerystringParameter("ll", lat + "," + lotitude);
            request.addQuerystringParameter("sort", "1");
            //request.addQuerystringParameter("limit", "10");
            request.addQuerystringParameter("radius_filter", radios);
            //request.addQuerystringParameter("category_filter", "pizza");
            this.service.signRequest(this.accessToken, request);
            Response response = request.send();
            return response.getBody();
    }

}
