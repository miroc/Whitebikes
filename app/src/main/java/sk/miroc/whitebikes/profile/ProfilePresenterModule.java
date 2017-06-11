package sk.miroc.whitebikes.profile;

import dagger.Module;
import dagger.Provides;
import sk.miroc.whitebikes.login.service.LoginService;

@Module
class ProfilePresenterModule {
    private ProfileContract.View view;

    public ProfilePresenterModule(ProfileContract.View view) {
        this.view = view;
    }

    @Provides
    ProfileContract.View provideProfileContractView() {
        return view;
    }

    @Provides
    ProfileContract.Presenter provideProfileContractPresenter(LoginService loginService){
        return new ProfilePresenter(view, loginService);
    }
}
