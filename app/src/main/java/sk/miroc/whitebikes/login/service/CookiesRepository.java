package sk.miroc.whitebikes.login.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

public class CookiesRepository {
    static final String PREF_NAME = "pref_cookies";
    static final String COOKIES_SET = "cookies_set";
    private SharedPreferences preferences;

    public CookiesRepository(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public Set<String> getSet(){
        return preferences.getStringSet(COOKIES_SET, new HashSet<String>());
    }

    public void saveSet(Set<String> set){
        SharedPreferences.Editor edit = preferences.edit();
        edit.putStringSet(COOKIES_SET, set);
        edit.commit();
    }


    public void delete(){
        Timber.d("deleting tokens from repository");
        preferences.edit().clear().apply();
    }

    public boolean hasCookies(){
        return !getSet().isEmpty();
    }
}
