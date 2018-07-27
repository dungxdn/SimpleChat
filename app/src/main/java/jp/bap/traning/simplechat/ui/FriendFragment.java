package jp.bap.traning.simplechat.ui;

import android.widget.ExpandableListView;
import android.widget.Toast;

import io.realm.RealmList;

import java.util.List;

import jp.bap.traning.simplechat.database.RealmDAO;
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

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;

import static io.realm.RealmObject.removeAllChangeListeners;

import static jp.bap.traning.simplechat.model.User.userComparator;
import static jp.bap.traning.simplechat.utils.Common.getUserLogin;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_friend)
public class FriendFragment extends BaseFragment implements FriendExpandLvAdapter.Listener {

    @ViewById
    ExpandableListView mExpandFriend;
    private ArrayList<User> mUserList;
    private FriendExpandLvAdapter mFriendAdapter;
    private ArrayList<String> mListheader;
    private HashMap<String, ArrayList<User>> mDataUser;
    private AddRoomPresenter mAddRoomPresenter;
    private List<Integer> mListUserId;
    private User mUserLogin = getUserLogin();
    private GetRoomPresenter mGetRoomPresenter;
    private RealmList<User> mUserRealmList;
    private ArrayList<User> me;
    private RealmDAO mRealmDAO;
    private static final String ACTIVITY_VIDEO_CALL = "VideoCallActivity";
    private static final String ACTIVITY_AUDIO_CALL = "AudioCallActivity";
    private static final String ACTIVITY_CHAT = "ChatTalkActivity";

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
        mRealmDAO = new RealmDAO();
        mListUserId = new ArrayList<>();
        mGetRoomPresenter = new GetRoomPresenter();
        mUserRealmList = new RealmList<>();

        mListheader = new ArrayList<>();
        mListheader.add(getString(R.string.title_me));
        mListheader.add(getString(R.string.title_friend));

        //Create list include mine user to add to HashMap
        me = new ArrayList<>();

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
    public void onStart() {
        super.onStart();
        mRealmDAO.realmChanged((o, check) -> {
            me.clear();
            me.add(Common.getUserLogin());
            mFriendAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //rove MessageListener
        mRealmDAO.removeRealmChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        me.clear();
        me.add(Common.getUserLogin());
        mFriendAdapter.notifyDataSetChanged();
        if (ChatService.getChat() != null) {
            ChatService.getChat().getUsersOnline();
        }
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
    }

    //remove user offline
    @Override
    public void onUserOffline(User user) {
        super.onUserOffline(user);
        mUserList.remove(user);
        mFriendAdapter.notifyDataSetChanged();
    }

    //insert user online
    @Override
    public void onUserOnline(User users) {
        super.onUserOnline(users);
        if (users.getId() == Common.getUserLogin().getId()) {

        } else if (checkValidUser(users.getId()) >= 0) {        //update User
            mUserList.set(checkValidUser(users.getId()), users);
            Collections.sort(mUserList, userComparator);
            mFriendAdapter.notifyDataSetChanged();
        } else {                                                //add User
            mUserList.add(users);
            Collections.sort(mUserList, userComparator);
            mFriendAdapter.notifyDataSetChanged();
        }
    }

    private int checkValidUser(int id) {
        for (int i = 0; i < mUserList.size(); i++) {
            if (id == mUserList.get(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    //Chat
    @Override
    public void onChat(User user) {
        ((MainActivity) getActivity()).showProgressBar();
        //get room from realm.
        Room room = Common.getRoomWithUser(user.getId());
        if (room != null) {
            ChatTalksActivity_.intent(this).roomId(room.getRoomId()).start();
            ((MainActivity) getActivity()).hiddenProgressBar();
        } else {
            addRoomAndSaveRoomToRealm(mUserRealmList, mListUserId, user.getId(), ACTIVITY_CHAT);
        }
    }

    @Override
    public void onCallAudio(int userId) {
        ((MainActivity) getActivity()).showProgressBar();
        //get room from realm.
        Room room = Common.getRoomWithUser(userId);
        if (room != null) {
            CallActivity_.intent(getContext())
                    .roomId(room.getRoomId())
                    .isIncoming(false)
                    .isAudioCall(true)
                    .start();
            ((MainActivity) getActivity()).hiddenProgressBar();
            getActivity().overridePendingTransition(R.anim.anim_from_midle, R.anim.anim_to_midle);
        } else {
            addRoomAndSaveRoomToRealm(mUserRealmList, mListUserId, userId, ACTIVITY_VIDEO_CALL);
        }
    }

    @Override
    public void onCallVideo(int userId) {
        ((MainActivity) getActivity()).showProgressBar();
        //get room from realm.
        Room room = Common.getRoomWithUser(userId);
        if (room != null) {
            CallActivity_.intent(getContext())
                    .roomId(room.getRoomId())
                    .isIncoming(false)
                    .isAudioCall(false)
                    .start();
            ((MainActivity) getActivity()).hiddenProgressBar();
            getActivity().overridePendingTransition(R.anim.anim_from_midle, R.anim.anim_to_midle);
        } else {
            addRoomAndSaveRoomToRealm(mUserRealmList, mListUserId, userId, ACTIVITY_VIDEO_CALL);
        }
    }

    public void addRoomAndSaveRoomToRealm(RealmList<User> mUserRealmList, List<Integer> mListUserId,
                                          int userId, String activity) {
        if (mUserRealmList.size() != 0) {
            mUserRealmList.clear();
        }
        if (mListUserId.size() != 0) {
            mListUserId.clear();
        }
        mListUserId.add(userId);
        mAddRoomPresenter.addroom(mListUserId, Common.TYPE_GROUP_TWO_PEOPLE, "", null, new AddRoomView() {
            @Override
            public void onSuccess(AddRoomResponse result) {
                Room mRoom = new Room();
                RoomData roomData = result.getData();
                mRoom.setRoomId(roomData.getRoomId());
                mRoom.setType(roomData.getType());
                mGetRoomPresenter.getRoom(roomData.getRoomId(), new GetRoomView() {
                    @Override
                    public void onSuccess(GetRoomResponse result) {
                        List<User> mUserInRoomList = result.getData().getUsers();
                        for (User u : mUserInRoomList) {
                            mUserRealmList.add(u);
                        }
                        mRoom.setUsers(mUserRealmList);
                        new RoomDAO().insertOrUpdate(mRoom);
                        ((MainActivity) getActivity()).hiddenProgressBar();
                        //Start Activity
                        switch (activity) {
                            case ACTIVITY_VIDEO_CALL:
                                //Start CallActivity
                                CallActivity_.intent(getContext())
                                        .roomId(result.getData().getRoomId())
                                        .isIncoming(false)
                                        .isAudioCall(false)
                                        .start();
                                ((MainActivity) getActivity()).hiddenProgressBar();
                                getActivity().overridePendingTransition(R.anim.anim_from_midle, R.anim.anim_to_midle);
                                break;
                            case ACTIVITY_CHAT:
                                //Start ChatActivity
                                ChatTalksActivity_.intent(FriendFragment.this)
                                        .roomId(result.getData().getRoomId())
                                        .start();
                                break;
                            case ACTIVITY_AUDIO_CALL:
                                //Start CallActivity
                                CallActivity_.intent(getContext())
                                        .roomId(result.getData().getRoomId())
                                        .isIncoming(false)
                                        .isAudioCall(true)
                                        .start();
                                ((MainActivity) getActivity()).hiddenProgressBar();
                                getActivity().overridePendingTransition(R.anim.anim_from_midle, R.anim.anim_to_midle);
                                break;
                        }
                    }

                    @Override
                    public void onError(String message, int code) {
                        ((MainActivity) getActivity()).hiddenProgressBar();
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    @Override
                    public void onFailure() {
                        ((MainActivity) getActivity()).hiddenProgressBar();
                        Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }

            @Override
            public void onError(String message, int code) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).hiddenProgressBar();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Fail!", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).hiddenProgressBar();
            }
        });
    }



}
