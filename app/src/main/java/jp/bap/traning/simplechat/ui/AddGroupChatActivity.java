package jp.bap.traning.simplechat.ui;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
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
    private static final int sTYPE_GROUP = 1;
    private RealmList<User> mUserRealmList;

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
        if (mIdList.size() <= 1){
            hiddenProgressBar(mProgressBar);
            Toast.makeText(this, "can not create group, please check!", Toast.LENGTH_SHORT).show();
            return;
        }
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
        mAddRoomPresenter.addroom(mIdList, sTYPE_GROUP, new AddRoomView() {
            @Override
            public void onSuccess(AddRoomResponse result) {
                hiddenProgressBar(mProgressBar);
                Room mRoom = new Room();
                RoomData mRoomData = result.getData();
                mRoom.setRoomId(mRoomData.getRoomId());
                mRoom.setType(mRoomData.getType());
                mRoom.setUsers(mUserRealmList);
                new RoomDAO().insertOrUpdate(mRoom);
                mUserRealmList.clear();
                //Start ChatActivity
                ChatTalksActivity_.intent(AddGroupChatActivity.this)
                        .roomId(result.getData().getRoomId())
                        .start();
                finish();
            }

            @Override
            public void onError(String message, int code) {
                hiddenProgressBar(mProgressBar);

            }

            @Override
            public void onFailure() {
                hiddenProgressBar(mProgressBar);

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
}
