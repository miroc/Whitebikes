package sk.miroc.whitebikes.rentbike;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.miroc.whitebikes.R;
import sk.miroc.whitebikes.data.models.Bike;
import timber.log.Timber;

public class RentBikeActivity extends AppCompatActivity {
    public static final String EXTRA_BIKE = "BIKE";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.open_code_text) TextView openCodeText;
    @BindView(R.id.new_code_text) TextView newCodeText;
    @BindView(R.id.confirm_button) Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_bike);
        ButterKnife.bind(this);

        Bike bike = getIntent().getParcelableExtra(EXTRA_BIKE);
        if (bike == null) {
            Timber.w("onCreate: missing bike parameter, exiting activity");
            finish();
            return;
        }

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null){
            ab.setTitle(getResources().getString(R.string.bike_number, bike.getBikeNumber()));
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
