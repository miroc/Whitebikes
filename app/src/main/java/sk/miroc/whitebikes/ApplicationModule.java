package sk.miroc.whitebikes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.maps.android.ui.IconGenerator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sk.miroc.whitebikes.login.service.CookiesRepository;
import sk.miroc.whitebikes.login.service.LoginService;

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

    @Provides
    @Singleton
    LoginService providesLoginService(CookiesRepository repository) {
        return new LoginService(repository);
    }

    @Provides
    @Singleton
    CookiesRepository providesSessionTokenRepository(Context context) {
        return new CookiesRepository(context);
    }
}
