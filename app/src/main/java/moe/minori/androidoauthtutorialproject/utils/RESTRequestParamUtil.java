package moe.minori.androidoauthtutorialproject.utils;

import android.util.Log;
import android.util.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Minori on 2015-11-16.
 */
public class RESTRequestParamUtil
{

    /**
     * Util for quick request making
     *
     * @param originalURL, URL to prepend
     * @param params,      ArrayList of "Pair of URL Parameter / Value"
     * @return output
     */
    public static String paramURLfier (final String originalURL, final ArrayList<Pair<String, String>> params)
    {

        // if there was no parameter or parameter array was null, return originalURL
        if (params == null || params.size() == 0)
        {
            return originalURL;
        } else
        {
            StringBuilder builder = new StringBuilder();
            try
            {
                builder.append(originalURL);

                // first character of post param
                builder.append("?");

                for (int i = 0; i < params.size(); i++)
                {
                    Pair<String, String> one = params.get(i);

                    builder.append(one.first);
                    builder.append("=");

                    builder.append(URLEncoder.encode(one.second, "UTF-8"));


                    // if next entry exists, append "&"
                    if (params.size() > i + 1)
                        builder.append("&");
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            // for debug
            Log.d("RESTRequestParamUtil", builder.toString());

            return builder.toString();
        }
    }

    public static Map<String, List<String>> UrlParamfier (final String url)
    {
        Map<String, List<String>> params = new HashMap<>();
        String[] urlParts = url.split("\\?");

        try
        {
            if (urlParts.length > 1)
            {
                String query = urlParts[1];
                for (String param : query.split("&"))
                {
                    String pair[] = param.split("=", 2);
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1)
                    {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }
                    List<String> values = params.get(key);
                    if (values == null)
                    {
                        values = new ArrayList<>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return params;
    }
}
