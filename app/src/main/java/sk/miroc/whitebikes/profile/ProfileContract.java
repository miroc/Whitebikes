package sk.miroc.whitebikes.profile;


import sk.miroc.whitebikes.base.BasePresenter;
import sk.miroc.whitebikes.base.BaseView;
import sk.miroc.whitebikes.data.models.UserStatus;

public interface ProfileContract {

    interface View extends BaseView<Presenter> {
        void gotoLoginScreen();

        void setUserStatus(UserStatus userStatus);

        void setUsername(String username);
    }

    interface Presenter extends BasePresenter {
    }
}
