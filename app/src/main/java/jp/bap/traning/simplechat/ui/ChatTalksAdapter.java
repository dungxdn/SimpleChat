package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.presenter.chattalks.ChatTalksPresenter;
import jp.bap.traning.simplechat.presenter.chattalks.PopUpBottomSheet;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.Event;
import jp.bap.traning.simplechat.utils.SharedPrefs;

public class ChatTalksAdapter extends RecyclerView.Adapter {
    private ArrayList<Message> messageArrayList;
    private Context mContext;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_IMAGE_SEND = 3;
    private static final int VIEW_TYPE_MESSAGE_IMAGE_RECEIVED = 4;
    private static final int VIEW_TYPE_MESSAGE_LINK_SEND = 5;
    private static final int VIEW_TYPE_MESSAGE_LINK_RECEIVED = 6;
    interface Listener {
        void onViewDone();
    }
    Listener mListener;

    public ChatTalksAdapter(Context mContext, ArrayList<Message> messageArrayList,Listener listener) {
        this.messageArrayList = messageArrayList;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Message mMessage = messageArrayList.get(position);
        //Check Who is owner message
        if (mMessage.getUserID() == Common.mMineId) {
            //Check message is String or Image
            if(Common.typeImage.equals(mMessage.getType())) {
                return VIEW_TYPE_MESSAGE_IMAGE_SEND;
            }
            else  if(Common.typeText.equals(mMessage.getType())){
                return VIEW_TYPE_MESSAGE_SENT;
            }
            else {  //Common.typeLink.equals(mMessage.getType()))
                return VIEW_TYPE_MESSAGE_LINK_SEND;
            }
        }
        //Message from Friend
        else {
            if(Common.typeImage.equals(mMessage.getType())) {
                return VIEW_TYPE_MESSAGE_IMAGE_RECEIVED;
            }
            else if(Common.typeText.equals(mMessage.getType())){
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
            else {
                return VIEW_TYPE_MESSAGE_LINK_RECEIVED;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
                View view = layoutInflater.inflate(R.layout.view_holder_chat_my_message, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            View view = layoutInflater.inflate(R.layout.view_holder_chat_receive_message, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_IMAGE_SEND){
            View view = layoutInflater.inflate(R.layout.view_holder_message_image_right, parent, false);
            return new ImageMessageViewHolder(view);
        } else  if (viewType == VIEW_TYPE_MESSAGE_IMAGE_RECEIVED){
            View view = layoutInflater.inflate(R.layout.view_holder_message_image_left, parent, false);
            return new ImageMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_LINK_SEND) {
            View view = layoutInflater.inflate(R.layout.view_holder_message_link_right, parent, false);
            return new LinkMessageViewHolder(view);
        } else {    //viewType == VIEW_TYPE_MESSAGE_LINK_RECEIVED
            View view = layoutInflater.inflate(R.layout.view_holder_message_link_left, parent, false);
            return new LinkMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message mMessage = messageArrayList.get(position);
        if(Common.typeText.equals(mMessage.getType())) {
            MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
            messageViewHolder.txtMessage.setText(mMessage.getContent());
            if(Common.mMineId != mMessage.getUserID()) {
                messageViewHolder.txtName.setText(new UserDAO().getUser(mMessage.getUserID()).getFirstName());
            }
            //Set Avatar
            setAvatar(mMessage.getUserID(),messageViewHolder.mAvatar);
        }
        else if(Common.typeLink.equals(mMessage.getType())) {
            LinkMessageViewHolder linkMessageViewHolder = (LinkMessageViewHolder) holder;
            String arr[] = mMessage.getContent().split(";");
            linkMessageViewHolder.linkMessage.setText(arr[0]);
            linkMessageViewHolder.linkDescription.setText(arr[1]);
            if(Common.mMineId != mMessage.getUserID()) {
                linkMessageViewHolder.txtName.setText(new UserDAO().getUser(mMessage.getUserID()).getFirstName());
            }
            //Set Avatar
            setAvatar(mMessage.getUserID(),linkMessageViewHolder.mAvatar);
        }
        else {      //== Common.typeImage
            ImageMessageViewHolder imageMessageViewHolder = (ImageMessageViewHolder) holder;
            Common.setImage(mContext,mMessage.getContent(),imageMessageViewHolder.imageView);
            if(Common.mMineId != mMessage.getUserID()) {
                imageMessageViewHolder.txtName.setText(new UserDAO().getUser(mMessage.getUserID()).getFirstName());
            }
            //Set Avatar
            setAvatar(mMessage.getUserID(),imageMessageViewHolder.mAvatar);
            mListener.onViewDone();
           }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


    class MessageViewHolder extends RecyclerView.ViewHolder{
        CircleImageView mAvatar;
        AppCompatTextView txtMessage;
        TextView txtName;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.mAvatar);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtName = itemView.findViewById(R.id.txtName);
            itemView.setOnLongClickListener((View view) -> {
                Message message = messageArrayList.get(getAdapterPosition());
                PopUpBottomSheet popUpBottomSheet = PopUpBottomSheet.getInstance(message);
                popUpBottomSheet.show( ((AppCompatActivity)mContext).getSupportFragmentManager(),PopUpBottomSheet.class.getSimpleName() );
                return false;
            });
        }
    }

    class ImageMessageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CircleImageView mAvatar;
        TextView txtName;
        public ImageMessageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageMessageChatContent);
            mAvatar = itemView.findViewById(R.id.mAvatar);
            txtName = itemView.findViewById(R.id.txtName);

            imageView.setOnClickListener(view -> {
                Message message = messageArrayList.get(getAdapterPosition());
                FullScreenImageActivity_.intent(mContext).urlImage(message.getContent()).start();
            });
        }
    }

    class LinkMessageViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mAvatar;
        TextView txtName;
        AppCompatTextView linkMessage;
        AppCompatTextView linkDescription;

        public LinkMessageViewHolder(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.mAvatar);
            txtName = itemView.findViewById(R.id.txtName);
            linkMessage = itemView.findViewById(R.id.txtLink);
            linkDescription = itemView.findViewById(R.id.txtLinkDescription);
        }
    }

    private void setAvatar(int id,CircleImageView mAvatar) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);
        Glide.with(mContext).load(new UserDAO().getUser(id).getAvatar()).apply(options).into(mAvatar);
    }
}
