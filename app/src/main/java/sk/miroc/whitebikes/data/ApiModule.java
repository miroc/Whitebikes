package sk.miroc.whitebikes.data;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
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
    public OldApi provideBackendApiService(Retrofit retrofit) {
        return retrofit.create(OldApi.class);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Converter.Factory converterFactory,
                                    OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
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
