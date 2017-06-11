package sk.miroc.whitebikes.profile;

import sk.miroc.whitebikes.login.service.LoginService;

public class ProfilePresenter implements ProfileContract.Presenter{
    private final LoginService loginService;
    private ProfileContract.View view;

//    @Inject
    public ProfilePresenter(ProfileContract.View view, LoginService loginService){
        this.view = view;
        this.loginService = loginService;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        if (!loginService.isUserLoggedIn()){
            view.gotoLoginScreen();
        }
    }
}
