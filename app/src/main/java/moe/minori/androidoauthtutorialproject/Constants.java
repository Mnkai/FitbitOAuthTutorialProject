package moe.minori.androidoauthtutorialproject;

/**
 * Created by Minori on 2015-11-16.
 */
public class Constants {

    /**
     * Auth URL to perform OAuth
     * OAuth procedure will return Access Token (and Refresh Token, if expiry is set)
     */
    public static final String authURL = "https://www.fitbit.com/oauth2/authorize";

    /**
     * Callback URL scheme will allow user to return to application without further interaction with PIN etc.
     * this URL will be checked every time if the auth has been successful when page is loaded.
     * For this example, this app responds by calling "minorimoe://" system-wide.
     */
    public static final String callBack = OAuthTutorialProject.getContext().getString(R.string.callback);

    // Application specific constants

    /**
     * Fitbit API application id from settings on dev.fitbit.com
     */
    public static final String clientId = "22B2RQ";

    /**
     * predefined API access endpoint
     */
    public static final String accessEndpointURL = "https://api.fitbit.com/1/user/-/profile.json";

}
