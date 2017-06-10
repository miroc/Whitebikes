package sk.miroc.whitebikes.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.HashSet;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sk.miroc.whitebikes.WhiteBikesApp;
import sk.miroc.whitebikes.utils.networking.AddCookiesInterceptor;
import sk.miroc.whitebikes.utils.networking.LoggingInterceptor;
import sk.miroc.whitebikes.utils.networking.ReceivedCookiesInterceptor;

import static sk.miroc.whitebikes.utils.networking.AddCookiesInterceptor.PREF_COOKIES;

/**
 * Created by miroc on 25/04/2017.
 */

public class TempUtils {
    /**
     * Until we use the old API, use this retrofit with custom cookie manager
     * @param context
     */
    public static Retrofit getRetrofitWithCookies(Context context){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new AddCookiesInterceptor(context));
        builder.addInterceptor(new ReceivedCookiesInterceptor(context));
        builder.addInterceptor(new LoggingInterceptor());
        OkHttpClient client = builder.build();
        return new Retrofit.Builder()
                .baseUrl(WhiteBikesApp.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static HashSet<String> getCookiesPreferences(Context context){
        HashSet<String> preferences = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).
                getStringSet(PREF_COOKIES, new HashSet<String>());
        return preferences;
    }
}
