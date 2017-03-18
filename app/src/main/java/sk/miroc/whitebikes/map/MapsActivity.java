package sk.miroc.whitebikes.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.BubbleIconFactory;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sk.miroc.whitebikes.R;
import sk.miroc.whitebikes.data.WhiteBikesApiOld;
import sk.miroc.whitebikes.data.models.Stand;
import sk.miroc.whitebikes.standdetail.StandActivity;
import timber.log.Timber;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String BASE_URL = "http://whitebikes.info/";
    private GoogleMap map;
    protected Retrofit retrofit;
    protected IconGenerator iconFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        iconFactory = new IconGenerator(this);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Move camera to bratislava
        LatLng bratislava = new LatLng(48.145, 17.1071373);
//        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(bratislava));
        map.moveCamera(CameraUpdateFactory.zoomTo(15));

        loadStands();
    }

    private void loadStands(){
        WhiteBikesApiOld apiOld = retrofit.create(WhiteBikesApiOld.class);
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
                Timber.e(t, "something brutally failed.");
            }
        });

    }

    private void addStands(List<Stand> stands){
        for (Stand stand : stands){
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                    (iconFactory.makeIcon(stand.getStandName())));

            MarkerOptions markerSpec = new MarkerOptions()
                    .position(new LatLng(stand.getLat(), stand.getLon()))
                    .icon(icon)
                    .title(stand.getStandName());

            map.addMarker(markerSpec).setTag(stand);
            map.setOnMarkerClickListener(marker -> {
                Intent intent = new Intent(MapsActivity.this, StandActivity.class);
                intent.putExtra(StandActivity.EXTRA_STAND, (Stand) marker.getTag());
                startActivity(intent);
                return false;
            });
        }
    }
}
