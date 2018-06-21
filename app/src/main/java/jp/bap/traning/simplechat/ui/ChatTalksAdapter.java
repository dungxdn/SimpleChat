package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;

public class ChatTalksAdapter extends RecyclerView.Adapter {
    private ArrayList<Message> messageArrayList;
    private Context mContext;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_IMAGE_SEND = 3;
    private static final int VIEW_TYPE_MESSAGE_IMAGE_RECEIVED = 4;
    private int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);

    public ChatTalksAdapter(Context mContext, ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        Message mMessage = messageArrayList.get(position);
        //Check Who is owner message
        if (mMessage.getUserID() == mMineId) {
            //Check message is String or Image
            if(Common.typeImage.equals(mMessage.getType())) {
                return VIEW_TYPE_MESSAGE_IMAGE_SEND;
            }
            else {
                return VIEW_TYPE_MESSAGE_SENT;
            }
        } else {
            if(Common.typeImage.equals(mMessage.getType())) {
                return VIEW_TYPE_MESSAGE_IMAGE_RECEIVED;
            }
            else {
                return VIEW_TYPE_MESSAGE_RECEIVED;
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
        } else {
            View view = layoutInflater.inflate(R.layout.view_holder_message_image_left, parent, false);
            return new ImageMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message mMessage = messageArrayList.get(position);
        if(Common.typeText.equals(mMessage.getType())) {
            MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
            messageViewHolder.txtMessage.setText(mMessage.getContent());
        }
        else {
            ImageMessageViewHolder imageMessageViewHolder = (ImageMessageViewHolder) holder;
//            Glide.with(mContext).load(mMessage.getContent()).into(imageMessageViewHolder.imageView);
            //Test
            Bitmap bitmap = Common.StringToBitMap(mMessage.getContent());
            imageMessageViewHolder.imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


    class MessageViewHolder extends RecyclerView.ViewHolder{
        CircleImageView mAvatar;
        AppCompatTextView txtMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.mAvatar);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }
    }

    class ImageMessageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageMessageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageMessageChatContent);
        }
    }

}
