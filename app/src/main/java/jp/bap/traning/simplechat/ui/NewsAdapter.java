package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.News;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;

public class NewsAdapter extends RecyclerView.Adapter {
    private ArrayList<News> newsArrayList;
    private Context mContext;

    public NewsAdapter(Context mContext, ArrayList<News> newsArrayList) {
        this.newsArrayList = newsArrayList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.view_holder_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        News mNews = newsArrayList.get(position);
        NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
        newsViewHolder.txtName.setText(mNews.getUser().getFirstName() + " " + mNews.getUser().getLastName());
        newsViewHolder.txtName2.setText(mNews.getUser().getFirstName() + " " + mNews.getUser().getLastName());
        newsViewHolder.txtDescription.setText(mNews.getDescription());
        newsViewHolder.txtLike.setText(mNews.getIsLike() + " " + mContext.getResources().getString(R.string.text_like)+" ");
        newsViewHolder.txtComment.setText(mNews.getCountComment() + " " + mContext.getResources().getString(R.string.text_comment));
        if (mNews.getUsersLike().contains(Common.getUserLogin()) == true) {
            newsViewHolder.imageButtonLike.setImageResource(R.drawable.heart);
        }
        Common.setImage(mContext, mNews.getImageView(), newsViewHolder.imageView);
        Common.setAvatar(mContext, mNews.getUser().getId(), newsViewHolder.avatar);
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        private Animation animButtonLike;
        CircleImageView avatar;
        AppCompatTextView txtName, txtName2;
        AppCompatImageView imageView;
        AppCompatTextView txtDescription;
        AppCompatImageButton imageButtonLike;
        AppCompatImageButton imageButtonComment;
        AppCompatImageButton imageButtonShare;
        AppCompatTextView txtLike;
        AppCompatTextView txtComment;

        public NewsViewHolder(View itemView) {
            super(itemView);
            addControls();
            addEvents();
        }

        private void addControls() {
            avatar = itemView.findViewById(R.id.mAvatarNews);
            txtName = itemView.findViewById(R.id.txtName);
            txtName2 = itemView.findViewById(R.id.txtName2);
            imageView = itemView.findViewById(R.id.imgNews);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imageButtonLike = itemView.findViewById(R.id.btnLike);
            imageButtonShare = itemView.findViewById(R.id.btnShare);
            imageButtonComment = itemView.findViewById(R.id.btnComment);
            txtLike = itemView.findViewById(R.id.countLike);
            txtComment = itemView.findViewById(R.id.countComment);
            animButtonLike = AnimationUtils.loadAnimation(mContext,R.anim.anim_hyperspace_jump);
        }

        private void addEvents() {
            imageView.setOnClickListener(view -> {
                News mNews = newsArrayList.get(getAdapterPosition());
                FullScreenImageActivity_.intent(mContext).urlImage(mNews.getImageView()).start();
                ((Activity) mContext).overridePendingTransition(R.anim.anim_zoom, 0);
            });

            imageButtonShare.setOnClickListener(view -> {
                News mNews = newsArrayList.get(getAdapterPosition());
                Message mMessage = new Message(mNews.getImageView(), Common.getUserLogin().getId(), -1, Common.typeImage);
                SharingMessageActivity_.intent(mContext).message(mMessage).start();
                ((Activity) mContext).overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
            });

            imageButtonLike.setOnClickListener(view -> {
                News mNews = newsArrayList.get(getAdapterPosition());
                RealmList<User> temp = mNews.getUsersLike();        //Update list User Like and isLike
                if (mNews.getUsersLike().contains(Common.getUserLogin()) == false) {
                    temp.add(Common.getUserLogin());
                    mNews.setUsersLike(temp);
                    imageButtonLike.setImageResource(R.drawable.heart);
                    mNews.setIsLike(mNews.getIsLike() + 1);
                    imageButtonLike.startAnimation(animButtonLike);
                } else {
                    temp.remove(Common.getUserLogin());
                    mNews.setUsersLike(temp);
                    imageButtonLike.setImageResource(R.drawable.like);
                    mNews.setIsLike(mNews.getIsLike() - 1);
                    imageButtonLike.clearAnimation();
                }
                txtLike.setText(mNews.getIsLike() + " " + mContext.getResources().getString(R.string.text_like)+" ");
                if (ChatService.getChat() != null) {
                    //Gui su kien bam like
                    ChatService.getChat().emitOnLikeNews(Common.getUserLogin(), mNews);
                }
            });

            imageButtonComment.setOnClickListener(view -> {
                CommentActivity_.intent(mContext).mNews(newsArrayList.get(getAdapterPosition())).start();
                ((Activity) mContext).overridePendingTransition(R.anim.anim_slides_down, R.anim.anim_slides_up);
            });

        }
    }
}
