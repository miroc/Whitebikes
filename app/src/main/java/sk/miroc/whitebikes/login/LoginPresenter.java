package sk.miroc.whitebikes.login;

import android.support.annotation.NonNull;

public class LoginPresenter implements LoginContract.Presenter{
    private LoginContract.View view;

    public LoginPresenter(@NonNull LoginContract.View view){
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void start() {

    }


    @Override
    public void login(String email, String password) {

    }
}
