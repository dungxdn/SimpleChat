package jp.bap.traning.simplechat.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    private Listener mListener;

    interface Listener {
        void onChat(User user);
        void onCallVideo(int userId);
        void onCallAudio(int userId);
    }

    public FriendAdapter(Context context, ArrayList<User> userList, Listener listener) {
        mListUser = userList;
        mContext = context;
        mListener = listener;
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
        friendholder.mUserName.setText(user.getFirstName());
        friendholder.mAvatar.setOnClickListener(view -> {
            Dialog mDialog = new Dialog(mContext);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.dialog_detail_profile_layout);
            AppCompatTextView tvUsername = mDialog.findViewById(R.id.mTvUserName);
            AppCompatButton btnChat = mDialog.findViewById(R.id.mBtnChat);
            AppCompatButton btnCall = mDialog.findViewById(R.id.mBtnCall);
            AppCompatButton btnCallVideo = mDialog.findViewById(R.id.mBtnCallVideo);
            btnChat.setOnClickListener(view1 -> {
                mListener.onChat(user);
                mDialog.dismiss();
            });
            btnCall.setOnClickListener(view1 -> {
                mListener.onCallAudio(user.getId());
                mDialog.dismiss();
            });
            btnCallVideo.setOnClickListener(view1 -> {
                mListener.onCallVideo(user.getId());
                mDialog.dismiss();
            });
            tvUsername.setText(user.getFirstName()+" "+user.getLastName());
            mDialog.show();
        });
//        friendholder.mAvatar.setOnClickListener(view -> mListener.onChat(user));
        friendholder.mImgButtonCall.setOnClickListener(view -> mListener.onCallAudio(user.getId()));
        friendholder.mImgButtonCallVideo.setOnClickListener(view -> mListener.onCallVideo(user.getId()));
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
