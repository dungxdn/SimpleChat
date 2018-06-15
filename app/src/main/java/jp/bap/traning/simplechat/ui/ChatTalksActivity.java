package jp.bap.traning.simplechat.ui;

import  android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.service.ChatService;

@EActivity(R.layout.activity_chat_talks)
public class ChatTalksActivity extends BaseActivity {
    ArrayList<String> listMessage;
    ArrayAdapter arrayAdapter;
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
                Toast.makeText(ChatTalksActivity.this,"Send Message",Toast.LENGTH_SHORT).show();
                ChatService.getChat().sendMessage(edtMessage.getText().toString(),13);
                edtMessage.setText("");
            }
        }
    }

    @Override
    public void afterView() {
        init();
    }

    private void init() {
        listMessage= new ArrayList<>();
        listMessage.add("aa");
        listMessage.add("bb");
        listMessage.add("cc");
        arrayAdapter = new ArrayAdapter(ChatTalksActivity.this,android.R.layout.simple_list_item_1,listMessage);
        listViewChat.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReceiverMessage(String message, int roomId) {
        Toast.makeText(ChatTalksActivity.this,"Nhan Duoc Message",Toast.LENGTH_SHORT).show();
        super.onReceiverMessage(message, roomId);
        listMessage.add(message+"--"+roomId);
        arrayAdapter.notifyDataSetChanged();
    }
}
