package sk.miroc.whitebikes;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModule {
    private WhiteBikesApp application;

    public AndroidModule(WhiteBikesApp application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return application.getResources();
    }
}
