package sk.miroc.whitebikes.standdetail;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import sk.miroc.whitebikes.R;
import sk.miroc.whitebikes.WhiteBikesApp;
import sk.miroc.whitebikes.data.WhiteBikesApiOld;
import sk.miroc.whitebikes.data.models.Stand;
import sk.miroc.whitebikes.data.models.StandBikes;
import timber.log.Timber;

public class StandActivity extends AppCompatActivity {
    public static final String EXTRA_STAND = "STAND";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.stand_description) TextView standDescriptionText;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.stand_image) ImageView standImage;

    @Inject WhiteBikesApiOld apiOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand);
        ((WhiteBikesApp)getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        Stand stand = getIntent().getParcelableExtra(EXTRA_STAND);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        toolbarLayout.setTitle(stand.getStandName());
        standDescriptionText.setText(stand.getStandDescription());

        Call<StandBikes> call = apiOld.getStandBikes("list", stand.getStandName());

        call.enqueue(new Callback<StandBikes>() {
            @Override
            public void onResponse(Call<StandBikes> call, Response<StandBikes> response) {
                Timber.i("Loading stand bikes succeeded, code: %d", response.code());
                showBikes(response.body());

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

    private void showBikes(StandBikes standBikes) {
        List<Integer> bikeNumbers = standBikes.getContent();
        

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


}
