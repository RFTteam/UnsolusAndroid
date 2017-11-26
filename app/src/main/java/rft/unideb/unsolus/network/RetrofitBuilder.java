package rft.unideb.unsolus.network;

import android.os.Build;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import rft.unideb.unsolus.BuildConfig;

/**
 * Created by Tibor on 2017. 11. 21..
 */

public class RetrofitBuilder {

    private static final String BASE_URL = "https://127.0.0.1:8000/api/";

    private final static OkHttpClient client = buildClient();
    private final static Retrofit retrofit = buildRetrofit(client);

    private static OkHttpClient buildClient(){
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder()
                                                  .addHeader("Accept", "application/json")
                                                  .addHeader("Connection", "close");

                request = builder.build();

                return chain.proceed(request);
            }
        });

        if (BuildConfig.DEBUG){
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        return builder.build();
    }

    private static Retrofit buildRetrofit(OkHttpClient client){
        return new Retrofit.Builder()
                           .baseUrl(BASE_URL)
                           .client(client)
                           .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    public static <T> T createService(Class<T> service){
        return retrofit.create(service);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
