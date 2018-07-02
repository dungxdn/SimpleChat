package jp.bap.traning.simplechat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.realm.ObjectChangeSet;
import io.realm.OrderedCollectionChangeSet;
import io.realm.RealmResults;
import javax.annotation.Nullable;
import jp.bap.traning.simplechat.database.MessageDAO;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmList;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.message.MessagePresenter;
import jp.bap.traning.simplechat.presenter.message.MessageView;
import jp.bap.traning.simplechat.ui.BaseFragment;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;

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
    private MessageDAO mMessageDAOForListener;

    @Override
    public void afterView() {
        init();
    }

    private void init() {
        mListRoom = new ArrayList<>();
        mChatAdapter = new ChatAdapter(getContext(), mListRoom);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerRoom.setLayoutManager(mLayoutManager);
        mRecyclerRoom.setItemAnimator(new DefaultItemAnimator());
        mRecyclerRoom.setAdapter(mChatAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(), 1);
        mRecyclerRoom.addItemDecoration(mDividerItemDecoration);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        mMessageDAOForListener = new MessageDAO();
        mMessageDAOForListener.realmChanged(new MessageDAO.Listener() {
            @Override
            public void onRealmChanged(Object o, int check) {
                Log.d(TAG, "onRMessageChanged: " + check);
                // TODO : RealmChangedListener
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mListRoom.clear();
        RoomDAO roomDAO = new RoomDAO();

        for (Room room : roomDAO.getAllRoom()) {
            //Create MessagePresenter
            this.messagePresenter = new MessagePresenter(new MessageView() {
                @Override
                public void getAllMessage(ArrayList<Message> messagesList) {
                    Message lastMessage = messagesList.get(0);
                    for (int i = 0; i < messagesList.size(); i++) {
                        if (messagesList.get(i).getId() > lastMessage.getId()) {
                            lastMessage = messagesList.get(i);
                        }
                    }
                    room.setLastMessage(lastMessage);
                }

                @Override
                public void errorGetAllMessage(int roomID) {
                }
            });
            //GetConverstation
            messagePresenter.getAllMessage(room.getRoomId());
            mListRoom.add(room);
        }

        Log.d(TAG, "onResume: mListRoom " + mListRoom.size());
        mChatAdapter.notifyDataSetChanged();


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: MessageChange");
        //rove MessageListener
        mMessageDAOForListener.removeRealmChanged();
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
            if (users.get(i).getId() == Common.mMineId) {
                return true;
            }
            i++;
        }
        return false;
    }
}
