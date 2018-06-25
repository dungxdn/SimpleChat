package jp.bap.traning.simplechat.presenter.addrooms;

import java.util.List;
import jp.bap.traning.simplechat.response.AddRoomResponse;

public class AddRoomPresenter {

    private AddRoomInteractor mAddRoomInteractor;
    private int type;

    public AddRoomPresenter() {
        this.mAddRoomInteractor = new AddRoomInteractor();
    }

    public void addroom(List<Integer> ids, int type, AddRoomView callback) {
        this.type = type;
        mAddRoomInteractor.addRoom(ids, type, new AddRoomView() {
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
