package sk.miroc.whitebikes.standdetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.miroc.whitebikes.data.OldApi;
import sk.miroc.whitebikes.data.models.Bike;
import sk.miroc.whitebikes.data.models.Stand;
import sk.miroc.whitebikes.data.models.StandBikes;
import timber.log.Timber;

public class StandPresenter implements StandContract.Presenter{
    private StandContract.View view;
    private Stand stand;
    private OldApi api;
    private List<Bike> bikes;

    public StandPresenter(StandContract.View view, OldApi api, Stand stand){
        this.view = view;
        this.stand = stand;
        this.view.setPresenter(this);
        this.api = api;
    }

    @Override
    public void start() {
        view.setStandDescription(stand.getStandDescription());
        view.setStandName(stand.getStandName());

        if (stand.getStandPhoto() != null && !stand.getStandPhoto().isEmpty()){
            view.loadStandPhoto(stand.getStandPhoto());
        }

        loadBikesOnStand();
    }

    private void loadBikesOnStand(){
        Call<StandBikes> call = api.getStandBikes("list", stand.getStandName());

        call.enqueue(new Callback<StandBikes>() {
            @Override
            public void onResponse(Call<StandBikes> call, Response<StandBikes> response) {
                Timber.i("Loading stand bikes succeeded, response: %s", response.body());
                Timber.d("StandBikes: %s", response.body());
                processBikes(response.body());
            }
            @Override
            public void onFailure(Call<StandBikes> call, Throwable t) {
                Timber.e(t, "loading stand bikes failed");
            }
        });
    }

    private void processBikes(StandBikes standBikes) {
        bikes = new ArrayList<>();
        List<String> bikeNumbers = standBikes.getContent();
        for (int i = 0; i < bikeNumbers.size(); i++){
            String bikeNumber = bikeNumbers.get(i);
            boolean hasNote = false;
            if (bikeNumber.startsWith("*")){
                bikeNumber = bikeNumber.substring(1);
                hasNote = true;
            }
            bikes.add(new Bike(bikeNumber, hasNote, standBikes.getNotes().get(i)));
        }
        view.addBikeButtons(bikes);
    }

}
