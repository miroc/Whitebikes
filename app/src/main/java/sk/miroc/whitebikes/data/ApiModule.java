package sk.miroc.whitebikes.data;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {
    private String baseUrl;
    public ApiModule(String baseUrl){
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    public OldApi provideOldApi(Retrofit retrofit) {
        return retrofit.create(OldApi.class);
    }

//    @Provides
//    @Singleton
//    public Retrofit provideRetrofit(Converter.Factory converterFactory,
//                                    OkHttpClient okHttpClient) {
//        return new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(converterFactory)
//                .client(okHttpClient)
//                .build();
//    }

    @Provides
    @Singleton
    public Retrofit provideRetrofitWithCookies(
            Converter.Factory converterFactory,
            OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .build();
    }

    @Provides
    @Singleton
    public Converter.Factory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }


    @Singleton
    @Provides
    public Gson provideGson() {
        return new Gson();
    }

}
