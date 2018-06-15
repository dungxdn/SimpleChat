package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.service.ChatService;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_friend)
public class FriendFragment extends BaseFragment {
    @ViewById
    RecyclerView mRecyclerFriend;
    private ArrayList<User> mUserList;
    private FriendAdapter mFriendAdapter;

    @Override
    public void afterView() {
        init();
    }

    private void init() {
        mUserList = new ArrayList<>();
        mFriendAdapter = new FriendAdapter(getContext(), mUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerFriend.setLayoutManager(mLayoutManager);
        mRecyclerFriend.setItemAnimator(new DefaultItemAnimator());
        mRecyclerFriend.setAdapter(mFriendAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerFriend.getContext(),1);
        mRecyclerFriend.addItemDecoration(mDividerItemDecoration);
        if (ChatService.getChat() != null) {
            ChatService.getChat().emitGetUsersOnline();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mFriendAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetUsersOnline(List<User> users) {
        super.onGetUsersOnline(users);
        mUserList.addAll(users);
        mFriendAdapter.notifyDataSetChanged();
    }
}
