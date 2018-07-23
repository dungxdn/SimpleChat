package jp.bap.traning.simplechat.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.realm.RealmList;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Comment;
import jp.bap.traning.simplechat.model.News;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.comment.CommentPresenter;
import jp.bap.traning.simplechat.presenter.news.NewsPresenter;
import jp.bap.traning.simplechat.service.CallbackManager;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.Event;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment
public abstract class BaseFragment extends Fragment implements CallbackManager.Listener {
    CallbackManager mCallback;
    private WeakReference<Activity> mWeakReferance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback = new CallbackManager(getContext());
        mCallback.register(this);
        mWeakReferance = new WeakReference<>(getActivity());
    }

    public abstract void afterView();

    @AfterViews
    public void initView() {
        this.afterView();
    }

    public Activity getBaseActivity() {
        return mWeakReferance.get();
    }

    @Override
    public void onMessage(Event type, JSONObject data) {
        switch (type) {
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
                    Common.usersOnline.clear();
                    Common.usersOnline = usersOnline;
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
                    Common.usersOnline.remove(user);
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
                    Common.usersOnline.add(user);
                    onUserOnline(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case CREATE_ROOM: {
                try {
                    if (data.length() == 0) return;
                    String roomID = data.getString("roomId");
                    String typeRoom = data.getString("type");
                    String roomName = (data.getString("roomName") != null) ? data.getString("roomName") : "";
                    ArrayList<User> arrayUserRoom = new ArrayList<>();
                    JSONArray jsonArray = data.getJSONArray("users");
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String userRoom = jsonArray.getString(i);
                        User user = gson.fromJson(userRoom, User.class);
                        arrayUserRoom.add(user);
                    }
                    if (Common.checkValidUser(arrayUserRoom) == true) {
                        RealmList<User> usersRealmList = new RealmList<>();
                        for (User u : arrayUserRoom) {
                            usersRealmList.add(u);
                        }
                        int mRoomID = Integer.parseInt(roomID);
                        int mTypeRoom = Integer.parseInt(typeRoom);
                        Room room = new Room();
                        room.setRoomName(roomName);
                        room.setRoomId(mRoomID);
                        room.setType(mTypeRoom);
                        room.setUsers(usersRealmList);
                        new RoomDAO().insertOrUpdate(room);
                        createUserRoom(room);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case NEWS: {
                try {
                    String news = data.getString("news");
                    Gson gson = new Gson();
                    News mNews = gson.fromJson(news, News.class);
                    onNewsCome(mNews);
                    new NewsPresenter().insertOrUpdateNews(mNews);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case LIKE_NEWS: {
                try {
                    String news = data.getString("news");
                    String user = data.getString("user");
                    Gson gson = new Gson();
                    News mNews = gson.fromJson(news, News.class);
                    User mUser = gson.fromJson(user, User.class);
                    onUserLikeNews(mUser, mNews);
                    new NewsPresenter().insertOrUpdateNews(mNews);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void onUserLikeNews(User mUser, News mNews) {
    }

    public void onReceiverListUsersOnline(ArrayList<User> users) {
    }

    public void onUserOffline(User users) {
    }

    public void onUserOnline(User users) {
    }

    public void createUserRoom(Room room) {
    }

    public void onNewsCome(News news) {
    }


}
