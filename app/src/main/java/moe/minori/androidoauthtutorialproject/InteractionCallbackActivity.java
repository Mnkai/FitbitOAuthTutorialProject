package moe.minori.androidoauthtutorialproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArraySet;

/**
 * Created by Minori on 2015-11-16.
 */
public class InteractionCallbackActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        callbackProcess(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        callbackProcess(getIntent());

    }

    private void callbackProcess (Intent i)
    {
        Uri uri = i.getData();
        if ( uri != null && uri.toString().startsWith(Constants.callBack) )
        {
            for ( String query : uri.getQueryParameterNames() )
            {
                if ( query.equals("access_token"))
                {
                    MainActivity.accessToken =  uri.getQueryParameter(query);
                }
                else if ( query.equals("expires_in"))
                {
                    MainActivity.expires_in = Integer.parseInt(uri.getQueryParameter(query));
                }
            }


            Intent intent = new Intent(InteractionCallbackActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
