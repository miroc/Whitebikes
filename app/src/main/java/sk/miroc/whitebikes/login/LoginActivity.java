package sk.miroc.whitebikes.login;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sk.miroc.whitebikes.R;
import sk.miroc.whitebikes.WhiteBikesApp;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    @BindView(R.id.main_layout) LinearLayout mainLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.number) AutoCompleteTextView number;
    @BindView(R.id.password) EditText password;
    @Inject LoginContract.Presenter presenter;
    @BindView(R.id.log_in_button) Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
            ab.setTitle(R.string.log_in);
        }

        DaggerLoginComponent.builder()
                .loginPresenterModule(new LoginPresenterModule(this))
                .applicationComponent(
                        ((WhiteBikesApp) getApplication()).getApplicationComponent()).build()
                .inject(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @OnClick(R.id.log_in_button)
    public void onLoginClicked() {
        presenter.login(number.getText().toString(), password.getText().toString());
    }

    @Override
    public void gotoPreviousActivity() {
        finish();
    }

    @Override
    public void errorInvalidCredentials() {
        password.setError(getString(R.string.errorInvalidCredentials));
    }

    @Override
    public void errorUnknown() {
        Toast.makeText(this, R.string.errorUnableToLogIn, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void informLoginSuccessful() {
        Toast.makeText(this, R.string.loginSuccessful, Toast.LENGTH_SHORT).show();
    }
}

