package sk.miroc.whitebikes.rentbike;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sk.miroc.whitebikes.R;
import timber.log.Timber;

public class RentBikeActivity extends AppCompatActivity {
    public static final String EXTRA_BIKE_NUMBER = "BIKE_NUMBER";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.open_code_text) TextView openCodeText;
    @BindView(R.id.new_code_text) TextView newCodeText;
    @BindView(R.id.confirm_button) Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_bike);
        ButterKnife.bind(this);

        int bikeNumber = getIntent().getIntExtra(EXTRA_BIKE_NUMBER, -1);
        if (bikeNumber == -1) {
            Timber.w("onCreate: missing bike number, exiting activity");
            finish();
        }
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.bike_number, bikeNumber));
        }

        Typeface face = Typeface.createFromAsset(getAssets(),
                "CutiveMono.ttf");

        openCodeText.setTypeface(face);
        openCodeText.setText("2634");
        newCodeText.setTypeface(face);
        newCodeText.setText("1091");

        confirmButton.setOnClickListener(this::onConfirmClicked);
    }

    void onConfirmClicked(View view) {
        finish();
    }
}