package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;

import static jp.bap.traning.simplechat.model.User.userComparator;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_friend)
public class FriendFragment extends BaseFragment implements FriendAdapter.Listener {
    private int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
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

    @Override
    public void afterView() {
        init();
    }

    private void init() {
        if (ChatService.getChat() != null) {
            ChatService.getChat().getUsersOnline();
        }
        User user = getUserLogin();
        mTvUserName.setText(user.getFirstName() + " " + user.getLastName());
        mUserList = new ArrayList<>();
        //
        mFriendAdapter = new FriendAdapter(getContext(), mUserList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerFriend.setLayoutManager(mLayoutManager);
        mRecyclerFriend.setItemAnimator(new DefaultItemAnimator());
        mRecyclerFriend.setAdapter(mFriendAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerFriend.getContext(), 1);
        mRecyclerFriend.addItemDecoration(mDividerItemDecoration);
        mFriendAdapter.notifyDataSetChanged();
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

    //get friend list from API
    @Override
    public void onReceiverListUsersOnline(ArrayList<User> users) {
        super.onReceiverListUsersOnline(users);
        for(int i = 0; i<users.size();i++) {
            mUserList.add(users.get(i));
        }
        mFriendAdapter.notifyDataSetChanged();
        mTvTitleFriend.setText(getString(R.string.title_friend) + " (" + mUserList.size() + ")");

    }

    //remove user offline
    @Override
    public void onUserOffline(User user) {
        super.onUserOffline(user);
        Toast.makeText(getContext(),"User Offline: "+user.getLastName(),Toast.LENGTH_LONG).show();
        mUserList.remove(user);
        mFriendAdapter.notifyDataSetChanged();
        mTvTitleFriend.setText(getString(R.string.title_friend) + " (" + mUserList.size() + ")");
    }

    //insert user online
    @Override
    public void onUserOnline(User users) {
        super.onUserOnline(users);
        boolean checkValidUser = mUserList.contains(users);
        if(users.getId()==mMineId) {

        }
        else if(checkValidUser == true) {

        }
        else {
            mUserList.add(users);
            Collections.sort(mUserList,userComparator);
            mFriendAdapter.notifyDataSetChanged();
            mTvTitleFriend.setText(getString(R.string.title_friend) + " (" + mUserList.size() + ")");
        }
    }


    //Chat
    @Override
    public void onChat(int userId) {
        Room room = Common.getRoomWithUser(userId);
        if (room != null) {
            ChatTalksActivity_.intent(this)
                    .roomId(room.getRoomId())
                    .start();
        } else {
            // TODO: 6/18/18 Tao room bang API 
        }
    }

    @Override
    public void onCallVideo(int userId) {

    }

    @Override
    public void onCallAudio(int userId) {

    }
}
