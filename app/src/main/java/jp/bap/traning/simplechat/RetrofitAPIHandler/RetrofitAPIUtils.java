package jp.bap.traning.simplechat.RetrofitAPIHandler;

import jp.bap.traning.simplechat.interfaces.ApiService;

public class RetrofitAPIUtils {

    public static final String BASE_URL = "http://172.16.1.77:3000";

    public static ApiService getApiClient(){
        return RetrofitAPIClient.getClient(BASE_URL).create(ApiService.class);
    }
}
