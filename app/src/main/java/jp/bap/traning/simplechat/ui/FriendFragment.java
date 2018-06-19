package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import io.realm.Realm;
import io.realm.RealmList;
import java.util.List;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomPresenter;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomView;
import jp.bap.traning.simplechat.response.AddRoomResponse;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_friend)
public class FriendFragment extends BaseFragment implements FriendAdapter.Listener, AddRoomView {
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
    private RealmList<User> mUserRealmList;
    private static int sTYPE_2PERSON = 0;

    @Override
    public void afterView() {
        init();
    }

    private void init() {
        User user = getUserLogin();
        mTvUserName.setText(user.getFirstName() + " " + user.getLastName());

        mUserList = new ArrayList<>();
        mUserList = getAllFriend();

        mAddRoomPresenter = new AddRoomPresenter(this);
        mListUserId = new ArrayList<>();

        mFriendAdapter = new FriendAdapter(getContext(), mUserList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerFriend.setLayoutManager(mLayoutManager);
        mRecyclerFriend.setItemAnimator(new DefaultItemAnimator());
        mRecyclerFriend.setAdapter(mFriendAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerFriend.getContext(), 1);
        mRecyclerFriend.addItemDecoration(mDividerItemDecoration);

    }

    @Override
    public void onResume() {
        super.onResume();
        mTvTitleFriend.setText(getString(R.string.title_friend) + " (" + mUserList.size() + ")");
        mFriendAdapter.notifyDataSetChanged();
    }

    private User getUserLogin() {
        int id = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
        //get user from Realm
        return new UserDAO().getUser(id);
    }

    //get friend list from API
    private ArrayList<User> getAllFriend() {
        //fake data
        ArrayList<User> list = new ArrayList<>();
        User user1 = new User();
        user1.setFirstName("User 1");
        list.add(user1);

        User user2 = new User();
        user2.setFirstName("User 2");
        list.add(user2);

        User user3 = new User();
        user3.setFirstName("User 3");
        list.add(user3);
        list.add(user3);
        list.add(user3);
        list.add(user3);
        list.add(user3);
        list.add(user3);
        list.add(user3);
        list.add(user3);
        list.add(user3);
        list.add(user3);
        list.add(user3);
        return list;

    }

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
            Log.e("addRoom", "addRoom");
        }
    }

    @Override
    public void onCallVideo(int userId) {

    }

    @Override
    public void onCallAudio(int userId) {

    }

    @Override
    public void onAddRoomSuccess(AddRoomResponse addRoomResponse) {
        //TODO: Save to Realm, Start ChatActivity
        //Save to Realm
        List<Room> mListRoom = new ArrayList<>();
        Room mRoom = new Room();
        mRoom.setRoomId(addRoomResponse.getData().getIdRoom());
        mRoom.setType(sTYPE_2PERSON);
        mRoom.setUsers(mUserRealmList);
        mListRoom.add(mRoom);
        new RoomDAO().insertOrUpdate(mListRoom);
        //Start ChatActivity
        ChatTalksActivity_.intent(this)
                .roomId(addRoomResponse.getData().getIdRoom())
                .start();
        Log.e("addRoom", "Success");
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
