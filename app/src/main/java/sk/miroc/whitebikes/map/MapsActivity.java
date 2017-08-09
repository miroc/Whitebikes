package sk.miroc.whitebikes.map;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import sk.miroc.whitebikes.R;
import sk.miroc.whitebikes.WhiteBikesApp;
import sk.miroc.whitebikes.data.OldApi;
import sk.miroc.whitebikes.data.models.Stand;
import sk.miroc.whitebikes.login.service.LoginService;
import sk.miroc.whitebikes.profile.ProfileActivity;
import sk.miroc.whitebikes.standdetail.StandActivity;
import sk.miroc.whitebikes.utils.PermissionUtils;
import timber.log.Timber;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;



    @Inject Retrofit retrofit;
    @Inject OldApi oldApi;
    @Inject IconGenerator iconGenerator;
    @Inject LoginService loginService;

    @BindView(R.id.button_info) Button buttonInfo;
    @BindView(R.id.button_return) Button buttonReturn;

    @BindView(R.id.find_my_location) FloatingActionButton findMyLocationButton;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheet;

    @BindView(R.id.search_view) MaterialSearchView searchView;


    private BottomSheetBehavior bottomSheetBehavior;

    private ActionBarDrawerToggle navDrawerToggle;
    private boolean permissionDenied = false;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    // TODO replace with actual call
    private List<Stand> tempClosestStands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        ((WhiteBikesApp) getApplication()).getApplicationComponent().inject(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        initNavDrawer();
        initSearchView();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            navDrawerItemClicked(item);
            return false;
        });
    }

    private void initSearchView() {
        // TODO
        // this is only temporary, set stands as adapter
        // https://github.com/MiguelCatalan/MaterialSearchView

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void navDrawerItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile: {
                Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.help: {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(WhiteBikesApp.HELP_URL));
                startActivity(i);
                break;
            }
//            case R.id.history:
//                Timber.d("HISTORY CLICKED");
//                break;
            default:
                Timber.w("Unknown menuItem ID clicked: %d", item.getItemId());
        }
    }

    private void initNavDrawer() {
        navDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(navDrawerToggle);
        navDrawerToggle.syncState();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        navDrawerToggle.syncState();
    }

    @OnClick(R.id.find_my_location)
    void onFindMyLocationClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            Timber.i("Finding last location: %s", lastLocation);
            if (lastLocation != null) {
                LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Move camera to bratislava
        LatLng bratislava = new LatLng(48.145, 17.1071373);
        map.moveCamera(CameraUpdateFactory.newLatLng(bratislava));
        map.moveCamera(CameraUpdateFactory.zoomTo(15));

        loadStands();
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (map != null) {
            // Access to the location has been granted to the app.
//            map.setMyLocationEnabled(true);
//            map.getUiSettings().setMyLocationButtonEnabled(true);
//            map.getUiSettings().setCompassEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            permissionDenied = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
//                searchView.open(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void loadStands() {
        Timber.d("loadStands");
        Call<List<Stand>> call = oldApi.getStands("map:markers");
        call.enqueue(new Callback<List<Stand>>() {
            @Override
            public void onResponse(Call<List<Stand>> call, Response<List<Stand>> response) {
                Timber.d("Response received.");
                int responseCode = response.code();
                List<Stand> standsList = response.body();
                Timber.d("Response code: %d, standsCount: %d", responseCode, standsList.size());

                addStandsToMap(standsList);
            }

            @Override
            public void onFailure(Call<List<Stand>> call, Throwable t) {
                Timber.e(t, "loading stands failed");
            }
        });
    }

    private void addStandsToMap(List<Stand> stands) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int redColor = ContextCompat.getColor(this, R.color.red);
        int greenColor = ContextCompat.getColor(this, R.color.green_bike);
        int blueColor = ContextCompat.getColor(this, R.color.blue_bike_repair);

        this.tempClosestStands = stands.subList(0, 2);


        for (Stand stand : stands) {
            View v = inflater.inflate(R.layout.map_bike_icon, null);
            TextView bikeCountText = (TextView) v.findViewById(R.id.bike_count);
            ImageView bikeIcon = (ImageView) v.findViewById(R.id.bike_icon);
            if (stand.getStandName().contains("SERVIS")) {
                bikeIcon.setColorFilter(blueColor);
            } else if (stand.getBikecount().equals("0")) {
                bikeIcon.setColorFilter(redColor);
            } else {
                bikeIcon.setColorFilter(greenColor);
            }


            bikeCountText.setText(stand.getBikecount());
            iconGenerator.setContentView(v);

            Bitmap bitmap = iconGenerator.makeIcon(stand.getStandName());
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
            try {
                MarkerOptions markerSpec = new MarkerOptions()
                        .position(stand.getLatLng())
                        .icon(icon);

                map.addMarker(markerSpec).setTag(stand);
                map.setOnMarkerClickListener(marker -> {
                    Intent intent = new Intent(MapsActivity.this, StandActivity.class);
                    intent.putExtra(StandActivity.EXTRA_STAND, (Stand) marker.getTag());
                    startActivity(intent);
                    return false;
                });
            } catch (Stand.InvalidLocationException e) {
                Timber.e(e);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Connected to google location services");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Connection to google maps was suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.e("Unable to connect to google maps, connection result: %s", connectionResult);
    }

    @OnClick({R.id.button_info, R.id.button_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_info:
                if (bottomSheetBehavior.getState() == STATE_COLLAPSED){
                    bottomSheetBehavior.setState(STATE_EXPANDED);
                } else if (bottomSheetBehavior.getState() == STATE_EXPANDED) {
                    bottomSheetBehavior.setState(STATE_COLLAPSED);
                }
                break;
            case R.id.button_return:
                if(tempClosestStands == null){
                    return;
                }
                ArrayList<Stand> standArrayList = new ArrayList<>(tempClosestStands);
                DialogFragment newFragment = ClosestStandDialogFragment.newInstance(standArrayList);
                newFragment.show(getSupportFragmentManager(), "dialog");
                break;
        }
    }
}
