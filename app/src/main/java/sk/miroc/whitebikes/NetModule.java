package sk.miroc.whitebikes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.maps.android.ui.IconGenerator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sk.miroc.whitebikes.data.WhiteBikesApiOld;

/**
 * Created by miroc on 18/03/2017.
 */

@Module
public class NetModule {
    String baseUrl;
    public NetModule(String baseUrl){
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    // TODO: add context rather than application context
    @Provides
    @Singleton
    IconGenerator providesIconGenerator(Application application){
        return new IconGenerator(application);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(this.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    WhiteBikesApiOld provideOldApi(Retrofit retrofit){
        return retrofit.create(WhiteBikesApiOld.class);
    }
}
