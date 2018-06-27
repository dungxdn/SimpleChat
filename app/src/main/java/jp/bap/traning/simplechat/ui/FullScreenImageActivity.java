package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.presenter.chattalks.ChatTalksPresenter;

@EActivity(R.layout.activity_full_screen_image)
public class FullScreenImageActivity extends BaseActivity {
    private Bitmap bitmap;
    @ViewById
    ImageView fullScreenImage;
    @Extra
    String urlImage;

    @Override
    public void afterView() {
        bitmap = new ChatTalksPresenter().StringToBitMap(urlImage);
        loadImage(bitmap);
    }

    private void loadImage(Bitmap bitmap) {
        fullScreenImage.setImageBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
