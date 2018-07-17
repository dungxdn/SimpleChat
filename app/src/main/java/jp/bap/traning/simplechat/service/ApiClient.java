package jp.bap.traning.simplechat.service;

import android.util.Log;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import jp.bap.traning.simplechat.BuildConfig;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;
import jp.bap.traning.simplechat.interfaces.ApiService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dungpv on 6/14/18.
 */

public class ApiClient {
    private static final long TIME_OUT = 300000;
    private static final String AUTHORIZATION = "Authorization";
    private volatile static ApiClient sInstance;
    private ApiService mApiService;

    private ApiClient() {

    }

    private static ApiClient getInstance() {
        if (sInstance == null) {
            sInstance = new ApiClient();
            sInstance.init();
        }
        return sInstance;
    }

    public synchronized static ApiService getService() {
        return getInstance().mApiService;
    }

    private void init() {
        String host = Common.URL_SERVER;
        int auth = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);

        // init OkHttpClient
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient().newBuilder();
        okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);

        // Log
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.interceptors().add(logInterceptor);
        }
        // AUTHORIZATION
        okHttpBuilder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header(AUTHORIZATION, String.valueOf(auth))
                    .addHeader("Accept-Language", Locale.getDefault().getLanguage())
                    .method(original.method(), original.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .client(okHttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        mApiService = retrofit.create(ApiService.class);
    }

    public static void stopApilient() {
        sInstance = null;
    }
}
