package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.presenter.message.MessagePresenter;
import jp.bap.traning.simplechat.presenter.message.MessageView;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;
import jp.bap.traning.simplechat.widget.CustomToolbar_;

@EActivity(R.layout.activity_chat_talks)
public class ChatTalksActivity extends BaseActivity {
    private MessagePresenter messagePresenter;
    ArrayList<Message> listMessage;
    ChatTalksAdapter chatTalksAdapter;
    Message message;
    private int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
    @ViewById
    RecyclerView listViewChat;
    @ViewById
    EditText edtMessage;
    @ViewById
    CustomToolbar_ mToolbar;

    @Extra
    int roomId;

    @Override
    public void afterView() {
        setupToolbar();
        init();
    }

    @Click
    void imgSendMessage() {
        if (edtMessage.getText().toString().isEmpty()) {
            Toast.makeText(ChatTalksActivity.this, "Edit Message is Empty", Toast.LENGTH_SHORT).show();
        } else {
            if (ChatService.getChat() != null) {
                //show in the UI
                message = new Message(edtMessage.getText().toString(), mMineId, roomId);
                listMessage.add(message);
                chatTalksAdapter.notifyDataSetChanged();
                //Send event to the Socket
                ChatService.getChat().sendMessage(message, message.getRoomID());
                edtMessage.setText("");
                //Save into Realm Database
                messagePresenter.insertOrUpdateMessage(message);

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
        //Create MessagePresenter
        this.messagePresenter = new MessagePresenter(new MessageView() {
            @Override
            public void insertMessage(Message message) {
                Toast.makeText(ChatTalksActivity.this, "Insert thanh cong Message " + message.getContent(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void errorInsertMessage(Message message) {
                Toast.makeText(ChatTalksActivity.this, "Khong insert duoc Message " + message.getContent(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void getAllMessage(ArrayList<Message> messagesList) {
                Toast.makeText(ChatTalksActivity.this, "Lay thanh cong toan bo Message " + messagesList.size() + "--" + messagesList.get(0).getContent(), Toast.LENGTH_LONG).show();
                for (int i = 0; i < messagesList.size(); i++) {
                    listMessage.add(messagesList.get(i));
                }
                chatTalksAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorGetAllMessage(int roomID) {
                Toast.makeText(ChatTalksActivity.this, "Khong getALLMessage dc " + roomID, Toast.LENGTH_LONG).show();
            }
        }) {

        };
        //GetConverstation
        messagePresenter.getAllMessage(roomId);

    }

    private void setupToolbar() {
        mToolbar.getCallButton().setVisibility(View.VISIBLE);
        mToolbar.getCallVideoButton().setVisibility(View.VISIBLE);
        mToolbar.getSettingButton().setImageDrawable(getResources().getDrawable(R.drawable.ic_more_vert));
        mToolbar.setTitle(Common.getNameRoomFromRoomId(roomId));
        mToolbar.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onReceiverMessage(Message message) {
        super.onReceiverMessage(message);
        listMessage.add(message);
        chatTalksAdapter.notifyDataSetChanged();
        //Save into Realm Database
        messagePresenter.insertOrUpdateMessage(message);
    }


}
