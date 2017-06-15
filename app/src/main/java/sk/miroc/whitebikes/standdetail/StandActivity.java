package sk.miroc.whitebikes.standdetail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.miroc.whitebikes.R;
import sk.miroc.whitebikes.WhiteBikesApp;
import sk.miroc.whitebikes.data.OldApi;
import sk.miroc.whitebikes.data.models.Bike;
import sk.miroc.whitebikes.data.models.Stand;

public class StandActivity extends AppCompatActivity implements StandContract.View{
    public static final String EXTRA_STAND = "STAND";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.stand_name) TextView standNameText;
    @BindView(R.id.stand_description) TextView standDescriptionText;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.stand_image) ImageView standImage;
    @BindView(R.id.bikes_list) LinearLayout bikesList;

    @Inject OldApi apiOld;
    @Inject LayoutInflater inflater;
    private StandContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand);
        ButterKnife.bind(this);

        Stand stand = getIntent().getParcelableExtra(EXTRA_STAND);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(true);
            ab.setTitle(" "); // TODO disable title in better way
        }

        ((WhiteBikesApp)getApplication()).getApplicationComponent().inject(this);
        new StandPresenter(this, apiOld, stand);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }



    private void askToRent(Bike bike) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void setStandName(String text) {
        standNameText.setText(text);

    }


    @Override
    public void setStandDescription(String text) {
        standDescriptionText.setText(text);
    }

    @Override
    public void loadStandPhoto(String standPhotoUrl) {
        Glide.with(this).load(standPhotoUrl).into(standImage);
    }

    @Override
    public void addBikeButtons(List<Bike> bikes) {
        for (Bike bike : bikes){
            View v = inflater.inflate(!bike.hasNote() ? R.layout.button_bike : R.layout.button_bike_broken, null);
            Button button = (Button) v.findViewById(R.id.button);
            button.setText(getResources().getString(R.string.bike_number, bike.getBikeNumber()));
            button.setTag(bike);
            button.setOnClickListener(this::onRentButtonClicked);
            bikesList.addView(v);
        }
    }

    @Override
    public void setPresenter(StandContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void onRentButtonClicked(View view) {
        Bike bike = (Bike) view.getTag();
//        askToRent(bike);

        DialogFragment newFragment = RentBikeDialogFragment.newInstance(bike);
        newFragment.show(getSupportFragmentManager(), "dialog");

//        Intent intent = new Intent(this, RentBikeActivity.class);
//        // TODO first ask in a dialog
//        intent.putExtra(RentBikeActivity.EXTRA_BIKE_NUMBER, bikeNumber);
//        startActivity(intent);
//        finish();
    }


    public static class RentBikeDialogFragment extends DialogFragment {
        static final String EXTRA_BIKE = "bike";
        public RentBikeDialogFragment() {
            // Empty constructor required for DialogFragment
        }

        private Bike bike;

        public static RentBikeDialogFragment newInstance(Bike bike) {
            RentBikeDialogFragment frag = new RentBikeDialogFragment();
            Bundle args = new Bundle();
            args.putParcelable(EXTRA_BIKE, bike);
            frag.setArguments(args);
            return frag;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            this.bike = getArguments().getParcelable(EXTRA_BIKE);
            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
//            b.setTitle(getString(R.string.bike_number, bike.getBikeNumber()));
            String rentQuestion = String.format("Do you want to rent bike %s?", bike.getBikeNumber());
            if (bike.hasNote()){
                b.setMessage("Note: " + bike.getNote() + "\n\n" + rentQuestion);
            } else {
                b.setMessage(rentQuestion);
            }

            b.setPositiveButton(R.string.rent, (dialog, which) -> {

            });
            b.setNegativeButton(R.string.cancel, (dialog, which) -> {
                if (dialog != null){
                    dialog.dismiss();
                }
            });

            return b.create();
        }
    }
}
