package sk.miroc.whitebikes.login;

import dagger.Component;
import sk.miroc.whitebikes.ApplicationComponent;
import sk.miroc.whitebikes.utils.ActivityScoped;

@ActivityScoped
@Component(
        dependencies = ApplicationComponent.class,
        modules = LoginPresenterModule.class)
public interface LoginComponent {
    void inject(LoginActivity activity);
}
