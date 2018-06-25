package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import io.realm.RealmList;

import java.util.List;

import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.RoomData;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomPresenter;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomView;
import jp.bap.traning.simplechat.presenter.getroom.GetRoomPresenter;
import jp.bap.traning.simplechat.presenter.getroom.GetRoomView;
import jp.bap.traning.simplechat.response.AddRoomResponse;

import jp.bap.traning.simplechat.response.GetRoomResponse;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;

import static jp.bap.traning.simplechat.model.User.userComparator;
import static jp.bap.traning.simplechat.utils.Common.getUserLogin;
import static jp.bap.traning.simplechat.utils.SharedPrefs.KEY_SAVE_ID;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_friend)
public class FriendFragment extends BaseFragment implements FriendAdapter.Listener {
    private int mMineId = SharedPrefs.getInstance().getData(KEY_SAVE_ID, Integer.class);
    @ViewById
    CircleImageView mImgAvatar;
    @ViewById
    AppCompatTextView mTvUserName;
    @ViewById
    AppCompatTextView mTvTitleFriend;
    @ViewById
    AppCompatTextView mtvStatus;
    @ViewById
    RecyclerView mRecyclerFriend;
    private ArrayList<User> mUserList;
    private FriendAdapter mFriendAdapter;
    private AddRoomPresenter mAddRoomPresenter;
    private List<Integer> mListUserId;
    private static int sTYPE_2PERSON = 0;
    private User mUserLogin = getUserLogin();
    private GetRoomPresenter mGetRoomPresenter;
    private RealmList<User> mUserRealmList;

    @Override
    public void afterView() {
        init();
    }

    private void init() {
        if (ChatService.getChat() != null) {
            ChatService.getChat().getUsersOnline();
        }
        mTvUserName.setText(mUserLogin.getFirstName() + " " + mUserLogin.getLastName());

        mUserList = new ArrayList<>();
        mAddRoomPresenter = new AddRoomPresenter();
        mListUserId = new ArrayList<>();
        mGetRoomPresenter = new GetRoomPresenter();
        mUserRealmList = new RealmList<>();

        mFriendAdapter = new FriendAdapter(getContext(), mUserList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerFriend.setLayoutManager(mLayoutManager);
        mRecyclerFriend.setItemAnimator(new DefaultItemAnimator());
        mRecyclerFriend.setAdapter(mFriendAdapter);
        DividerItemDecoration mDividerItemDecoration =
                new DividerItemDecoration(mRecyclerFriend.getContext(), 1);
        mRecyclerFriend.addItemDecoration(mDividerItemDecoration);
        mFriendAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //get friend list from API
    @Override
    public void onReceiverListUsersOnline(ArrayList<User> users) {
        super.onReceiverListUsersOnline(users);
        mUserList.clear();
        for (int i = 0; i < users.size(); i++) {
            mUserList.add(users.get(i));
        }
        Collections.sort(mUserList, userComparator);
        mFriendAdapter.notifyDataSetChanged();
        mTvTitleFriend.setText(getString(R.string.title_friend) + " (" + mUserList.size() + ")");
    }

    //remove user offline
    @Override
    public void onUserOffline(User user) {
        super.onUserOffline(user);
        mUserList.remove(user);
        mFriendAdapter.notifyDataSetChanged();
        mTvTitleFriend.setText(
                getResources().getString(R.string.title_friend) + " (" + mUserList.size() + ")");
    }

    //insert user online
    @Override
    public void onUserOnline(User users) {
        super.onUserOnline(users);
        boolean checkValidUser = mUserList.contains(users);
        if (users.getId() == mMineId) {

        } else if (checkValidUser) {

        } else {
            mUserList.add(users);
            Collections.sort(mUserList, userComparator);
            mFriendAdapter.notifyDataSetChanged();
            mTvTitleFriend.setText(
                    getString(R.string.title_friend) + " (" + mUserList.size() + ")");
        }
    }

    //Chat
    @Override
    public void onChat(User user) {
        //get room from realm.
        Room room = Common.getRoomWithUser(user.getId());
        if (room != null) {
            ChatTalksActivity_.intent(this).roomId(room.getRoomId()).start();
        } else {
            // add Room
            if (mUserRealmList.size() != 0){
                mUserRealmList.clear();
            }
            if (mListUserId.size() != 0){
                mListUserId.clear();
            }
            mListUserId.add(user.getId());
            mAddRoomPresenter.addroom(mListUserId, sTYPE_2PERSON, new AddRoomView() {
                @Override
                public void onSuccess(AddRoomResponse result) {
                    //Save to Realm
                    Room mRoom = new Room();
                    RoomData roomData = result.getData();
                    mRoom.setRoomId(roomData.getRoomId());
                    mRoom.setType(roomData.getType());
                    mGetRoomPresenter.getRoom(roomData.getRoomId(), new GetRoomView() {
                        @Override
                        public void onSuccess(GetRoomResponse result) {
                            List<User> mUserInRoomList = result.getData().getUsers();
                            for (User u : mUserInRoomList){
                                mUserRealmList.add(u);
                            }
                            mRoom.setUsers(mUserRealmList);
                            new RoomDAO().insertOrUpdate(mRoom);
                        }

                        @Override
                        public void onError(String message, int code) {

                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                    //Start ChatActivity
                    ChatTalksActivity_.intent(FriendFragment.this)
                            .roomId(result.getData().getRoomId())
                            .start();
                }

                @Override
                public void onError(String message, int code) {

                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    @Override
    public void onCallVideo(int userId) {

    }

    @Override
    public void onCallAudio(int userId) {

    }
}
