package sk.miroc.whitebikes.utils.networking;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import sk.miroc.whitebikes.login.service.CookiesRepository;

public class AddCookiesInterceptor implements Interceptor {
    private final CookiesRepository repository;

    public AddCookiesInterceptor(CookiesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        for (String cookie : repository.getSet()) {
            builder.addHeader("Cookie", cookie);
        }
        return chain.proceed(builder.build());
    }
}
