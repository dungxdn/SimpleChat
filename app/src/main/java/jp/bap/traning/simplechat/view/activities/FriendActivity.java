package jp.bap.traning.simplechat.view.activities;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import jp.bap.traning.simplechat.BaseActivity;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.User;

/**
 * Created by dungpv on 6/11/18.
 */

@EActivity(R.layout.activity_friend)
public class FriendActivity extends BaseActivity {
    @ViewById
    RecyclerView mRecyclerFriend;
    @ViewById
    TextView helloUser;
    private List<User> mListUser;
    private FriendAdapter mFriendAdapter;
    @Override
    public void afterView() {
        Intent getIntent = getIntent();
        helloUser.setText("Xin Chao, "+getIntent.getStringExtra("userName"));
        mListUser = new ArrayList<>();
        User user1 = new User();
        user1.setFirstName("User 1");
        user1.setStatus("Cooling 1");
        mListUser.add(user1);

        User user2 = new User();
        user2.setFirstName("User 2");
        user2.setStatus("Cooling 2");
        mListUser.add(user2);

        User user3 = new User();
        user3.setFirstName("User 3");
        user3.setStatus("Cooling 3");
        mListUser.add(user3);

        mFriendAdapter = new FriendAdapter(this, mListUser);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerFriend.setLayoutManager(mLayoutManager);
        mRecyclerFriend.setItemAnimator(new DefaultItemAnimator());
        mRecyclerFriend.setAdapter(mFriendAdapter);
        mFriendAdapter.notifyDataSetChanged();
    }
}
