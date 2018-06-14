package jp.bap.traning.simplechat;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.view.activities.FriendAdapter;

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
        User user1 = new User();
        user1.setFirstName("User 1");
        user1.setStatus("Cooling 1");
        mUserList.add(user1);

        User user2 = new User();
        user2.setFirstName("User 2");
        user2.setStatus("Cooling 2");
        mUserList.add(user2);

        User user3 = new User();
        user3.setFirstName("User 3");
        user3.setStatus("Cooling 3");
        mUserList.add(user3);

        mFriendAdapter = new FriendAdapter(getContext(),mUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerFriend.setLayoutManager(mLayoutManager);
        mRecyclerFriend.setItemAnimator(new DefaultItemAnimator());
        mRecyclerFriend.setAdapter(mFriendAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFriendAdapter.notifyDataSetChanged();
    }
}
