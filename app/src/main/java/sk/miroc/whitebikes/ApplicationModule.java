package sk.miroc.whitebikes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.maps.android.ui.IconGenerator;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;

@Module
public class ApplicationModule {
    @Provides
    @Singleton
    IconGenerator providesIconGenerator(Context context){
        return new IconGenerator(context);
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
