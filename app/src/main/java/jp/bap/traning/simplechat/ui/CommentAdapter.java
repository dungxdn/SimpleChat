package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.Comment;
import jp.bap.traning.simplechat.utils.Common;

public class CommentAdapter extends RecyclerView.Adapter {
    private ArrayList<Comment> commentArrayList;
    private Context mContext;

    public CommentAdapter(Context mContext, ArrayList<Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.view_holder_activity_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Comment mComment = commentArrayList.get(position);
        CommentViewHolder mCommentViewHolder = (CommentViewHolder) holder;
        mCommentViewHolder.txtName.setText(mComment.getUser().getFirstName() + " " + mComment.getUser().getLastName());
        mCommentViewHolder.txtComment.setText(mComment.getComment());
        Common.setAvatar(mContext,mComment.getUser().getId(), mCommentViewHolder.avatar);
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        AppCompatTextView txtName;
        AppCompatTextView txtComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.mAvatar);
            txtName = itemView.findViewById(R.id.txtName);
            txtComment = itemView.findViewById(R.id.txtComment);
        }
    }

    private void setAvatar(int id, CircleImageView mAvatar) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);
        Glide.with(mContext).load(new UserDAO().getUser(id).getAvatar()).apply(options).into(mAvatar);
    }
}
