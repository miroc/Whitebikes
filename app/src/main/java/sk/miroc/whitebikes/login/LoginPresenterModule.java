package sk.miroc.whitebikes.login;

import dagger.Module;
import dagger.Provides;
import sk.miroc.whitebikes.data.OldApi;

@Module
class LoginPresenterModule {
    private LoginContract.View view;

    public LoginPresenterModule(LoginContract.View view) {
        this.view = view;
    }

    @Provides
    LoginContract.View provideProfileContractView() {
        return view;
    }

    @Provides
    LoginContract.Presenter provideLoginContractPresenter(OldApi api){
        return new LoginPresenter(view, api);
    }
}
