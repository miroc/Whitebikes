package sk.miroc.whitebikes.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import sk.miroc.whitebikes.data.models.Stand;
import sk.miroc.whitebikes.data.models.StandBikes;

/**
 * Created by miroc on 18/03/2017.
 * Only Temporary api for testing
 */

public interface OldApi {
    // call with ?action=map:markers
    @GET("/command.php")
    Call<List<Stand>> getStands(@Query("action") String action);

    // call with ?action=list&stand=MANDERLAK
    @GET("/command.php")
    Call<StandBikes> getStandBikes(@Query("action") String action, @Query("stand") String stand);

    // call with ?action=login
    @FormUrlEncoded
    @POST("/command.php")
    Call<List<Stand>> login(@Query("action") String action, @Field("number") String number, @Field("password") String password);
}
