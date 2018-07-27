package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.RoomData;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomPresenter;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomView;
import jp.bap.traning.simplechat.presenter.message.MessagePresenter;
import jp.bap.traning.simplechat.response.AddRoomResponse;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;
import jp.bap.traning.simplechat.widget.CustomToolbar_;

@EActivity(R.layout.activity_sharing_message)
public class SharingMessageActivity extends BaseActivity  {
    private ArrayList<User> mUserList;
    private List<Integer> mIdListPick;
    private SharingMessageAdapter sharingMessageAdapter;
    private ArrayList<Room> roomsExist;
    private static int sRoomId;
    private AddRoomPresenter mAddRoomPresenter2;
    private ArrayList<Integer> listUsersId;
    private static final int sDEFAULT_VALUE_IF_NOT_EXITS_GROUP = 0;
    private RealmList<User> mUserRealmList;

    @ViewById
    CustomToolbar_ mToolbar;
    @ViewById
    AppCompatTextView txtSharingMessage;
    @ViewById
    RecyclerView mRecyclerUser;
    @ViewById
    ProgressBar mProgressBar;
    @Extra
    Message message;
    @Click
    void btnShareMessage() {
        showProgressBar(mProgressBar);
        if (mIdListPick.size() <= 0){
            hiddenProgressBar(mProgressBar);
            Toast.makeText(this, "Pick someone!", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Integer i : mIdListPick) {
            if (checkValidUserInAllRoom(roomsExist, i) == true) {
                if (ChatService.getChat() != null) {
                    Message mMessage = new Message(message.getContent(), Common.getUserLogin().getId(), sRoomId, message.getType());
                    ChatService.getChat().emitSendMessage(mMessage, sRoomId);
                    //Save into Realm Database
                    new MessagePresenter().insertOrUpdateMessage(mMessage);
                }
            } else {
                listUsersId.clear();
                listUsersId.add(i);
                    int result = isRoomExits(new RoomDAO().getAllRoom(), listUsersId);
                    if (result == sDEFAULT_VALUE_IF_NOT_EXITS_GROUP){
                     //Tao nhom roi gui tin nhan
                     mAddRoomPresenter2.addroom(listUsersId, 0, null, null, new AddRoomView() {
                         @Override
                         public void onSuccess(AddRoomResponse result) {
                             mUserRealmList.add(Common.getUserLogin());
                             mUserRealmList.add(getUserFromId(mUserList,i));
                             sRoomId = result.getData().getRoomId();
                             Room mRoom = new Room();
                             RoomData mRoomData = result.getData();
                             mRoom.setRoomId(mRoomData.getRoomId());
                             mRoom.setType(mRoomData.getType());
                             mRoom.setUsers(mUserRealmList);
                             mRoom.setRoomName(mRoomData.getRoomName());
                             new RoomDAO().insertOrUpdate(mRoom);
                             mUserRealmList.clear();

                            if (ChatService.getChat() != null) {
                                Message mMessage = new Message(message.getContent(), Common.getUserLogin().getId(), sRoomId, message.getType());
                                ChatService.getChat().emitSendMessage(mMessage, sRoomId);
                                //Save into Realm Database
                                new MessagePresenter().insertOrUpdateMessage(mMessage);
                            }
                        }

                         @Override
                         public void onError(String message, int code) {}

                         @Override
                         public void onFailure() {}
                     });
                    }
                }
            }
        hiddenProgressBar(mProgressBar);
        finish();

    }

    @Override
    public void afterView() {
        setupToolbar();
        init();
    }

    private void init() {
        //getALlRoom
        roomsExist = new RoomDAO().getAllRoom();
        txtSharingMessage.setText(message.getContent());
        mProgressBar.setVisibility(View.GONE);
        mUserList = new ArrayList<>();
        if (ChatService.getChat() != null) {
            ChatService.getChat().getUsersOnline();
        }
        mIdListPick = new ArrayList<>();
        if (mIdListPick.size() != 0){
            mIdListPick.clear();
        }

        sharingMessageAdapter = new SharingMessageAdapter(this, mUserList, new SharingMessageAdapter.Listener() {
            @Override
            public void onAddId(int id) {
                mIdListPick.add(id);
            }

            @Override
            public void onRemoveId(int id) {
                mIdListPick.remove(new Integer(id));
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerUser.setLayoutManager(mLayoutManager);
        mRecyclerUser.setAdapter(sharingMessageAdapter);
        sharingMessageAdapter.notifyDataSetChanged();
        //
        mAddRoomPresenter2 = new AddRoomPresenter();
        listUsersId = new ArrayList<>();
        mUserRealmList = new RealmList<>();
    }

    public void setupToolbar() {
        mToolbar.setTitle(getResources().getString(R.string.title_sharing_message));
        mToolbar.getBackButton().setOnClickListener(v -> finish());
        mToolbar.getSettingButton().setVisibility(View.GONE);
    }

    @Override
    public void onReceiverListUsersOnline(ArrayList<User> users) {
        super.onReceiverListUsersOnline(users);
        mUserList.clear();
        for (User u : users) {
            mUserList.add(u);
        }
        sharingMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUserOnline(User users) {
        super.onUserOnline(users);
        mUserList.add(users);
        sharingMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUserOffline(User users) {
        super.onUserOffline(users);
        mUserList.remove(users);
        sharingMessageAdapter.notifyDataSetChanged();
    }

    public boolean checkValidUserInAllRoom(ArrayList<Room> rooms, int id) {
        for(Room room : rooms) {
            if(room.getType()==0) {
                for(User user : room.getUsers()) {
                    if(id == user.getId()) {
                        sRoomId = room.getRoomId();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int isRoomExits(List<Room> roomList, List<Integer> idList ){
        for (Room r : roomList){
            if (r.getUsers().size() == idList.size()) {
                if (isListIdExitInRoom(r, idList)) return r.getRoomId();
            }
        }
        return sDEFAULT_VALUE_IF_NOT_EXITS_GROUP;
    }
    public boolean isListIdExitInRoom(Room room, List<Integer> listId){
        for (Integer i : listId){
            if (!isIdExitsInListUser(room.getUsers(), i)) return false;
        }
        return true;
    }

    public boolean isIdExitsInListUser(List<User> userList, int id){
        for (User u : userList){
            if (id == u.getId()) return true;
        }
        return false;
    }

    public User getUserFromId(ArrayList<User> users,int id) {
        for(User user : users) {
            if(user.getId()==id) {
                return user;
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
