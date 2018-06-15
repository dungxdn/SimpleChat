package jp.bap.traning.simplechat.ui;


import android.support.v4.app.Fragment;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.service.CallbackManager;
import jp.bap.traning.simplechat.utils.Event;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment
public abstract class BaseFragment extends Fragment implements CallbackManager.Listener {
    private CallbackManager mSocketCallback;
    private Gson mGson;

    public abstract void afterView();

    @AfterViews
    public void initView() {
        this.afterView();
        mGson = new Gson();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSocketCallback == null) {
            mSocketCallback = new CallbackManager(getContext());
        }
        mSocketCallback.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSocketCallback != null) {
            mSocketCallback.unregister();
        }
    }

    @Override
    public void onMessage(Event type, JSONObject data) {
        switch (type) {
            case USER_GET_ONLINE:
                try {
                    JSONArray jsonArray = data.getJSONArray("users");
                    List<User> users = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        users.add(mGson.fromJson(jsonArray.get(i).toString(), User.class));
                    }
                    onGetUsersOnline(users);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void onGetUsersOnline(List<User> users) {}
}
