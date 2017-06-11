package sk.miroc.whitebikes.login;

import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.miroc.whitebikes.data.OldApi;
import sk.miroc.whitebikes.data.models.Stand;
import timber.log.Timber;

public class LoginPresenter implements LoginContract.Presenter{
    private LoginContract.View view;
    private OldApi api;

    public LoginPresenter(@NonNull LoginContract.View view, OldApi authenticatedApi){
        this.view = view;
        this.api = authenticatedApi;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void login(String number, String password) {
        api.login("login", number, password)
                .enqueue(new Callback<List<Stand>>() {
            @Override
            public void onResponse(Call<List<Stand>> call, Response<List<Stand>> response) {
                int code = response.code();
                Timber.d("response code: %d", code);
                if (code == 301){
                    String location = response.headers().get("Location");
                    if (location.contains("error")){
                        Timber.w("Log in unsuccessful, probably invalid credentials");
                        view.errorInvalidCredentials();

                    } else {
                        Timber.i("User successfully logged in");
                        view.informLoginSuccessful();
                        view.gotoPreviousActivity();
                    }
                } else {
                    Timber.w("Unkown error code");
                    view.errorUnknown();
                }
            }

            @Override
            public void onFailure(Call<List<Stand>> call, Throwable t) {
                Timber.w(t);
                view.errorUnknown();
            }
        });
    }
}
