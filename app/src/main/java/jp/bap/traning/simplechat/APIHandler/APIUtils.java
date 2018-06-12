package jp.bap.traning.simplechat.APIHandler;

import jp.bap.traning.simplechat.interfaces.APIInterface;

public class APIUtils {

    public static final String BASE_URL = "http://172.16.1.77:3000";

    public static APIInterface getApiClient(){
        return APIClient.getClient(BASE_URL).create(APIInterface.class);
    }
}
