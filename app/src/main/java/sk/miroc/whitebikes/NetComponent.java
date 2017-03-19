package sk.miroc.whitebikes;

import javax.inject.Singleton;

import dagger.Component;
import sk.miroc.whitebikes.map.MapsActivity;
import sk.miroc.whitebikes.standdetail.StandActivity;

/**
 * Created by miroc on 18/03/2017.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(MapsActivity activity);
    void inject(StandActivity activity);
}
