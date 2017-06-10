package sk.miroc.whitebikes.data;


import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Module
public class ClientModule {

    private static final String HTTP_CACHE_PATH = "http-cache";
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String PRAGMA = "Pragma";

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(){
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        return okHttpClient.build();
    }

    @Singleton
    @Provides
    @Named("headersInterceptor")
    public Interceptor provideHeadersInterceptor() {
        return chain -> {
            Request original = chain.request();
            Request modified = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(modified);
        };
    }
}

