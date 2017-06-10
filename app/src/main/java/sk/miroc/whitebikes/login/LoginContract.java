package sk.miroc.whitebikes.login;


import sk.miroc.whitebikes.base.BasePresenter;
import sk.miroc.whitebikes.base.BaseView;

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void errorEmptyEmail();

        void errorInvalidEmail();

        void errorEmptyPassword();

        void errorInvalidCredentials();

        void errorUnknown();

        void gotoMealsScreen();
    }

    interface Presenter extends BasePresenter {
        void login(String email, String password);
    }
}
