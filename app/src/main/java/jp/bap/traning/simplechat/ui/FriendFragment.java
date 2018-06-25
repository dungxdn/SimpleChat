package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

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
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.RoomData;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomPresenter;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomView;
import jp.bap.traning.simplechat.response.AddRoomResponse;
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
public class FriendFragment extends BaseFragment implements FriendExpandLvAdapter.Listener {
    private int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);

    @ViewById
    ExpandableListView mExpandFriend;
    private ArrayList<User> mUserList;
    private FriendExpandLvAdapter mFriendAdapter;
    private ArrayList<String> mListheader;
    private HashMap<String, ArrayList<User>> mDataUser;
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
        mUserList = new ArrayList<>();

        mAddRoomPresenter = new AddRoomPresenter();
        mListUserId = new ArrayList<>();
        mGetRoomPresenter = new GetRoomPresenter();
        mUserRealmList = new RealmList<>();

        mListheader = new ArrayList<>();
        mListheader.add(getString(R.string.title_me));
        mListheader.add(getString(R.string.title_friend));

        //Create list include mine user to add to HashMap
        ArrayList<User> me = new ArrayList<>();
        me.add(getUserLogin());

        mDataUser = new HashMap<>();
        mDataUser.put(mListheader.get(0), me);
        mDataUser.put(mListheader.get(1), mUserList);
        mFriendAdapter = new FriendExpandLvAdapter(getActivity(), mListheader, mDataUser, this);
        mExpandFriend.setAdapter(mFriendAdapter);
        mExpandFriend.setGroupIndicator(null);


        for (int i = 0; i < mListheader.size(); i++) {
            mExpandFriend.expandGroup(i);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private User getUserLogin() {
        int id = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
        //get user from Realm
        return new UserDAO().getUser(id);
    }

    @Override
    public void onReceiverListUsersOnline(ArrayList<User> users) {
        super.onReceiverListUsersOnline(users);
        mUserList.clear();
        for (int i = 0; i < users.size(); i++) {
            mUserList.add(users.get(i));
        }
        Collections.sort(mUserList, userComparator);
        mFriendAdapter.notifyDataSetChanged();
//        mTvTitleFriend.setText(getString(R.string.title_friend) + " (" + mUserList.size() + ")");

    }

    //remove user offline
    @Override
    public void onUserOffline(User user) {
        super.onUserOffline(user);
        mUserList.remove(user);
        mFriendAdapter.notifyDataSetChanged();
//        mTvTitleFriend.setText(getResources().getString(R.string.title_friend) + " (" + mUserList.size() + ")");
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
//            mTvTitleFriend.setText(getString(R.string.title_friend) + " (" + mUserList.size() + ")");
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
