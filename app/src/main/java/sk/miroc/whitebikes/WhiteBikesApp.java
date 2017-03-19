package sk.miroc.whitebikes;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by miroc on 18/03/2017.
 */

public class WhiteBikesApp extends Application {
    public static final String BASE_URL = "http://whitebikes.info/";
    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(BASE_URL))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
//            Timber.plant(new CrashReportingTree());
        }
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }
}
