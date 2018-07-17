package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.User;

public class AddGroupChatAdapter
        extends RecyclerView.Adapter<AddGroupChatAdapter.AddGroupChatViewHolder> {

    private ArrayList<User> mListUser;
    private Context mContext;
    private Listener mCallback;

    interface Listener {
        void onAddId(int id);

        void onRemoveId(int id);
    }

    public AddGroupChatAdapter(Context context, ArrayList<User> listUser, Listener callback) {
        mListUser = listUser;
        mContext = context;
        mCallback = callback;
    }

    @Override
    public AddGroupChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View itemView = layoutInflater.inflate(R.layout.viewholder_add_group_chat, parent, false);
        return new AddGroupChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AddGroupChatViewHolder holder, int position) {
        User user = mListUser.get(position);
        holder.mUserNameAddGroupChat.setText(user.getFirstName() + " " + user.getLastName());
        holder.mCheckBoxAddGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.mIscheck) {
                    holder.mCheckBoxAddGroupChat.setChecked(true);
                    mCallback.onAddId(user.getId());
                    holder.mIscheck = true;
                } else {
                    holder.mCheckBoxAddGroupChat.setChecked(false);
                    mCallback.onRemoveId(user.getId());
                    holder.mIscheck = false;
                }
            }
        });
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);
        Glide.with(mContext).load(user.getAvatar()).apply(options).into(holder.mAvatarAddGroupChat);
    }

    @Override
    public int getItemCount() {
        return mListUser.size();
    }

    class AddGroupChatViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mAvatarAddGroupChat;
        AppCompatTextView mUserNameAddGroupChat;
        AppCompatTextView mStatusAddGroupChat;
        CheckBox mCheckBoxAddGroupChat;
        boolean mIscheck = false;

        public AddGroupChatViewHolder(View itemView) {
            super(itemView);
            mAvatarAddGroupChat = itemView.findViewById(R.id.mImgAvatarAddGroupChat);
            mUserNameAddGroupChat = itemView.findViewById(R.id.mTvUserNameAddGroupChat);
            mStatusAddGroupChat = itemView.findViewById(R.id.mTvStatusAddGroupChat);
            mCheckBoxAddGroupChat = itemView.findViewById(R.id.mCheckBoxAddGroupChat);
        }
    }
}
