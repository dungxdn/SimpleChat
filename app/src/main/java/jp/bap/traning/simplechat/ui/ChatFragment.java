package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.ui.BaseFragment;

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

    @Click
    void mBtnAddGroupChat(){
        AddGroupChatActivity_.intent(getContext()).start();
    }

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
    public void onResume() {
        super.onResume();
        mListRoom.clear();
        for (Room room : new RoomDAO().getAllRoom()) {
            mListRoom.add(room);
        }
        mChatAdapter.notifyDataSetChanged();

    }

}
