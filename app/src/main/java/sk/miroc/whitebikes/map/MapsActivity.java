package sk.miroc.whitebikes.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
import sk.miroc.whitebikes.data.WhiteBikesApiOld;
import sk.miroc.whitebikes.data.models.Stand;
import sk.miroc.whitebikes.standdetail.StandActivity;
import sk.miroc.whitebikes.utils.PermissionUtils;
import timber.log.Timber;

// ak chcem bez horneho baru tak fragmentActivity
public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap map;

    @Inject
    Retrofit retrofit;
    @Inject
    WhiteBikesApiOld apiOld;
    @Inject
    IconGenerator iconFactory;

    @BindView(R.id.find_my_location)
    FloatingActionButton findMyLocationButton;

    private boolean permissionDenied = false;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        ((WhiteBikesApp) getApplication()).getNetComponent().inject(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
    }

    @OnClick(R.id.find_my_location)
    void onFindMyLocationClick(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            Timber.i("Finding last location: %s", lastLocation);
            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        map.setOnMyLocationButtonClickListener(this);

        // Move camera to bratislava
        LatLng bratislava = new LatLng(48.145, 17.1071373);
//        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(bratislava));
        map.moveCamera(CameraUpdateFactory.zoomTo(15));

        loadStands();
        enableMyLocation();
//        googleMap.getUiSettings().setMapToolbarEnabled(false);
//        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//        googleMap.getUiSettings().setCompassEnabled(true);
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
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
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
        Call<List<Stand>> call = apiOld.getStands("map:markers");
        call.enqueue(new Callback<List<Stand>>() {
            @Override
            public void onResponse(Call<List<Stand>> call, Response<List<Stand>> response) {
                Timber.d("Response received.");
                int responseCode = response.code();
                List<Stand> standsList = response.body();
                Timber.d("Response code: %d, standsCount: %d", responseCode, standsList.size());

                addStands(standsList);
            }

            @Override
            public void onFailure(Call<List<Stand>> call, Throwable t) {
                Timber.e(t, "loading stands failed");
            }
        });
    }

    private void addStands(List<Stand> stands) {
        for (Stand stand : stands) {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                    (iconFactory.makeIcon(stand.getStandName())));
            try {
                MarkerOptions markerSpec = new MarkerOptions()
                        .position(stand.getLatLng())
                        .icon(icon);
//                        .title(stand.getStandName());

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

//    @Override
//    public boolean onMyLocationButtonClick() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            map.moveCamera(CameraUpdateFactory.newLatLng(lastLocation.get));
//        }
//        return false;
//    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Connected to google location services");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
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
}
