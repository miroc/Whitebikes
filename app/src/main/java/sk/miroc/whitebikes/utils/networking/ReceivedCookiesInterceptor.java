package sk.miroc.whitebikes.utils.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;
import sk.miroc.whitebikes.utils.TempUtils;

public class ReceivedCookiesInterceptor implements Interceptor {
    private Context context;
    HashSet<String> cookies;
    public ReceivedCookiesInterceptor(Context context) {
        this.context = context;
        this.cookies = TempUtils.getCookiesPreferences(context);
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
//            HashSet<String> cookies = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context)
//                    .getStringSet("PREF_COOKIES", new HashSet<String>());

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
            memes.putStringSet("PREF_COOKIES", cookies).apply();
            memes.commit();
        }
        return originalResponse;
    }
}