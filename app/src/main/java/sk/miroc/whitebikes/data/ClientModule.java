package sk.miroc.whitebikes.data;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import sk.miroc.whitebikes.login.service.CookiesRepository;
import sk.miroc.whitebikes.utils.networking.AddCookiesInterceptor;
import sk.miroc.whitebikes.utils.networking.ReceivedCookiesInterceptor;

@Module
public class ClientModule {
    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor,
                                            AddCookiesInterceptor cookiesInterceptor,
                                            ReceivedCookiesInterceptor receivedCookiesInterceptor,
                                            @Named("isDebug") boolean isDebug){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(false)
                .addInterceptor(cookiesInterceptor)
                .addInterceptor(receivedCookiesInterceptor);
        if (isDebug){
            builder.addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(loggingInterceptor);
        }
        return builder.build();
    }

    @Singleton
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @Provides
    @Singleton
    public AddCookiesInterceptor provideAddCookiesInterceptor(CookiesRepository repository){
        return new AddCookiesInterceptor(repository);
    }

    @Provides
    @Singleton
    public ReceivedCookiesInterceptor provideReceivedCookiesInterceptor(CookiesRepository repository){
        return new ReceivedCookiesInterceptor(repository);
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

