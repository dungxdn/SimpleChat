package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

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

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_friend)
public class FriendFragment extends BaseFragment implements FriendExpandLvAdapter.Listener {
    private int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
//    @ViewById
//    CircleImageView mImgAvatar;
//    @ViewById
//    AppCompatTextView mTvUserName;
//    @ViewById
//    AppCompatTextView mTvTitleFriend;
//    @ViewById
//    AppCompatTextView mtvStatus;
    @ViewById
    ExpandableListView mExpandFriend;
    private ArrayList<User> mUserList;
    private FriendExpandLvAdapter mFriendAdapter;
    private ArrayList<String> mListheader;
    private HashMap<String, ArrayList<User>> mDataUser;
    private AddRoomPresenter mAddRoomPresenter;
    private List<Integer> mListUserId;
    private RealmList<User> mUserRealmList;
    private static int sTYPE_2PERSON = 0;

    @Override
    public void afterView() {
        init();
    }

    private void init() {
        if (ChatService.getChat() != null) {
            ChatService.getChat().getUsersOnline();
        }
        mUserList = new ArrayList<>();

        mAddRoomPresenter = new AddRoomPresenter(new AddRoomView() {
            @Override
            public void onAddRoomSuccess(AddRoomResponse addRoomResponse) {
                //TODO: Save to Realm, Start ChatActivity
                //Save to Realm
                Room mRoom = new Room();
                RoomData roomData = addRoomResponse.getData();
                mRoom.setRoomId(roomData.getRoomId());
                mRoom.setType(roomData.getType());
                mRoom.setUsers(mUserRealmList);
                new RoomDAO().insertOrUpdate(mRoom);
                //Start ChatActivity
                ChatTalksActivity_.intent(FriendFragment.this)
                        .roomId(addRoomResponse.getData().getRoomId())
                        .start();
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
        });
        mListUserId = new ArrayList<>();
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
//        mExpandFriend.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                Log.d("onGroupClick:", "worked");
//                parent.expandGroup(groupPosition);
//                return false;
//            }
//        });


//        mFriendAdapter = new FriendAdapter(getContext(), mUserList, this);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        mRecyclerFriend.setLayoutManager(mLayoutManager);
//        mRecyclerFriend.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerFriend.setAdapter(mFriendAdapter);
//        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerFriend.getContext(), 1);
//        mRecyclerFriend.addItemDecoration(mDividerItemDecoration);
//        mFriendAdapter.notifyDataSetChanged();
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

        } else if (checkValidUser == true) {

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
        Room room = Common.getRoomWithUser(user.getId());
        if (room != null) {
            ChatTalksActivity_.intent(this)
                    .roomId(room.getRoomId())
                    .start();
        } else {
            // TODO: 6/18/18 Tao room bang API
            // add Room
            mListUserId.add(user.getId());
            mAddRoomPresenter.addroom(mListUserId, sTYPE_2PERSON);
            mUserRealmList.add(user);
        }
    }

    @Override
    public void onCallVideo(int userId) {

    }

    @Override
    public void onCallAudio(int userId) {

    }
}
