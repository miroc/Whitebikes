package sk.miroc.whitebikes.profile;


import sk.miroc.whitebikes.base.BasePresenter;
import sk.miroc.whitebikes.base.BaseView;

public interface ProfileContract {

    interface View extends BaseView<Presenter> {
        void gotoLoginScreen();
    }

    interface Presenter extends BasePresenter {
    }
}
