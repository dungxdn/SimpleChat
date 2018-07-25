package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.presenter.message.MessagePresenter;
import jp.bap.traning.simplechat.presenter.message.MessageView;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.widget.CustomToolbar;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_search_group_chat)
public class SearchGroupChatActivity extends BaseActivity {

    @ViewById
    CustomToolbar mToolbar;
    @ViewById
    RecyclerView mRecyclerRoom;
    private ArrayList<Room> mListRoom;
    private ChatAdapter mChatAdapter;
    private MessagePresenter messagePresenter;

    @Override
    public void afterView() {
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRoomFromRealm();
    }

    public void init() {
        setupToolbar();
        configRecyclerView();
        search(mToolbar.getSearchView());
    }

    public void setupToolbar() {
        mToolbar.getBackButton().setVisibility(View.GONE);
        mToolbar.getSearchView().setVisibility(View.VISIBLE);
        mToolbar.getTvTitle().setVisibility(View.GONE);
        mToolbar.getSettingButton().setVisibility(View.GONE);
        mToolbar.getBackButton().setVisibility(View.VISIBLE);
        mToolbar.getBackButton().setOnClickListener((v) -> {finish();});
    }

    public void configRecyclerView() {
        mListRoom = new ArrayList<>();
        mChatAdapter = new ChatAdapter(this, mListRoom);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerRoom.setLayoutManager(mLayoutManager);
        mRecyclerRoom.setItemAnimator(new DefaultItemAnimator());
        mRecyclerRoom.setAdapter(mChatAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this, 1);
        mRecyclerRoom.addItemDecoration(mDividerItemDecoration);
        getRoomFromRealm();
    }

    public void getRoomFromRealm() {
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
            room.setRoomName(Common.getFullRoomFromRoomId(room.getRoomId()).getRoomName());
            room.setAvatar(Common.getFullRoomFromRoomId(room.getRoomId()).getAvatar());
            mListRoom.add(room);
        }
        mChatAdapter.notifyDataSetChanged();
    }

    private void search(android.support.v7.widget.SearchView searchView) {

        searchView.setOnQueryTextListener(
                new android.support.v7.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (mChatAdapter != null) {
                            mChatAdapter.getFilter().filter(query);
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (mChatAdapter != null) mChatAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
    }
}
