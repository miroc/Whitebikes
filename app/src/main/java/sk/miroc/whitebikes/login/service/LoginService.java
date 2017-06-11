package sk.miroc.whitebikes.login.service;

import timber.log.Timber;

public class LoginService {
    private CookiesRepository repository;

    public LoginService (CookiesRepository repository){
        this.repository = repository;
    }

    public boolean isUserLoggedIn() {
        // basic check if the session is saved
        return repository.hasCookies();
    }

    public void logout(){
        repository.delete();
        Timber.i("User was logged out.");
    }
}
