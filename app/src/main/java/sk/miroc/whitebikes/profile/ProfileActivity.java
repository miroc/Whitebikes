package sk.miroc.whitebikes.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import sk.miroc.whitebikes.R;
import sk.miroc.whitebikes.WhiteBikesApp;
import sk.miroc.whitebikes.login.LoginActivity;

public class ProfileActivity extends AppCompatActivity implements ProfileContract.View {
    @Inject ProfileContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.profile);
        }

        DaggerProfileComponent.builder()
                .profilePresenterModule(new ProfilePresenterModule(this))
                .applicationComponent(
                        ((WhiteBikesApp) getApplication()).getApplicationComponent()).build()
                .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void gotoLoginScreen() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
