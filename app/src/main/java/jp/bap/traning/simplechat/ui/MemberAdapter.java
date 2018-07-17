package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.User;

/**
 * Created by Admin on 7/12/2018.
 */

public class MemberAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<User> mListUser;

    public MemberAdapter(Context mContext, ArrayList<User> mListUser) {
        this.mContext = mContext;
        this.mListUser = mListUser;
        Log.d("nfndsjfh", "MemberAdapter: "+mListUser.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.member_holder,parent,false);
        return new MemberViewholder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User user = mListUser.get(position);
        MemberViewholder memberViewholder = (MemberViewholder) holder;
        memberViewholder.tvName.setText(user.getLastName()+" "+user.getFirstName());
        RequestOptions options = new RequestOptions();
        Log.d("nfndsjfh", "onBindViewHolder: "+user.getFirstName());
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);
        Glide.with(mContext).load(user.getAvatar()).apply(options).into(memberViewholder.imgAvatar);

    }

    @Override
    public int getItemCount() {
        return mListUser.size();
    }
    class MemberViewholder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar;
        AppCompatTextView tvName;

        public MemberViewholder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
