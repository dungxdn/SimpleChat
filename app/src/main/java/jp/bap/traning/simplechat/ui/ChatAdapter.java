package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;

/**
 * Created by Admin on 6/16/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    private ArrayList<Room> mListRoom;
    private ArrayList<Message> mListMessage;
    private Context mContext;
    private String TAG = "ChatAdapter";

    public ChatAdapter(Context mContext, ArrayList<Room> mListRoom, ArrayList<Message> mListMessage) {
        this.mListRoom = mListRoom;
        this.mListMessage = mListMessage;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewholder_fragment_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Room room = mListRoom.get(position);
        Message message = mListMessage.get(position);
        ChatViewHolder chatHolder = (ChatViewHolder) holder;
        chatHolder.setRoomId(room.getRoomId());
        chatHolder.mTvUserChat.setText(Common.getNameRoomFromRoomId(room.getRoomId()));
        if (message == null) {
            chatHolder.mTvContent.setText("Chưa có tin nhắn nào.");
            chatHolder.mTvTime.setVisibility(View.GONE);
        } else {
            chatHolder.mTvTime.setVisibility(View.VISIBLE);
            chatHolder.mTvContent.setText(message.getContent());
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(message.getId());
            chatHolder.mTvTime.setText(time.get(Calendar.HOUR_OF_DAY)+ ":"+time.get(Calendar.MINUTE));
        }

    }

    @Override
    public int getItemCount() {
        return mListRoom.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView mAvatar;
        AppCompatTextView mTvUserChat;
        AppCompatTextView mTvContent;
        AppCompatTextView mTvTime;
        int mRoomId;

        public ChatViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAvatar = itemView.findViewById(R.id.mImgAvatar);
            mTvTime = itemView.findViewById(R.id.mTvTime);
            mTvContent = itemView.findViewById(R.id.mTvContent);
            mTvUserChat = itemView.findViewById(R.id.mTvUserChat);
        }

        private void setRoomId(int roomId) {
            mRoomId = roomId;
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + mRoomId);
            ChatTalksActivity_.intent(mContext).roomId(mRoomId).start();
        }
    }
}
