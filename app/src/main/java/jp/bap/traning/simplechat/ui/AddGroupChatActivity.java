package jp.bap.traning.simplechat.ui;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import io.realm.RealmList;
import java.util.ArrayList;
import java.util.List;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.RoomData;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomPresenter;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomView;
import jp.bap.traning.simplechat.response.AddRoomResponse;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.widget.CustomToolbar_;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_add_group_chat)
public class AddGroupChatActivity extends BaseActivity {

    private String TITLE = "New Group";
    private AddGroupChatAdapter mAddGroupChatAdapter;
    private ArrayList<User> mUserList;
    private List<Integer> mIdList;
    private AddRoomPresenter mAddRoomPresenter;
    private static int sTYPE_GROUP = 1;
    private RealmList<User> mUserRealmList;
    private static final int sDEFAULT_VALUE_IF_NOT_EXITS_GROUP = 0;
    private static String TAG = "AddGroupChat";

    @ViewById
    CustomToolbar_ mToolbar;

    @ViewById
    RecyclerView mRecyclerFriend;

    @ViewById
    ProgressBar mProgressBar;

    @AfterViews
    public void afterView() {
        setupToolbar();
        init();
    }

    @Click
    void mBtnCreate(){
        showProgressBar(mProgressBar);
        //if haven't pick someone
        if (mIdList.size() <= 0){
            Toast.makeText(this, "Pick someone!", Toast.LENGTH_SHORT).show();
            hiddenProgressBar(mProgressBar);
            return;
        }
        //
        List<Integer> mIdListFully = new ArrayList<>();
        mIdListFully.clear();
        mIdListFully.add(Common.getUserLogin().getId());
        for (Integer i : mIdList) {
            mIdListFully.add(i);
        }
        //if group have 2 people
        if (mIdListFully.size() == 2){
            int result = isRoomExits(new RoomDAO().getAllRoom(), mIdListFully);
            sTYPE_GROUP = 0;
            if (result != sDEFAULT_VALUE_IF_NOT_EXITS_GROUP){
                Log.d(TAG, "Get Exists Group (" + result +")");
                ChatTalksActivity_.intent(AddGroupChatActivity.this)
                        .roomId(result)
                        .start();
                hiddenProgressBar(mProgressBar);
                finish();
                return;
            }
        } else {
            sTYPE_GROUP = 1;
        }
        //init RealmList
        mUserRealmList.clear();
        for (Integer i : mIdList) {
            for (User u : mUserList) {
                if (u.getId() == i){
                    mUserRealmList.add(u);
                    break;
                }
            }
        }
        mUserRealmList.add(Common.getUserLogin());
        //add Room to server
        mAddRoomPresenter.addroom(mIdList, sTYPE_GROUP, new AddRoomView() {
            @Override
            public void onSuccess(AddRoomResponse result) {
                //insert or update room to Realm
                hiddenProgressBar(mProgressBar);
                Room mRoom = new Room();
                RoomData mRoomData = result.getData();
                mRoom.setRoomId(mRoomData.getRoomId());
                mRoom.setType(mRoomData.getType());
                mRoom.setUsers(mUserRealmList);
                new RoomDAO().insertOrUpdate(mRoom);
                mUserRealmList.clear();
                //Start ChatActivity
                Log.d(TAG, "Create New Group");
                ChatTalksActivity_.intent(AddGroupChatActivity.this)
                        .roomId(result.getData().getRoomId())
                        .start();
                finish();
            }

            @Override
            public void onError(String message, int code) {
                hiddenProgressBar(mProgressBar);
                Toast.makeText(AddGroupChatActivity.this, code + ", " +message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                hiddenProgressBar(mProgressBar);
                Toast.makeText(AddGroupChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupToolbar(){
        mToolbar.setTitle(TITLE);
        mToolbar.getBackButton().setOnClickListener(v -> finish());
        mToolbar.getSettingButton().setVisibility(View.GONE);
    }

    public void init(){
        mProgressBar.setVisibility(View.GONE);
        if (ChatService.getChat() != null) {
            ChatService.getChat().getUsersOnline();
        }
        mUserRealmList = new RealmList<>();
        mAddRoomPresenter = new AddRoomPresenter();
        mIdList = new ArrayList<>();
        if (mIdList.size() != 0){
            mIdList.clear();
        }
        mUserList = new ArrayList<>();
        mAddGroupChatAdapter = new AddGroupChatAdapter(this, mUserList,
                new AddGroupChatAdapter.Listener() {
                    @Override
                    public void onAddId(int id) {
                        mIdList.add(id);
                    }

                    @Override
                    public void onRemoveId(int id) {
                        mIdList.remove(new Integer(id));
                    }
                });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerFriend.setLayoutManager(mLayoutManager);
        mRecyclerFriend.setAdapter(mAddGroupChatAdapter);
        mAddGroupChatAdapter.notifyDataSetChanged();
    }

    //get friend list from API

    @Override
    public void onReceiverListUsersOnline(ArrayList<User> users) {
        super.onReceiverListUsersOnline(users);
        mUserList.clear();
        for (User u : users){
            mUserList.add(u);
        }
        mAddGroupChatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUserOnline(User users) {
        super.onUserOnline(users);
        mUserList.add(users);
        mAddGroupChatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUserOffline(User users) {
        super.onUserOffline(users);
        mUserList.remove(users);
        mAddGroupChatAdapter.notifyDataSetChanged();
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
}
