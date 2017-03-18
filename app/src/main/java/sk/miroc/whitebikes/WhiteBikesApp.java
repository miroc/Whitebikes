package sk.miroc.whitebikes;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by miroc on 18/03/2017.
 */

public class WhiteBikesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
//            Timber.plant(new CrashReportingTree());
        }
    }
}
