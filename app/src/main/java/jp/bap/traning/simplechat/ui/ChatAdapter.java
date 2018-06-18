package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import jp.bap.traning.simplechat.model.User;

/**
 * Created by Admin on 6/16/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    private ArrayList<User> mListUser;
    private Context mContext;
    private FriendAdapter.Listener mListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
