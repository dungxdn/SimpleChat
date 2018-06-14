package jp.bap.traning.simplechat.RetrofitAPIHandler;

import jp.bap.traning.simplechat.interfaces.APIInterface;

public class RetrofitAPIUtils {

    public static final String BASE_URL = "http://172.16.1.77:3000";

    public static APIInterface getApiClient(){
        return RetrofitAPIClient.getClient(BASE_URL).create(APIInterface.class);
    }
}
