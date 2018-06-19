package jp.bap.traning.simplechat.presenter.addrooms;

import android.util.Log;
import java.util.List;
import jp.bap.traning.simplechat.response.AddRoomResponse;

public class AddRoomPresenter implements AddRoomView{

    private AddRoomView mAddRoomView;
    private AddRoomInteractor mAddRoomInteractor;

    public AddRoomPresenter(AddRoomView addRoomView) {
        this.mAddRoomInteractor = new AddRoomInteractor();
        mAddRoomView = addRoomView;
    }

    public void addroom(List<Integer> ids, int type){
        mAddRoomInteractor.addRoom(ids, type, mAddRoomView);
        Log.e("addRoom", "AddRoomPresenter");
    }

    @Override
    public void onAddRoomSuccess(AddRoomResponse addRoomResponse) {
        mAddRoomView.onAddRoomSuccess(addRoomResponse);
    }

    @Override
    public void onAddRoomFail() {

    }

    @Override
    public void onSuccess(AddRoomResponse result) {

    }

    @Override
    public void onError(String message, int code) {

    }
}
