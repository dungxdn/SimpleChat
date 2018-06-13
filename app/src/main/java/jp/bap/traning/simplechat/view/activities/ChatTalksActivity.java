package jp.bap.traning.simplechat.view.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import jp.bap.traning.simplechat.R;

@EActivity(R.layout.activity_chat_talks)
public class ChatTalksActivity extends AppCompatActivity {

    @ViewById
    ListView listViewChat;
    @ViewById
    EditText edtMessage;
    @ViewById
    ImageButton imgSendMessage;

    @AfterViews
    public void view() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("aa");
        strings.add("bb");
        strings.add("cc");
        ArrayAdapter arrayAdapter = new ArrayAdapter(ChatTalksActivity.this,android.R.layout.simple_list_item_1,strings);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerViewChat.setLayoutManager(mLayoutManager);
//        recyclerViewChat.setItemAnimator(new DefaultItemAnimator());
        listViewChat.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

}
