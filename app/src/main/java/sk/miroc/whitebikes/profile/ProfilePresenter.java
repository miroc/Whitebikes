package sk.miroc.whitebikes.profile;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.miroc.whitebikes.data.OldApi;
import sk.miroc.whitebikes.data.models.UserStatus;
import sk.miroc.whitebikes.login.service.LoginService;
import timber.log.Timber;

public class ProfilePresenter implements ProfileContract.Presenter{
    private final LoginService loginService;
    private ProfileContract.View view;
    private OldApi api;

    // TODO - inject here instead of ProfilePresenterModule
//    @Inject
    public ProfilePresenter(ProfileContract.View view, LoginService loginService, OldApi api){
        this.view = view;
        this.loginService = loginService;
        this.view.setPresenter(this);
        this.api = api;
    }

    @Override
    public void start() {
        if (!loginService.isUserLoggedIn()){
            view.gotoLoginScreen();
        }
        loadUser();
    }


    private void loadUser() {
        api.getUserStatus("map:status").enqueue(new Callback<UserStatus>() {
            @Override
            public void onResponse(Call<UserStatus> call, Response<UserStatus> response) {
                int code = response.code();
                Timber.d("Response code: %d", code);
                if (response.isSuccessful()){
                    UserStatus userStatus = response.body();
                    Timber.d("Raw response: %s", response.raw().toString());
                    Timber.i("Received userStatus: %s", userStatus);
                    view.setUserStatus(userStatus);
                } else {
                    Timber.w("Unable to load user profile");
                }
            }

            @Override
            public void onFailure(Call<UserStatus> call, Throwable t) {
                Timber.w(t, "Unable to load profile");

            }
        });
    }

    @Override
    public void logout() {
        loginService.logout();
        view.informLogout();
        view.closeScreen();
    }
}
