package sk.miroc.whitebikes.profile;

import dagger.Component;
import sk.miroc.whitebikes.ApplicationComponent;
import sk.miroc.whitebikes.utils.ActivityScoped;

@ActivityScoped
@Component(
        dependencies = ApplicationComponent.class,
        modules = ProfilePresenterModule.class)
public interface ProfileComponent {
    void inject(ProfileActivity profileActivity);
}
