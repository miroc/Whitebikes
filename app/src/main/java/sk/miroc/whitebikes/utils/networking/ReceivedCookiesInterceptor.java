package sk.miroc.whitebikes.utils.networking;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;

import okhttp3.Interceptor;
import okhttp3.Response;
import sk.miroc.whitebikes.login.service.CookiesRepository;
import timber.log.Timber;

public class ReceivedCookiesInterceptor implements Interceptor {
    private final CookiesRepository repository;
    public ReceivedCookiesInterceptor(CookiesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            Set<String> cookies = repository.getSet();
            for (String cookie : originalResponse.headers("Set-Cookie")){
                Timber.d("Intercepting cookie: %s", cookie);
                cookies.add(cookie);
            }

            repository.saveSet(cookies);
        }
        return originalResponse;
    }
}
