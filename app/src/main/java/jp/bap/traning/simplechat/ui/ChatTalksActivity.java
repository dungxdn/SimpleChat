package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.SharedPrefs;

@EActivity(R.layout.activity_chat_talks)
public class ChatTalksActivity extends BaseActivity {
    ArrayList<Message> listMessage;
    ChatTalksAdapter chatTalksAdapter;
    Message message;
    private int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
    @ViewById
    RecyclerView listViewChat;
    @ViewById
    EditText edtMessage;

    @Extra
    int roomId;

    @Override
    public void afterView() {
        init();
    }

    @Click
    void imgSendMessage() {
        if (edtMessage.getText().toString().isEmpty()) {
            Toast.makeText(ChatTalksActivity.this, "Edit Message is Empty", Toast.LENGTH_SHORT).show();
        } else {
            if (ChatService.getChat() != null) {
                message = new Message(edtMessage.getText().toString(), mMineId, roomId);
                listMessage.add(message);
                chatTalksAdapter.notifyDataSetChanged();
                ChatService.getChat().sendMessage(message, message.getRoomID());
                edtMessage.setText("");

            }
        }
    }

    private void init() {
        listMessage = new ArrayList<>();
        chatTalksAdapter = new ChatTalksAdapter(this, listMessage);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listViewChat.setLayoutManager(mLayoutManager);
        listViewChat.setItemAnimator(new DefaultItemAnimator());
        listViewChat.setAdapter(chatTalksAdapter);
        chatTalksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReceiverMessage(Message message) {
        super.onReceiverMessage(message);
        listMessage.add(message);
        chatTalksAdapter.notifyDataSetChanged();
    }
}
