package jp.bap.traning.simplechat.service;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dungpv on 6/14/18.
 */

public class ForbiddenInterceptor implements Interceptor {

    private static final String TAG = ForbiddenInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response.code() == 403) {
            Log.e(TAG, "intercept: ");
        }
        return response;
    }
}