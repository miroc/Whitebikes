package sk.miroc.whitebikes.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sk.miroc.whitebikes.R;

public class LoginActivity extends AppCompatActivity  {

    @BindView(R.id.main_layout) LinearLayout mainLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.email) AutoCompleteTextView email;
    @BindView(R.id.password) EditText password;
    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

