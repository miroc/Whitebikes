package sk.miroc.whitebikes;

import javax.inject.Singleton;

import dagger.Component;
import sk.miroc.whitebikes.data.ApiModule;
import sk.miroc.whitebikes.data.ClientModule;
import sk.miroc.whitebikes.map.MapsActivity;
import sk.miroc.whitebikes.standdetail.StandActivity;

@Singleton
@Component(modules = {
        AndroidModule.class,
        ApplicationModule.class,
        ApiModule.class,
        ClientModule.class
})public interface ApplicationComponent {
    void inject(MapsActivity activity);
    void inject(StandActivity activity);

}
