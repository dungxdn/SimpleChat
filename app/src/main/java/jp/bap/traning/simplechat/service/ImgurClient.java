package jp.bap.traning.simplechat.service;

import jp.bap.traning.simplechat.interfaces.ApiService;
import jp.bap.traning.simplechat.interfaces.ImgurService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 6/27/2018.
 */

public class ImgurClient {
    private static ImgurClient sInstance;
    private ImgurService mImgurService;

    private ImgurClient() {

    }

    private synchronized static ImgurClient getInstance() {
        if (sInstance == null) {
            sInstance = new ImgurClient();
            sInstance.init();
        }
        return sInstance;
    }

    private void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imgur.com/3/")
//                .client(okHttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        mImgurService = retrofit.create(ImgurService.class);
    }

    public synchronized static ImgurService getService() {
        return getInstance().mImgurService;
    }
    public static void stopImgurClient() {
        sInstance = null;
    }
}
