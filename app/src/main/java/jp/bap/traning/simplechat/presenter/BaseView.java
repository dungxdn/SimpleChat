package jp.bap.traning.simplechat.presenter;

/**
 * Created by dungpv on 6/14/18.
 */

public interface BaseView<R> {
    void onSuccess(R result);

    void onError(String message, int code);

    void onFailure();
}
