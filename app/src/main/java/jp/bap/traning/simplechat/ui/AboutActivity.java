package jp.bap.traning.simplechat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.widget.CustomToolbar_;

@EActivity(R.layout.activity_about)
public class AboutActivity extends BaseActivity {
    @ViewById
    CustomToolbar_ mToolbar;

    @Override
    public void afterView() {
        mToolbar.setTitle(getResources().getString(R.string.str_about));
        mToolbar.getBackButton().setOnClickListener(view -> finish());
        mToolbar.getSettingButton().setVisibility(View.GONE);
    }
}
