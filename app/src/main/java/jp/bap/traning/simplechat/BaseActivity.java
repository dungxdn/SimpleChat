package jp.bap.traning.simplechat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

import jp.bap.traning.simplechat.chat.ChatService;

/**
 * Created by dungpv on 6/7/18.
 */

@EActivity
@WindowFeature(Window.FEATURE_NO_TITLE)
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract void afterView();

    @AfterViews
    public void initView() {
        this.afterView();
    }
}
