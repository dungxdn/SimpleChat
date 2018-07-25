package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import io.realm.RealmList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.utils.Common;

/**
 * Created by Admin on 6/16/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter implements Filterable {
    private ArrayList<Room> mListRoom;
    private ArrayList<Room> mListRoomFilter;
    private Context mContext;
    private String TAG = "ChatAdapter";

    public ChatAdapter(Context mContext, ArrayList<Room> mListRoom) {
        this.mListRoom = mListRoom;
        this.mListRoomFilter = mListRoom;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.viewholder_fragment_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Room room = mListRoom.get(position);
        Message lastMessage = room.getLastMessage();
        ChatViewHolder chatHolder = (ChatViewHolder) holder;
        chatHolder.setRoomId(room.getRoomId());

        chatHolder.mTvUserChat.setText(room.getRoomName());
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);
        Glide.with(mContext).load(room.getAvatar()).apply(options).into(chatHolder.mAvatar);

        if (lastMessage == null) {
            chatHolder.mTvContent.setText(R.string.no_message);
            chatHolder.mTvTime.setVisibility(View.GONE);
        } else {
            chatHolder.mTvTime.setVisibility(View.VISIBLE);
            chatHolder.mTvContent.setText(lastMessage.getContent());
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(lastMessage.getId());
            Date dateMessage = time.getTime();
            Date current = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

            if (sdf.format(dateMessage).equals(sdf.format(current))) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm");
                String strtime = sdf1.format(dateMessage);

                chatHolder.mTvTime.setText(strtime);
            } else {
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM");
                String strtime = sdf1.format(dateMessage);
                chatHolder.mTvTime.setText(strtime);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mListRoom.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString();

                if (charString.isEmpty()) {
                    mListRoom = mListRoomFilter;
                } else {
                    ArrayList<Room> filteredlist = new ArrayList<>();

                    for (Room r : mListRoomFilter) {
                        if (r.getType() == Common.TYPE_GROUP_MORE_PEOPLE) {
                            if (r.getRoomName() != null &&
                                    r.getRoomName().toLowerCase().contains(charString.toLowerCase())) {
                                if (r.getRoomName().toLowerCase().contains(charString.toLowerCase())
                                        || isStringExistsInUsers(r.getUsers(), charString)) {
                                    filteredlist.add(r);
                                }
                            }
                        } else {
                            if (isStringExistsInUsers(r.getUsers(), charString)) {
                                filteredlist.add(r);
                            }
                        }
                    }
                    mListRoom = filteredlist;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListRoom;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mListRoom = (ArrayList<Room>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public boolean isStringExistsInUsers(RealmList<User> users, String string) {
        for (User u : users) {
            if (u.getId() != Common.getUserLogin().getId()) {
                if (u.getFirstName().toLowerCase().contains(string.toLowerCase()) || u.getLastName()
                        .toLowerCase()
                        .contains(string.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
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
            ((Activity) mContext).overridePendingTransition(R.anim.anim_slides_in_right,R.anim.anim_slides_out_left);
        }
    }
}
