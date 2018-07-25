package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.chattalks.ChatTalksListener;
import jp.bap.traning.simplechat.presenter.chattalks.ChatTalksPresenter;
import jp.bap.traning.simplechat.presenter.chattalks.PopUpBottomSheet;
import jp.bap.traning.simplechat.presenter.message.MessagePresenter;
import jp.bap.traning.simplechat.presenter.message.MessageView;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImagePresenter;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImageView;
import jp.bap.traning.simplechat.response.ImageResponse;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.widget.CustomToolbar;

@EActivity(R.layout.activity_chat_talks)
public class ChatTalksActivity extends BaseActivity {
    private MessagePresenter messagePresenter;
    private ChatTalksPresenter chatTalksPresenter;
    ArrayList<Message> listMessage;
    ChatTalksAdapter chatTalksAdapter;
    Message message;
    private RequestOptions options;
    private UploadImagePresenter mUploadImagePresenter;
    private static String linkImage = "";
    private Room mRoom;
    @ViewById
    RecyclerView listViewChat;
    @ViewById
    EditText edtMessage;
    @ViewById
    CustomToolbar mToolbar;
    @ViewById
    ProgressBar mProgressBar;
    @Extra
    int roomId;

    @Override
    public void afterView() {
        overridePendingTransition(R.anim.anim_together, 0);
        init();
        setupToolbar();
        addEvents();
    }

    @Click
    void imgSendMessage() {
        if (edtMessage.getText().toString().trim().isEmpty()) {
            edtMessage.startAnimation(AnimationUtils.loadAnimation(ChatTalksActivity.this, R.anim.anim_shake));
            Toast.makeText(ChatTalksActivity.this, "Edit Message is Empty", Toast.LENGTH_SHORT).show();
        } else {
            if (ChatService.getChat() != null) {
                //show in the UI
                String messageChat = edtMessage.getText().toString();
                if (chatTalksPresenter.containsLink(messageChat) == true) {
                    chatTalksPresenter.requestURL(messageChat);
                } else {
                    message = new Message(messageChat, Common.getUserLogin().getId(), roomId, Common.typeText);
                    listMessage.add(message);
                    chatTalksAdapter.notifyDataSetChanged();
                    listViewChat.smoothScrollToPosition(listMessage.size() - 1);
                    //Send event to the Socket
                    ChatService.getChat().emitSendMessage(message, message.getRoomID());
                    //Save into Realm Database
                    messagePresenter.insertOrUpdateMessage(message);
                }
                edtMessage.setText("");
            }
        }
    }

    @Click
    void imgImage() {
        Common.selectImage(this);
        showProgressBar(mProgressBar);
    }


    private void setupToolbar() {
        if (mRoom.getType()==0){
            mToolbar.getCallButton().setVisibility(View.VISIBLE);
            mToolbar.getCallVideoButton().setVisibility(View.VISIBLE);
        }
        mToolbar.getSettingButton().setImageDrawable(getResources().getDrawable(R.drawable.ic_more_vert));
        mToolbar.getSettingButton().setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(ChatTalksActivity.this, mToolbar.getSettingButton());
            popup.getMenuInflater()
                    .inflate(R.menu.menu_chat_talk, popup.getMenu());
            if (mRoom.getType() == 0) {
                popup.getMenu().getItem(0).setVisible(false);
            }
            popup.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.member:
                        showDialogMember();
                        break;
                    case R.id.setting:
                        break;
                    case R.id.help:
                        break;
                }
                return true;
            });
            popup.show();
        });
        mToolbar.setTitle(mRoom.getRoomName());
        mToolbar.getBackButton().setOnClickListener(view -> finish());
        mToolbar.setTitle(Common.getFullRoomFromRoomId(roomId).getRoomName());
        mToolbar.getBackButton().setOnClickListener(view -> finish());
        mToolbar.getCallVideoButton().setOnClickListener(view -> {
            User mUser = Common.getFriendFromRoom(new RoomDAO().getRoomFromRoomId(roomId));
            if (Common.checkUserOnline(mUser.getId()) == true) {
                CallActivity_.intent(ChatTalksActivity.this).isIncoming(false).isAudioCall(false).roomId(roomId).start();
                overridePendingTransition(R.anim.anim_from_midle, R.anim.anim_to_midle);
            } else {
                CallBusyActivity_.intent(ChatTalksActivity.this).mUser(mUser).status(Common.CALL_NO_ONE).start();
            }
        });
        mToolbar.getCallButton().setOnClickListener(view -> {
            User mUser = Common.getFriendFromRoom(new RoomDAO().getRoomFromRoomId(roomId));
            if (Common.checkUserOnline(mUser.getId()) == true) {
                CallActivity_.intent(ChatTalksActivity.this).isIncoming(false).isAudioCall(true).roomId(roomId).start();
                overridePendingTransition(R.anim.anim_from_midle, R.anim.anim_to_midle);
            } else {
                CallBusyActivity_.intent(ChatTalksActivity.this).mUser(mUser).status(Common.CALL_NO_ONE).start();
            }
        });
    }

    private void showDialogMember() {
        Dialog mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_member_group);
        ArrayList<User> listUser = new ArrayList<>();
        for (User u : mRoom.getUsers()) {
            listUser.add(u);
        }
        RecyclerView listMember = mDialog.findViewById(R.id.lvMember);
        AppCompatTextView tvGroupName = mDialog.findViewById(R.id.tvGroupName);
        tvGroupName.setText(mRoom.getRoomName());
        MemberAdapter memberAdapter = new MemberAdapter(this, listUser);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listMember.setLayoutManager(mLayoutManager);
        listMember.setItemAnimator(new DefaultItemAnimator());

        listMember.setAdapter(memberAdapter);
        memberAdapter.notifyDataSetChanged();
        mDialog.show();

    }

    private void init() {
        mProgressBar.setVisibility(View.GONE);
        //Initial RecyclerView
        mRoom = Common.getFullRoomFromRoomId(roomId);
        listMessage = new ArrayList<>();
        chatTalksAdapter = new ChatTalksAdapter(this, listMessage, () -> hiddenProgressBar(mProgressBar));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listViewChat.setLayoutManager(mLayoutManager);
        listViewChat.setItemAnimator(new DefaultItemAnimator());
        listViewChat.setAdapter(chatTalksAdapter);
        chatTalksAdapter.notifyDataSetChanged();
        //Create MessagePresenter
        this.messagePresenter = new MessagePresenter(new MessageView() {
            @Override
            public void getAllMessage(ArrayList<Message> messagesList) {
                listMessage.clear();
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
        //Get Converstation
        messagePresenter.getAllMessage(roomId);
        //Create ChatTalksPresenter
        this.chatTalksPresenter = new ChatTalksPresenter(new ChatTalksListener() {
            @Override
            public void onRequestURLSuccess(String link, String title, String srcImage) {
                if (srcImage.isEmpty()) {
                    srcImage = "fail";
                }
                message = new Message(link + ";" + title + ";" + srcImage, Common.getUserLogin().getId(), roomId, Common.typeLink);
                listMessage.add(message);
                chatTalksAdapter.notifyDataSetChanged();
                listViewChat.smoothScrollToPosition(listMessage.size() - 1);
                //Send event to the Socket
                ChatService.getChat().emitSendMessage(message, message.getRoomID());
                //Save into Realm Database
                messagePresenter.insertOrUpdateMessage(message);
            }

            @Override
            public void onRequestURLFailed(String link) {
                message = new Message(link + ";" + "No preview available" + ";" + "fail", Common.getUserLogin().getId(), roomId, Common.typeLink);
                listMessage.add(message);
                chatTalksAdapter.notifyDataSetChanged();
                listViewChat.smoothScrollToPosition(listMessage.size() - 1);
                //Send event to the Socket
                ChatService.getChat().emitSendMessage(message, message.getRoomID());
                //Save into Realm Database
                messagePresenter.insertOrUpdateMessage(message);
            }
        });
        // Initial Glide
        mUploadImagePresenter = new UploadImagePresenter();
        options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);

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
            Common.hideKeyboard((Activity) view.getContext());
            return false;
        });
    }

    @Override
    public void onReceiverMessage(Message message) {
        super.onReceiverMessage(message);
        if (message.getRoomID() == roomId) {
            if (listMessage.contains(message) == true) {
            } else {
                listMessage.add(message);
                chatTalksAdapter.notifyDataSetChanged();
                listViewChat.smoothScrollToPosition(listMessage.size() - 1);
            }
        }
    }

    //Test: send Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            hiddenProgressBar(mProgressBar);
        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            try {
                File mFile = new File(image.getPath());
                mUploadImagePresenter.uploadImage("", "", "", "", mFile, new UploadImageView() {
                    @Override
                    public void onSuccess(ImageResponse result) {
                        linkImage = result.getData().getLink();
                        message = new Message(linkImage, Common.getUserLogin().getId(), roomId, Common.typeImage);
                        listMessage.add(message);
                        chatTalksAdapter.notifyDataSetChanged();
                        listViewChat.smoothScrollToPosition(listMessage.size() - 1);
//                        Save into Realm Database
                        messagePresenter.insertOrUpdateMessage(message);
                        //Send event to the Socket
                        ChatService.getChat().emitSendMessage(message, message.getRoomID());
                    }

                    @Override
                    public void onError(String message, int code) {
                        Log.d("ChatTalksActivity", "onError: ");
                    }

                    @Override
                    public void onFailure() {
                        Log.d("ChatTalksActivity", "onFailure: ");
                    }
                });

//                //Create a bitmap covert to String : sendMessage
//                Bitmap bitmap = chatTalksPresenter.readBitmapAndScale(image.getPath());
//                String bitMapImage = chatTalksPresenter.BitMapToString(bitmap);
//                //Create a message model and sendChatMessage
//                Message message = new Message(bitMapImage, Common.mMineId, roomId, Common.typeImage);
//                listMessage.add(message);
//                chatTalksAdapter.notifyDataSetChanged();
//                listViewChat.smoothScrollToPosition(listMessage.size() - 1);
//                //Save into Realm Database
//                messagePresenter.insertOrUpdateMessage(message);
//                //Send event to the Socket
//                ChatService.getChat().sendMessage(message, message.getRoomID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ChatTalksActivity", "onResume");
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (PopUpBottomSheet.checkChange == 1) {
            PopUpBottomSheet.checkChange = -1;
            finish();
        }
    }

}