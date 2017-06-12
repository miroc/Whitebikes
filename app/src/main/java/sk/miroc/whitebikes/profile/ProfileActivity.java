package sk.miroc.whitebikes.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.miroc.whitebikes.R;
import sk.miroc.whitebikes.WhiteBikesApp;
import sk.miroc.whitebikes.data.models.UserStatus;
import sk.miroc.whitebikes.login.LoginActivity;

public class ProfileActivity extends AppCompatActivity implements ProfileContract.View {
    @Inject ProfileContract.Presenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.test_text) TextView testText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
            ab.setTitle(R.string.profile);
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

    @Override
    public void setUserStatus(UserStatus userStatus) {
        testText.setText(userStatus.toString());

    }
}
