package moe.minori.androidoauthtutorialproject;

import android.net.Uri;
import android.net.http.HttpAuthHeader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import moe.minori.androidoauthtutorialproject.customtab.CustomTabActivityHelper;
import moe.minori.androidoauthtutorialproject.utils.RESTRequestParamUtil;
import moe.minori.androidoauthtutorialproject.utils.StreamUtil;

// Android Support Library for Chrome Custom Tabs

/**
 * Android OAuth Tutorial Project
 * <p/>
 * Author: Hiraoka Minori
 * License: Apache License 2.0 unless discussed with author
 * Third Party Licenses
 * -part of Chrome Custom Tabs: Apache License 2.0 (https://github.com/GoogleChrome/custom-tabs-client)
 */
public class MainActivity extends AppCompatActivity
{

    public static String accessToken = null;
    public static int expires_in = -1;
    public static String refreshToken = null;

    public static String accessResult = null;

    private boolean isInFlatedUI = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Fill hardcoded constants to UI on first UI inflation
        if (!isInFlatedUI)
        {
            TextView authUrlTextView = (TextView) findViewById(R.id.authUrlTextView);
            authUrlTextView.setText(authUrlTextView.getText() + Constants.authURL);

            TextView accessEndpointTextView = (TextView) findViewById(R.id.accessEndpointTextView);
            accessEndpointTextView.setText(accessEndpointTextView.getText() + Constants.accessEndpointURL);

            isInFlatedUI = true;
        }

        // Fill non-hardcoded variables to UI on UI resume
        TextView accessTokenTextView = (TextView) findViewById(R.id.accessTokenTextView);
        if (accessToken != null)
            accessTokenTextView.setText(getString(R.string.accessTokenTextViewString) + accessToken);

        TextView refreshTokenTextView = (TextView) findViewById(R.id.refreshTokenTextView);
        if (refreshToken != null)
            refreshTokenTextView.setText(getString(R.string.refreshTokenTextViewString) + refreshToken);

        TextView expiresInTextView = (TextView) findViewById(R.id.tokenExpiryTextView);
        expiresInTextView.setText(getString(R.string.tokenExpiryTextViewString) + expires_in);

        EditText accessResultEditText = (EditText) findViewById(R.id.accessResultEditText);
        if ( accessResult != null )
            accessResultEditText.setText(accessResult);
    }

    /**
     * Called when button is pressed - calling method's name is defined by layout XML, and must have View param.
     *
     * @param v
     */
    public void onClick(View v)
    {
        if (v.getId() == R.id.getTokensViaOAuthButton) // First button, get Access Token & Refresh Token via Chrome Custom Tab
        {
            // In order to use Chrome Custom Tab, phone must have modern Chrome for Android installed

            // We will use Support Library to avoid using AIDL interface
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();

            // Since some API requires user to use Custom Tabs, we will not provide failback to webview - notice null param.
            CustomTabActivityHelper customTabActivityHelper = new CustomTabActivityHelper();

            // Implicit grant flow request URL params variable - since we don't have web server.
            ArrayList<Pair<String, String>> urlParams = new ArrayList<>();

            urlParams.add(new Pair<>("client_id", Constants.clientId));
            urlParams.add(new Pair<>("response_type", "token"));
            urlParams.add(new Pair<>("scope", "profile"));
            urlParams.add(new Pair<>("redirect_uri", "http://kawaii.na.minori.ga/fitbitDuctTapeParser.html"));
            urlParams.add(new Pair<>("prompt", "consent"));


            CustomTabActivityHelper.openCustomTab(this, customTabsIntent, Uri.parse(RESTRequestParamUtil.paramURLfier(Constants.authURL, urlParams)), null);


        }
        else if (v.getId() == R.id.accessApiButton) // Second button, Access to API with Access Token
        {
            // Since we are dealing with network methods, we will use AsyncTask.

            new AsyncTask<Void, Void, String>()
            {
                @Override
                protected String doInBackground(Void... params)
                {
                    // this block will not be executed in UI thread

                    // try simple API request - profile reading

                    // for API request, we need access token and HTTP GET request

                    String result = null;

                    try
                    {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpGet request = new HttpGet();
                        request.setURI(new URI(Constants.accessEndpointURL));

                        // add authorization params - BASIC
                        request.setHeader("Authorization", "Bearer " + accessToken);

                        HttpResponse response = httpClient.execute(request);

                        result = StreamUtil.convertStreamToString(response.getEntity().getContent());

                    }
                    catch (URISyntaxException | IOException e)
                    {
                        e.printStackTrace();
                    }

                    accessResult = result;

                    return result;
                }

                @Override
                protected void onPostExecute(String s)
                {
                    super.onPostExecute(s);
                    // this block will be executed in UI thread

                    // from onResume()
                    EditText accessResultEditText = (EditText) findViewById(R.id.accessResultEditText);
                    if ( accessResult != null )
                        accessResultEditText.setText(accessResult);
                }
            }.execute();
        }
    }
}
