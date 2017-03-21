package sk.miroc.whitebikes.rentbike;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.miroc.whitebikes.R;
import sk.miroc.whitebikes.data.models.Stand;
import timber.log.Timber;

public class RentBikeActivity extends AppCompatActivity {
    public static final String EXTRA_BIKE_NUMBER = "BIKE_NUMBER";
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_bike);
        ButterKnife.bind(this);


        int bikeNumber = getIntent().getIntExtra(EXTRA_BIKE_NUMBER, -1);
        if (bikeNumber == -1){
            Timber.w("onCreate: missing bike number, exiting activity");
            finish();
        }
        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.bike_number, bikeNumber));
        }
    }
}
