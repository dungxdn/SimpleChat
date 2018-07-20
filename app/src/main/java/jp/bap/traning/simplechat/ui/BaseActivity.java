package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Comment;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.comment.CommentPresenter;
import jp.bap.traning.simplechat.presenter.message.MessagePresenter;
import jp.bap.traning.simplechat.service.CallbackManager;
import jp.bap.traning.simplechat.utils.Event;

/**
 * Created by dungpv on 6/7/18.
 */

@EActivity
@WindowFeature(Window.FEATURE_NO_TITLE)
public abstract class BaseActivity extends AppCompatActivity implements CallbackManager.Listener {
    CallbackManager mCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback = new CallbackManager(this);
        mCallback.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallback.unregister();
    }

    public abstract void afterView();

    @AfterViews
    public void initView() {
        this.afterView();
    }

    @Override
    public void onMessage(Event type, JSONObject data) {
        switch (type) {
            case CONNECT:
                onConnectedSocket();
                break;

            case MESSAGE_RECEIVER:
                try {
                    Gson gson = new Gson();
                    String objectMessage = data.get("chatMessage").toString();
                    Message message = gson.fromJson(objectMessage, Message.class);
                    onReceiverMessage(message);
                    SoundManage.setAudioForMsgAndCall(this,R.raw.message_ringging,false);
                    new MessagePresenter().insertOrUpdateMessage(message);

                    //update lastMessage in Room into Realm
                    RoomDAO roomDAO = new RoomDAO();
                    Room room = roomDAO.getRoomFromRoomId(message.getRoomID());
                    if (room != null) {
                        room.setLastMessage(message);
                        roomDAO.insertOrUpdate(room);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case USER_ONLINE: {
                try {
                    if (data.length() == 0) return;
                    JSONArray jsonArray = data.getJSONArray("users");
                    ArrayList<User> usersOnline = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String objectUser = jsonArray.getString(i);
                        User user = gson.fromJson(objectUser, User.class);
                        usersOnline.add(user);
                    }
                    onReceiverListUsersOnline(usersOnline);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case ON_USER_OFFLINE: {
                try {
                    String objectUserOffline = data.getString("user");
                    Gson gson = new Gson();
                    User user = gson.fromJson(objectUserOffline, User.class);
                    onUserOffline(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case ON_USER_ONLINE: {
                try {
                    String objectUserOffline = data.getString("user");
                    Gson gson = new Gson();
                    User user = gson.fromJson(objectUserOffline, User.class);
                    onUserOnline(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case COMMENT: {
                try {
                    Gson gson = new Gson();
                    String objectComment = data.getString("comment");
                    Comment comment = gson.fromJson(objectComment, Comment.class);
                    new CommentPresenter().insertOrUpdate(comment);
                    onCommentReceive(comment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case CALL_CONTENT: {
                onCallContent(data);
                break;
            }

            case CALL_ACCEPT: {
                onCallAccept();
                break;
            }

            case CALL_STOP: {
                onCallStop();
                break;
            }

            case CALL_BUSY:{
                try{
                    int status = data.getInt("status");
                    onCallBusy(status);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }

            case CALL: {
                try {
                    int roomId = data.getInt("roomId");
                    boolean isAudioCall = data.getBoolean("isAudioCall");
                    onCall(roomId, isAudioCall);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case TURN_ON_CAMERA: {
                try {
                    boolean isOn = data.getBoolean("isOn");
                    int userId = data.getInt("userId");
                    onTurnOnCamera(isOn, userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void onCommentReceive(Comment comment) {
    }

    public void onConnectedSocket() {
    }

    public void onReceiverMessage(Message message) {
    }

    public void onReceiverListUsersOnline(ArrayList<User> users) {
    }

    public void onUserOffline(User users) {
    }

    public void onUserOnline(User users) {
    }

    public void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void hiddenProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public boolean isConnectedNetwork() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void onCallStop() {
    }

    public void onCallContent(JSONObject data) {
    }

    public void onCallAccept() {
    }

    public void onCall(int roomId, boolean isAudioCall) {
    }

    public void onCallBusy(int status){};

    public void onTurnOnCamera(boolean isOn, int userId) {
    }
}
