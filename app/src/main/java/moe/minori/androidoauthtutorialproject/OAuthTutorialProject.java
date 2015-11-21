package moe.minori.androidoauthtutorialproject;

import android.app.Application;
import android.content.Context;

/**
 * Placeholder class for context retrieval
 * Created by Minori on 2015-11-16.
 */
public class OAuthTutorialProject extends Application
{

    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();

        context = this;
    }

    public static Context getContext()
    {
        return context;
    }
}
