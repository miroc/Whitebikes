package sk.miroc.whitebikes.standdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.miroc.whitebikes.R;
import sk.miroc.whitebikes.WhiteBikesApp;
import sk.miroc.whitebikes.data.WhiteBikesApiOld;
import sk.miroc.whitebikes.data.models.Stand;
import sk.miroc.whitebikes.data.models.StandBikes;
import sk.miroc.whitebikes.map.MapsActivity;
import sk.miroc.whitebikes.rentbike.RentBikeActivity;
import timber.log.Timber;

public class StandActivity extends AppCompatActivity {
    public static final String EXTRA_STAND = "STAND";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.stand_name) TextView standNameText;
    @BindView(R.id.stand_description) TextView standDescriptionText;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.stand_image) ImageView standImage;
    @BindView(R.id.bikes_list) LinearLayout bikesList;

    @Inject WhiteBikesApiOld apiOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand);
        ((WhiteBikesApp)getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        Stand stand = getIntent().getParcelableExtra(EXTRA_STAND);

        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        toolbarLayout.setTitle(" "); // TODO disable title in better way
        standNameText.setText(stand.getStandName());
        standDescriptionText.setText(stand.getStandDescription());

        Call<StandBikes> call = apiOld.getStandBikes("list", stand.getStandName());

        call.enqueue(new Callback<StandBikes>() {
            @Override
            public void onResponse(Call<StandBikes> call, Response<StandBikes> response) {
                Timber.i("Loading stand bikes succeeded, response: %s", response.toString());
                Timber.d("StandBikes: %s", response.body());
                addBikeButtons(response.body());

            }
            @Override
            public void onFailure(Call<StandBikes> call, Throwable t) {
                Timber.e(t, "loading stand bikes failed");
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

        if (!stand.getStandPhoto().isEmpty()){
            Glide.with(this).load(stand.getStandPhoto()).into(standImage);
        }
    }

    private void addBikeButtons(StandBikes standBikes) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        List<String> bikeNumbers = standBikes.getContent();

        List<String> bikeNumbers = Arrays.asList("*23","133","7");
        for (String bikeNumber : bikeNumbers){
            boolean broken = false;
            if (bikeNumber.startsWith("*")){
                bikeNumber = bikeNumber.substring(1);
                broken = true;
            }
            View v = inflater.inflate(!broken ? R.layout.button_bike : R.layout.button_bike_broken, null);
            Button button = (Button) v.findViewById(R.id.button);
            button.setText(getResources().getString(R.string.bike_number, bikeNumber));
            button.setTag(Integer.parseInt(bikeNumber));
            button.setOnClickListener(this::onRentButtonClicked);
            bikesList.addView(v);
        }
    }

    private void onRentButtonClicked(View view) {
        int bikeNumber = (int) view.getTag();
        Intent intent = new Intent(this, RentBikeActivity.class);
        // TODO first ask in a dialog
        intent.putExtra(RentBikeActivity.EXTRA_BIKE_NUMBER, bikeNumber);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }





}
