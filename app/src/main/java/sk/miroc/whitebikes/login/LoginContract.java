package sk.miroc.whitebikes.login;


import sk.miroc.whitebikes.base.BasePresenter;
import sk.miroc.whitebikes.base.BaseView;

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void gotoPreviousActivity();

        void errorInvalidCredentials();
        void errorUnknown();

        void informLoginSuccessful();
    }

    interface Presenter extends BasePresenter {
        void login(String number, String password);
    }
}
