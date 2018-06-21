package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.InputStream;
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
        addEvents();
    }

    @Click
    void imgSendMessage() {
        if (edtMessage.getText().toString().isEmpty()) {
            Toast.makeText(ChatTalksActivity.this, "Edit Message is Empty", Toast.LENGTH_SHORT).show();
        } else {
            if (ChatService.getChat() != null) {
                //show in the UI
                message = new Message(edtMessage.getText().toString(), mMineId, roomId, Common.typeText);
                listMessage.add(message);
                chatTalksAdapter.notifyDataSetChanged();
                listViewChat.smoothScrollToPosition(listMessage.size() - 1);
                //Send event to the Socket
                ChatService.getChat().sendMessage(message, message.getRoomID());
                edtMessage.setText("");
                //Save into Realm Database
                messagePresenter.insertOrUpdateMessage(message);
            }
        }
    }

    @Click
    void imgImage() {
        ImagePicker.create(this)
                .returnMode(ReturnMode.GALLERY_ONLY)
                .single()
                .start();
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
            public void getAllMessage(ArrayList<Message> messagesList) {
                for (int i = 0; i < messagesList.size(); i++) {
                    listMessage.add(messagesList.get(i));
                }
                chatTalksAdapter.notifyDataSetChanged();
                listViewChat.smoothScrollToPosition(listMessage.size() - 1);
            }

            @Override
            public void errorGetAllMessage(int roomID) {
            }
        }) {

        };
        //GetConverstation
        messagePresenter.getAllMessage(roomId);
    }

    public void addEvents() {
        listViewChat.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                listViewChat.postDelayed(() -> {
                    int scrollTo = listViewChat.getAdapter().getItemCount() - 1;
                    scrollTo = (scrollTo >= 0) ? scrollTo : 0;
                    listViewChat.scrollToPosition(scrollTo);
                }, 10);
            }
        });

        listViewChat.setOnTouchListener((view, motionEvent) -> {
            hideKeyboard((Activity) view.getContext());
            return false;
        });

    }

    @Override
    public void onReceiverMessage(Message message) {
        super.onReceiverMessage(message);
        if (message.getRoomID() == roomId) {
            listMessage.add(message);
            chatTalksAdapter.notifyDataSetChanged();
            listViewChat.smoothScrollToPosition(listMessage.size() - 1);
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Test
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            try {
                //Create a bitmap covert to String : sendMessage
                Bitmap bitmap = Common.readBitmapAndScale(image.getPath());
                String bitMapImage = Common.BitMapToString(bitmap);
                //Create a message model and sendChatMessage
                Message message = new Message(bitMapImage, mMineId, roomId, Common.typeImage);
                listMessage.add(message);
                chatTalksAdapter.notifyDataSetChanged();
                listViewChat.smoothScrollToPosition(listMessage.size() - 1);
                Log.e("ChatMessageImage: ", bitMapImage);
                //Save into Realm Database
                messagePresenter.insertOrUpdateMessage(message);
                //Send event to the Socket
                ChatService.getChat().sendMessage(message, message.getRoomID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
