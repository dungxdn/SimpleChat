package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.User;

public class SharingMessageAdapter extends RecyclerView.Adapter<SharingMessageAdapter.SharingMessageViewHolder> {
    private ArrayList<User> mListUser;
    private Context mContext;
    private SharingMessageAdapter.Listener mCallback;

    interface Listener {
        void onAddId(int id);

        void onRemoveId(int id);
    }

    public SharingMessageAdapter(Context mContext, ArrayList<User> mListUser, SharingMessageAdapter.Listener mCallback) {
        this.mListUser = mListUser;
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    @Override
    public SharingMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View itemView = layoutInflater.inflate(R.layout.viewholder_add_group_chat, parent, false);
        return new SharingMessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SharingMessageViewHolder holder, int position) {
        User user = mListUser.get(position);
        holder.mUserName.setText(user.getFirstName() + " " + user.getLastName());
        holder.mCheckBokSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.mIscheck) {
                    holder.mCheckBokSharing.setChecked(true);
                    mCallback.onAddId(user.getId());
                    holder.mIscheck = true;
                } else {
                    holder.mCheckBokSharing.setChecked(false);
                    mCallback.onRemoveId(user.getId());
                    holder.mIscheck = false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListUser.size();
    }

    class SharingMessageViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mAvatar;
        AppCompatTextView mUserName;
        AppCompatTextView mStatus;
        CheckBox mCheckBokSharing;
        boolean mIscheck = false;

        public SharingMessageViewHolder(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.mImgAvatarAddGroupChat);
            mUserName = itemView.findViewById(R.id.mTvUserNameAddGroupChat);
            mStatus = itemView.findViewById(R.id.mTvStatusAddGroupChat);
            mCheckBokSharing = itemView.findViewById(R.id.mCheckBoxAddGroupChat);
        }
    }
}
