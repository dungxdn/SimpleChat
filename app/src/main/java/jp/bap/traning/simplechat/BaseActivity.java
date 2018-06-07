package jp.bap.traning.simplechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

/**
 * Created by dungpv on 6/7/18.
 */

@EActivity
@WindowFeature(Window.FEATURE_NO_TITLE)
public abstract class BaseActivity extends AppCompatActivity {
    private static final String URL_SERVER = "http://172.16.0.31:3000";
    private static final String USER_NAME = "dungpv";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ChatService.getChat() == null) {
            Intent i = new Intent(this, ChatService.class);
            i.putExtra("host", URL_SERVER);
            i.putExtra("token", USER_NAME);
            startService(i);
        }
    }

    public abstract void afterView();

    @AfterViews
    public void initView() {
        this.afterView();
    }
}
