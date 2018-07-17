package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import jp.bap.traning.simplechat.database.MessageDAO;

import jp.bap.traning.simplechat.database.RealmDAO;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.message.MessagePresenter;
import jp.bap.traning.simplechat.presenter.message.MessageView;
import jp.bap.traning.simplechat.utils.Common;

import static jp.bap.traning.simplechat.model.Room.roomComparator;
import static jp.bap.traning.simplechat.model.User.userComparator;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_chat)
public class ChatFragment extends BaseFragment {
    private static final String TAG = "ChatFragment";
    @ViewById
    RecyclerView mRecyclerRoom;
    private ArrayList<Room> mListRoom;
    private ChatAdapter mChatAdapter;
    private MessagePresenter messagePresenter;
    private RealmDAO mRealmDAO;

    @Override
    public void afterView() {
        init();
    }

    private void init() {
        mRealmDAO = new RealmDAO();
        mListRoom = new ArrayList<>();
        mChatAdapter = new ChatAdapter(getContext(), mListRoom);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerRoom.setLayoutManager(mLayoutManager);
        mRecyclerRoom.setItemAnimator(new DefaultItemAnimator());
        mRecyclerRoom.setAdapter(mChatAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(), 1);
        mRecyclerRoom.addItemDecoration(mDividerItemDecoration);
        getRoomData();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        mRealmDAO.realmChanged((o, check) -> {
            Log.d(TAG, "onRMessageChanged: " + check);
            // TODO : RealmChangedListener
            getRoomData();
            mChatAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        getRoomData();
    }

    private void getRoomData() {
        mListRoom.clear();
        RoomDAO roomDAO = new RoomDAO();

        for (Room room : roomDAO.getAllRoom()) {
            //Create MessagePresenter
            this.messagePresenter = new MessagePresenter(new MessageView() {
                @Override
                public void getAllMessage(ArrayList<Message> messagesList) {
                    Message lastMessage = messagesList.get(messagesList.size() - 1);
                    room.setLastMessage(lastMessage);
                }

                @Override
                public void errorGetAllMessage(int roomID) {
                }
            });
            //GetConverstation
            messagePresenter.getAllMessage(room.getRoomId());
            room.setRoomName(Common.getFullRoomFromRoomId(room.getRoomId()).getRoomName());
            room.setAvatar(Common.getFullRoomFromRoomId(room.getRoomId()).getAvatar());
            mListRoom.add(room);
        }
        Log.d(TAG, "onResume: mListRoom " + mListRoom.size());
        Collections.sort(mListRoom, roomComparator);
        mChatAdapter.notifyDataSetChanged();


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: MessageChange");
        //remove MessageListener
        mRealmDAO.removeRealmChanged();
    }

    @Override
    public void createUserRoom(Room room) {
        super.createUserRoom(room);
        mListRoom.add(room);
        mChatAdapter.notifyDataSetChanged();
    }


    private void getALlRoom() {
        mListRoom.clear();
        for (Room room : new RoomDAO().getAllRoom()) {
            mListRoom.add(room);
        }
        mChatAdapter.notifyDataSetChanged();
    }

    private boolean checkValidUser(ArrayList<User> users) {
        int i = 0;
        while (i < users.size()) {
            if (users.get(i).getId() == Common.getUserLogin().getId()) {
                return true;
            }
            i++;
        }
        return false;
    }
}
