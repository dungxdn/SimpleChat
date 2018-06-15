package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.User;

/**
 * Created by dungpv on 6/11/18.
 */

public class FriendAdapter extends RecyclerView.Adapter {
    private ArrayList<User> mListUser;
    private Context mContext;

    public FriendAdapter(Context context, ArrayList<User> userList) {
        mListUser = userList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewholder_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User user = mListUser.get(position);
        FriendViewHolder friendholder = (FriendViewHolder) holder;
        friendholder.mUserName.setText(user.getLastName() + " " + user.getFirstName());
        friendholder.mImgButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "mImgButtonCall; " + position, Toast.LENGTH_SHORT).show();
            }
        });
        friendholder.mImgButtonCallVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "mImgButtonCallVideo ; " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListUser.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mAvatar;
        AppCompatTextView mUserName;
        AppCompatTextView mStatus;
        AppCompatImageButton mImgButtonCall;
        AppCompatImageButton mImgButtonCallVideo;

        public FriendViewHolder(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.mImgAvatar);
            mUserName = itemView.findViewById(R.id.mTvUserName);
            mStatus = itemView.findViewById(R.id.mtvStatus);
            mImgButtonCallVideo = itemView.findViewById(R.id.mImgButtonCallVideo);
            mImgButtonCall = itemView.findViewById(R.id.mImgButtonCall);
        }
    }
}
