package jp.bap.traning.simplechat.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.service.CallbackManager;
import jp.bap.traning.simplechat.utils.Event;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment
public abstract class BaseFragment extends Fragment implements CallbackManager.Listener {
    CallbackManager mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback = new CallbackManager(getContext());
        mCallback.register(this);
    }

    public abstract void afterView();

    @AfterViews
    public void initView() {
        this.afterView();
    }

    @Override
    public void onMessage(Event type, JSONObject data) {
        switch (type) {
            case USER_ONLINE:
                try{
                    JSONArray jsonArray = data.getJSONArray("uses");
                    ArrayList<User> usersOnline = new ArrayList<>();
                    Gson gson = new Gson();
                    for(int i=0; i<jsonArray.length();i++) {
                        String objectUser = jsonArray.getString(i);
                        User user = gson.fromJson(objectUser,User.class);
                        usersOnline.add(user);
                    }
                    onReceiverListUsersOnline(usersOnline);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public void onReceiverListUsersOnline(ArrayList<User> users) {
    }

}
