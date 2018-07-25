package jp.bap.traning.simplechat.presenter.addrooms;

import java.util.List;

import javax.annotation.Nullable;
import jp.bap.traning.simplechat.response.AddRoomResponse;

public class AddRoomPresenter {

    private AddRoomInteractor mAddRoomInteractor;

    public AddRoomPresenter() {
        this.mAddRoomInteractor = new AddRoomInteractor();
    }

    public void addroom(List<Integer> ids, int type, @Nullable String roomName,
            @Nullable String avatar, AddRoomView callback) {
        mAddRoomInteractor.addRoom(ids, type, roomName, avatar, new AddRoomView() {
            @Override
            public void onSuccess(AddRoomResponse result) {
                result.getData().setType(type);
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
