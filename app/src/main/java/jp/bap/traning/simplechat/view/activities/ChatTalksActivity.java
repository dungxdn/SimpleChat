package jp.bap.traning.simplechat.view.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import jp.bap.traning.simplechat.BaseApp;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.chat.ChatService;
import jp.bap.traning.simplechat.chat.Event;
import jp.bap.traning.simplechat.view.service.MessageChangeReceiver;

@EActivity(R.layout.activity_chat_talks)
public class ChatTalksActivity extends BaseApp{
    private static final String URL_SERVER = "http://172.16.0.31:3000";
    private static final String SEND_MESSAGE = "Send Message";
    private static final String RECEIVE_MESSAGE = "Receive Message";
    public MessageChangeReceiver messageChangeReceiver;
    ArrayList<String> listMessage;
    @ViewById
    ListView listViewChat;
    @ViewById
    EditText edtMessage;
    @Click
    void imgSendMessage() {
        Intent intent = new Intent();
        intent.setAction(SEND_MESSAGE);
        sendBroadcast(intent);
    }

    @AfterViews
    public void view() {
        init();
        if(connectToServer(URL_SERVER,1)==true) {
            Toast.makeText(ChatTalksActivity.this,"Connect Server is Success",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(ChatTalksActivity.this,"Connect Server is NOT Success",Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        listMessage= new ArrayList<>();
        listMessage.add("aa");
        listMessage.add("bb");
        listMessage.add("cc");
        ArrayAdapter arrayAdapter = new ArrayAdapter(ChatTalksActivity.this,android.R.layout.simple_list_item_1,listMessage);
        listViewChat.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    private boolean connectToServer(String server, int userID) {
        if(ChatService.getChat()==null) {
            Intent intent = new Intent(this, ChatService.class);
            intent.putExtra("host", server);
            intent.putExtra("token",userID);
            startService(intent);
            return true;
        }
        return false;
    }

    private void initBroadCastReceiver() {
        messageChangeReceiver = new MessageChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(SEND_MESSAGE);
        registerReceiver(messageChangeReceiver,intentFilter);
    }

}
