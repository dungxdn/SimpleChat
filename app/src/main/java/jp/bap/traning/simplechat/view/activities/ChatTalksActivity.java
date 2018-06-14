package jp.bap.traning.simplechat.view.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import org.json.JSONObject;

import java.util.ArrayList;

import jp.bap.traning.simplechat.BaseActivity;
import jp.bap.traning.simplechat.BaseApp;
import jp.bap.traning.simplechat.Common;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.chat.ChatManager;
import jp.bap.traning.simplechat.chat.ChatService;
import jp.bap.traning.simplechat.chat.Event;
import jp.bap.traning.simplechat.interfaces.ListenerInterface;
import jp.bap.traning.simplechat.view.service.MessageChangeReceiver;

@EActivity(R.layout.activity_chat_talks)
public class ChatTalksActivity extends BaseActivity{
    private static final String LISTEN_EVENT="send message";
    ArrayList<String> listMessage;
    ChatManager chatManager;
    @ViewById
    ListView listViewChat;
    @ViewById
    EditText edtMessage;
    @Click
    void imgSendMessage() {
        if(edtMessage.getText().toString().isEmpty()==true) {
            Toast.makeText(ChatTalksActivity.this,"Edit Message is Empty",Toast.LENGTH_SHORT).show();
        }
        else {
            if(ChatService.getChat() !=null) {
                Toast.makeText(ChatTalksActivity.this,"5-Send Message",Toast.LENGTH_SHORT).show();
                ChatService.getChat().sendMessage(edtMessage.getText().toString(),5);
                //Thong bao cho broadcast receiver ve su kien send
//                Intent intent =  new Intent();
//                intent.setAction(LISTEN_EVENT);
//                sendBroadcast(intent);
            }

        }
    }

    @Override
    public void afterView() {
        init();
        Common.connectToServerSocket(this,Common.URL_SERVER,"5");
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

}
