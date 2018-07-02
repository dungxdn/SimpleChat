package jp.bap.traning.simplechat.presenter.updateuser;

import jp.bap.traning.simplechat.response.BaseResponse;

public class UpdateUserPresenter {

    private UpdateUserInteractor mUpdateUserInteractor;

    public UpdateUserPresenter() {
        mUpdateUserInteractor = new UpdateUserInteractor();
    }

    public void updateUser(String firstName, String lastName, String avatar, UpdateUserView callback){
        mUpdateUserInteractor.updateUser(firstName, lastName, avatar, new UpdateUserView() {
            @Override
            public void onSuccess(BaseResponse result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(String message, int code) {
                callback.onError(message, code);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }
}
