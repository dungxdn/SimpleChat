package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.presenter.chattalks.ChatTalksPresenter;
import jp.bap.traning.simplechat.utils.Common;

@EActivity(R.layout.activity_full_screen_image)
public class FullScreenImageActivity extends BaseActivity {
    @ViewById
    AppCompatImageView fullScreenImage;
    @Extra
    String urlImage;

    @Override
    public void afterView() {
        Common.setImage(FullScreenImageActivity.this, urlImage, fullScreenImage);
    }

    @Click(R.id.fullScreenImage)
    public void click(){
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
