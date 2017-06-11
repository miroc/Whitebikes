package sk.miroc.whitebikes;

import android.app.Application;

import com.facebook.stetho.Stetho;

import sk.miroc.whitebikes.data.ApiModule;
import sk.miroc.whitebikes.utils.MyDebugTree;
import timber.log.Timber;

/**
 * Created by miroc on 18/03/2017.
 */

public class WhiteBikesApp extends Application {
    public static final String BASE_URL = "http://whitebikes.info/";
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .apiModule(new ApiModule(BASE_URL))
                .build();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            Timber.plant(new MyDebugTree());
        }
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
